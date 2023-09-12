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
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Functional tests for the VH endpoint (/resources/video-hearing/{id}).
 */
@SpringBootTest
@ActiveProfiles(profiles = "functional")
class DeleteVideoHearingTest {

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
    void vhDeleteVideoHearingSuccessful() throws IOException {
        Response response = restClientHelper.performSecurePostRequestAndValidateWithResponse(
                getJsonPayloadFileAsString("vh/create-vh-hearing.json"),
                HeaderHelper.createHeaders("VH"),
                "/resources/video-hearing",
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
