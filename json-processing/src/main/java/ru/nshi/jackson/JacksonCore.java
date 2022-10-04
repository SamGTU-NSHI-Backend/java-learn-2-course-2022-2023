package ru.nshi.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class JacksonCore {
    public static final String JSON_PATH = "json-processing/src/main/resources/example.json";

    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException {
        mapper.findAndRegisterModules();
        try (InputStream stream = new FileInputStream(JSON_PATH)) {
            JsonNode node = mapper.readTree(stream);
//            parseUseJsonPath(node);
            parseUseClasses(node);
        }
    }

    public static void parseUseClasses(JsonNode node) throws JsonProcessingException, NoSuchFieldException, IllegalAccessException {
        JsonNode results = node.get("results");

        for (JsonNode result : results) {
            TrackResult trackResult = mapper.treeToValue(result, TrackResult.class);
            if (trackResult.getArtist() == null) {
                continue;
            }
            Duration duration = Duration.of(trackResult.getTrackTimeMillis(),
                    ChronoUnit.MILLIS);
            String artistName = getArtistNameById(trackResult.getArtist());
            TrackResponse response = new TrackResponse(trackResult.getTrackName(), artistName, trackResult.getCountry(),
                    duration.toString());
            System.out.println(mapper.writeValueAsString(response));
        }
    }

    public static void parseUseJsonPath(JsonNode node) throws JsonProcessingException {
        int resultCount = node
                .at("/resultCount").asInt();

        System.out.println("Results count: " + resultCount);

        JsonNode results = node.get("results");

        for (JsonNode result : results) {
            JsonNode artistIdNode = result.get("artistId");
            if (artistIdNode.isNull()) {
                continue;
            }
            long artistId = artistIdNode
                    .asLong();

            String artistName = getArtistNameById(artistId);
            String trackName = result.get("trackName").asText();
            long trackTimeMillis = result.get("trackTimeMillis").asLong();
            String country = result.get("country").asText();

            Duration duration = Duration.of(trackTimeMillis, ChronoUnit.MILLIS);

            ObjectNode objectNode = mapper.createObjectNode()
                    .put("artistName", artistName)
                    .put("trackName", trackName)
                    .put("trackTime", duration.toString())
                    .put("country", country);

            System.out.println(mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(objectNode));
        }
    }

    public static String getArtistNameById(long artistId) {
        return "George Strait";
    }
}
