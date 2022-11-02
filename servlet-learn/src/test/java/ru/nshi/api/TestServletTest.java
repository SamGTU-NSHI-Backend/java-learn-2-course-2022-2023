package ru.nshi.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class TestServletTest {
    private final ObjectMapper mapper = new ObjectMapper();
    public final String ENDPOINT = "http://localhost:8088/test";

    @Test
    @Disabled("handle only")
    void request() throws IOException, InterruptedException {
        String message = "test";
        Message requestData = new Message(message);
        HttpResponse<InputStream> request = HttpClient.newBuilder().build()
            .send(HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofByteArray(mapper.writeValueAsBytes(requestData)))
                .header("Content-Type", TestServlet.JSON_VALUE)
                .uri(URI.create(ENDPOINT))
                .build(), HttpResponse.BodyHandlers.ofInputStream());

        Message result = mapper.readValue(request.body(), Message.class);
        Assertions.assertEquals(message.toUpperCase(), result.getValue());
    }

}
