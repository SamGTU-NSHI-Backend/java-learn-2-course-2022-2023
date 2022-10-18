package ru.nshi.jackson.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ru.nshi.jackson.model.Dog;

import java.io.IOException;
import java.time.OffsetDateTime;

public class DogSerializer extends StdSerializer<Dog> {

    public DogSerializer() {
        super(Dog.class);
    }

    @Override
    public void serialize(Dog value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("doggyName", value.getName());
        gen.writeStringField("serializationDate", OffsetDateTime.now().toString());
        gen.writeEndObject();
    }
}
