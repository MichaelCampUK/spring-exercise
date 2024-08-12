package com.michaelcamp.risknarrative.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.michaelcamp.risknarrative.data.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.okForContentType;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CompanySearchControllerIntegrationTest {

    private static final WireMockServer wireMockServer = new WireMockServer(9090);
    private static Company expectedCompanyOne;
    private static Company expectedCompanyTwo;
    private static Company expectedCompanyThree;
    private static Officer expectedOfficerOne;
    private static Officer expectedOfficerTwo;
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public static void setup() {
        wireMockServer.start();

        // Company response contains 2 active companies and 1 inactive
        createStubResponse("/search?Query=SomeCompanyName", "src/test/resources/company_SomeName.json");
        // Company response contains 1 active company
        createStubResponse("/search?Query=SomeNumber1", "src/test/resources/company_SomeNumber.json");
        // Officer responses contains 2 active officers and 1 inactive
        createStubResponse("/officers?CompanyNumber=SomeNumber1", "src/test/resources/officers_SomeNumber1.json");
        createStubResponse("/officers?CompanyNumber=SomeNumber2", "src/test/resources/officers_SomeNumber1.json");
        createStubResponse("/officers?CompanyNumber=SomeNumber3", "src/test/resources/officers_SomeNumber1.json");

        expectedOfficerOne = Officer.builder()
                .name("Officer 1 name")
                .address(Address.builder()
                        .premises("Officer 1 premises")
                        .address_line_1("Officer 1 address line")
                        .locality("Officer 1 locality")
                        .postal_code("Officer 1 postcode")
                        .country("Officer 1 country")
                        .build())
                .officer_role("Officer 1 role")
                .appointed_on("2017-04-01")
                .build();

        expectedOfficerTwo = Officer.builder()
                .name("Officer 2 name")
                .address(Address.builder()
                        .premises("Officer 2 premises")
                        .address_line_1("Officer 2 address line")
                        .locality("Officer 2 locality")
                        .postal_code("Officer 2 postcode")
                        .country("Officer 2 country")
                        .build())
                .officer_role("Officer 2 role")
                .appointed_on("2018-04-01")
                .build();

        expectedCompanyOne = Company.builder()
                .companyNumber("SomeNumber1")
                .company_type("ltd")
                .title("SomeCompany1Name")
                .company_status("active")
                .date_of_creation("2008-02-11")
                .address(Address.builder()
                        .premises("Some Number 1 premises")
                        .address_line_1("Some Number 1 address line")
                        .locality("Some Number 1 locality")
                        .postal_code("Some Number 1 postcode")
                        .country("Some Number 1 country")
                        .build())
                .officers(List.of(expectedOfficerOne, expectedOfficerTwo))
                .build();

        expectedCompanyTwo = Company.builder()
                .companyNumber("SomeNumber2")
                .company_type("ltd")
                .title("SomeCompany2Name")
                .company_status("active")
                .date_of_creation("2009-02-11")
                .address(Address.builder()
                        .premises("Some Number 2 premises")
                        .address_line_1("Some Number 2 address line")
                        .locality("Some Number 2 locality")
                        .postal_code("Some Number 2 postcode")
                        .country("Some Number 2 country")
                        .build())
                .officers(List.of(expectedOfficerOne, expectedOfficerTwo))
                .build();

        expectedCompanyThree = Company.builder()
                .companyNumber("SomeNumber3")
                .company_type("ltd")
                .title("SomeCompany3Name")
                .company_status("inactive")
                .date_of_creation("2010-02-11")
                .address(Address.builder()
                        .premises("Some Number 3 premises")
                        .address_line_1("Some Number 3 address line")
                        .locality("Some Number 3 locality")
                        .postal_code("Some Number 3 postcode")
                        .country("Some Number 3 country")
                        .build())
                .officers(List.of(expectedOfficerOne, expectedOfficerTwo))
                .build();
    }

    @AfterAll
    public static void teardown() {
        wireMockServer.stop();
    }

    private static void createStubResponse(String requestMapping, String filename) {
        try {
            wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo(requestMapping))
                    .willReturn(okForContentType("application/json",
                            Files.readString(Paths.get(filename)))));
        } catch (Exception ex) {
            throw new RuntimeException("Failed to create stub response", ex);
        }
    }

    private String sendRequest(boolean onlyActiveCompanies, CompanySearchRequest searchRequest) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("x-api-key", "someApiKey");
        headers.setContentType(MediaType.APPLICATION_JSON);

        String json = toJson(searchRequest);

        String body = restTemplate.exchange("/companysearch?onlyActiveCompanies=" + onlyActiveCompanies,
                HttpMethod.POST,
                new HttpEntity<>(json, headers),
                String.class).getBody();

        System.out.println("body " + body);

        return body;
    }

    private String toJson(CompanySearchRequest sarchRequest) {
        try {
            return mapper.writeValueAsString(sarchRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to build search request", e);
        }
    }

    @Test
    public void searchUsingCompanyName_includeInactiveCompanies() throws JsonProcessingException {

        String body = sendRequest(false,
                CompanySearchRequest
                        .builder()
                        .companyName("SomeCompanyName")
                        .build());
        CompanySearchResult result = mapper.readValue(body, CompanySearchResult.class);
        assertThat(result.getTotal_results()).isEqualTo(3);
        assertThat(result.getItems()).hasSize(3);

        checkCompany(expectedCompanyOne,
                findCompanyByNumber(result.getItems(), "SomeNumber1"));
        checkCompany(expectedCompanyTwo,
                findCompanyByNumber(result.getItems(), "SomeNumber2"));
        checkCompany(expectedCompanyThree,
                findCompanyByNumber(result.getItems(), "SomeNumber3"));
    }

    @Test
    public void searchUsingCompanyName_excludeInactiveCompanies() throws JsonProcessingException {

        String body = sendRequest(true,
                CompanySearchRequest
                        .builder()
                        .companyName("SomeCompanyName")
                        .build());
        CompanySearchResult result = mapper.readValue(body, CompanySearchResult.class);
        assertThat(result.getTotal_results()).isEqualTo(2);
        assertThat(result.getItems()).hasSize(2);

        checkCompany(expectedCompanyOne,
                findCompanyByNumber(result.getItems(), "SomeNumber1"));
        checkCompany(expectedCompanyTwo,
                findCompanyByNumber(result.getItems(), "SomeNumber2"));
    }

    @Test
    public void searchUsingCompanyNumber() throws JsonProcessingException {

        String body = sendRequest(false,
                CompanySearchRequest
                        .builder()
                        .companyNumber("SomeNumber1")
                        .build());
        CompanySearchResult result = mapper.readValue(body, CompanySearchResult.class);
        assertThat(result.getTotal_results()).isEqualTo(1);
        assertThat(result.getItems()).hasSize(1);

        checkCompany(expectedCompanyOne,
                findCompanyByNumber(result.getItems(), "SomeNumber1"));
    }

    private void checkCompany(Company expectedCompany, Company actualCompany) {
        assertThat(actualCompany).isNotNull();
        assertThat(actualCompany.getCompanyNumber()).isEqualTo(expectedCompany.getCompanyNumber());
        assertThat(actualCompany.getCompany_type()).isEqualTo(expectedCompany.getCompany_type());
        assertThat(actualCompany.getTitle()).isEqualTo(expectedCompany.getTitle());
        assertThat(actualCompany.getCompany_status()).isEqualTo(expectedCompany.getCompany_status());
        assertThat(actualCompany.getDate_of_creation()).isEqualTo(expectedCompany.getDate_of_creation());

        checkAddress(expectedCompany.getAddress(), actualCompany.getAddress());

        assertThat(actualCompany.getOfficers()).hasSize(expectedCompany.getOfficers().size());
        expectedCompany.getOfficers().forEach(officer ->
                checkOfficer(officer, findOfficerByName(actualCompany.getOfficers(), officer.getName()))
        );
    }

    private void checkOfficer(Officer expectedOfficer, Officer actualOfficer) {
        assertThat(actualOfficer).isNotNull();
        assertThat(actualOfficer.getName()).isEqualTo(expectedOfficer.getName());
        assertThat(actualOfficer.getOfficer_role()).isEqualTo(expectedOfficer.getOfficer_role());
        assertThat(actualOfficer.getAppointed_on()).isEqualTo(expectedOfficer.getAppointed_on());
        assertThat(actualOfficer.getResigned_on()).isNull();
        checkAddress(expectedOfficer.getAddress(), actualOfficer.getAddress());
    }

    private void checkAddress(Address expectedAddress, Address actualAddress) {
        assertThat(actualAddress).isNotNull();
        assertThat(actualAddress.getAddress_line_1())
                .isEqualTo(expectedAddress.getAddress_line_1());
        assertThat(actualAddress.getPremises())
                .isEqualTo(expectedAddress.getPremises());
        assertThat(actualAddress.getCountry())
                .isEqualTo(expectedAddress.getCountry());
        assertThat(actualAddress.getLocality())
                .isEqualTo(expectedAddress.getLocality());
        assertThat(actualAddress.getPostal_code())
                .isEqualTo(expectedAddress.getPostal_code());
    }

    private Company findCompanyByNumber(List<Company> companies, String companyNumber) {
        return companies.stream()
                .filter(company -> company.getCompanyNumber().equalsIgnoreCase(companyNumber))
                .findFirst().orElseThrow(() -> new RuntimeException("Company not found " + companyNumber));
    }

    private Officer findOfficerByName(List<Officer> officers, String officerName) {
        return officers.stream()
                .filter(officer -> officer.getName().equalsIgnoreCase(officerName))
                .findFirst().orElseThrow(() -> new RuntimeException("Officer not found " + officerName));
    }
}
