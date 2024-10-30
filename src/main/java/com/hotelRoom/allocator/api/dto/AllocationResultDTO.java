package com.hotelRoom.allocator.api.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AllocationResultDTO(int usagePremium, BigDecimal revenuePremium, int usageEconomy, BigDecimal revenueEconomy) {}

