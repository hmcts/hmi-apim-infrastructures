package uk.gov.hmcts.reform.hmi.resources;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.hmi.helper.HeaderHelper;
import uk.gov.hmcts.reform.hmi.helper.RestClientHelper;

import java.net.UnknownHostException;
import java.util.Map;

/**
 * Functional tests for the endpoint used by different consumers (/resources/linked-hearing-group).
 */
@SpringBootTest
@ActiveProfiles(profiles = "functional")
class PostLinkHearingGroupTest {

    @Value("${apim_url}")
    private String apimUrl;

    @Autowired
    RestClientHelper restClientHelper;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = apimUrl;
    }

    /**
     * Test with a valid set of headers, but empty body, response should return 400.
     */
    @Test
    void postCreateLinkHearingGroupFail() throws UnknownHostException {
        restClientHelper.performSecurePostRequestAndValidate(
                "{}",
                HeaderHelper.createHeaders("SNL"),
                "/resources/linked-hearing-group",
                "",
                400
        );
    }

    /**
     * Test with a Invalid header, response should return 400.
     */
    @Test
    void postCreateLinkHearingGroupInvalidHeaderFail() throws UnknownHostException {
        Map<String, String> requestHeader =  HeaderHelper.createHeaders("SNL");
        requestHeader.remove("Destination-System");

        restClientHelper.performSecurePostRequestAndValidate(
                "{}",
                requestHeader,
                "/resources/linked-hearing-group",
                "Missing/Invalid Header Destination-System",
                400
        );
    }
}
