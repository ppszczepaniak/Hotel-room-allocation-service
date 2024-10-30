package com.hotelRoom.allocator.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class DoubleArrayValidatorTest {

    private DoubleArrayValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new DoubleArrayValidator();
        context = mock(ConstraintValidatorContext.class);

        ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        doNothing().when(context).disableDefaultConstraintViolation();
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
    }

    @Test
    void shouldReturnFalseWhenListIsNull() {
        assertFalse(validator.isValid(null, context));
        verify(context).buildConstraintViolationWithTemplate("cannot be a null list");
    }

    @Test
    void shouldReturnTrueWhenListIsEmpty() {
        assertTrue(validator.isValid(Collections.emptyList(), context));
    }

    @Test
    void shouldReturnFalseWhenListContainsNull() {
        List<Double> listWithNull = Arrays.asList(1.0, null, 2.0);
        assertFalse(validator.isValid(listWithNull, context));
        verify(context).buildConstraintViolationWithTemplate("cannot contain null values");
    }

    @Test
    void shouldReturnFalseWhenListContainsNaN() {
        List<Double> listWithNaN = Arrays.asList(1.0, Double.NaN, 2.0);
        assertFalse(validator.isValid(listWithNaN, context));
        verify(context).buildConstraintViolationWithTemplate("value must be a finite number");
    }

    @Test
    void shouldReturnFalseWhenListContainsInfinity() {
        List<Double> listWithInfinity = Arrays.asList(1.0, Double.POSITIVE_INFINITY, 2.0);
        assertFalse(validator.isValid(listWithInfinity, context));
        verify(context).buildConstraintViolationWithTemplate("value must be a finite number");
    }

    @Test
    void shouldReturnTrueWhenListContainsValidValues() {
        List<Double> validList = Arrays.asList(1.0, 2.0, 3.0);
        assertTrue(validator.isValid(validList, context));
    }
}
