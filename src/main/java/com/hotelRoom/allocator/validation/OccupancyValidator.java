package com.hotelRoom.allocator.validation;

import com.hotelRoom.allocator.api.dto.HotelInputDTO;
import com.hotelRoom.allocator.api.dto.OccupancyRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@AllArgsConstructor
@Component
public class OccupancyValidator {
    private final Validator validator;

    public void validate(OccupancyRequestDTO occupancyRequestDTO) {
        Set<ConstraintViolation<OccupancyRequestDTO>> violations = validator.validate(occupancyRequestDTO);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Validation failed for fields: ");
            for (ConstraintViolation<OccupancyRequestDTO> violation : violations) {
                errorMessage.append(violation.getPropertyPath()).append(": ").append(violation.getMessage());
            }
            throw new ConstraintViolationException(errorMessage.toString(), violations);
        }
    }

    public void validate(HotelInputDTO hotelInputDTO) {
        Set<ConstraintViolation<HotelInputDTO>> violations = validator.validate(hotelInputDTO);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Validation failed for fields: ");
            for (ConstraintViolation<HotelInputDTO> violation : violations) {
                errorMessage.append(violation.getPropertyPath()).append(": ").append(violation.getMessage());
            }
            throw new ConstraintViolationException(errorMessage.toString(), violations);
        }
    }
}
