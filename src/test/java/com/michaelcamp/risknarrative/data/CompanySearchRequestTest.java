package com.michaelcamp.risknarrative.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

public class CompanySearchRequestTest {

    private CompanySearchRequest companySearchRequest;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        companySearchRequest = null;
    }

    @Test
    public void checkValidFalseWhenRequestEmpty() {
        companySearchRequest = CompanySearchRequest.builder().build();
        assertThat(companySearchRequest.checkValid()).isFalse();
    }

    @Test
    public void checkValidFalseWhenNameAndNumberNull() {
        companySearchRequest = CompanySearchRequest.builder()
                .companyName(null)
                .companyNumber(null)
                .build();
        assertThat(companySearchRequest.checkValid()).isFalse();
    }

    @Test
    public void checkValidTrueWhenNameNotNull() {
        companySearchRequest = CompanySearchRequest.builder()
                .companyName("someName")
                .build();
        assertThat(companySearchRequest.checkValid()).isTrue();
    }

    @Test
    public void checkValidTrueWhenNumberNotNull() {
        companySearchRequest = CompanySearchRequest.builder()
                .companyNumber("someNumber")
                .build();
        assertThat(companySearchRequest.checkValid()).isTrue();
    }

    @Test
    public void checkValidTrueWhenNameAndNumberNotNull() {
        companySearchRequest = CompanySearchRequest.builder()
                .companyName("someName")
                .companyNumber("someNumber")
                .build();
        assertThat(companySearchRequest.checkValid()).isTrue();
    }

    @Test
    public void calcSearchTermUsesNumberWhenAvailable() {
        companySearchRequest = CompanySearchRequest.builder()
                .companyName("someName")
                .companyNumber("someNumber")
                .build();
        assertThat(companySearchRequest.calcSearchTerm()).isEqualTo("someNumber");
    }

    @Test
    public void calcSearchTermUsesNameWhenNumberNotAvailable() {
        companySearchRequest = CompanySearchRequest.builder()
                .companyName("someName")
                .build();
        assertThat(companySearchRequest.calcSearchTerm()).isEqualTo("someName");
    }
}
