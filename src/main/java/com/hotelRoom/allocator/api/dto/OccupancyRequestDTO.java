package com.hotelRoom.allocator.api.dto;

import com.hotelRoom.allocator.domain.model.Guest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.util.List;

@Builder
public record OccupancyRequestDTO(@PositiveOrZero int freePremium,
                                  @PositiveOrZero int freeEconomy,
                                  @NotNull List<@NotNull Guest> guests) {
}
