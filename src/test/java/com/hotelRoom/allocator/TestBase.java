package com.hotelRoom.allocator;

import com.hotelRoom.allocator.domain.model.Guest;
import com.hotelRoom.allocator.domain.model.MoneyFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

public class TestBase {

    public static BigDecimal money(double amount) {
        return MoneyFactory.createMoney(amount).amount();
    }

    public static List<Guest> randomTwentyGuests() {
        return IntStream.range(1, randomInt0to20() + 1)
                .mapToObj(i -> new Guest(money(Math.random() * 1000)))
                .toList();
    }

    public static List<Guest> randomTwentyPremiumGuests() {
        return IntStream.range(1, 21)
                .mapToObj(i -> new Guest(money(Math.random() * 100 + 100)))
                .toList();
    }

    public static List<Guest> randomTwentyEconomyGuests() {
        return IntStream.range(1, 21)
                .mapToObj(i -> new Guest(money(Math.random() * 99)))
                .toList();
    }

    public static List<Guest> randomPremiumGuests() {
        return IntStream.range(1, randomInt0to20() + 1)
                .mapToObj(i -> new Guest(money(Math.random() * 100 + 100)))
                .toList();
    }

    public static List<Guest> randomEconomyGuests() {
        return IntStream.range(1, randomInt0to20() + 1)
                .mapToObj(i ->  new Guest((money(Math.random() * 99))))
                .toList();
    }

    public static int randomInt0to100() {
        return (int) (Math.random() * 100);
    }

    public static int randomInt0to20() {
        return (int) (Math.random() * 20);
    }
}
