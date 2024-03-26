package uk.gov.hmcts.reform.hmi.sessions;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.hmi.helper.HeaderHelper;
import uk.gov.hmcts.reform.hmi.helper.RestClientHelper;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Functional tests for the endpoint used by different consumers (/sessions).
 */
@SpringBootTest
@ActiveProfiles(profiles = "functional")
class GetSessionsTest {

    private static final String REQUEST_SESSION_TYPE = "requestSessionType";
    @Autowired
    RestClientHelper restClientHelper;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "https://sds-api-mgmt.staging.platform.hmcts.net/hmi";
    }

    /**
     * Test with a valid GET request and check response payload, expect 200.
     */
    @Test
    void sessionsSuccessful() throws UnknownHostException {
        Map<String, String> queryParameters = new ConcurrentHashMap<>();
        queryParameters.put(REQUEST_SESSION_TYPE, "CJ");
        queryParameters.put("requestStartDate", "2022-02-25T09:00:00Z");
        queryParameters.put("requestEndDate", "2022-03-01T09:00:00Z");

        restClientHelper.performSecureGetRequestAndValidateWithQueryParams(HeaderHelper.createHeaders("SNL"),
                "/sessions",
                queryParameters,
                "",
                200
        );
    }

    /**
     * Test with a valid GET request as source DLRM and check response payload, expect 200.
     */
    @Test
    void sessionsSuccessfulForDlrm() throws UnknownHostException {
        Map<String, String> queryParameters = new ConcurrentHashMap<>();
        queryParameters.put(REQUEST_SESSION_TYPE, "CJ");
        queryParameters.put("requestStartDate", "2022-02-25T09:00:00Z");
        queryParameters.put("requestEndDate", "2022-03-01T09:00:00Z");

        restClientHelper.performSecureGetRequestAndValidateWithQueryParams(HeaderHelper.createHeaders("SNL", "DLRM"),
                "/sessions",
                queryParameters,
                "",
                200
        );
    }

    /**
     * Test with a Invalid header, response should return 400.
     */
    @Test
    void sessionsHeaderFail() throws IOException {
        Map<String, String> requestHeader =  HeaderHelper.createHeaders("SNL");
        requestHeader.remove("Destination-System");

        Map<String, String> queryParameters = new ConcurrentHashMap<>();
        queryParameters.put(REQUEST_SESSION_TYPE, "CJ");
        queryParameters.put("requestStartDate", "2022-02-25T09:00:00Z");
        queryParameters.put("requestEndDate", "2022-03-01T09:00:00Z");

        restClientHelper.performSecureGetRequestAndValidateWithQueryParams(
                requestHeader,
                "/sessions",
                queryParameters,
                "Missing/Invalid Header Destination-System",
                400
        );
    }
}
