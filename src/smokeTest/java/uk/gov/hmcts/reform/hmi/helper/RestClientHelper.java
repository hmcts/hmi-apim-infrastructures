package uk.gov.hmcts.reform.hmi.helper;

import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;

/**
 * Class to help with sending http requests.
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

    public void performGetRequestAndValidate(String path, String expectedBody,
                                                    int expectedStatusCode) {

        when().request("GET", path)
                .then()
                .log()
                .ifValidationFails()
                .assertThat()
                .body(containsString(expectedBody))
                .statusCode(expectedStatusCode);
    }

    public void performSecureGetRequestAndValidate(String path, String expectedBody,
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
