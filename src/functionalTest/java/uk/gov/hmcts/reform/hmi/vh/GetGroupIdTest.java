package uk.gov.hmcts.reform.hmi.vh;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.hmi.helper.HeaderHelper;
import uk.gov.hmcts.reform.hmi.helper.RestClientHelper;

import java.net.UnknownHostException;

/**
 * Functional tests for the VH endpoint (/hearings/{id}/hearings).
 */
@SpringBootTest
@ActiveProfiles(profiles = "functional")
class GetGroupIdTest {

    @Autowired
    RestClientHelper restClientHelper;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "https://sds-api-mgmt.staging.platform.hmcts.net/hmi";
    }

    /**
     * Test with a valid hearing id and a valid set of headers.
     */
    @Test
    void vhGetRetrieveVideoHearingsSuccessful() throws UnknownHostException {
        restClientHelper.performSecureGetRequestAndValidate(
                "/hearings/f138520a-2a20-4b08-9777-a53fbb651e33/hearings",
                "[]",
                200,
                HeaderHelper.createHeaders("VH")
        );
    }

    /**
     * Test with an invalid hearing id and a valid set of headers.
     */
    @Test
    void vhGetRetrieveVideoHearingsInvalidId() throws UnknownHostException {
        restClientHelper.performSecureGetRequestAndValidate(
                "/hearings/test/hearings",
                "The value 'test' is not valid.",
                400,
                HeaderHelper.createHeaders("VH")
        );
    }
}
