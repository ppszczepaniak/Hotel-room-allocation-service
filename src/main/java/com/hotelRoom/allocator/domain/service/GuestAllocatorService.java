package com.hotelRoom.allocator.domain.service;

import com.hotelRoom.allocator.api.dto.AllocationResultDTO;
import com.hotelRoom.allocator.api.dto.OccupancyRequestDTO;
import com.hotelRoom.allocator.domain.model.Guest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GuestAllocatorService {
    AllocationResultDTO allocateGuests(OccupancyRequestDTO request);

    List<Guest> sortGuests(List<Guest> guests);
}
