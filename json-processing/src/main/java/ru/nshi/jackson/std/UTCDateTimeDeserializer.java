package ru.nshi.jackson.std;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class UTCDateTimeDeserializer extends StdDeserializer<OffsetDateTime> {

    protected UTCDateTimeDeserializer() {
        super(OffsetDateTime.class);
    }

    @Override
    public OffsetDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        String text = p.getText();
        return OffsetDateTime.parse(text)
            .withOffsetSameInstant(ZoneOffset.UTC)
            .truncatedTo(ChronoUnit.MINUTES);
    }
}
