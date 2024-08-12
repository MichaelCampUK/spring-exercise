package com.michaelcamp.risknarrative.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
public class Officer {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String officer_role;
    private String appointed_on;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String resigned_on;
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    public boolean checkActive() {
        return resigned_on == null;
    }
}
