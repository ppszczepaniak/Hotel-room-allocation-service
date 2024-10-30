package com.hotelRoom.allocator;

import com.hotelRoom.allocator.api.controller.OccupancyController;
import com.hotelRoom.allocator.api.mapper.OccupancyMapper;
import com.hotelRoom.allocator.config.CurrencyConfig;
import com.hotelRoom.allocator.domain.model.MoneyFactory;
import com.hotelRoom.allocator.domain.service.GuestAllocatorService;
import com.hotelRoom.allocator.validation.DoubleArrayValidator;
import com.hotelRoom.allocator.validation.OccupancyValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ChallengeApplicationTests {
    @Autowired
    GuestAllocatorService guestAllocatorService;
    @Autowired
    OccupancyController occupancyController;
    @Autowired
    OccupancyMapper occupancyMapper;
    @Autowired
    MoneyFactory moneyFactory;
    @Autowired
    CurrencyConfig currencyConfig;
    @Autowired
    DoubleArrayValidator doubleArrayValidator;
    @Autowired
    OccupancyValidator occupancyValidator;


    @Test
    void contextLoads() {
		assertThat(this).isNotNull();
		assertAll("Bean Initialization",
                () -> assertNotNull(guestAllocatorService, "GuestAllocatorService should not be null"),
                () -> assertNotNull(occupancyController, "OccupancyController should not be null"),
                () -> assertNotNull(occupancyMapper, "OccupancyMapper should not be null"),
                () -> assertNotNull(moneyFactory, "MoneyFactory should not be null"),
                () -> assertNotNull(currencyConfig, "CurrencyConfig should not be null"),
                () -> assertNotNull(doubleArrayValidator, "DoubleArrayValidator should not be null"),
                () -> assertNotNull(occupancyValidator, "OccupancyValidator should not be null")
        );
    }
}
