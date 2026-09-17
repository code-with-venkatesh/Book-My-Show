package com.driver.bookMyShow.Controllers;

import com.driver.bookMyShow.Dtos.RequestDtos.TicketEntryDto;
import com.driver.bookMyShow.Dtos.ResponseDtos.ApiResponse;
import com.driver.bookMyShow.Dtos.ResponseDtos.TicketResponseDto;
import com.driver.bookMyShow.Services.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/book")
    public ResponseEntity<?> ticketBooking(
            @RequestBody TicketEntryDto ticketEntryDto
    ) {
        try {
            TicketResponseDto result =
                    ticketService.ticketBooking(ticketEntryDto);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(result);
        } catch (Exception exception) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.of(exception.getMessage()));
        }
    }
}