package com.michaelcamp.risknarrative.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Company {
    @Id
    @JsonProperty("company_number")
    private String companyNumber;
    private String company_type;
    private String title;
    private String company_status;
    private String date_of_creation;
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Officer> officers;

    public boolean checkActive() {
        return "active".equalsIgnoreCase(company_status);
    }
}
