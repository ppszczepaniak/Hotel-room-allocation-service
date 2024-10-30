package com.hotelRoom.allocator.api.dto;

import com.hotelRoom.allocator.validation.ValidDoubleArray;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record HotelInputDTO(
        @Schema(description = "Number of premium rooms available", example = "10", required = true)
        @PositiveOrZero int premiumRooms,

        @Schema(description = "Number of economy rooms available", example = "20", required = true)
        @PositiveOrZero int economyRooms,

        @Schema(description = "List of potential guests, should be positive numbers", required = true)
        @ValidDoubleArray List<@Positive @Digits(integer = 10, fraction = 2) Double> potentialGuests) {}
