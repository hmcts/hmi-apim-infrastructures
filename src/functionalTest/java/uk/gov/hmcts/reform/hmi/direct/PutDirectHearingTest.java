package uk.gov.hmcts.reform.hmi.direct;

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
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;

/**
 * Functional tests for the endpoint used by different consumers (/hearings/sessions/{sessionId}).
 */
@SpringBootTest
@ActiveProfiles(profiles = "functional")
class PutDirectHearingTest {

    @Autowired
    RestClientHelper restClientHelper;

    private final Random rand;

    public PutDirectHearingTest()  throws NoSuchAlgorithmException {
        rand = SecureRandom.getInstanceStrong();
    }

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "https://sds-api-mgmt.staging.platform.hmcts.net/hmi";
    }

    /**
     * Test with a valid set of headers, but empty body, response should return 400.
     */
    @Test
    @Disabled
    void putDirectHearingFail() throws UnknownHostException {
        int randomId = rand.nextInt(99_999_999);
        String hearingsIdRootContext = String.format("/hearings/sessions/%s", randomId);

        restClientHelper.performSecurePutRequestAndValidate(
                "{}",
                HeaderHelper.createHeaders("SNL"),
                hearingsIdRootContext,
                "",
                400
        );
    }

    /**
     * Test with a Invalid header, response should return 400.
     */
    @Test
    @Disabled
    void putDirectHearingInvalidHeaderFail() throws UnknownHostException {
        int randomId = rand.nextInt(99_999_999);
        String hearingsIdRootContext = String.format("/hearings/sessions/%s", randomId);

        Map<String, String> requestHeader =  HeaderHelper.createHeaders("SNL");
        requestHeader.remove("Destination-System");

        restClientHelper.performSecurePutRequestAndValidate(
                "{}",
                requestHeader,
                hearingsIdRootContext,
                "Missing/Invalid Header Destination-System",
                400
        );
    }
}
