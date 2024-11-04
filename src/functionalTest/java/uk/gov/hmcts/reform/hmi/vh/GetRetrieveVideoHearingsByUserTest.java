package uk.gov.hmcts.reform.hmi.vh;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${apim_url}")
    private String apimUrl;
    @Autowired
    RestClientHelper restClientHelper;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = apimUrl;
    }

    /**
     * Test with a valid user and valid headers, response should return the hearing.
     * We have asked VH about this test. Authentication is failing for this endpoint which is
     * bit strange because it works for other endpoints.
     * To deploy PUB-2640, we need to disable this test temporarily.
     * Once PUB-2640 is deployed, We will enable this test.
     * Ticket cannot be moved to done column until this test will be enabled.
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
