package com.driver.bookMyShow.Controllers;

import com.driver.bookMyShow.Dtos.RequestDtos.TheaterEntryDto;
import com.driver.bookMyShow.Dtos.RequestDtos.TheaterSeatEntryDto;
import com.driver.bookMyShow.Dtos.ResponseDtos.ApiResponse;
import com.driver.bookMyShow.Services.TheaterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/theater")
public class TheaterController {

    private final TheaterService theaterService;

    public TheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    @PostMapping("/addNew")
    public ResponseEntity<ApiResponse> addTheater(
            @RequestBody TheaterEntryDto theaterEntryDto
    ) {
        try {
            String result =
                    theaterService.addTheater(theaterEntryDto);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.of(result));
        } catch (Exception exception) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.of(exception.getMessage()));
        }
    }

    @PostMapping("/addTheaterSeat")
    public ResponseEntity<ApiResponse> addTheaterSeat(
            @RequestBody TheaterSeatEntryDto entryDto
    ) {
        try {
            String result =
                    theaterService.addTheaterSeat(entryDto);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.of(result));
        } catch (Exception exception) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.of(exception.getMessage()));
        }
    }
}