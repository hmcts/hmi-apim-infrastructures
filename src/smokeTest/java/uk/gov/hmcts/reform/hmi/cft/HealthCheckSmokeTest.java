package uk.gov.hmcts.reform.hmi.cft;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.hmi.helper.RestClientHelper;

/**
 * Smoke tests for the CFT endpoint (health).
 */
@ActiveProfiles(profiles = "smoke")
class HealthCheckSmokeTest {

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "https://sds-api-mgmt.staging.platform.hmcts.net/hmi";
    }

    @Test
    void cftHealthCheckTest() {
        RestClientHelper.performGetRequestAndValidate(
                "/hmc-health",
                "Welcome to hmc-hmi-inbound-adapter",
                200
        );
    }
}
