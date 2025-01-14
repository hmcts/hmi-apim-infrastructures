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
 * Functional tests for the VH endpoint (/hearings/{id}/clone).
 */
@SpringBootTest
@ActiveProfiles(profiles = "functional")
class PostCloneVideoHearingTest {

    @Value("${apim_url}")
    private String apimUrl;

    @Autowired
    RestClientHelper restClientHelper;

    private String apiVersion = "";

    @BeforeEach
    void setup() {

        RestAssured.baseURI = apimUrl;

        if (apimUrl.contains("test")) {
            apiVersion = "?version=v2";
        }
    }

    /**
     * Test with a valid hearing id and a valid set of headers, response should return 200.
     */
    @Test
    void vhPostCloneVideoHearingSuccessful() throws IOException {
        Response response = restClientHelper.performSecurePostRequestAndValidateWithResponse(
                getJsonPayloadFileAsString("vh/create-vh-hearing.json"),
                HeaderHelper.createHeaders("VH"),
                "/resources/video-hearing" + apiVersion,
                "id",
                201
        );

        restClientHelper.performSecurePostRequestAndValidate(
                "{}",
                HeaderHelper.createHeaders("VH"),
                String.format("/hearings/%s/clone", getHearingId(response)) + apiVersion,
                "",
                200
        );
    }

    /**
     * Test with an invalid hearing id and a valid set of headers, response should return 400.
     */
    @Test
    void vhPostCloneVideoHearingInvalidId() throws UnknownHostException {
        restClientHelper.performSecurePostRequestAndValidate(
                "{}",
                HeaderHelper.createHeaders("VH"),
                "/hearings/invalid/clone",
                "",
                404
        );
    }
}
