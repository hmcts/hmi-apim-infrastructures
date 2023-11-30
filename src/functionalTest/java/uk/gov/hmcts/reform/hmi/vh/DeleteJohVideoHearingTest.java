package uk.gov.hmcts.reform.hmi.vh;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.hmi.helper.HeaderHelper;
import uk.gov.hmcts.reform.hmi.helper.RestClientHelper;

import java.io.IOException;
import java.net.UnknownHostException;

import static uk.gov.hmcts.reform.hmi.helper.FileHelper.getHearingId;
import static uk.gov.hmcts.reform.hmi.helper.FileHelper.getJsonPayloadFileAsString;

/**
 * Functional tests for the VH endpoint (/hearings/{hearingId}/joh/{personalCode}).
 */
@SpringBootTest
@ActiveProfiles(profiles = "functional")
class DeleteJohVideoHearingTest {

    @Autowired
    RestClientHelper restClientHelper;

    private String validHearingId = "";

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "https://sds-api-mgmt.staging.platform.hmcts.net/hmi";
    }

    /**
     * Test with a valid hearing id and a valid set of headers, response will be 404
     * because we do not have judge id assigned on VH side.
     */
    @Test
    void vhDeleteJohVideoHearingFailed() throws IOException {
        Response response = restClientHelper.performSecurePostRequestAndValidateWithResponse(
                getJsonPayloadFileAsString("vh/create-vh-hearing.json"),
                HeaderHelper.createHeaders("VH"),
                "/resources/video-hearing",
                "id",
                201
        );

        validHearingId = getHearingId(response);

        restClientHelper.performSecureDeleteRequestAndValidate(
                "",
                HeaderHelper.createHeaders("VH"),
                String.format("/hearings/%s/joh/%s", validHearingId, "123"),
                "",
                404
        );
    }

    /**
     * Test with an invalid hearing id and a valid set of headers, response should return 404.
     */
    @Test
    void vhDeleteJohVideoHearingInvalidId() throws UnknownHostException {
        restClientHelper.performSecureDeleteRequestAndValidate(
                "{}",
                HeaderHelper.createHeaders("VH"),
                String.format("/hearings/%s/joh/invalid", validHearingId),
                "",
                404
        );
    }
}
