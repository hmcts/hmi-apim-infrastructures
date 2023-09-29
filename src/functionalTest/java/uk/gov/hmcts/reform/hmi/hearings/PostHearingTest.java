package uk.gov.hmcts.reform.hmi.hearings;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.hmi.helper.HeaderHelper;
import uk.gov.hmcts.reform.hmi.helper.RestClientHelper;

import java.io.IOException;
import java.util.Map;

import static uk.gov.hmcts.reform.hmi.helper.FileHelper.getJsonPayloadFileAsString;

/**
 * Functional tests for the endpoint used by different consumers (/hearings).
 */
@SpringBootTest
@ActiveProfiles(profiles = "functional")
class PostHearingTest {

    @Autowired
    RestClientHelper restClientHelper;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "https://sds-api-mgmt.staging.platform.hmcts.net/hmi";
    }

    /**
     * Test with a valid payload and a valid set of headers and valid payload, expect 400.
     */
    @Test
    void postHearingCreateFail() throws IOException {
        restClientHelper.performSecurePostRequestAndValidateWithResponse(
                getJsonPayloadFileAsString("hearings/create-hearing-request-payload.json"),
                HeaderHelper.createHeaders("SNL"),
                "/hearings",
                "",
                400
        );
    }

    /**
     * Test with a Invalid header, response should return 400.
     */
    @Test
    void postHearingCreateInvalidHeaderFail() throws IOException {
        Map<String, String> requestHeader =  HeaderHelper.createHeaders("SNL");
        requestHeader.remove("Destination-System");

        restClientHelper.performSecurePostRequestAndValidateWithResponse(
                getJsonPayloadFileAsString("hearings/create-hearing-request-payload.json"),
                requestHeader,
                "/hearings",
                "Missing/Invalid Header Destination-System",
                400
        );
    }
}
