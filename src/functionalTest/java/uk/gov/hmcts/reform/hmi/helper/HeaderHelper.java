package uk.gov.hmcts.reform.hmi.helper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Helper class for creating the headers required to send requests into HMI.
 */
@SuppressWarnings("HideUtilityClassConstructor")
public final class HeaderHelper {

    /**
     * Create the standard set of headers required to send data into HMI APIM.
     *
     * @param destinationSystem The system that the data will be sent to e.g. PIH.
     * @param sourceSystem The system that the data will be sent from e.g. CFT.
     * @return a map containing the headers.
     */
    public static Map<String, String> createHeaders(String destinationSystem, String sourceSystem) {

        final LocalDateTime now = LocalDateTime.now();
        final String requestCreatedAt = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss'Z'"));

        Map<String,String> headersAsMap = new ConcurrentHashMap<>();
        headersAsMap.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headersAsMap.put("Accept", MediaType.APPLICATION_JSON_VALUE);
        headersAsMap.put("Source-System", sourceSystem);
        headersAsMap.put("Destination-System", destinationSystem);
        headersAsMap.put("Request-Created-At", requestCreatedAt);
        return headersAsMap;
    }

    /**
     * Create the standard set of headers required to send data into HMI APIM.
     *
     * @param destinationSystem The system that the data will be sent to e.g. PIH.
     * @return a map containing the headers.
     */
    public static Map<String, String> createHeaders(String destinationSystem) throws UnknownHostException {
        return createHeaders(destinationSystem, "EMULATOR");
    }

    /**
     * Helper method to only add a value into a map if not null or empty and if absent.
     *
     * @param map The map to add the new value to.
     * @param key The key to add into the map.
     * @param value The value associated with the key to add into the map.
     */
    public static void putIfNotNullOrEmpty(Map<String, String> map, String key, String value) {
        if (StringUtils.isNotBlank(value)) {
            map.putIfAbsent(key, value);
        }
    }
}
