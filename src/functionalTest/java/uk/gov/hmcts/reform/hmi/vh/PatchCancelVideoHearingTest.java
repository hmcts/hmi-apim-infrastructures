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
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Functional tests for the VH endpoint (/hearings/{id}/cancel).
 */
@SpringBootTest
@ActiveProfiles(profiles = "functional")
class PatchCancelVideoHearingTest {

    @Autowired
    RestClientHelper restClientHelper;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "https://sds-api-mgmt.staging.platform.hmcts.net/hmi";
    }

    /**
     * Test with a valid hearing id and a valid set of headers and valid payload, expect 204.
     */
    @Test
    void vhPatchCancelVideoHearingSuccessful() throws IOException {
        Response response = restClientHelper.performSecurePostRequestAndValidateWithResponse(
                getJsonPayloadFileAsString("vh/create-vh-hearing.json"),
                HeaderHelper.createHeaders("VH"),
                "/resources/video-hearing",
                "id",
                201
        );

        restClientHelper.performSecurePatchRequestAndValidate(
                "{\"updated_by\": \"string\",\"cancel_reason\": \"string\"}",
                HeaderHelper.createHeaders("VH"),
                String.format("/hearings/%s/cancel", getHearingId(response)),
                "",
                204
        );
    }

    /**
     * Test with a valid hearing id and a valid set of headers but no payload, expect 400.
     */
    @Test
    void vhPatchCancelVideoHearingNoPayload() throws UnknownHostException {
        restClientHelper.performSecurePatchRequestAndValidate(
                "",
                HeaderHelper.createHeaders("VH"),
                "/hearings/933cf0bb-418c-4664-892b-00b56f05fae9/cancel",
                "Bad Request.",
                400
        );
    }

    /**
     * Takes in a file name and returns it as a string, used to extract json from payload.
     *
     * @param fileName The name of the file.
     * @return A string of the json content within the file.
     */
    private String getJsonPayloadFileAsString(String fileName) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        return Files.readString(Path.of(classLoader.getResource(fileName).getPath()));
    }

    /**
     * Get the hearing id from the response.
     *
     * @param response The response from creating a hearing.
     * @return A string of the hearing id.
     */
    private String getHearingId(Response response) {
        String newlyCreatedHearing = response.getHeader("Location");
        return newlyCreatedHearing.substring(newlyCreatedHearing.lastIndexOf('/') + 1);
    }
}
