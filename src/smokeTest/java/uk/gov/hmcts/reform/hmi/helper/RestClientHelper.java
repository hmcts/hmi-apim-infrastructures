package uk.gov.hmcts.reform.hmi.helper;

import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;

/**
 * Class to help with sending http requests.
 */
@SuppressWarnings("HideUtilityClassConstructor")
public final class RestClientHelper {

    @Value("${token_url}")
    private static String tokenUrl;

    @Value("${token_tenant_id}")
    private static String tokenTenantId;

    @Value("${grant_type}")
    private static String grantType;

    @Value("${client_id}")
    private static String clientId;

    @Value("${client_secret}")
    private static String clientSecret;

    @Value("${scope}")
    private static String scope;

    public static void performGetRequestAndValidate(String path, String expectedBody,
                                                    int expectedStatusCode) {

        when().request("GET", path)
                .then()
                .log()
                .ifValidationFails()
                .assertThat()
                .body(containsString(expectedBody))
                .statusCode(expectedStatusCode);
    }

    public static void performSecureGetRequestAndValidate(String path, String expectedBody,
                                                    int expectedStatusCode, Map<String, String> headers) {
        given().headers(headers).auth().oauth2(generateOAuthToken())
                .when().request("GET", path).then()
                .log().ifValidationFails().assertThat()
                .body(containsString(expectedBody))
                .statusCode(expectedStatusCode);
    }

    /**
     * Call to microsoft to get a bearer token.
     *
     * @return A bearer token that can be used in the requests to hmi.
     */
    private static String generateOAuthToken() {
        String fullTokenApiUrl = String.format(tokenUrl, tokenTenantId);
        final String bodyForToken = String.format("grant_type=%s&client_id=%s&client_secret=%s&scope=%s",
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
