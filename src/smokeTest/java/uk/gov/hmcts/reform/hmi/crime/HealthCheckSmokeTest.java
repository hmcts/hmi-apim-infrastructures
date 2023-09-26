package uk.gov.hmcts.reform.hmi.crime;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
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

    @Test
    void crimeHealthCheckTest() {
        RestClientHelper.performGetRequestAndValidate(
                "/crime-health",
                "{“knock-knock”: \"who's there??\"}",
                200
        );
    }
}