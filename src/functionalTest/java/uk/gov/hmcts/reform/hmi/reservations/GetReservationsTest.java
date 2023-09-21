package uk.gov.hmcts.reform.hmi.reservations;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.hmi.helper.HeaderHelper;
import uk.gov.hmcts.reform.hmi.helper.RestClientHelper;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * Functional test for the endpoint used by different consumers (/reservations).
 */
@SpringBootTest
@ActiveProfiles(profiles = "functional")
class GetReservationsTest {

    @Autowired
    RestClientHelper restClientHelper;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "https://sds-api-mgmt.staging.platform.hmcts.net/hmi";
    }

    /**
     * Test with a valid GET request and check response payload, expect 200.
     */
    @Test
    void reservationsSuccessful() throws UnknownHostException {
        restClientHelper.performSecureGetRequestAndValidate(HeaderHelper.createHeaders("SNL"),
                "/reservations",
                "",
                200
        );
    }

    /**
     * Test with a Invalid header, response should return 400.
     */
    @Test
    void reservationsInvalidHeaderFail() throws IOException {
        Map<String, String> requestHeader =  HeaderHelper.createHeaders("SNL");
        requestHeader.remove("Destination-System");

        restClientHelper.performSecureGetRequestAndValidate(requestHeader,
                "/reservations",
                "Missing/Invalid Header Destination-System",
                400
        );
    }
}
