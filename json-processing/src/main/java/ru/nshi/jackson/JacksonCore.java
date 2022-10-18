package ru.nshi.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

public class JacksonCore {
    public static final String API_BASE = "https://itunes.apple.com/";
    public static final String SEARCH_SUFFIX = "search";
    public static final String LOOKUP_SUFFIX = "lookup";

    public static final HttpClient client = HttpClient.newHttpClient();

    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException, InterruptedException {
        mapper.findAndRegisterModules();

        HttpResponse<String> response = searchSongs("AC/DC", 10, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() >= 300) {
            System.out.println("Error: " + response.body());
            return;
        }

        JsonNode node = mapper.readTree(response.body());
//        parseUseJsonPath(node);
        parseUseClasses(node);
    }

    static URI createUri(String base, Map<String, ?> params) {
        String paramsStr = params.entrySet().stream()
            .map(p -> p.getKey() + "=" + URLEncoder.encode(p.getValue().toString(), StandardCharsets.UTF_8))
            .collect(Collectors.joining("&"));
        return URI.create(base + "?" + paramsStr);
    }

    static <T> HttpResponse<T> searchSongs(String artistName, int limit, HttpResponse.BodyHandler<T> handler) throws IOException, InterruptedException {
        Map<String, ?> params = Map.of("limit", limit,
            "term", artistName,
            "media", "music",
            "entity", "song",
            "attribute", "artistTerm");

        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(createUri(API_BASE + SEARCH_SUFFIX, params))
            .build();

        return client.send(request, handler);
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
                duration.toString(), trackResult.getArtist());
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
        Map<String, ?> params = Map.of("id", artistId);
        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(createUri(API_BASE + LOOKUP_SUFFIX, params)).build();
        HttpResponse<InputStream> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            JsonNode jsonNode = mapper.readTree(response.body());
            if (jsonNode.at("/resultCount").asLong() < 1) {
                throw new RuntimeException("Result list is empty");
            }
            jsonNode = jsonNode.at("/results");
            String name = jsonNode.get(0).at("/artistName").asText("George Strait");
            return name;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
