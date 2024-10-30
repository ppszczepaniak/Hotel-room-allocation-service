package com.hotelRoom.allocator.api.controller;

import com.hotelRoom.allocator.api.dto.AllocationResultDTO;
import com.hotelRoom.allocator.api.dto.HotelInputDTO;
import com.hotelRoom.allocator.api.dto.OccupancyRequestDTO;
import com.hotelRoom.allocator.api.mapper.OccupancyMapper;
import com.hotelRoom.allocator.domain.service.GuestAllocatorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static com.hotelRoom.allocator.TestBase.randomTwentyGuests;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OccupancyController.class)
class OccupancyControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    GuestAllocatorService guestAllocatorService;

    @MockBean
    OccupancyMapper occupancyMapper;

    private static final List<Double> someOffers = List.of(1.0, 2.0, 3.0);

    @Test
    void whenInvalidPremiumInput_thenReturns400WithPremiumErrorMessage() throws Exception {
        HotelInputDTO negativeRooms = new HotelInputDTO(-1, 1, someOffers);
        mockMvc.perform(post("/occupancy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(negativeRooms)))
                .andDo(print())
                .andExpect(jsonPath("$.error").value("field `premiumRooms`: must be greater than or equal to 0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenOtherInvalidEconomyInput_thenReturns400WithEconomyErrorMessage() throws Exception {
        HotelInputDTO negativeRooms = new HotelInputDTO(0, -1, someOffers);
        mockMvc.perform(post("/occupancy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(negativeRooms)))
                .andDo(print())
                .andExpect(jsonPath("$.error").value("field `economyRooms`: must be greater than or equal to 0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenInternalServerError_thenReturns500WithErrorMessage() throws Exception {
        HotelInputDTO validRequest = new HotelInputDTO(5, 10, List.of(1.0, 2.0, 3.0));
        when(occupancyMapper.toDomain(validRequest)).thenReturn(new OccupancyRequestDTO(1, 2, randomTwentyGuests()));
        when(guestAllocatorService.allocateGuests(any(OccupancyRequestDTO.class)))
                .thenThrow(new RuntimeException("Simulated exception"));

        mockMvc.perform(post("/occupancy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("other internal server error; please try again later"));
    }

    @Test
    void shouldReturnAllocationResultForValidRequest() throws Exception {
        HotelInputDTO someRequest = new HotelInputDTO(5, 10, someOffers);
        when(occupancyMapper.toDomain(someRequest)).thenReturn(new OccupancyRequestDTO(1, 2, randomTwentyGuests()));

        when(guestAllocatorService.allocateGuests(any(OccupancyRequestDTO.class)))
                .thenReturn(new AllocationResultDTO(1, BigDecimal.valueOf(2), 3, BigDecimal.valueOf(4)));

        mockMvc.perform(post("/occupancy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(someRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usagePremium").value(1))
                .andExpect(jsonPath("$.revenuePremium").value(2.00))
                .andExpect(jsonPath("$.usageEconomy").value(3))
                .andExpect(jsonPath("$.revenueEconomy").value(4.00));
    }
}
