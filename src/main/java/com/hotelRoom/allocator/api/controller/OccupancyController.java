package com.hotelRoom.allocator.api.controller;

import com.hotelRoom.allocator.api.dto.AllocationResultDTO;
import com.hotelRoom.allocator.api.dto.HotelInputDTO;
import com.hotelRoom.allocator.api.dto.OccupancyRequestDTO;
import com.hotelRoom.allocator.api.mapper.OccupancyMapper;
import com.hotelRoom.allocator.domain.service.GuestAllocatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
@Tag(name = "Occupancy Management", description = "Operations related to guest occupancy allocation")
public class OccupancyController {
    private final GuestAllocatorService guestAllocatorService;
    private final OccupancyMapper occupancyMapper;



    @Operation(summary = "Calculate room occupancy",
            description = "Calculates the occupancy based on the provided hotel input.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation"),
                    @ApiResponse(responseCode = "400", description = "Bad Request - Validation errors",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping(path = "/occupancy", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AllocationResultDTO> calculateOccupancy(@Valid @RequestBody HotelInputDTO request) {
        log.info("Received request: {}", request);
        OccupancyRequestDTO occupancyRequestDTO = occupancyMapper.toDomain(request);
        AllocationResultDTO result = guestAllocatorService.allocateGuests(occupancyRequestDTO);
        log.debug("Allocation result: {}", result);

        return ResponseEntity.ok(result);
    }
}
