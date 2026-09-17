package com.driver.bookMyShow.Services;

import com.driver.bookMyShow.Dtos.RequestDtos.TheaterEntryDto;
import com.driver.bookMyShow.Dtos.RequestDtos.TheaterSeatEntryDto;
import com.driver.bookMyShow.Enums.SeatType;
import com.driver.bookMyShow.Exceptions.TheaterIsNotPresentOnThisAddress;
import com.driver.bookMyShow.Exceptions.TheaterIsPresentOnThatAddress;
import com.driver.bookMyShow.Models.Theater;
import com.driver.bookMyShow.Models.TheaterSeat;
import com.driver.bookMyShow.Repositories.TheaterRepository;
import com.driver.bookMyShow.Transformers.TheaterTransformer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TheaterService {

    private final TheaterRepository theaterRepository;

    public TheaterService(TheaterRepository theaterRepository) {
        this.theaterRepository = theaterRepository;
    }

    @Transactional
    public String addTheater(TheaterEntryDto theaterEntryDto) {
        if (theaterRepository.existsByAddress(
                theaterEntryDto.getAddress()
        )) {
            throw new TheaterIsPresentOnThatAddress();
        }

        Theater theater =
                TheaterTransformer.theaterDtoToTheater(theaterEntryDto);

        theaterRepository.save(theater);

        return "Theater has been saved successfully";
    }

    @Transactional
    public String addTheaterSeat(TheaterSeatEntryDto entryDto) {
        Theater theater = theaterRepository
                .findByAddress(entryDto.getAddress())
                .orElseThrow(TheaterIsNotPresentOnThisAddress::new);

        if (!theater.getTheaterSeatList().isEmpty()) {
            throw new IllegalStateException(
                    "Seats have already been added to this theater"
            );
        }

        int seatsPerRow = entryDto.getNoOfSeatInRow();
        int premiumSeats = entryDto.getNoOfPremiumSeat();
        int classicSeats = entryDto.getNoOfClassicSeat();

        if (seatsPerRow <= 0) {
            throw new IllegalArgumentException(
                    "Number of seats per row must be greater than zero"
            );
        }

        if (premiumSeats < 0 || classicSeats < 0) {
            throw new IllegalArgumentException(
                    "Seat count cannot be negative"
            );
        }

        List<TheaterSeat> seatList = theater.getTheaterSeatList();

        int seatIndex = 0;

        seatIndex = createSeats(
                theater,
                seatList,
                seatIndex,
                classicSeats,
                seatsPerRow,
                SeatType.CLASSIC
        );

        createSeats(
                theater,
                seatList,
                seatIndex,
                premiumSeats,
                seatsPerRow,
                SeatType.PREMIUM
        );

        theaterRepository.save(theater);

        return "Theater seats have been added successfully";
    }

    private int createSeats(
            Theater theater,
            List<TheaterSeat> seatList,
            int startingIndex,
            int numberOfSeats,
            int seatsPerRow,
            SeatType seatType
    ) {
        int seatIndex = startingIndex;

        for (int i = 0; i < numberOfSeats; i++) {
            int rowNumber = (seatIndex / seatsPerRow) + 1;
            int seatPosition = seatIndex % seatsPerRow;
            char seatLetter = (char) ('A' + seatPosition);

            TheaterSeat theaterSeat = new TheaterSeat();
            theaterSeat.setSeatNo(rowNumber + String.valueOf(seatLetter));
            theaterSeat.setSeatType(seatType);
            theaterSeat.setTheater(theater);

            seatList.add(theaterSeat);
            seatIndex++;
        }

        return seatIndex;
    }
}