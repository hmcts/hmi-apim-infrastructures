package uk.gov.hmcts.reform.hmi.pip;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.hmi.helper.HeaderHelper;
import uk.gov.hmcts.reform.hmi.helper.RestClientHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static uk.gov.hmcts.reform.hmi.helper.HeaderHelper.putIfNotNullOrEmpty;

/**
 * Functional tests for the PIP endpoint (publication).
 */
@SpringBootTest
@ActiveProfiles(profiles = "functional")
@SuppressWarnings({"PMD.TooManyMethods", "PMD.AvoidReassigningParameters", "PMD.LawOfDemeter"})
class PostPublicationTest {

    @Value("${apim_url}")
    private String apimUrl;
    @Autowired
    RestClientHelper restClientHelper;

    private static final LocalDateTime CURRENT_DATETIME = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

    private static final String DESTINATION_SYSTEM = "PIH";
    private static final String EMULATOR = "EMULATOR";
    private static final String LIST = "LIST";
    private static final String PUBLIC = "PUBLIC";
    private static final String ENGLISH = "ENGLISH";
    private static final String COURT_ID = "5555";
    private static final String CARE_STANDARDS_LIST = "CARE_STANDARDS_LIST";
    private static final String SUCCESSFUL_TEST_FILE = "pip/successful-care-standards-list.json";
    private static final String ENDPOINT = "/pih/publication";

    @BeforeEach
    void setup() {
        RestAssured.baseURI = apimUrl;
    }

    /**
     * Tests with a valid payload and a set of valid headers, response should return an artefactId.
     */
    @Test
    void pipPostPublicationSuccessful() throws IOException {
        Map<String, String> requestHeaders = updateHeadersWithPipSpecific(
                HeaderHelper.createHeaders(DESTINATION_SYSTEM),
                EMULATOR,
                LIST,
                PUBLIC,
                ENGLISH,
                CURRENT_DATETIME.toString(),
                CURRENT_DATETIME.plusDays(1).toString(),
                CARE_STANDARDS_LIST,
                COURT_ID,
                CURRENT_DATETIME.toString()
        );

        restClientHelper.performSecurePostRequestAndValidate(
                getJsonPayloadFileAsString(SUCCESSFUL_TEST_FILE),
                requestHeaders,
                ENDPOINT,
                "artefactId",
                201
        );
    }

    /**
     * Test with an invalid json payload, this should be rejected and a reason returned.
     */
    @Test
    void pipPostPublicationInvalidJson() throws IOException {
        Map<String, String> requestHeaders = updateHeadersWithPipSpecific(
                HeaderHelper.createHeaders(DESTINATION_SYSTEM),
                EMULATOR,
                LIST,
                PUBLIC,
                ENGLISH,
                CURRENT_DATETIME.toString(),
                CURRENT_DATETIME.plusDays(1).toString(),
                CARE_STANDARDS_LIST,
                COURT_ID,
                CURRENT_DATETIME.toString()
        );

        restClientHelper.performSecurePostRequestAndValidate(
                getJsonPayloadFileAsString("pip/invalid-care-standards-list.json"),
                requestHeaders,
                ENDPOINT,
                "$.document: required property 'publicationDate' not found",
                400
        );
    }

    /**
     * Test with a missing provenance, should be allowed through as source system gets converted to provenance in
     * policy file.
     */
    @Test
    void pipPostPublicationMissingProvenance() throws IOException {
        Map<String, String> requestHeaders = updateHeadersWithPipSpecific(
                HeaderHelper.createHeaders(DESTINATION_SYSTEM),
                null,
                LIST,
                PUBLIC,
                ENGLISH,
                CURRENT_DATETIME.toString(),
                CURRENT_DATETIME.plusDays(1).toString(),
                CARE_STANDARDS_LIST,
                COURT_ID,
                CURRENT_DATETIME.toString()
        );

        restClientHelper.performSecurePostRequestAndValidate(
                getJsonPayloadFileAsString(SUCCESSFUL_TEST_FILE),
                requestHeaders,
                ENDPOINT,
                "artefactId",
                201
        );
    }

    /**
     * Test with a missing type, this is a required header within pip.
     */
    @Test
    void pipPostPublicationMissingType() throws IOException {
        Map<String, String> requestHeaders = updateHeadersWithPipSpecific(
                HeaderHelper.createHeaders(DESTINATION_SYSTEM),
                EMULATOR,
                null,
                PUBLIC,
                ENGLISH,
                CURRENT_DATETIME.toString(),
                CURRENT_DATETIME.plusDays(1).toString(),
                CARE_STANDARDS_LIST,
                COURT_ID,
                CURRENT_DATETIME.toString()
        );

        restClientHelper.performSecurePostRequestAndValidate(
                getJsonPayloadFileAsString(SUCCESSFUL_TEST_FILE),
                requestHeaders,
                ENDPOINT,
                "x-type is mandatory however an empty value is provided",
                400
        );
    }

    /**
     * Test with a missing sensitivity, this should be allowed through as pip defaults the value.
     */
    @Test
    void pipPostPublicationMissingSensitivity() throws IOException {
        Map<String, String> requestHeaders = updateHeadersWithPipSpecific(
                HeaderHelper.createHeaders(DESTINATION_SYSTEM),
                EMULATOR,
                LIST,
                null,
                ENGLISH,
                CURRENT_DATETIME.toString(),
                CURRENT_DATETIME.plusDays(1).toString(),
                CARE_STANDARDS_LIST,
                COURT_ID,
                CURRENT_DATETIME.toString()
        );

        restClientHelper.performSecurePostRequestAndValidate(
                getJsonPayloadFileAsString(SUCCESSFUL_TEST_FILE),
                requestHeaders,
                ENDPOINT,
                "artefactId",
                201
        );
    }

    /**
     * Test with a missing language, this is a required header within pip.
     */
    @Test
    void pipPostPublicationMissingLanguage() throws IOException {
        Map<String, String> requestHeaders = updateHeadersWithPipSpecific(
                HeaderHelper.createHeaders(DESTINATION_SYSTEM),
                EMULATOR,
                LIST,
                PUBLIC,
                null,
                CURRENT_DATETIME.toString(),
                CURRENT_DATETIME.plusDays(1).toString(),
                CARE_STANDARDS_LIST,
                COURT_ID,
                CURRENT_DATETIME.toString()
        );

        restClientHelper.performSecurePostRequestAndValidate(
                getJsonPayloadFileAsString(SUCCESSFUL_TEST_FILE),
                requestHeaders,
                ENDPOINT,
                "x-language is mandatory however an empty value is provided",
                400
        );
    }

    /**
     * Test with a missing display from, this is a required header within pip when type is LIST.
     */
    @Test
    void pipPostPublicationMissingDisplayFrom() throws IOException {
        Map<String, String> requestHeaders = updateHeadersWithPipSpecific(
                HeaderHelper.createHeaders(DESTINATION_SYSTEM),
                EMULATOR,
                LIST,
                PUBLIC,
                ENGLISH,
                null,
                CURRENT_DATETIME.plusDays(1).toString(),
                CARE_STANDARDS_LIST,
                COURT_ID,
                CURRENT_DATETIME.toString()
        );

        restClientHelper.performSecurePostRequestAndValidate(
                getJsonPayloadFileAsString(SUCCESSFUL_TEST_FILE),
                requestHeaders,
                ENDPOINT,
                "x-display-from Field is required for artefact type LIST",
                400
        );
    }

    /**
     * Test with a missing display to, this is a required header within pip when type is LIST.
     */
    @Test
    void pipPostPublicationMissingDisplayTo() throws IOException {
        Map<String, String> requestHeaders = updateHeadersWithPipSpecific(
                HeaderHelper.createHeaders(DESTINATION_SYSTEM),
                EMULATOR,
                LIST,
                PUBLIC,
                ENGLISH,
                CURRENT_DATETIME.toString(),
                null,
                CARE_STANDARDS_LIST,
                COURT_ID,
                CURRENT_DATETIME.toString()
        );

        restClientHelper.performSecurePostRequestAndValidate(
                getJsonPayloadFileAsString(SUCCESSFUL_TEST_FILE),
                requestHeaders,
                ENDPOINT,
                "x-display-to Field is required for artefact type LIST",
                400
        );
    }

    /**
     * Test with a missing list type, this is a required header within pip.
     */
    @Test
    void pipPostPublicationMissingListType() throws IOException {
        Map<String, String> requestHeaders = updateHeadersWithPipSpecific(
                HeaderHelper.createHeaders(DESTINATION_SYSTEM),
                EMULATOR,
                LIST,
                PUBLIC,
                ENGLISH,
                CURRENT_DATETIME.toString(),
                CURRENT_DATETIME.plusDays(1).toString(),
                null,
                COURT_ID,
                CURRENT_DATETIME.toString()
        );

        restClientHelper.performSecurePostRequestAndValidate(
                getJsonPayloadFileAsString(SUCCESSFUL_TEST_FILE),
                requestHeaders,
                ENDPOINT,
                "x-list-type is mandatory however an empty value is provided",
                400
        );
    }

    /**
     * Test with a missing court id, this is a required header within pip.
     */
    @Test
    void pipPostPublicationMissingCourtId() throws IOException {
        Map<String, String> requestHeaders = updateHeadersWithPipSpecific(
                HeaderHelper.createHeaders(DESTINATION_SYSTEM),
                EMULATOR,
                LIST,
                PUBLIC,
                ENGLISH,
                CURRENT_DATETIME.toString(),
                CURRENT_DATETIME.plusDays(1).toString(),
                CARE_STANDARDS_LIST,
                null,
                CURRENT_DATETIME.toString()
        );

        restClientHelper.performSecurePostRequestAndValidate(
                getJsonPayloadFileAsString(SUCCESSFUL_TEST_FILE),
                requestHeaders,
                ENDPOINT,
                "x-court-id is mandatory however an empty value is provided",
                400
        );
    }

    /**
     * Test with a missing content date, this is a required header within pip.
     */
    @Test
    void pipPostPublicationMissingContentDate() throws IOException {
        Map<String, String> requestHeaders = updateHeadersWithPipSpecific(
                HeaderHelper.createHeaders(DESTINATION_SYSTEM),
                EMULATOR,
                LIST,
                PUBLIC,
                ENGLISH,
                CURRENT_DATETIME.toString(),
                CURRENT_DATETIME.plusDays(1).toString(),
                CARE_STANDARDS_LIST,
                COURT_ID,
                null
        );

        restClientHelper.performSecurePostRequestAndValidate(
                getJsonPayloadFileAsString(SUCCESSFUL_TEST_FILE),
                requestHeaders,
                ENDPOINT,
                "x-content-date is mandatory however an empty value is provided",
                400
        );
    }

    @Test
    void pipPostPublicationMissingOAuth() throws IOException {
        Map<String, String> requestHeaders = updateHeadersWithPipSpecific(
                HeaderHelper.createHeaders(DESTINATION_SYSTEM),
                EMULATOR,
                LIST,
                PUBLIC,
                ENGLISH,
                CURRENT_DATETIME.toString(),
                CURRENT_DATETIME.plusDays(1).toString(),
                CARE_STANDARDS_LIST,
                COURT_ID,
                CURRENT_DATETIME.toString()
        );

        restClientHelper.performPostRequestAndValidate(
                getJsonPayloadFileAsString(SUCCESSFUL_TEST_FILE),
                requestHeaders,
                ENDPOINT,
                "Access denied due to invalid OAuth information",
                401
        );
    }

    /**
     * Method to update the default header map with pip specific headers.
     *
     * @param headers The map to add the new headers to.
     * @param provenance The provenance required in pip e.g. EMULATOR.
     * @param type The type of publication e.g. LIST.
     * @param sensitivity The sensitivity of the publication e.g. PUBLIC.
     * @param language The language of the publication e.g. ENGLISH.
     * @param displayFrom The date to display the publication from.
     * @param displayTo The date to display the publication to.
     * @param listType The type of list e.g. CARE_STANDARDS_LIST.
     * @param courtId The id of the court to send the publication to.
     * @param contentDate The content date of the publication.
     * @return The same input map with the updated pip headers.
     */
    private Map<String, String> updateHeadersWithPipSpecific(Map<String, String> headers,
                                                             String provenance,
                                                             String type,
                                                             String sensitivity,
                                                             String language,
                                                             String displayFrom,
                                                             String displayTo,
                                                             String listType,
                                                             String courtId,
                                                             String contentDate) {
        if (headers == null) {
            headers = new ConcurrentHashMap<>();
        }

        putIfNotNullOrEmpty(headers, "x-provenance", provenance);
        putIfNotNullOrEmpty(headers, "x-type", type);
        putIfNotNullOrEmpty(headers, "x-sensitivity", sensitivity);
        putIfNotNullOrEmpty(headers, "x-language", language);
        putIfNotNullOrEmpty(headers, "x-display-from", displayFrom);
        putIfNotNullOrEmpty(headers, "x-display-to", displayTo);
        putIfNotNullOrEmpty(headers, "x-list-type", listType);
        putIfNotNullOrEmpty(headers, "x-court-id", courtId);
        putIfNotNullOrEmpty(headers, "x-content-date", contentDate);

        return headers;
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
}
