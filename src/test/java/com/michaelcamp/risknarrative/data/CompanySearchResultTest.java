package com.michaelcamp.risknarrative.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CompanySearchResultTest {

    @Mock
    private Company companyOne;

    @Mock
    private Company companyTwo;

    @Mock
    private Company companyThree;

    private CompanySearchResult companySearchResult;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        companySearchResult = null;
    }

    @Test
    public void fieldsSetWhenGivenListOfResults() {
        companySearchResult = CompanySearchResult.of(List.of(companyOne, companyTwo));
        assertThat(companySearchResult.getItems()).hasSize(2);
        assertThat(companySearchResult.getItems()).contains(companyOne);
        assertThat(companySearchResult.getItems()).contains(companyTwo);
        assertThat(companySearchResult.getItems()).doesNotContain(companyThree);
        assertThat(companySearchResult.getTotal_results()).isEqualTo(2);
    }

    @Test
    public void fieldsSetWhenGivenSingleResult() {
        companySearchResult = CompanySearchResult.of(companyOne);
        assertThat(companySearchResult.getItems()).hasSize(1);
        assertThat(companySearchResult.getItems()).contains(companyOne);
        assertThat(companySearchResult.getItems()).doesNotContain(companyTwo);
        assertThat(companySearchResult.getItems()).doesNotContain(companyThree);
        assertThat(companySearchResult.getTotal_results()).isEqualTo(1);
    }
}
