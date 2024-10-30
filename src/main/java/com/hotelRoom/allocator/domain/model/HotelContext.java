package com.hotelRoom.allocator.domain.model;

import com.hotelRoom.allocator.domain.exceptions.HotelContextException;
import com.hotelRoom.allocator.domain.exceptions.RoomAllocationException;
import lombok.Builder;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

import static com.hotelRoom.allocator.domain.model.MoneyFactory.ZERO;

@Builder(toBuilder = true)
public record HotelContext(List<Room> availableRooms,
                           List<Room> takenRooms,
                           Map<RoomType, Money> revenue,
                           int remainingOffers) {

    public static HotelContext createContext(int availablePremiumRooms, int availableEconomyRooms, int remainingOffers) {
        if (availablePremiumRooms < 0 || availableEconomyRooms < 0 || remainingOffers <= 0) {
            throw new HotelContextException("Invalid room numbers or offers provided for hotel context creation.");
        }

        List<Room> availableRooms = Stream.concat(
                Stream.generate(() -> new Room(RoomType.PREMIUM)).limit(availablePremiumRooms),
                Stream.generate(() -> new Room(RoomType.ECONOMY)).limit(availableEconomyRooms)
        ).toList();

        List<Room> takenRooms = Collections.emptyList();

        Map<RoomType, Money> revenue = new EnumMap<>(RoomType.class);
        revenue.put(RoomType.PREMIUM, ZERO);
        revenue.put(RoomType.ECONOMY, ZERO);

        return new HotelContext(availableRooms, takenRooms, revenue, remainingOffers);
    }

    public HotelContext allocateToPremiumRoomWith(@NonNull BigDecimal amount) throws RoomAllocationException {
        return allocateRoom(RoomType.PREMIUM, amount);
    }

    public HotelContext allocateToEconomyRoomWith(@NonNull BigDecimal amount) throws RoomAllocationException {
        return allocateRoom(RoomType.ECONOMY, amount);
    }

    private HotelContext allocateRoom(RoomType roomType, @NonNull BigDecimal amount) throws RoomAllocationException {
        if (!isRoomAvailable(roomType)) {
            throw new RoomAllocationException(String.format("Trying to allocate roomType %s, but no such rooms are available.", roomType.name()));
        }

        List<Room> updatedAvailableRooms = removeRoom(availableRooms, roomType);
        List<Room> updatedTakenRooms = addRoom(takenRooms, roomType);
        Map<RoomType, Money> updatedRevenue = incrementRevenueFor(roomType, MoneyFactory.createMoney(amount));

        return this.toBuilder()
                .availableRooms(updatedAvailableRooms)
                .takenRooms(updatedTakenRooms)
                .revenue(updatedRevenue)
                .remainingOffers(remainingOffers - 1)
                .build();
    }

    public boolean hasAvailableRooms() {
        return isRoomAvailable(RoomType.PREMIUM) || isRoomAvailable(RoomType.ECONOMY);
    }

    public boolean isPremiumAvailable() {
        return isRoomAvailable(RoomType.PREMIUM);
    }

    public boolean isEconomyAvailable() {
        return isRoomAvailable(RoomType.ECONOMY);
    }

    private boolean isRoomAvailable(RoomType roomType) {
        return availableRooms.stream().anyMatch(room -> room.roomType().equals(roomType));
    }

    public Integer currentlyAvailable(RoomType roomType) {
        return (int) availableRooms.stream().filter(room -> room.roomType().equals(roomType)).count();
    }

    public Integer currentlyTaken(RoomType roomType) {
        return (int) takenRooms.stream().filter(room -> room.roomType().equals(roomType)).count();
    }

    private List<Room> removeRoom(List<Room> rooms, RoomType roomType) {
        List<Room> updatedRooms = new ArrayList<>(rooms);

        updatedRooms.stream()
                .filter(room -> room.roomType().equals(roomType))
                .findFirst()
                .ifPresent(updatedRooms::remove);

        return updatedRooms;
    }

    private List<Room> addRoom(List<Room> rooms, RoomType roomType) {
        List<Room> updatedRooms = new ArrayList<>(rooms);
        updatedRooms.add(new Room(roomType));
        return updatedRooms;
    }

    private Map<RoomType, Money> incrementRevenueFor(RoomType roomType, Money amount) {
        Map<RoomType, Money> updatedRevenue = new EnumMap<>(revenue);
        updatedRevenue.put(roomType, currentRevenue(roomType).add(amount));
        return updatedRevenue;
    }

    public Money currentRevenue(RoomType roomType) {
        return revenue.get(roomType);
    }
}

