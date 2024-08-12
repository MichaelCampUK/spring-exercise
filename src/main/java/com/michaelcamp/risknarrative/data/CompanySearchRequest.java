package com.michaelcamp.risknarrative.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanySearchRequest {
    private String companyName;
    private String companyNumber;

    public String calcSearchTerm() {
        return companyNumber != null ? companyNumber : companyName;
    }

    public boolean checkValid() {
        return companyName != null || companyNumber != null;
    }
}
