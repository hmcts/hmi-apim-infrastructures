package uk.gov.hmcts.reform.hmi.crime;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.hmi.helper.RestClientHelper;

/**
 * Smoke tests for the Crime endpoint (health).
 */
@ActiveProfiles(profiles = "smoke")
class HealthCheckSmokeTest {

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "https://sds-api-mgmt.staging.platform.hmcts.net/hmi";
    }

    /**
     * This test needs to enable once we implement ticket PUB-2629
     */
    @Test
    @Disabled
    void crimeHealthCheckTest() {
        RestClientHelper.performGetRequestAndValidate(
                "/crime-health",
                "",
                200
        );
    }
}
