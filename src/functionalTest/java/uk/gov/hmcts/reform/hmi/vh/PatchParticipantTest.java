package uk.gov.hmcts.reform.hmi.vh;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
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

@SpringBootTest
@ActiveProfiles(profiles = "functional")
@SuppressWarnings("PMD.LawOfDemeter")
class PatchParticipantTest {

    @Value("${apim_url}")
    private String apimUrl;
    @Autowired
    RestClientHelper restClientHelper;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = apimUrl;
    }

    /**
     * Test with a valid participant id and update it with valid payload, expect 200.
     */
    @Test
    void vhPatchParticipantTestSuccessful() throws IOException {
        Response response = restClientHelper.performSecurePostRequestAndValidateWithResponse(
                getJsonPayloadFileAsString("vh/create-vh-hearing.json"),
                HeaderHelper.createHeaders("VH"),
                "/resources/video-hearing?version=v2",
                "id",
                201
        );

        Response participantResponse = restClientHelper.performSecurePostRequestAndValidateWithResponse(
                getJsonPayloadFileAsString("vh/create-participant.json"),
                HeaderHelper.createHeaders("VH"),
                String.format("/%s/participants%s", getHearingId(response),
                        "?version=v2"),
                "",
                200
        );

        String participantResponseBody = participantResponse.getBody().asString();
        JsonPath jsonPath = new JsonPath(participantResponseBody);

        restClientHelper.performSecurePatchRequestAndValidate(
                getJsonPayloadFileAsString("vh/update-participant.json")
                        .replace("PARTICIPANT_ID", jsonPath.getString("id[0]")),
                HeaderHelper.createHeaders("VH"),
                String.format("/%s/participants/%s?version=v2", getHearingId(response),
                        jsonPath.getString("id[0]")),
                "",
                200
        );
    }

    /**
     * Test with an invalid hearing id and a valid set of headers and valid payload, expect 400.
     */
    @Test
    void vhPatchParticipantTestInvalidId() throws IOException {

        restClientHelper.performSecurePatchRequestAndValidate(
                "",
                HeaderHelper.createHeaders("VH"),
                "/test/participants/test?version=v2",
                "The value 'test' is not valid.",
                400
        );
    }
}
