package uk.gov.hmcts.reform.hmi.direct;

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

/**
 * Functional tests for the endpoint used by different consumers (/listings).
 */
@SpringBootTest
@ActiveProfiles(profiles = "functional")
class PostDirectListingTest {

    @Autowired
    RestClientHelper restClientHelper;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "https://sds-api-mgmt.staging.platform.hmcts.net/hmi";
    }

    /**
     * Test with a valid set of headers, but empty body, response should return 400.
     */
    @Test
    void postDirectListingFail() throws UnknownHostException {
        restClientHelper.performSecurePostRequestAndValidate(
                "{}",
                HeaderHelper.createHeaders("SNL"),
                "/listings",
                "",
                400
        );
    }

    /**
     * Test with a Invalid header, response should return 400.
     */
    @Test
    void postDirectListingHeaderFail() throws IOException {
        Map<String, String> requestHeader =  HeaderHelper.createHeaders("SNL");
        requestHeader.remove("Destination-System");

        restClientHelper.performSecurePostRequestAndValidateWithResponse(
                "{}",
                requestHeader,
                "/listings",
                "Missing/Invalid Header Destination-System",
                400
        );
    }
}
