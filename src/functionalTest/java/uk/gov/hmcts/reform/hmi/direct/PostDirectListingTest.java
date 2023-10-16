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
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;

import static uk.gov.hmcts.reform.hmi.helper.FileHelper.getJsonPayloadFileAsString;

/**
 * Functional tests for the endpoint used by different consumers (/listings).
 */
@SpringBootTest
@ActiveProfiles(profiles = "functional")
class PostDirectListingTest {

    @Autowired
    RestClientHelper restClientHelper;

    private final Random rand;

    private static String randomHearingId;

    public PostDirectListingTest()  throws NoSuchAlgorithmException {
        rand = SecureRandom.getInstanceStrong();
    }

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "https://sds-api-mgmt.staging.platform.hmcts.net/hmi";
    }

    /**
     * Test with a valid set of headers, but empty body, response should return 400.
     * Need to fix the payload once LA deploy DirectListing config on their SIT environment.
     */
    @Test
    void postDirectListingFail() throws IOException {
        randomHearingId = String.format("HMI_%s", rand.nextInt(999_999_999));
        restClientHelper.performSecurePostRequestAndValidate(
                getJsonPayloadFileAsString("directlistings/create-direct-listings-request-payload.json")
                        .replace("HMI_CASE_LISTING_ID", randomHearingId),
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
