package uk.gov.hmcts.reform.hmi.hearings;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
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
 * Functional tests for the endpoint used by different consumers (/hearings).
 */
@SpringBootTest
@ActiveProfiles(profiles = "functional")
class HearingTest {

    @Autowired
    RestClientHelper restClientHelper;

    private final Random rand;

    private static String randomHearingId;

    private static Integer randomNumber;

    private static final String DESTINATION = "SNL";

    public HearingTest()  throws NoSuchAlgorithmException {
        rand = SecureRandom.getInstanceStrong();
    }

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "https://sds-api-mgmt.staging.platform.hmcts.net/hmi";
    }

    /**
     * Test with a valid payload and a valid set of headers and valid payload, expect 400.
     */
    @Test
    @Order(1)
    void postHearingCreateSuccess() throws IOException {
        randomNumber = rand.nextInt(999_999_999);
        randomHearingId = String.format("HMI_%s", randomNumber);
        restClientHelper.performSecurePostRequestAndValidateWithResponse(
                getJsonPayloadFileAsString("hearings/create-hearing-request-payload.json")
                        .replace("HMI_CASE_LISTING_ID", randomHearingId),
                HeaderHelper.createHeaders(DESTINATION),
                "/hearings",
                "",
                202
        );
    }

    /**
     * Test Update Hearing with a payload for SNL, a valid set of headers and valid payload, expect 202.
     */
    @Test
    @Order(2)
    void putHearingSuccess() throws IOException {
        restClientHelper.performSecurePutRequestAndValidate(
                getJsonPayloadFileAsString("hearings/update-hearing-request-payload.json")
                        .replace("HMI_CASE_LISTING_ID", randomHearingId)
                        .replace("\"CASE_VERSION_ID\"", randomNumber.toString()),
                HeaderHelper.createHeaders(DESTINATION),
                "/hearings/" + randomHearingId,
                "",
                202
        );
    }

    /**
     * Test with an Invalid header, response should return 400.
     */
    @Test
    @Order(3)
    void postHearingCreateInvalidHeaderFail() throws IOException {
        Map<String, String> requestHeader =  HeaderHelper.createHeaders(DESTINATION);
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
