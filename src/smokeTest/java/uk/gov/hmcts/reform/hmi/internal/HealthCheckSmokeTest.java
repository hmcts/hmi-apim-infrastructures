package uk.gov.hmcts.reform.hmi.internal;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.hmi.helper.RestClientHelper;

/**
 * Smoke tests for the HMI internal endpoints (Health and Liveness).
 */
@ActiveProfiles(profiles = "smoke")
class HealthCheckSmokeTest {

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "https://sds-api-mgmt.staging.platform.hmcts.net/hmi";
    }

    @Test
    void internalHealthCheckTest() {
        RestClientHelper.performGetRequestAndValidate(
                "/",
                "\"status\": \"Up\"",
                200
        );
    }

    // TODO COMMENTED OUT UNTIL HMIS-1243 is played
    //    @Test
    //    void internalLivenessHealthCheckTest() {
    //        RestClientHelper.performSecureGetRequestAndValidate(
    //                "/liveness",
    //                "Welcome to pip-data-management",
    //                200,
    //                HeaderHelper.createHeaders("MOCK")
    //        );
    //    }
}
