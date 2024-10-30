package com.hotelRoom.allocator.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DoubleArrayValidator implements ConstraintValidator<ValidDoubleArray, List<Double>> {

    @Override
    public boolean isValid(List<Double> value, ConstraintValidatorContext context) {

        if (value == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("cannot be a null list").addConstraintViolation();
            return false;
        }

        for (Double num : value) {
            if (num == null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("cannot contain null values").addConstraintViolation();
                return false;
            }
            if (!isFinite(num)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("value must be a finite number").addConstraintViolation();
                return false;
            }
        }

        return true;
    }

    private boolean isFinite(double value) {
        return !Double.isNaN(value) && !Double.isInfinite(value);
    }
}
