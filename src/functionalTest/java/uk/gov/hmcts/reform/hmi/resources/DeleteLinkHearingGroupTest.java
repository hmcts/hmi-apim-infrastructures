package uk.gov.hmcts.reform.hmi.resources;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.hmi.helper.HeaderHelper;
import uk.gov.hmcts.reform.hmi.helper.RestClientHelper;

import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;

/**
 * Functional tests for the endpoint used by different consumers (/resources/linked-hearing-group).
 */
@SpringBootTest
@ActiveProfiles(profiles = "functional")
class DeleteLinkHearingGroupTest {

    @Autowired
    RestClientHelper restClientHelper;

    private final Random rand;

    public DeleteLinkHearingGroupTest()  throws NoSuchAlgorithmException {
        rand = SecureRandom.getInstanceStrong();
    }

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "https://sds-api-mgmt.staging.platform.hmcts.net/hmi";
    }

    /**
     * Test with a valid set of headers, but empty body, response should return 400.
     */
    @Test
    void deleteLinkHearingGroupFail() throws UnknownHostException {
        int randomId = rand.nextInt(99_999_999);
        String resourcesLinkedHearingGroupIdRootContext = String.format("/resources/linked-hearing-group/%s", randomId);

        restClientHelper.performSecureDeleteRequestAndValidate(
                "{}",
                HeaderHelper.createHeaders("CFT"),
                resourcesLinkedHearingGroupIdRootContext,
                "",
                400
        );
    }

    /**
     * Test with a Invalid header, response should return 400.
     */
    @Test
    void deleteLinkHearingGroupInvalidHeaderFail() throws UnknownHostException {
        int randomId = rand.nextInt(99_999_999);
        String resourcesLinkedHearingGroupIdRootContext = String.format("/resources/linked-hearing-group/%s", randomId);

        Map<String, String> requestHeader =  HeaderHelper.createHeaders("CFT");
        requestHeader.remove("Destination-System");

        restClientHelper.performSecureDeleteRequestAndValidate(
                "{}",
                requestHeader,
                resourcesLinkedHearingGroupIdRootContext,
                "Missing/Invalid Header Destination-System",
                400
        );
    }

}
