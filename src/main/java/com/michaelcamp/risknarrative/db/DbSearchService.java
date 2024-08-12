package com.michaelcamp.risknarrative.db;

import com.michaelcamp.risknarrative.data.Company;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DbSearchService {
    private final Logger logger = LoggerFactory.getLogger(DbSearchService.class);
    private CompanyRepository companyRepository;

    public Optional<Company> findCompany(String companyNumber) {
        logger.info("Finding company in DB with companyNumber {}", companyNumber);
        return companyRepository.findById(companyNumber);
    }

    public void saveCompany(Company company) {
        logger.info("Saving company in DB {}", company);
        companyRepository.save(company);
    }

}
