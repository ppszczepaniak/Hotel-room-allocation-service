package com.hotelRoom.allocator.domain.service.impl;

import com.hotelRoom.allocator.api.dto.AllocationResultDTO;
import com.hotelRoom.allocator.api.dto.OccupancyRequestDTO;
import com.hotelRoom.allocator.api.mapper.OccupancyMapper;
import com.hotelRoom.allocator.domain.model.Guest;
import com.hotelRoom.allocator.domain.model.HotelContext;
import com.hotelRoom.allocator.domain.model.RoomType;
import com.hotelRoom.allocator.domain.service.GuestAllocatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GuestAllocatorServiceImpl implements GuestAllocatorService {
    @Value("${hotel.upgrade.policy}")
    private BigDecimal upgradeThreshold;
    private final OccupancyMapper occupancyMapper;

    @Override
    public List<Guest> sortGuests(List<Guest> guests) {
        return guests.stream()
                .sorted(Comparator.comparing(Guest::offer).reversed())
                .toList();
    }

    @Override
    public AllocationResultDTO allocateGuests(OccupancyRequestDTO request) {
        log.info("Allocating guests for request: {}", request);
        var guests = request.guests();
        var freePremium = request.freePremium();
        var freeEconomy = request.freeEconomy();

        if (guests.isEmpty()) {
            log.warn("No guests to allocate rooms for.");
            return emptyResult();
        }

        if (freePremium == 0 && freeEconomy == 0) {
            log.warn("No available rooms.");
            return emptyResult();
        }

        var sortedGuests = sortGuests(guests);
        return allocate(freePremium, freeEconomy, sortedGuests);
    }

    private AllocationResultDTO allocate(int freePremiumRooms, int freeEconomyRooms, List<Guest> sortedGuests) {
        log.info("Starting guests allocation. Free Premium rooms: {}, Free Economy rooms: {}, Guests: {}", freePremiumRooms, freeEconomyRooms, sortedGuests);

        var sortedOffers = sortedGuests.stream().map(Guest::offer).toList();
        log.debug("Sorted offers: {}", sortedOffers);

        var hotelContext = HotelContext.createContext(freePremiumRooms, freeEconomyRooms, sortedOffers.size());

        for (BigDecimal offer : sortedOffers) {
            log.debug("Processing offer: {}", offer);
            if (!hotelContext.hasAvailableRooms()) {
                log.info("No more available rooms, exiting allocation.");
                break;
            }

            if (isPremium(offer)) {
                hotelContext = tryToAllocateToPremiumRoom(offer, hotelContext);
            } else {
                hotelContext = tryToAllocateToEconomyRoom(offer, hotelContext);
            }
        }

        var result = occupancyMapper.toResult(hotelContext);
        log.info("Completed guests allocation. Result: {}", result);
        return result;
    }

    private boolean isPremium(BigDecimal offer) {
        return offer.compareTo(upgradeThreshold) >= 0;
    }

    private static HotelContext tryToAllocateToPremiumRoom(BigDecimal amount, HotelContext context) {
        if (!context.isPremiumAvailable()) {
            return context;
        }

        return context.allocateToPremiumRoomWith(amount);
    }

    private static HotelContext tryToAllocateToEconomyRoom(BigDecimal amount, HotelContext context) {
        if (isUpgradePossible(context)) {
            return tryToAllocateToPremiumRoom(amount, context);
        }

        if (!context.isEconomyAvailable()) {
            return context;
        }

        return context.allocateToEconomyRoomWith(amount);
    }

    private static boolean isUpgradePossible(HotelContext context) {
        return context.isPremiumAvailable() && context.remainingOffers() > context.currentlyAvailable(RoomType.ECONOMY);
    }

    private static AllocationResultDTO emptyResult() {
        return AllocationResultDTO.builder()
                .usagePremium(0)
                .revenuePremium(BigDecimal.ZERO)
                .usageEconomy(0)
                .revenueEconomy(BigDecimal.ZERO)
                .build();
    }
}
