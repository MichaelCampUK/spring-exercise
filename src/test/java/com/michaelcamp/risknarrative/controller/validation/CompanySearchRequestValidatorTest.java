package com.michaelcamp.risknarrative.controller.validation;

import com.michaelcamp.risknarrative.data.CompanySearchRequest;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

public class CompanySearchRequestValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private CompanySearchRequest searchRequest;

    private CompanySearchRequestValidator validator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        validator = new CompanySearchRequestValidator();
    }

    @Test
    public void notValidIfRequestNull() {
        assertThat(validator.isValid(null, context)).isFalse();
    }

    @Test
    public void notValidIfRequestEmpty() {
        assertThat(validator.isValid(searchRequest, context)).isFalse();
    }

    @Test
    public void notValidIfCheckFalse() {
        Mockito.when(searchRequest.checkValid()).thenReturn(false);
        assertThat(validator.isValid(searchRequest, context)).isFalse();
    }

    @Test
    public void validIfCheckTrue() {
        Mockito.when(searchRequest.checkValid()).thenReturn(true);
        assertThat(validator.isValid(searchRequest, context)).isTrue();
    }
}
