package com.driver.bookMyShow.Services;

import com.driver.bookMyShow.Dtos.RequestDtos.ShowEntryDto;
import com.driver.bookMyShow.Dtos.RequestDtos.ShowSeatEntryDto;
import com.driver.bookMyShow.Dtos.RequestDtos.ShowTimingsDto;
import com.driver.bookMyShow.Enums.SeatType;
import com.driver.bookMyShow.Exceptions.MovieDoesNotExists;
import com.driver.bookMyShow.Exceptions.ShowDoesNotExists;
import com.driver.bookMyShow.Exceptions.TheaterDoesNotExists;
import com.driver.bookMyShow.Models.Movie;
import com.driver.bookMyShow.Models.Show;
import com.driver.bookMyShow.Models.ShowSeat;
import com.driver.bookMyShow.Models.Theater;
import com.driver.bookMyShow.Models.TheaterSeat;
import com.driver.bookMyShow.Repositories.MovieRepository;
import com.driver.bookMyShow.Repositories.ShowRepository;
import com.driver.bookMyShow.Repositories.TheaterRepository;
import com.driver.bookMyShow.Transformers.ShowTransformer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.List;

@Service
public class ShowService {

    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;

    public ShowService(
            ShowRepository showRepository,
            MovieRepository movieRepository,
            TheaterRepository theaterRepository
    ) {
        this.showRepository = showRepository;
        this.movieRepository = movieRepository;
        this.theaterRepository = theaterRepository;
    }

    @Transactional
    public String addShow(ShowEntryDto showEntryDto) {
        Movie movie = movieRepository
                .findById(showEntryDto.getMovieId())
                .orElseThrow(MovieDoesNotExists::new);

        Theater theater = theaterRepository
                .findById(showEntryDto.getTheaterId())
                .orElseThrow(TheaterDoesNotExists::new);

        Show show = ShowTransformer.showDtoToShow(showEntryDto);

        show.setMovie(movie);
        show.setTheater(theater);

        showRepository.save(show);

        movie.getShows().add(show);
        theater.getShowList().add(show);

        return "Show has been added successfully";
    }

    @Transactional
    public String associateShowSeats(ShowSeatEntryDto showSeatEntryDto) {
        Show show = showRepository
                .findById(showSeatEntryDto.getShowId())
                .orElseThrow(ShowDoesNotExists::new);

        /*
         * Prevent duplicate show seats if this endpoint is called
         * more than once for the same show.
         */
        if (!show.getShowSeatList().isEmpty()) {
            throw new IllegalStateException(
                    "Seats are already associated with this show"
            );
        }

        Theater theater = show.getTheater();
        List<TheaterSeat> theaterSeats = theater.getTheaterSeatList();
        List<ShowSeat> showSeats = show.getShowSeatList();

        for (TheaterSeat theaterSeat : theaterSeats) {
            ShowSeat showSeat = new ShowSeat();

            showSeat.setSeatNo(theaterSeat.getSeatNo());
            showSeat.setSeatType(theaterSeat.getSeatType());
            showSeat.setShow(show);
            showSeat.setIsAvailable(Boolean.TRUE);
            showSeat.setIsFoodContains(Boolean.FALSE);

            if (theaterSeat.getSeatType() == SeatType.CLASSIC) {
                showSeat.setPrice(
                        showSeatEntryDto.getPriceOfClassicSeat()
                );
            } else {
                showSeat.setPrice(
                        showSeatEntryDto.getPriceOfPremiumSeat()
                );
            }

            showSeats.add(showSeat);
        }

        showRepository.save(show);

        return "Show seats have been associated successfully";
    }

    @Transactional(readOnly = true)
    public List<Time> showTimingsOnDate(ShowTimingsDto showTimingsDto) {
        return showRepository.getShowTimingsOnDate(
                showTimingsDto.getDate(),
                showTimingsDto.getTheaterId(),
                showTimingsDto.getMovieId()
        );
    }

    @Transactional(readOnly = true)
    public String movieHavingMostShows() {
        Integer movieId = showRepository.getMostShowsMovie();

        if (movieId == null) {
            throw new MovieDoesNotExists();
        }

        return movieRepository
                .findById(movieId)
                .orElseThrow(MovieDoesNotExists::new)
                .getMovieName();
    }
}