package com.hotelRoom.allocator.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Error response format")
public record ErrorResponse(
        @Schema(description = "Error message", example = "field `premiumRooms` must be greater than or equal to 0")
        String error) {
}
