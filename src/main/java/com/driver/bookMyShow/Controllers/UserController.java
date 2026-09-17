package com.driver.bookMyShow.Controllers;

import com.driver.bookMyShow.Dtos.RequestDtos.UserEntryDto;
import com.driver.bookMyShow.Dtos.ResponseDtos.ApiResponse;
import com.driver.bookMyShow.Dtos.ResponseDtos.TicketResponseDto;
import com.driver.bookMyShow.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/addNew")
    public ResponseEntity<ApiResponse> addNewUser(
            @RequestBody UserEntryDto userEntryDto
    ) {
        try {
            String result = userService.addUser(userEntryDto);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.of(result));
        } catch (Exception exception) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.of(exception.getMessage()));
        }
    }

    @GetMapping("/allTickets/{userId}")
    public ResponseEntity<?> allTickets(
            @PathVariable Integer userId
    ) {
        try {
            List<TicketResponseDto> result =
                    userService.allTickets(userId);

            return ResponseEntity.ok(result);
        } catch (Exception exception) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.of(exception.getMessage()));
        }
    }
}