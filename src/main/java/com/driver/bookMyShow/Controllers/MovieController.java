package com.driver.bookMyShow.Controllers;

import com.driver.bookMyShow.Dtos.RequestDtos.MovieEntryDto;
import com.driver.bookMyShow.Dtos.ResponseDtos.ApiResponse;
import com.driver.bookMyShow.Services.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/addNew")
    public ResponseEntity<ApiResponse> addMovie(
            @RequestBody MovieEntryDto movieEntryDto
    ) {
        try {
            String result = movieService.addMovie(movieEntryDto);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.of(result));
        } catch (Exception exception) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.of(exception.getMessage()));
        }
    }

    @GetMapping("/totalCollection/{movieId}")
    public ResponseEntity<?> totalCollection(
            @PathVariable Integer movieId
    ) {
        try {
            Long result = movieService.totalCollection(movieId);
            return ResponseEntity.ok(result);
        } catch (Exception exception) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.of(exception.getMessage()));
        }
    }
}