package com.hotelRoom.allocator.domain.model;

import com.hotelRoom.allocator.domain.exceptions.RoomAllocationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class HotelContextTest {

    private HotelContext hotelContext;

    @BeforeEach
    void setUp() {
        hotelContext = HotelContext.createContext(2, 3, 5);
    }

    @Test
    void shouldCreateValidHotelContext() {
        assertNotNull(hotelContext);
        assertEquals(2, hotelContext.currentlyAvailable(RoomType.PREMIUM));
        assertEquals(3, hotelContext.currentlyAvailable(RoomType.ECONOMY));
        assertEquals(5, hotelContext.remainingOffers());
        assertTrue(hotelContext.hasAvailableRooms());
    }

    @ParameterizedTest
    @CsvSource({
            "-1,1,2, Invalid room numbers or offers provided for hotel context creation.",
            "2,-3,2, Invalid room numbers or offers provided for hotel context creation.",
            "1,2,0, Invalid room numbers or offers provided for hotel context creation."
    })
    void shouldThrowExceptionForInvalidRoomCount(int premium, int eco, int offers, String errorMessage) {
        Exception exception = assertThrows(Exception.class, () -> HotelContext.createContext(premium, eco, offers));
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void shouldAllocatePremiumRoomSuccessfully() throws RoomAllocationException {
        BigDecimal allocationAmount = BigDecimal.valueOf(100.0000);
        HotelContext updatedContext = hotelContext.allocateToPremiumRoomWith(allocationAmount);

        assertNotNull(updatedContext);
        assertEquals(1, updatedContext.currentlyAvailable(RoomType.PREMIUM));
        assertEquals(1, updatedContext.currentlyTaken(RoomType.PREMIUM));
        assertEquals(MoneyFactory.createMoney(BigDecimal.valueOf(100.0)).amount(), updatedContext.currentRevenue(RoomType.PREMIUM).amount());
        assertEquals(4, updatedContext.remainingOffers());
    }

    @Test
    void shouldAllocateEconomyRoomSuccessfully() throws RoomAllocationException {
        BigDecimal allocationAmount = BigDecimal.valueOf(99.99);
        HotelContext updatedContext = hotelContext.allocateToEconomyRoomWith(allocationAmount);

        assertNotNull(updatedContext);
        assertEquals(2, updatedContext.currentlyAvailable(RoomType.ECONOMY));
        assertEquals(1, updatedContext.currentlyTaken(RoomType.ECONOMY));
        assertEquals(MoneyFactory.createMoney(BigDecimal.valueOf(99.99)).amount(), updatedContext.currentRevenue(RoomType.ECONOMY).amount());
        assertEquals(4, updatedContext.remainingOffers());
    }

    @Test
    void shouldThrowExceptionWhenAllocatingUnavailableRoom() {
        hotelContext = hotelContext.allocateToPremiumRoomWith(BigDecimal.valueOf(100.0));
        hotelContext = hotelContext.allocateToPremiumRoomWith(BigDecimal.valueOf(100.0));

        Exception exception = assertThrows(RoomAllocationException.class, () -> {
            hotelContext.allocateToPremiumRoomWith(BigDecimal.valueOf(100.0));
        });
        assertEquals("Trying to allocate roomType PREMIUM, but no such rooms are available.", exception.getMessage());
    }
}
