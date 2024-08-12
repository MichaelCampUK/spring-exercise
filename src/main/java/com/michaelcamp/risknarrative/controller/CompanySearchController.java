package com.michaelcamp.risknarrative.controller;

import com.michaelcamp.risknarrative.data.CompanySearchRequest;
import com.michaelcamp.risknarrative.data.CompanySearchResult;
import com.michaelcamp.risknarrative.service.CompanySearchService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companysearch")
public class CompanySearchController {
    private final Logger logger = LoggerFactory.getLogger(CompanySearchController.class);
    private final CompanySearchService companySearchService;

    public CompanySearchController(CompanySearchService companySearchService) {
        this.companySearchService = companySearchService;
    }

    @PostMapping(produces = "application/json")
    public CompanySearchResult findCompanies(@Valid @RequestBody CompanySearchRequest searchRequest,
                                             @RequestHeader("x-api-key") String apiKey,
                                             @RequestParam boolean onlyActiveCompanies) {
        logger.info("Finding companies with search {} and api {} and onlyActiveCompanies {}",
                searchRequest, apiKey, onlyActiveCompanies);
        return companySearchService.findCompanies(searchRequest, apiKey, onlyActiveCompanies);
    }
}

