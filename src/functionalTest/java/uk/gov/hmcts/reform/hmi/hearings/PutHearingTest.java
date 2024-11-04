package uk.gov.hmcts.reform.hmi.hearings;

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
import java.util.Map;

/**
 * Functional tests for the endpoint used by different consumers to update hearing (/hearings).
 */
@SpringBootTest
@ActiveProfiles(profiles = "functional")
class PutHearingTest {

    @Value("${apim_url}")
    private String apimUrl;
    @Autowired
    RestClientHelper restClientHelper;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = apimUrl;
    }

    /**
     * Test with an empty payload for CRIME, a valid set of headers and valid payload, expect 404.
     */
    @Test
    void putHearingCrimeFail() throws IOException {
        restClientHelper.performSecurePutRequestAndValidate(
                "{}",
                HeaderHelper.createHeaders("CRIME"),
                "/hearings/123",
                "",
                404
        );
    }

    /**
     * Test with a empty payload for CFT, a valid set of headers and valid payload, expect 404.
     */
    @Test
    void putHearingCftFail() throws IOException {
        restClientHelper.performSecurePutRequestAndValidate(
                "{}",
                HeaderHelper.createHeaders("CFT"),
                "/hearings/123",
                "",
                404
        );
    }

    /**
     * Test with a Invalid header, response should return 400.
     */
    @Test
    
    void putHearingInvalidHeaderFail() throws IOException {
        Map<String, String> requestHeader =  HeaderHelper.createHeaders("CRIME");
        requestHeader.remove("Destination-System");

        restClientHelper.performSecurePutRequestAndValidate(
                "{}",
                requestHeader,
                "/hearings/123",
                "Missing/Invalid Header Destination-System",
                400
        );
    }

}
