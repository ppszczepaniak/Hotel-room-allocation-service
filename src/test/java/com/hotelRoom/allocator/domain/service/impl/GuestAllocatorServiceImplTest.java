package com.hotelRoom.allocator.domain.service.impl;

import com.hotelRoom.allocator.api.dto.AllocationResultDTO;
import com.hotelRoom.allocator.api.dto.OccupancyRequestDTO;
import com.hotelRoom.allocator.domain.model.Guest;
import com.hotelRoom.allocator.domain.service.GuestAllocatorService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static com.hotelRoom.allocator.TestBase.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class GuestAllocatorServiceImplTest {

    @Autowired
    GuestAllocatorService guestAllocatorService;

    @ParameterizedTest
    @MethodSource("roomAllocationTestsFromTheTask")
    void roomAllocationTestsFromTheTask(int premiumRooms, int economyRooms, List<Guest> guestsList, int expectedUsagePremium, BigDecimal expectedRevenuePremium, int expectedUsageEconomy, BigDecimal expectedRevenueEconomy) {
        performTests(premiumRooms, economyRooms, guestsList, expectedUsagePremium, expectedRevenuePremium, expectedUsageEconomy, expectedRevenueEconomy);
    }

    @ParameterizedTest
    @MethodSource("shouldReturnAllZeroesWhenThereAreNoOffers")
    void shouldReturnAllZeroesWhenThereAreNoOffers(int premiumRooms, int economyRooms, List<Guest> guestsList, int expectedUsagePremium, BigDecimal expectedRevenuePremium, int expectedUsageEconomy, BigDecimal expectedRevenueEconomy) {
        performTests(premiumRooms, economyRooms, guestsList, expectedUsagePremium, expectedRevenuePremium, expectedUsageEconomy, expectedRevenueEconomy);
    }

    @ParameterizedTest
    @MethodSource("shouldReturnAllZeroesWhenNoVacancy")
    void shouldReturnAllZeroesWhenNoVacancy(int premiumRooms, int economyRooms, List<Guest> guestsList, int expectedUsagePremium, BigDecimal expectedRevenuePremium, int expectedUsageEconomy, BigDecimal expectedRevenueEconomy) {
        performTests(premiumRooms, economyRooms, guestsList, expectedUsagePremium, expectedRevenuePremium, expectedUsageEconomy, expectedRevenueEconomy);
    }

    @ParameterizedTest
    @MethodSource("shouldReturnPremiumOnlyWhenPremiumOnlyGuests")
    void shouldReturnPremiumOnlyWhenPremiumOnlyGuests(int premiumRooms, int economyRooms, List<Guest> guestsList, int expectedUsagePremium, BigDecimal expectedRevenuePremium, int expectedUsageEconomy, BigDecimal expectedRevenueEconomy) {
        performTests(premiumRooms, economyRooms, guestsList, expectedUsagePremium, expectedRevenuePremium, expectedUsageEconomy, expectedRevenueEconomy);
    }

    @ParameterizedTest
    @MethodSource("shouldReturnEconomyOnlyWhenEconomyOnlyGuestsAndWithNoPremiumVacancy")
    void shouldReturnEconomyOnlyWhenEconomyOnlyGuestsAndWithNoPremiumVacancy(int premiumRooms, int economyRooms, List<Guest> guestsList, int expectedUsagePremium, BigDecimal expectedRevenuePremium, int expectedUsageEconomy, BigDecimal expectedRevenueEconomy) {
        performTests(premiumRooms, economyRooms, guestsList, expectedUsagePremium, expectedRevenuePremium, expectedUsageEconomy, expectedRevenueEconomy);
    }

    @ParameterizedTest
    @MethodSource("shouldReturnEconomyOnlyWhenEconomyOnlyGuestsAndWithFullEconomyVacancy")
    void shouldReturnEconomyOnlyWhenEconomyOnlyGuestsAndWithFullEconomyVacancy(int premiumRooms, int economyRooms, List<Guest> guestsList, int expectedUsagePremium, BigDecimal expectedRevenuePremium, int expectedUsageEconomy, BigDecimal expectedRevenueEconomy) {
        performTests(premiumRooms, economyRooms, guestsList, expectedUsagePremium, expectedRevenuePremium, expectedUsageEconomy, expectedRevenueEconomy);
    }

    @ParameterizedTest
    @MethodSource("shouldUpgradeAllEconomyGuestsToPremiumRoomsWhenEconomyOnlyGuestsButNoEconomyVacancy")
    void shouldUpgradeAllEconomyGuestsToPremiumRoomsWhenEconomyOnlyGuestsButNoEconomyVacancy(int premiumRooms, int economyRooms, List<Guest> guestsList, int expectedUsagePremium, BigDecimal expectedRevenuePremium, int expectedUsageEconomy, BigDecimal expectedRevenueEconomy) {
        performTests(premiumRooms, economyRooms, guestsList, expectedUsagePremium, expectedRevenuePremium, expectedUsageEconomy, expectedRevenueEconomy);
    }

    @ParameterizedTest
    @MethodSource("shouldAllocateTheRoomAsExpected")
    void shouldAllocateTheRoomAsExpected(int premiumRooms, int economyRooms, List<Guest> guestsList, int expectedUsagePremium, BigDecimal expectedRevenuePremium, int expectedUsageEconomy, BigDecimal expectedRevenueEconomy) {
        performTests(premiumRooms, economyRooms, guestsList, expectedUsagePremium, expectedRevenuePremium, expectedUsageEconomy, expectedRevenueEconomy);
    }

    private void performTests(int premiumRooms, int economyRooms, List<Guest> guestsList, int expectedOccupiedPremium, BigDecimal expectedRevenuePremium, int expectedOccupiedEconomy, BigDecimal expectedRevenueEconomy) {
        OccupancyRequestDTO request = new OccupancyRequestDTO(premiumRooms, economyRooms, guestsList);
        AllocationResultDTO result = guestAllocatorService.allocateGuests(request);
        assertAll(
                () -> assertThat(result.usagePremium()).isEqualByComparingTo(expectedOccupiedPremium),
                () -> assertThat(result.revenuePremium()).isEqualByComparingTo(expectedRevenuePremium),
                () -> assertThat(result.usageEconomy()).isEqualByComparingTo(expectedOccupiedEconomy),
                () -> assertThat(result.revenueEconomy()).isEqualByComparingTo(expectedRevenueEconomy)
        );
    }

    private static Stream<Arguments> roomAllocationTestsFromTheTask() {
        return Stream.of(
                Arguments.of(3, 3, guestsListFromTheTask(), 3, money(738), 3, money(167.99)),
                Arguments.of(7, 5, guestsListFromTheTask(), 6, money(1054), 4, money(189.99)),
                Arguments.of(2, 7, guestsListFromTheTask(), 2, money(583), 4, money(189.99))
        );
    }

    public static List<Guest> guestsListFromTheTask() {
        return Stream.of(23.00, 45.00, 155.00, 374.00, 22.00, 99.99, 100.00, 101.00, 115.00, 209.00)
                .map(BigDecimal::valueOf)
                .map(Guest::new)
                .toList();
    }

    public static Stream<Arguments> shouldReturnAllZeroesWhenThereAreNoOffers() {
        return Stream.of(
                Arguments.of(3, 3, Collections.emptyList(), 0, money(0), 0, money(0)),
                Arguments.of(7, 5, Collections.emptyList(), 0, money(0), 0, money(0)),
                Arguments.of(2, 7, Collections.emptyList(), 0, money(0), 0, money(0)),
                Arguments.of(0, 0, Collections.emptyList(), 0, money(0), 0, money(0)),
                Arguments.of(1, 0, Collections.emptyList(), 0, money(0), 0, money(0)),
                Arguments.of(0, 1, Collections.emptyList(), 0, money(0), 0, money(0)),
                Arguments.of(randomInt0to100(), randomInt0to100(), Collections.emptyList(), 0, money(0), 0, money(0))
        );
    }

    public static Stream<Arguments> shouldReturnAllZeroesWhenNoVacancy() {
        return Stream.of(
                Arguments.of(0, 0, randomTwentyGuests(), 0, money(0), 0, money(0)),
                Arguments.of(0, 0, randomTwentyGuests(), 0, money(0), 0, money(0)),
                Arguments.of(0, 0, randomTwentyGuests(), 0, money(0), 0, money(0)),
                Arguments.of(0, 0, randomTwentyGuests(), 0, money(0), 0, money(0))
        );
    }

    public static Stream<Arguments> shouldReturnPremiumOnlyWhenPremiumOnlyGuests() {
        return Stream.of(
                Arguments.of(1, 0, fivePremiumOffers(), 1, money(10000.00), 0, money(0)),
                Arguments.of(2, 0, fivePremiumOffers(), 2, money(11001.00), 0, money(0)),
                Arguments.of(4, 0, fivePremiumOffers(), 4, money(11203.00), 0, money(0)),
                Arguments.of(5, 0, fivePremiumOffers(), 5, money(11303.00), 0, money(0)),
                Arguments.of(100, 0, fivePremiumOffers(), 5, money(11303.00), 0, money(0)),
                Arguments.of(1, randomInt0to20(), fivePremiumOffers(), 1, money(10000.00), 0, money(0)),
                Arguments.of(2, randomInt0to20(), fivePremiumOffers(), 2, money(11001.00), 0, money(0)),
                Arguments.of(4, randomInt0to20(), fivePremiumOffers(), 4, money(11203.00), 0, money(0)),
                Arguments.of(5, randomInt0to20(), fivePremiumOffers(), 5, money(11303.00), 0, money(0)),
                Arguments.of(100, randomInt0to20(), fivePremiumOffers(), 5, money(11303.00), 0, money(0))
        );
    }

    public static List<Guest> fivePremiumOffers() {
        return Stream.of(100.00, 100.01, 101.99, 1001.00, 10000.00)
                .map(BigDecimal::valueOf)
                .map(Guest::new)
                .toList();
    }

    public static Stream<Arguments> shouldReturnEconomyOnlyWhenEconomyOnlyGuestsAndWithNoPremiumVacancy() {
        return Stream.of(
                Arguments.of(0, 1, fiveEconomyOffers(), 0, money(0), 1, money(99.99)),
                Arguments.of(0, 2, fiveEconomyOffers(), 0, money(0), 2, money(149.99)),
                Arguments.of(0, 3, fiveEconomyOffers(), 0, money(0), 3, money(151.99)),
                Arguments.of(0, 4, fiveEconomyOffers(), 0, money(0), 4, money(153.00)),
                Arguments.of(0, 5, fiveEconomyOffers(), 0, money(0), 5, money(154.00)),
                Arguments.of(0, 6, fiveEconomyOffers(), 0, money(0), 6, money(154.01)),
                Arguments.of(0, 100, fiveEconomyOffers(), 0, money(0), 6, money(154.01))
        );
    }

    public static Stream<Arguments> shouldReturnEconomyOnlyWhenEconomyOnlyGuestsAndWithFullEconomyVacancy() {
        return Stream.of(
                Arguments.of(randomInt0to20(), 6, fiveEconomyOffers(), 0, money(0), 6, money(154.01)),
                Arguments.of(6, 6, fiveEconomyOffers(), 0, money(0), 6, money(154.01)),
                Arguments.of(randomInt0to20(), 100, fiveEconomyOffers(), 0, money(0), 6, money(154.01)),
                Arguments.of(100, 100, fiveEconomyOffers(), 0, money(0), 6, money(154.01))
        );
    }

    public static List<Guest> fiveEconomyOffers() {
        return Stream.of(1.00, 2.00, 99.99, 50.00, 1.01, 0.01)
                .map(BigDecimal::valueOf)
                .map(Guest::new)
                .toList();
    }

    public static Stream<Arguments> shouldUpgradeAllEconomyGuestsToPremiumRoomsWhenEconomyOnlyGuestsButNoEconomyVacancy() {
        return Stream.of(
                Arguments.of(1, 0, fiveEconomyOffers(), 1, money(99.99), 0, money(0)),
                Arguments.of(2, 0, fiveEconomyOffers(), 2, money(149.99), 0, money(0)),
                Arguments.of(3, 0, fiveEconomyOffers(), 3, money(151.99), 0, money(0)),
                Arguments.of(4, 0, fiveEconomyOffers(), 4, money(153.00), 0, money(0)),
                Arguments.of(5, 0, fiveEconomyOffers(), 5, money(154.00), 0, money(0)),
                Arguments.of(6, 0, fiveEconomyOffers(), 6, money(154.01), 0, money(0)),
                Arguments.of(100, 0, fiveEconomyOffers(), 6, money(154.01), 0, money(0))
        );
    }

    public static Stream<Arguments> shouldAllocateTheRoomAsExpected() {
        List<Guest> randomTwentyPremiumGuests = randomTwentyPremiumGuests();
        List<Guest> randomTwentyEconomyGuests = randomTwentyEconomyGuests();

        return Stream.of(
                Arguments.of(10, 10, customList(1.00, 2.00, 3.00, 4.00, 5.00), 0, money(0), 5, money(15)),
                Arguments.of(10, 1, customList(1.00, 2.00, 3.00, 4.00, 5.00), 4, money(14), 1, money(1)),
                Arguments.of(10, 0, customList(1.00, 2.00, 3.00, 4.00, 5.00), 5, money(15), 0, money(0)),
                Arguments.of(4, 0, customList(1.00, 2.00, 3.00, 4.00, 5.00), 4, money(14), 0, money(0)),
                Arguments.of(3, 0, customList(1.00, 2.00, 3.00, 4.00, 5.00), 3, money(12), 0, money(0)),
                Arguments.of(3, 1, customList(1.00, 2.00, 3.00, 4.00, 5.00), 3, money(12), 1, money(2)),
                Arguments.of(3, 2, customList(1.00, 2.00, 3.00, 4.00, 5.00), 3, money(12), 2, money(3)),
                Arguments.of(2, 3, customList(1.00, 2.00, 3.00, 4.00, 5.00), 2, money(9), 3, money(6)),
                Arguments.of(0, 3, customList(1.00, 2.00, 3.00, 4.00, 5.00), 0, money(0), 3, money(12)),
                Arguments.of(1, 3, customList(1.00, 2.00, 3.00, 4.00, 5.00), 1, money(5), 3, money(9)),

                Arguments.of(10, 10, customList(1.00, 2.00, 3.00, 4.00, 5.00, 100.00, 200.00, 300.00), 3, money(600), 5, money(15)),
                Arguments.of(10, 1, customList(1.00, 2.00, 3.00, 4.00, 5.00, 100.00, 200.00, 300.00), 7, money(600 + 5 + 4 + 3 + 2), 1, money(1)),
                Arguments.of(10, 0, customList(1.00, 2.00, 3.00, 4.00, 5.00, 100.00, 200.00, 300.00), 8, money(600 + 5 + 4 + 3 + 2 + 1), 0, money(0)),
                Arguments.of(4, 0, customList(1.00, 2.00, 3.00, 4.00, 5.00, 100.00, 200.00, 300.00), 4, money(605), 0, money(0)),
                Arguments.of(3, 0, customList(1.00, 2.00, 3.00, 4.00, 5.00, 100.00, 200.00, 300.00), 3, money(600), 0, money(0)),
                Arguments.of(3, 1, customList(1.00, 2.00, 3.00, 4.00, 5.00, 100.00, 200.00, 300.00), 3, money(600), 1, money(5)),
                Arguments.of(3, 2, customList(1.00, 2.00, 3.00, 4.00, 5.00, 100.00, 200.00, 300.00), 3, money(600), 2, money(9)),
                Arguments.of(2, 3, customList(1.00, 2.00, 3.00, 4.00, 5.00, 100.00, 200.00, 300.00), 2, money(500), 3, money(12)),
                Arguments.of(0, 3, customList(1.00, 2.00, 3.00, 4.00, 5.00, 100.00, 200.00, 300.00), 0, money(0), 3, money(12)),
                Arguments.of(1, 3, customList(1.00, 2.00, 3.00, 4.00, 5.00, 100.00, 200.00, 300.00), 1, money(300), 3, money(12)),

                Arguments.of(20, 10, randomTwentyPremiumGuests, 20, randomTwentyPremiumGuests.stream().map(Guest::offer).reduce(BigDecimal.ZERO, BigDecimal::add), 0, money(0)),
                Arguments.of(20, 0, randomTwentyPremiumGuests, 20, randomTwentyPremiumGuests.stream().map(Guest::offer).reduce(BigDecimal.ZERO, BigDecimal::add), 0, money(0)),
                Arguments.of(1, 0, randomTwentyPremiumGuests, 1, randomTwentyPremiumGuests.stream().map(Guest::offer).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO), 0, money(0)),
                Arguments.of(2, 0, randomTwentyPremiumGuests, 2, randomTwentyPremiumGuests.stream().map(Guest::offer).sorted(Comparator.reverseOrder()).limit(2).reduce(BigDecimal.ZERO, BigDecimal::add), 0, money(0)),
                Arguments.of(2, 100, randomTwentyPremiumGuests, 2, randomTwentyPremiumGuests.stream().map(Guest::offer).sorted(Comparator.reverseOrder()).limit(2).reduce(BigDecimal.ZERO, BigDecimal::add), 0, money(0)),
                Arguments.of(50, 100, randomTwentyPremiumGuests, 20, randomTwentyPremiumGuests.stream().map(Guest::offer).reduce(BigDecimal.ZERO, BigDecimal::add), 0, money(0)),
                Arguments.of(50, 0, randomTwentyPremiumGuests, 20, randomTwentyPremiumGuests.stream().map(Guest::offer).reduce(BigDecimal.ZERO, BigDecimal::add), 0, money(0)),

                Arguments.of(10, 20, randomTwentyEconomyGuests, 0, money(0), 20, randomTwentyEconomyGuests.stream().map(Guest::offer).reduce(BigDecimal.ZERO, BigDecimal::add)),
                Arguments.of(0, 20, randomTwentyEconomyGuests, 0, money(0), 20, randomTwentyEconomyGuests.stream().map(Guest::offer).reduce(BigDecimal.ZERO, BigDecimal::add)),
                Arguments.of(0, 2, randomTwentyEconomyGuests, 0, money(0), 2, randomTwentyEconomyGuests.stream().map(Guest::offer).sorted(Comparator.reverseOrder()).limit(2).reduce(BigDecimal.ZERO, BigDecimal::add)),
                Arguments.of(0, 1, randomTwentyEconomyGuests, 0, money(0), 1, randomTwentyEconomyGuests.stream().map(Guest::offer).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO)),
                Arguments.of(100, 50, randomTwentyEconomyGuests, 0, money(0), 20, randomTwentyEconomyGuests.stream().map(Guest::offer).reduce(BigDecimal.ZERO, BigDecimal::add)),
                Arguments.of(0, 50, randomTwentyEconomyGuests, 0, money(0), 20, randomTwentyEconomyGuests.stream().map(Guest::offer).limit(20).reduce(BigDecimal.ZERO, BigDecimal::add)),
                Arguments.of(100, 0, randomTwentyEconomyGuests, 20, randomTwentyEconomyGuests.stream().map(Guest::offer).reduce(BigDecimal.ZERO, BigDecimal::add), 0, money(0)),
                Arguments.of(1, 2, randomTwentyEconomyGuests,
                        1, randomTwentyEconomyGuests.stream().map(Guest::offer).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO),
                        2, randomTwentyEconomyGuests.stream().map(Guest::offer).sorted(Comparator.reverseOrder()).skip(1).limit(2).reduce(BigDecimal.ZERO, BigDecimal::add)),
                Arguments.of(7, 7, randomTwentyEconomyGuests,
                        7, randomTwentyEconomyGuests.stream().map(Guest::offer).sorted(Comparator.reverseOrder()).limit(7).reduce(BigDecimal.ZERO, BigDecimal::add),
                        7, randomTwentyEconomyGuests.stream().map(Guest::offer).sorted(Comparator.reverseOrder()).skip(7).limit(7).reduce(BigDecimal.ZERO, BigDecimal::add))    ,
                Arguments.of(16, 4, randomTwentyEconomyGuests,
                        16, randomTwentyEconomyGuests.stream().map(Guest::offer).sorted(Comparator.reverseOrder()).limit(16).reduce(BigDecimal.ZERO, BigDecimal::add),
                        4, randomTwentyEconomyGuests.stream().map(Guest::offer).sorted(Comparator.reverseOrder()).skip(16).limit(4).reduce(BigDecimal.ZERO, BigDecimal::add))
        );
    }

    public static List<Guest> customList(Double... guestsOffer) {
        return Stream.of(guestsOffer)
                .map(BigDecimal::valueOf)
                .map(Guest::new)
                .toList();
    }
}
