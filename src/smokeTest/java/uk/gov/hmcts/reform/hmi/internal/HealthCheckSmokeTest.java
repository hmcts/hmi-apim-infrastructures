package uk.gov.hmcts.reform.hmi.internal;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.hmi.helper.HeaderHelper;
import uk.gov.hmcts.reform.hmi.helper.RestClientHelper;

/**
 * Smoke tests for the HMI internal endpoints (Health and Liveness).
 */
@SpringBootTest
@ActiveProfiles(profiles = "smoke")
class HealthCheckSmokeTest {

    @Value("${apim_url}")
    private String apimUrl;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = apimUrl;
    }

    @Test
    void internalHealthCheckTest() {
        RestClientHelper.performGetRequestAndValidate(
                "/",
                "",
                200
        );
    }

    @Test
    void internalPrivateHealthCheckTest() {
        RestClientHelper.performSecureGetRequestAndValidate(
                "/health",
                "Up",
                200,
                HeaderHelper.createHeaders("HMI")
        );
    }
}
