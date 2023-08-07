package uk.gov.hmcts.reform.hmi.helper;

import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Helper class for creating the headers required to send requests into HMI.
 */
@SuppressWarnings("HideUtilityClassConstructor")
public final class HeaderHelper {

    public static Map<String, String> createHeaders(String destinationSystem) {

        final LocalDateTime now = LocalDateTime.now();
        final String requestCreatedAt = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss'Z'"));

        Map<String,String> headersAsMap = new ConcurrentHashMap<>();
        headersAsMap.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headersAsMap.put("Accept", MediaType.APPLICATION_JSON_VALUE);
        headersAsMap.put("Source-System", "EMULATOR");
        headersAsMap.put("Destination-System", destinationSystem);
        headersAsMap.put("Request-Created-At", requestCreatedAt);
        return headersAsMap;
    }
}
