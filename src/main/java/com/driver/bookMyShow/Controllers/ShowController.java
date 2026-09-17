package com.driver.bookMyShow.Controllers;

import com.driver.bookMyShow.Dtos.RequestDtos.ShowEntryDto;
import com.driver.bookMyShow.Dtos.RequestDtos.ShowSeatEntryDto;
import com.driver.bookMyShow.Dtos.RequestDtos.ShowTimingsDto;
import com.driver.bookMyShow.Dtos.ResponseDtos.ApiResponse;
import com.driver.bookMyShow.Services.ShowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.util.List;

@RestController
@RequestMapping("/show")
public class ShowController {

    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @PostMapping("/addNew")
    public ResponseEntity<ApiResponse> addShow(
            @RequestBody ShowEntryDto showEntryDto
    ) {
        try {
            String result = showService.addShow(showEntryDto);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.of(result));
        } catch (Exception exception) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.of(exception.getMessage()));
        }
    }

    @PostMapping("/associateSeats")
    public ResponseEntity<ApiResponse> associateShowSeats(
            @RequestBody ShowSeatEntryDto showSeatEntryDto
    ) {
        try {
            String result =
                    showService.associateShowSeats(showSeatEntryDto);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.of(result));
        } catch (Exception exception) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.of(exception.getMessage()));
        }
    }

    @GetMapping("/showTimingsOnDate")
    public ResponseEntity<?> showTimingsOnDate(
            ShowTimingsDto showTimingsDto
    ) {
        try {
            List<Time> result =
                    showService.showTimingsOnDate(showTimingsDto);

            return ResponseEntity.ok(result);
        } catch (Exception exception) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.of(exception.getMessage()));
        }
    }

    @GetMapping("/movieHavingMost Kathy")
    public ResponseEntity<?> movieHavingMostShows() {
        try {
            String movie = showService.movieHavingMostShows();
            return ResponseEntity.ok(movie);
        } catch (Exception exception) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.of(exception.getMessage()));
        }
    }
}