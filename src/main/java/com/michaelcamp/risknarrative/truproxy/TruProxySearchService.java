package com.michaelcamp.risknarrative.truproxy;

import com.michaelcamp.risknarrative.data.Company;
import com.michaelcamp.risknarrative.data.CompanySearchRequest;
import com.michaelcamp.risknarrative.data.CompanySearchResult;
import com.michaelcamp.risknarrative.data.Officer;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Service
@AllArgsConstructor
public class TruProxySearchService {
    private final Logger logger = LoggerFactory.getLogger(TruProxySearchService.class);
    private TruProxyWebClient webClient;

    public CompanySearchResult findCompanies(
            CompanySearchRequest searchRequest, String apiKey, boolean onlyActiveCompanies) {

        logger.info("Fining companies in TruProxy with search {} and api {}", searchRequest, apiKey);

        List<Company> companies = webClient.searchCompanies(searchRequest, apiKey);

        Predicate<Company> companyFilter = buildCompanyFilter(onlyActiveCompanies);

        List<Company> filteredCompanies = companies.stream()
                .filter(companyFilter)
                .map(c -> addOfficers(c, apiKey))
                .toList();

        return CompanySearchResult.of(filteredCompanies);
    }

    private Predicate<Company> buildCompanyFilter(boolean onlyActiveCompanies) {
        return onlyActiveCompanies ?
                Company::checkActive
                : c -> true;
    }

    private Company addOfficers(Company company, String apiKey) {
        company.setOfficers(findActiveOfficers(company, apiKey));
        return company;
    }

    private List<Officer> findActiveOfficers(Company company, String apiKey) {
        return findOfficers(company, apiKey).stream()
                .filter(Officer::checkActive)
                .toList();
    }

    private List<Officer> findOfficers(Company company, String apiKey) {
        List<Officer> officers = webClient.findOfficers(company, apiKey);
        return officers != null ? officers : Collections.emptyList();
    }
}
