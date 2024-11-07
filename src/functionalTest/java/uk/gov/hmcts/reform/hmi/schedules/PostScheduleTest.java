package uk.gov.hmcts.reform.hmi.schedules;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.hmi.helper.HeaderHelper;
import uk.gov.hmcts.reform.hmi.helper.RestClientHelper;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * Functional tests for the endpoint used by different consumers (/schedules).
 */
@SpringBootTest
@ActiveProfiles(profiles = "functional")
class PostScheduleTest {

    @Value("${apim_url}")
    private String apimUrl;
    @Autowired
    RestClientHelper restClientHelper;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = apimUrl;
    }

    /**
     * Test with a valid set of headers, but empty body, response should return 400.
     */
    @Test
    void postCreateScheduleFail() throws UnknownHostException {
        restClientHelper.performSecurePostRequestAndValidate(
                "{}",
                HeaderHelper.createHeaders("SNL"),
                "/schedules",
                "",
                400
        );
    }

    /**
     * Test with a Invalid header, response should return 400.
     */
    @Test
    void postCreateScheduleHeaderFail() throws IOException {
        Map<String, String> requestHeader =  HeaderHelper.createHeaders("SNL");
        requestHeader.remove("Destination-System");

        restClientHelper.performSecurePostRequestAndValidateWithResponse(
                "{}",
                requestHeader,
                "/schedules",
                "Missing/Invalid Header Destination-System",
                400
        );
    }
}
