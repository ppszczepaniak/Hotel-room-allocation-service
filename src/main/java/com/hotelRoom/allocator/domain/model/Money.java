package com.hotelRoom.allocator.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Optional;

public record Money(BigDecimal amount, Currency currency) {

    public static Money of(BigDecimal amount, Currency currency) {
        return new Money(amount, currency);
    }

    public Money(BigDecimal amount, Currency currency) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency;
    }

    public Money add(Money otherMoney) {
        return Optional.ofNullable(otherMoney)
                .map(this::addNonZeroAmount)
                .orElseThrow(() -> new IllegalArgumentException("Cannot add null Money"));
    }

    private Money addNonZeroAmount(Money otherMoney) {
        return Money.of(amount.add(otherMoney.amount), currency);
    }
}
