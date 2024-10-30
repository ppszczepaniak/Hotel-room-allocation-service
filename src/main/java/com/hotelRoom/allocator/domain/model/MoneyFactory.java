package com.hotelRoom.allocator.domain.model;

import com.hotelRoom.allocator.config.CurrencyConfig;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Currency;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class MoneyFactory {
    public static final Money ZERO = createMoney(BigDecimal.ZERO);

    static CurrencyConfig currencyConfig;

    public static Money createMoney(BigDecimal amount) {
        Currency currency = currencyConfig != null ? Currency.getInstance(currencyConfig.getCode()) : null;
        return Money.of(amount, currency);
    }

    public static Money createMoney(double amount) {
        return createMoney(BigDecimal.valueOf(amount));
    }
}
