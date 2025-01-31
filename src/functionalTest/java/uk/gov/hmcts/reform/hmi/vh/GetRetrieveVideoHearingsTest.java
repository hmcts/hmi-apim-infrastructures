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
 * Functional tests for the VH endpoint (/resources/video-hearing/{id}).
 */
@SpringBootTest
@ActiveProfiles(profiles = "functional")
class GetRetrieveVideoHearingsTest {

    @Value("${apim_url}")
    private String apimUrl;
    @Autowired
    RestClientHelper restClientHelper;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = apimUrl;
    }

    /**
     * Test with a valid hearing id and a valid set of headers, response should return the hearing.
     */
    @Test
    void vhGetRetrieveVideoHearingsSuccessful() throws IOException {
        Response response = restClientHelper.performSecurePostRequestAndValidateWithResponse(
                getJsonPayloadFileAsString("vh/create-vh-hearing.json"),
                HeaderHelper.createHeaders("VH"),
                "/resources/video-hearing?version=v2",
                "id",
                201
        );

        restClientHelper.performSecureGetRequestAndValidate(
                HeaderHelper.createHeaders("VH"),
                "/resources/video-hearing/" + getHearingId(response) + "?version=v2",
                "2030-08-17T09:00:00Z",
                200
        );
    }

    /**
     * Test with an invalid hearing id and a valid set of headers, response should return 400.
     */
    @Test
    void vhGetRetrieveVideoHearingsInvalid() throws UnknownHostException {
        restClientHelper.performSecureGetRequestAndValidate(
                HeaderHelper.createHeaders("VH"),
                "/resources/video-hearing/abcdef?version=v2",
                "The value 'abcdef' is not valid.",
                400
        );
    }

    /**
     * Test with a non-existent hearing id and a valid set of headers, response should return 404.
     */
    @Test
    void vhGetRetrieveVideoHearingsNotFound() throws UnknownHostException {
        restClientHelper.performSecureGetRequestAndValidate(
                HeaderHelper.createHeaders("VH"),
                "/resources/video-hearing/f761c4ee-3eb8-45f2-b5fe-011bbf800f25?version=v2",
                "Not Found",
                404
        );
    }
}