package com.hotelRoom.allocator.api.mapper;

import com.hotelRoom.allocator.api.dto.AllocationResultDTO;
import com.hotelRoom.allocator.api.dto.HotelInputDTO;
import com.hotelRoom.allocator.api.dto.OccupancyRequestDTO;
import com.hotelRoom.allocator.domain.model.Guest;
import com.hotelRoom.allocator.domain.model.HotelContext;
import com.hotelRoom.allocator.domain.model.MoneyFactory;
import com.hotelRoom.allocator.domain.model.RoomType;
import com.hotelRoom.allocator.validation.OccupancyValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class OccupancyMapper {
    private final OccupancyValidator occupancyValidator;

    public OccupancyRequestDTO toDomain(HotelInputDTO dto) {
        log.debug("Mapping HotelInputDTO to OccupancyRequestDTO: {}", dto);
        occupancyValidator.validate(dto);

        List<Guest> guests = dto.potentialGuests().stream()
                .map(amountDouble -> new Guest(MoneyFactory.createMoney(amountDouble).amount()))
                .toList();
        int freePremium = dto.premiumRooms();
        int freeEconomy = dto.economyRooms();

        log.debug("Converting to OccupancyRequestDTO with {} premium rooms and {} economy rooms.", freePremium, freeEconomy);
        OccupancyRequestDTO occupancyRequestDTO = new OccupancyRequestDTO(freePremium, freeEconomy, guests);
        occupancyValidator.validate(occupancyRequestDTO);

        return occupancyRequestDTO;
    }

    public AllocationResultDTO toResult(HotelContext hotelContext) {
        log.debug("Mapping HotelContext to AllocationResultDTO: {}", hotelContext);

        return AllocationResultDTO.builder()
                .usagePremium(hotelContext.currentlyTaken(RoomType.PREMIUM))
                .revenuePremium(hotelContext.currentRevenue(RoomType.PREMIUM).amount())
                .usageEconomy(hotelContext.currentlyTaken(RoomType.ECONOMY))
                .revenueEconomy(hotelContext.currentRevenue(RoomType.ECONOMY).amount())
                .build();
    }
}
