package com.michaelcamp.risknarrative.truproxy;

import com.michaelcamp.risknarrative.data.Company;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TruProxyCompanyResponse {
    private List<Company> items;
}
