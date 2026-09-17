package com.driver.bookMyShow.Dtos.ResponseDtos;

import java.time.LocalDateTime;

public record ApiResponse(
        String message,
        LocalDateTime timestamp
) {

    public static ApiResponse of(String message) {
        return new ApiResponse(message, LocalDateTime.now());
    }
}