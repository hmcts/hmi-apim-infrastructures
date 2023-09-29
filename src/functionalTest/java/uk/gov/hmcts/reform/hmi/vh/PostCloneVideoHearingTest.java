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
 * Functional tests for the VH endpoint (/hearings/{id}/clone).
 */
@SpringBootTest
@ActiveProfiles(profiles = "functional")
class PostCloneVideoHearingTest {

    @Autowired
    RestClientHelper restClientHelper;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "https://sds-api-mgmt.staging.platform.hmcts.net/hmi";
    }

    /**
     * Test with a valid hearing id and a valid set of headers, response should return 204.
     */
    @Test
    void vhPostCloneVideoHearingSuccessful() throws UnknownHostException {
        restClientHelper.performSecurePostRequestAndValidate(
                "{}",
                HeaderHelper.createHeaders("VH"),
                "/hearings/f761c4ee-3eb8-45f2-b5fe-011bbf800f29/clone",
                "",
                204
        );
    }

    /**
     * Test with an invalid hearing id and a valid set of headers, response should return 400.
     */
    @Test
    void vhPostCloneVideoHearingInvalidId() throws UnknownHostException {
        restClientHelper.performSecurePostRequestAndValidate(
                "{}",
                HeaderHelper.createHeaders("VH"),
                "/hearings/invalid/clone",
                "",
                400
        );
    }
}
