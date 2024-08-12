package com.michaelcamp.risknarrative.truproxy;

import com.michaelcamp.risknarrative.data.Officer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TruProxyOfficerResponse {
    private List<Officer> items;
}
