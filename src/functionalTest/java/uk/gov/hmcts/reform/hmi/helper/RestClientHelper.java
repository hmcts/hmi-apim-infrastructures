package uk.gov.hmcts.reform.hmi.helper;

import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

/**
 * Helper class to send http requests and validate the responses.
 */
@Service
@SuppressWarnings("HideUtilityClassConstructor")
public final class RestClientHelper {

    @Value("${token_url}")
    private String tokenUrl;

    @Value("${token_tenant_id}")
    private String tokenTenantId;

    @Value("${grant_type}")
    private String grantType;

    @Value("${client_id}")
    private String clientId;

    @Value("${client_secret}")
    private String clientSecret;

    @Value("${scope}")
    private String scope;

    /**
     * Perform a post request with an OAuth token and validate the response.
     *
     * @param payload The payload to send in the request.
     * @param headers The headers to send in the request.
     * @param path The path to send the request on.
     * @param expectedResponse The expected response body.
     * @param expectedStatusCode The expected response code.
     */
    public void performSecurePostRequestAndValidate(String payload,
                                                           Map<String, String> headers,
                                                           String path,
                                                           String expectedResponse,
                                                             int expectedStatusCode) {
        given().body(payload)
                .headers(headers)
                .auth().oauth2(generateOAuthToken())
                .when().request("POST", path).then()
                .log().ifValidationFails()
                .assertThat()
                .body(containsString(expectedResponse))
                .statusCode(expectedStatusCode);
    }

    /**
     * Perform a post request without an OAuth token and validate the response.
     *
     * @param payload The payload to send in the request.
     * @param headers The headers to send in the request.
     * @param path The path to send the request on.
     * @param expectedResponse The expected response body.
     * @param expectedStatusCode The expected response code.
     */
    public void performPostRequestAndValidate(String payload,
                                                           Map<String, String> headers,
                                                           String path,
                                                           String expectedResponse,
                                                           int expectedStatusCode) {

        given().body(payload)
                .headers(headers)
                .when().request("POST", path).then()
                .log().ifValidationFails()
                .assertThat()
                .body(containsString(expectedResponse))
                .statusCode(expectedStatusCode);
    }

    /**
     * Call to microsoft to get a bearer token.
     *
     * @return A bearer token that can be used in the requests to hmi.
     */
    private String generateOAuthToken() {
        String fullTokenApiUrl = String.format(tokenUrl, tokenTenantId);
        final String bodyForToken = String.format("grant_type=%s&client_id=%s&client_secret=%s&scope=%s/.default",
                grantType, clientId, clientSecret, scope);

        return given()
                .body(bodyForToken)
                .contentType(ContentType.URLENC)
                .baseUri(fullTokenApiUrl)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .response().jsonPath().getString("access_token");
    }
}