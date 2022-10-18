package ru.nshi.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class HttpClientPostWithJson {
    public static final HttpClient client = HttpClient.newHttpClient();
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException, InterruptedException {
        TrackResponse data = new TrackResponse("trackName", "artistName", "country",
            "1234", 1234L);
        HttpRequest request = HttpRequest.newBuilder()
            .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(data), StandardCharsets.UTF_8))
            .header("Content-Type", "application/json")
            .uri(URI.create("https://httpbin.org/post"))
            .build();

        HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

        System.out.println(mapper.readTree(response.body()));
    }
}
