package ru.nshi.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.nshi.jackson.model.Dog;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class JacksonIntroTest {

    ObjectMapper om = new ObjectMapper();

    @BeforeEach
    public void initEach() {
        om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        om.registerModule(new JavaTimeModule());
        om.findAndRegisterModules();
    }

    @Test
    @DisplayName("Custom deserialization method")
    public void testCustomDeserializer() throws JsonProcessingException {
        String json = "{\"birthDate\":\"2022-10-10T14:10:10.123+05\", " +
            "\"unrecognizedField\": \"unknown\"}";
        Dog dog = om.readerFor(Dog.class)
            .readValue(json);
        Assertions.assertEquals(
            OffsetDateTime.of(2022, 10, 10, 9, 10, 0, 0,
                ZoneOffset.UTC),
            dog.getBirthDate()
        );
        System.out.println(dog.getBirthDate());
    }

    @Test
    public void testDog() throws JsonProcessingException {
//        SimpleModule module = new SimpleModule();
//        module.addSerializer(new DogSerializer());
//        om.registerModule(module);
        Dog dog = new Dog();
        dog.setName("Doggy name");
        dog.setBirthDate(OffsetDateTime.now());

        String res = om.writerWithDefaultPrettyPrinter()
            .writeValueAsString(dog);
        System.out.println(res);
    }


}
