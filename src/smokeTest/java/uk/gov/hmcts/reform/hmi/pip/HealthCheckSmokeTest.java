package uk.gov.hmcts.reform.hmi.pip;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.hmi.helper.RestClientHelper;

/**
 * Smoke tests for the PIP endpoint (health).
 */
@SpringBootTest
@ActiveProfiles(profiles = "smoke")
class HealthCheckSmokeTest {

    @Autowired
    RestClientHelper restClientHelper;

    @Value("${apim_url}")
    private String apimUrl;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = apimUrl;
    }

    @Test
    void pipHealthCheckTest() {
        restClientHelper.performGetRequestAndValidate(
                "/pih/health",
                "",
                200
        );
    }
}
