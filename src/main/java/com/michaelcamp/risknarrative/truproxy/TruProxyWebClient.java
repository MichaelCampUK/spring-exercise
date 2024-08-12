package com.michaelcamp.risknarrative.truproxy;

import com.michaelcamp.risknarrative.data.Company;
import com.michaelcamp.risknarrative.data.CompanySearchRequest;
import com.michaelcamp.risknarrative.data.Officer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Service
public class TruProxyWebClient {

    private final Logger logger = LoggerFactory.getLogger(TruProxyWebClient.class);

    @Value("${clientUrl}")
    private String clientUrl;

    @Value("${searchUri}")
    private String searchUri;

    @Value("${officersUri}")
    private String officersUri;

    @Value("${clientApiKey}")
    private String clientApiKey;

    @Autowired
    private RestTemplate restTemplate;

    public List<Company> searchCompanies(CompanySearchRequest searchRequest, String apiKey) {

        logger.info("Finding companies in TruProxy with search {} and api {}", searchRequest, apiKey);

        ResponseEntity<TruProxyCompanyResponse> result =
                restTemplate.exchange(clientUrl + searchUri, HttpMethod.GET,
                        buildHttpEntity(apiKey), TruProxyCompanyResponse.class,
                        searchRequest.calcSearchTerm());

        logger.info("Found companies {}", result.getBody());

        return requireNonNull(result.getBody()).getItems();
    }

    private HttpEntity<String> buildHttpEntity(String apiKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(clientApiKey, apiKey);
        return new HttpEntity<>(headers);
    }

    public List<Officer> findOfficers(Company company, String apiKey) {

        logger.info("Finding officers in TruProxy with company {} and api {}", company, apiKey);

        ResponseEntity<TruProxyOfficerResponse> result =
                restTemplate.exchange(clientUrl + officersUri, HttpMethod.GET,
                        buildHttpEntity(apiKey), TruProxyOfficerResponse.class,
                        company.getCompanyNumber());

        logger.info("Found officers {}", result.getBody());

        return requireNonNull(result.getBody()).getItems();
    }

}
