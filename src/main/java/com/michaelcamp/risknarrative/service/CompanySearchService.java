package com.michaelcamp.risknarrative.service;

import com.michaelcamp.risknarrative.data.Company;
import com.michaelcamp.risknarrative.data.CompanySearchRequest;
import com.michaelcamp.risknarrative.data.CompanySearchResult;
import com.michaelcamp.risknarrative.db.DbSearchService;
import com.michaelcamp.risknarrative.truproxy.TruProxySearchService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CompanySearchService {
    private final Logger logger = LoggerFactory.getLogger(CompanySearchService.class);
    private final TruProxySearchService truProxySearchService;
    private DbSearchService dbSearchService;

    public CompanySearchResult findCompanies(CompanySearchRequest searchRequest, String apiKey,
                                             boolean onlyActiveCompanies) {
        logger.info("Finding companies in DB or TruProxy with search {} and api {}", searchRequest, apiKey);

        Optional<CompanySearchResult> dbCompanies =
                searchRequest.getCompanyNumber() != null ?
                        findDbCompanies(searchRequest) : Optional.empty();

        return dbCompanies.orElseGet(() ->
                findTruProxyCompanies(searchRequest, apiKey, onlyActiveCompanies));
    }

    private Optional<CompanySearchResult> findDbCompanies(CompanySearchRequest searchRequest) {
        logger.info("Finding company in DB with search {}", searchRequest);

        Optional<Company> company = dbSearchService.findCompany(searchRequest.getCompanyNumber());

        return company.map(CompanySearchResult::of);
    }

    private CompanySearchResult findTruProxyCompanies(
            CompanySearchRequest searchRequest, String apiKey, boolean onlyActiveCompanies) {

        final CompanySearchResult companySearchResult =
                truProxySearchService.findCompanies(searchRequest, apiKey, onlyActiveCompanies);

        companySearchResult.getItems().forEach(dbSearchService::saveCompany);

        return companySearchResult;
    }
}
