package com.driver.bookMyShow.Services;

import com.driver.bookMyShow.Dtos.RequestDtos.TicketEntryDto;
import com.driver.bookMyShow.Dtos.ResponseDtos.TicketResponseDto;
import com.driver.bookMyShow.Exceptions.RequestedSeatAreNotAvailable;
import com.driver.bookMyShow.Exceptions.ShowDoesNotExists;
import com.driver.bookMyShow.Exceptions.UserDoesNotExists;
import com.driver.bookMyShow.Models.Show;
import com.driver.bookMyShow.Models.ShowSeat;
import com.driver.bookMyShow.Models.Ticket;
import com.driver.bookMyShow.Models.User;
import com.driver.bookMyShow.Repositories.ShowRepository;
import com.driver.bookMyShow.Repositories.TicketRepository;
import com.driver.bookMyShow.Repositories.UserRepository;
import com.driver.bookMyShow.Transformers.TicketTransformer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ShowRepository showRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final String mailFrom;

    public TicketService(
            TicketRepository ticketRepository,
            ShowRepository showRepository,
            UserRepository userRepository,
            JavaMailSender mailSender,
            @Value("${app.mail.from}") String mailFrom
    ) {
        this.ticketRepository = ticketRepository;
        this.showRepository = showRepository;
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.mailFrom = mailFrom;
    }

    @Transactional
    public TicketResponseDto ticketBooking(
            TicketEntryDto ticketEntryDto
    ) {
        Show show = showRepository
                .findById(ticketEntryDto.getShowId())
                .orElseThrow(ShowDoesNotExists::new);

        User user = userRepository
                .findById(ticketEntryDto.getUserId())
                .orElseThrow(UserDoesNotExists::new);

        Set<String> requestedSeats =
                normalizeRequestedSeats(ticketEntryDto.getRequestSeats());

        int totalPrice = validateAndReserveSeats(
                show.getShowSeatList(),
                requestedSeats
        );

        String bookedSeats = String.join(",", requestedSeats);

        Ticket ticket = Ticket.builder()
                .totalTicketsPrice(totalPrice)
                .bookedSeats(bookedSeats)
                .user(user)
                .show(show)
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        user.getTicketList().add(savedTicket);
        show.getTicketList().add(savedTicket);

        TicketResponseDto response =
                TicketTransformer.returnTicket(show, savedTicket);

        /*
         * Phase 2 keeps email sending synchronous.
         * A later phase should publish an event and send the email
         * only after the database transaction commits successfully.
         */
        sendMailToUser(user, show, bookedSeats);

        return response;
    }

    private Set<String> normalizeRequestedSeats(
            List<String> requestedSeats
    ) {
        if (requestedSeats == null || requestedSeats.isEmpty()) {
            throw new RequestedSeatAreNotAvailable();
        }

        Set<String> normalizedSeats = new LinkedHashSet<>();

        for (String seatNumber : requestedSeats) {
            if (seatNumber == null || seatNumber.isBlank()) {
                throw new RequestedSeatAreNotAvailable();
            }

            normalizedSeats.add(
                    seatNumber.trim().toUpperCase()
            );
        }

        return normalizedSeats;
    }

    private int validateAndReserveSeats(
            List<ShowSeat> showSeats,
            Set<String> requestedSeats
    ) {
        Map<String, ShowSeat> seatsByNumber = new HashMap<>();

        for (ShowSeat showSeat : showSeats) {
            seatsByNumber.put(
                    showSeat.getSeatNo().toUpperCase(),
                    showSeat
            );
        }

        /*
         * Validate every requested seat before changing availability.
         * This avoids partially reserving seats when one requested
         * seat is invalid or unavailable.
         */
        for (String requestedSeat : requestedSeats) {
            ShowSeat showSeat = seatsByNumber.get(requestedSeat);

            if (showSeat == null ||
                    !Boolean.TRUE.equals(showSeat.getIsAvailable())) {
                throw new RequestedSeatAreNotAvailable();
            }
        }

        int totalPrice = 0;

        for (String requestedSeat : requestedSeats) {
            ShowSeat showSeat = seatsByNumber.get(requestedSeat);

            totalPrice += showSeat.getPrice();
            showSeat.setIsAvailable(Boolean.FALSE);
        }

        return totalPrice;
    }

    private void sendMailToUser(
            User user,
            Show show,
            String seats
    ) {
        String body = """
                Dear %s,

                Your ticket has been successfully booked.

                Ticket Details:

                Booked seat numbers: %s
                Movie name: %s
                Date: %s
                Time: %s
                Location: %s

                Enjoy the show!
                """.formatted(
                user.getName(),
                seats,
                show.getMovie().getMovieName(),
                show.getDate(),
                show.getTime(),
                show.getTheater().getAddress()
        );

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(user.getEmailId());
        message.setSubject("Ticket successfully booked");
        message.setText(body);

        mailSender.send(message);
    }
}