package uk.gov.hmcts.reform.hmi.vh;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.hmi.helper.HeaderHelper;
import uk.gov.hmcts.reform.hmi.helper.RestClientHelper;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Functional tests for the VH endpoint (/resources/video-hearing).
 */
@SpringBootTest
@ActiveProfiles(profiles = "functional")
class GetRetrieveVideoHearingsByUserTest {

    @Autowired
    RestClientHelper restClientHelper;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "https://sds-api-mgmt.staging.platform.hmcts.net/hmi";
    }

    /**
     * Test with a valid user and valid headers, response should return the hearing.
     */
    @Test
    @Disabled
    void vhGetRetrieveVideoHearingsByUserSuccessful() throws UnknownHostException {
        Map<String, String> params = new ConcurrentHashMap<>();
        params.put("username", "Manual01Clerk01@hearings.reform.hmcts.net");
        restClientHelper.performSecureGetRequestAndValidateWithQueryParams(
                HeaderHelper.createHeaders("VH"),
                "/resources/video-hearing",
                params,
                "2022-10-19T19:15:00Z",
                200
        );
    }

    /**
     * Test with a user and valid headers, response should return blank response as no hearings.
     */
    @Test
    void vhGetRetrieveVideoHearingsByUserSuccessfulBlankResponse() throws UnknownHostException {
        Map<String, String> params = new ConcurrentHashMap<>();
        params.put("username", "test.hmi.vh@justice.gov.uk");
        restClientHelper.performSecureGetRequestAndValidateWithQueryParams(
                HeaderHelper.createHeaders("VH"),
                "/resources/video-hearing",
                params,
                "[]",
                200
        );
    }

    /**
     * Test with no user and valid headers, response should return 500.
     */
    @Test
    void vhGetRetrieveVideoHearingsByUserNoUser() throws UnknownHostException {
        restClientHelper.performSecureGetRequestAndValidate(
                HeaderHelper.createHeaders("VH"),
                "/resources/video-hearing",
                "Object reference not set to an instance of an object.",
                500
        );
    }
}
