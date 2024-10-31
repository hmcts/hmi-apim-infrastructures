package uk.gov.hmcts.reform.hmi.vh;

import io.restassured.RestAssured;
import io.restassured.response.Response;
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

import static uk.gov.hmcts.reform.hmi.helper.FileHelper.getHearingId;
import static uk.gov.hmcts.reform.hmi.helper.FileHelper.getJsonPayloadFileAsString;

/**
 * Functional tests for the VH endpoint (/hearings/{hearingId}/joh).
 */
@SpringBootTest
@ActiveProfiles(profiles = "functional")
class PostJohVideoHearingTest {

    @Value("${apim_url}")
    private String apimUrl;
    @Autowired
    RestClientHelper restClientHelper;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = apimUrl;
    }

    /**
     * Test with a valid hearing id and a valid set of headers, response will be 404
     * because we do not have judge id assigned on VH side.
     */
    @Test
    void vhPostAddJohVideoHearingFailed() throws IOException {
        Response response = restClientHelper.performSecurePostRequestAndValidateWithResponse(
                getJsonPayloadFileAsString("vh/create-vh-hearing.json"),
                HeaderHelper.createHeaders("VH"),
                "/resources/video-hearing",
                "id",
                201
        );

        restClientHelper.performSecurePostRequestAndValidate(
                getJsonPayloadFileAsString("vh/create-joh-video-hearing.json"),
                HeaderHelper.createHeaders("VH"),
                String.format("/hearings/%s/joh", getHearingId(response)),
                "",
                404
        );
    }

    /**
     * Test with an invalid hearing id and a valid set of headers, response should return 400.
     */
    @Test
    void vhPostAddJohVideoHearingInvalidId() throws UnknownHostException {
        restClientHelper.performSecurePostRequestAndValidate(
                "{}",
                HeaderHelper.createHeaders("VH"),
                "/hearings/invalid/joh",
                "",
                400
        );
    }
}
