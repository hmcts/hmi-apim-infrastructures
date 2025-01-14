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

import static uk.gov.hmcts.reform.hmi.helper.FileHelper.getHearingId;
import static uk.gov.hmcts.reform.hmi.helper.FileHelper.getJsonPayloadFileAsString;

/**
 * Functional tests for the VH endpoint (/hearings/{hearingId}).
 */
@SpringBootTest
@ActiveProfiles(profiles = "functional")
class PutVideoHearingTest {

    @Value("${apim_url}")
    private String apimUrl;
    @Autowired
    RestClientHelper restClientHelper;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = apimUrl;
    }

    /**
     * Test update valid hearing with valid hearing.
     */
    @Test
    void vhPutUpdateVideoHearingsSuccessful() throws IOException {
        Response response = restClientHelper.performSecurePostRequestAndValidateWithResponse(
                getJsonPayloadFileAsString("vh/create-vh-hearing.json"),
                HeaderHelper.createHeaders("VH"),
                "/resources/video-hearing?version=v2",
                "id",
                201
        );

        String validHearingId = getHearingId(response);

        restClientHelper.performSecurePutRequestAndValidate(
                getJsonPayloadFileAsString("vh/update-vh-hearing.json"),
                HeaderHelper.createHeaders("VH"),
                "/resources/video-hearing/" +  validHearingId + "?version=v2",
                "id",
                200
        );

    }

    /**
     * Test with an invalid hearing id, expect 404.
     */
    @Test
    void vhPutUpdateInvalidVideoHearingsSuccessful() throws IOException {
        restClientHelper.performSecurePutRequestAndValidate(
                getJsonPayloadFileAsString("vh/update-vh-hearing.json"),
                HeaderHelper.createHeaders("VH"),
                "/resources/video-hearing/f761c4ee-3eb8-45f2-b5fe-011bbf800f25?version=v2",
                "",
                404
        );

    }
}
