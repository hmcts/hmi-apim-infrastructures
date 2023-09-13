package uk.gov.hmcts.reform.hmi.helper;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
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
     * Perform a post request with an OAuth token and validate the response and return the response.
     *
     * @param payload The payload to send in the request.
     * @param headers The headers to send in the request.
     * @param path The path to send the request on.
     * @param expectedResponse The expected response body.
     * @param expectedStatusCode The expected response code.
     */
    public Response performSecurePostRequestAndValidateWithResponse(String payload,
                                                                    Map<String, String> headers,
                                                                    String path,
                                                                    String expectedResponse,
                                                                    int expectedStatusCode) {
        return given().body(payload)
                .headers(headers)
                .auth().oauth2(generateOAuthToken())
                .when().request("POST", path).then()
                .log().ifValidationFails()
                .assertThat()
                .body(containsString(expectedResponse))
                .statusCode(expectedStatusCode)
                .and()
                .extract()
                .response();
    }

    /**
     * Perform a delete request with an OAuth token and validate the response.
     *
     * @param payload The payload to send in the request.
     * @param headers The headers to send in the request.
     * @param path The path to send the request on.
     * @param expectedResponse The expected response body.
     * @param expectedStatusCode The expected response code.
     */
    public void performSecureDeleteRequestAndValidate(String payload,
                                                    Map<String, String> headers,
                                                    String path,
                                                    String expectedResponse,
                                                    int expectedStatusCode) {
        given().body(payload)
                .headers(headers)
                .auth().oauth2(generateOAuthToken())
                .when().request("DELETE", path).then()
                .log().ifValidationFails()
                .assertThat()
                .body(containsString(expectedResponse))
                .statusCode(expectedStatusCode);
    }

    /**
     * Perform a patch request with an OAuth token and validate the response.
     *
     * @param payload The payload to send in the request.
     * @param headers The headers to send in the request.
     * @param path The path to send the request on.
     * @param expectedResponse The expected response body.
     * @param expectedStatusCode The expected response code.
     */
    public void performSecurePatchRequestAndValidate(String payload,
                                                    Map<String, String> headers,
                                                    String path,
                                                    String expectedResponse,
                                                    int expectedStatusCode) {
        given().body(payload)
                .headers(headers)
                .auth().oauth2(generateOAuthToken())
                .when().request("PATCH", path).then()
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
     * Perform a get request with an OAuth token, query parameters and validate the response.
     *
     * @param queryParams The query parameters to send in the request.
     * @param headers The headers to send in the request.
     * @param path The path to send the request on.
     * @param expectedResponse The expected response body.
     * @param expectedStatusCode The expected response code.
     */
    public void performSecureGetRequestAndValidateWithQueryParams(Map<String, String> headers,
                                                            String path,
                                                            final Map<String, String> queryParams,
                                                            String expectedResponse,
                                                            int expectedStatusCode) {

        given().queryParams(queryParams)
                .headers(headers)
                .auth().oauth2(generateOAuthToken())
                .when().request("GET", path).then()
                .log().ifValidationFails()
                .assertThat()
                .body(containsString(expectedResponse))
                .statusCode(expectedStatusCode);
    }

    /**
     * Perform a post request with an OAuth token and validate the response.
     *
     * @param headers The headers to send in the request.
     * @param path The path to send the request on.
     * @param expectedStatusCode The expected response code.
     */
    public void performSecureGetRequestAndValidate(Map<String, String> headers,
                                                    String path,
                                                    String expectedResponse,
                                                    int expectedStatusCode) {
        given()
                .headers(headers)
                .auth().oauth2(generateOAuthToken())
                .when().request("GET", path).then()
                .log().ifValidationFails()
                .assertThat()
                .body(containsString(expectedResponse))
                .statusCode(expectedStatusCode);
    }

    /**
     * Perform a get request without an OAuth token and validate the response.
     *
     * @param headers The headers to send in the request.
     * @param path The path to send the request on.
     * @param expectedResponse The expected response body.
     * @param expectedStatusCode The expected response code.
     */
    public void performGetRequestAndValidate(Map<String, String> headers,
                                              String path,
                                              String expectedResponse,
                                              int expectedStatusCode) {

        given()
                .headers(headers)
                .when().request("GET", path).then()
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
