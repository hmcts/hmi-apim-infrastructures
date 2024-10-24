package uk.gov.hmcts.reform.hmi.people;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.hmi.helper.HeaderHelper;
import uk.gov.hmcts.reform.hmi.helper.RestClientHelper;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootTest
@ActiveProfiles(profiles = "functional")
@TestPropertySource(locations = {"classpath:application.yaml"})
class PeopleTest {

    @Autowired
    RestClientHelper restClientHelper;

    private static final String DESTINATION_SYSTEM = "ELINKS";
    private static final String GET_PEOPLE_ENDPOINT = "/people";
    private static final String UPDATE_SINCE = "updated_since";
    private static final String PER_PAGE = "per_page";
    private static final String PAGE = "page";
    private String getPeopleById = "/people/%s";

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "https://sds-api-mgmt.staging.platform.hmcts.net/hmi";
    }

    @Test
    @Disabled
    void peopleGetSuccessful() throws IOException {
        Map<String, String> queryParameters = new ConcurrentHashMap<>();
        queryParameters.put(UPDATE_SINCE, "2019-01-29");
        queryParameters.put(PER_PAGE, "52");
        queryParameters.put(PAGE, "1");

        Map<String, String> requestHeader =  HeaderHelper.createHeaders(DESTINATION_SYSTEM);
        restClientHelper.performSecureGetRequestAndValidateWithQueryParams(requestHeader,
                GET_PEOPLE_ENDPOINT,
                queryParameters,
                "pagination",
                200);
    }

    @Test
    @Disabled
    void peopleGetByIdSuccessful() throws IOException {
        Map<String, String> requestHeader =  HeaderHelper.createHeaders(DESTINATION_SYSTEM);
        getPeopleById = String.format(getPeopleById, "PPLN1");

        restClientHelper.performSecureGetRequestAndValidate(requestHeader,
                  getPeopleById,
                  "",
                  404);
    }

    @Test
    void peopleGetMissingDestinationHeader() throws IOException {
        Map<String, String> requestHeader = HeaderHelper.createHeaders(DESTINATION_SYSTEM);
        requestHeader.remove("Destination-System");

        Map<String, String> queryParameters = new ConcurrentHashMap<>();
        queryParameters.put(UPDATE_SINCE, "2019-01-28");
        queryParameters.put(PER_PAGE, "50");
        queryParameters.put(PAGE, "1");

        restClientHelper.performSecureGetRequestAndValidateWithQueryParams(requestHeader,
                GET_PEOPLE_ENDPOINT,
                queryParameters,
                "Missing/Invalid Header Destination-System",
                400);
    }

    @Test
    void peopleGetInvalidQueryParams() throws IOException {
        Map<String, String> queryParameters = new ConcurrentHashMap<>();
        queryParameters.put(UPDATE_SINCE, "2019-02-29");
        queryParameters.put(PER_PAGE, "50");
        queryParameters.put(PAGE, "2");
        queryParameters.put("OneMore", "OneMore");

        Map<String, String> requestHeader = HeaderHelper.createHeaders(DESTINATION_SYSTEM);
        restClientHelper.performSecureGetRequestAndValidateWithQueryParams(requestHeader,
                GET_PEOPLE_ENDPOINT,
                queryParameters,
                "Invalid query parameter/s in the request URL.",
                400);
    }

    @Test
    void peopleGetMissingOAuth() throws IOException {
        Map<String, String> requestHeader = HeaderHelper.createHeaders(DESTINATION_SYSTEM);

        restClientHelper.performGetRequestAndValidate(requestHeader,
                GET_PEOPLE_ENDPOINT,
                "Access denied due to invalid OAuth information",
                401);
    }

    @Test
    void peopleGeByIdtMissingOAuth() throws IOException {
        Map<String, String> requestHeader = HeaderHelper.createHeaders(DESTINATION_SYSTEM);
        getPeopleById = String.format(getPeopleById, "PPLN2");

        restClientHelper.performGetRequestAndValidate(requestHeader,
                getPeopleById,
                "Access denied due to invalid OAuth information",
                401);
    }

    @Test
    void peopleGetByIdInvalidRequest() throws IOException {
        Map<String, String> requestHeader = HeaderHelper.createHeaders(DESTINATION_SYSTEM);

        getPeopleById = getPeopleById.substring(0, getPeopleById.lastIndexOf('/') + 1);
        restClientHelper.performSecureGetRequestAndValidate(requestHeader,
                getPeopleById,
                "",
                400);
    }
}
