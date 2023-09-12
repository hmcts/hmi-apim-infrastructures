package uk.gov.hmcts.reform.hmi.vh;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.hmi.helper.HeaderHelper;
import uk.gov.hmcts.reform.hmi.helper.RestClientHelper;

import java.net.UnknownHostException;

/**
 * Functional tests for the VH endpoint (/resources/video-hearing/{id}).
 */
@SpringBootTest
@ActiveProfiles(profiles = "functional")
class GetRetrieveVideoHearingsTest {

    @Autowired
    RestClientHelper restClientHelper;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "https://sds-api-mgmt.staging.platform.hmcts.net/hmi";
    }

    /**
     * Test with a valid hearing id and a valid set of headers, response should return the hearing.
     */
    @Test
    void vhGetRetrieveVideoHearingsSuccessful() throws UnknownHostException {
        restClientHelper.performSecureGetRequestAndValidate(
                "/resources/video-hearing/f761c4ee-3eb8-45f2-b5fe-011bbf800f29",
                "2022-11-02T10:00:00Z",
                200,
                HeaderHelper.createHeaders("VH")
        );
    }

    /**
     * Test with an invalid hearing id and a valid set of headers, response should return 400.
     */
    @Test
    void vhGetRetrieveVideoHearingsInvalid() throws UnknownHostException {
        restClientHelper.performSecureGetRequestAndValidate(
                "/resources/video-hearing/abcdef",
                "The value 'abcdef' is not valid.",
                400,
                HeaderHelper.createHeaders("VH")
        );
    }

    /**
     * Test with a non-existent hearing id and a valid set of headers, response should return 404.
     */
    @Test
    void vhGetRetrieveVideoHearingsNotFound() throws UnknownHostException {
        restClientHelper.performSecureGetRequestAndValidate(
                "/resources/video-hearing/f761c4ee-3eb8-45f2-b5fe-011bbf800f25",
                "Not Found",
                404,
                HeaderHelper.createHeaders("VH")
        );
    }
}
