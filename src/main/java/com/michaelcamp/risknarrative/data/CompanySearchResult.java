package com.michaelcamp.risknarrative.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanySearchResult {
    private int total_results;
    private List<Company> items;

    public static CompanySearchResult of(Company company) {
        return of(List.of(company));
    }

    public static CompanySearchResult of(List<Company> companies) {
        return CompanySearchResult.builder()
                .items(companies)
                .total_results(companies.size())
                .build();
    }
}
