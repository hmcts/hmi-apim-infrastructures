package uk.gov.hmcts.reform.hmi.people;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.hmi.helper.RestClientHelper;

@ActiveProfiles(profiles = "smoke")
class HealthCheckSmokeTest {

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "https://sds-api-mgmt.staging.platform.hmcts.net/hmi";
    }

    @Test
    @Disabled
    void peopleHealthCheckTest() {
        RestClientHelper.performGetRequestAndValidate(
                "/elinks-health",
                "",
                200
        );
    }
}
