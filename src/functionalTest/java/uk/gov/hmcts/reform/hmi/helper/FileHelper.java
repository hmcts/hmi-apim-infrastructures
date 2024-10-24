package uk.gov.hmcts.reform.hmi.helper;

import io.restassured.response.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SuppressWarnings({"HideUtilityClassConstructor", "PMD.LawOfDemeter"})
public class FileHelper {

    /**
     * Takes in a file name and returns it as a string, used to extract json from payload.
     *
     * @param fileName The name of the file.
     * @return A string of the json content within the file.
     */
    public static String getJsonPayloadFileAsString(String fileName) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        return Files.readString(Path.of(classLoader.getResource(fileName).getPath()));
    }

    /**
     * Get the hearing id from the response.
     *
     * @param response The response from creating a hearing.
     * @return A string of the hearing id.
     */
    public static String getHearingId(Response response) {
        String newlyCreatedHearing = response.getHeader("Location");
        return newlyCreatedHearing.substring(newlyCreatedHearing.lastIndexOf('/') + 1);
    }
}
