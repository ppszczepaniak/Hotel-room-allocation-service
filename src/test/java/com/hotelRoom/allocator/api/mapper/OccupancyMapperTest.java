package com.hotelRoom.allocator.api.mapper;

import com.hotelRoom.allocator.api.dto.HotelInputDTO;
import com.hotelRoom.allocator.api.dto.OccupancyRequestDTO;
import com.hotelRoom.allocator.domain.model.Guest;
import com.hotelRoom.allocator.validation.OccupancyValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.hotelRoom.allocator.domain.service.impl.GuestAllocatorServiceImplTest.guestsListFromTheTask;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

class OccupancyMapperTest {
    @Mock
    private OccupancyValidator occupancyValidator;

    private OccupancyMapper occupancyMapper;
    private static final List<Double> taskOffers = guestsListFromTheTask().stream().map(Guest::offer).map(BigDecimal::doubleValue).toList();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        occupancyMapper = new OccupancyMapper(occupancyValidator);
    }

    @ParameterizedTest
    @MethodSource("test")
    void shouldMapHotelInputDTOToOccupancyRequestDTO(int premiumRooms, int economyRooms, List<Double> guestOffers) {
        HotelInputDTO hotelInputDTO = new HotelInputDTO(premiumRooms, economyRooms, guestOffers);
        OccupancyRequestDTO result = occupancyMapper.toDomain(hotelInputDTO);

        assertNotNull(result);
        assertEquals(premiumRooms, result.freePremium());
        assertEquals(economyRooms, result.freeEconomy());
        assertEquals(guestOffers.size(), result.guests().size());

        verify(occupancyValidator).validate(any(OccupancyRequestDTO.class));
        verify(occupancyValidator).validate(any(HotelInputDTO.class));
    }

    public static Stream<Arguments> test() {
        return Stream.of(
                Arguments.of(3, 5, Arrays.asList(100.0,150.0,200.0)),
                Arguments.of(0, 0, Collections.emptyList()),
                Arguments.of(10,5 , Arrays.asList(1000.0,1500.0,2000.0,3000.)),
                Arguments.of(-1,-1 , Arrays.asList(-1000.0,-1500.0)),
                Arguments.of(-1,-1 ,taskOffers)

        );
    }

}
