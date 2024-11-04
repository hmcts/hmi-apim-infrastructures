package uk.gov.hmcts.reform.hmi.internal;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
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
