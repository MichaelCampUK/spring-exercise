package com.michaelcamp.risknarrative.db;

import com.michaelcamp.risknarrative.data.Company;
import org.springframework.data.repository.CrudRepository;

public interface CompanyRepository extends CrudRepository<Company, String> {
}
