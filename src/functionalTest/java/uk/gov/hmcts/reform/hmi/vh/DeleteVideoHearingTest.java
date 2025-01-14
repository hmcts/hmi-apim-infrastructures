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
 * Functional tests for the VH endpoint (/resources/video-hearing/{id}).
 */
@SpringBootTest
@ActiveProfiles(profiles = "functional")
class DeleteVideoHearingTest {

    @Value("${apim_url}")
    private String apimUrl;
    @Autowired
    RestClientHelper restClientHelper;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = apimUrl;
    }

    /**
     * Test with a valid hearing id and a valid set of headers and valid payload, expect 204.
     */
    @Test
    void vhDeleteVideoHearingSuccessful() throws IOException {
        Response response = restClientHelper.performSecurePostRequestAndValidateWithResponse(
                getJsonPayloadFileAsString("vh/create-vh-hearing.json"),
                HeaderHelper.createHeaders("VH"),
                "/resources/video-hearing?version=v2",
                "id",
                201
        );

        restClientHelper.performSecureDeleteRequestAndValidate(
                "",
                HeaderHelper.createHeaders("VH"),
                String.format("/resources/video-hearing/%s", getHearingId(response)),
                "",
                204
        );
    }

    /**
     * Test with an invalid hearing id and a valid set of headers and valid payload, expect 400.
     */
    @Test
    void vhDeleteVideoHearingInvalidId() throws IOException {

        restClientHelper.performSecureDeleteRequestAndValidate(
                "",
                HeaderHelper.createHeaders("VH"),
                "/resources/video-hearing/test",
                "The value 'test' is not valid.",
                400
        );
    }
}
