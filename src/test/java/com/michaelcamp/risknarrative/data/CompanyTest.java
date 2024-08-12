package com.michaelcamp.risknarrative.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

public class CompanyTest {

    private Company company;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        company = null;
    }

    @Test
    public void checkActiveFalseIfCompanyStatusNull() {
        company = Company.builder().company_status(null).build();
        assertThat(company.checkActive()).isFalse();
    }

    @Test
    public void checkActiveFalseIfCompanyStatusNotActive() {
        company = Company.builder().company_status("not_an_active_status").build();
        assertThat(company.checkActive()).isFalse();
    }

    @Test
    public void checkActiveTrueIfCompanyStatusActive() {
        company = Company.builder().company_status("active").build();
        assertThat(company.checkActive()).isTrue();
    }

    @Test
    public void checkActiveTrueWhenUpperCase() {
        company = Company.builder().company_status("ACTIVE").build();
        assertThat(company.checkActive()).isTrue();
    }
}
