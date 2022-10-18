package ru.nshi.jackson.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.nshi.jackson.std.UTCDateTimeDeserializer;

import java.time.OffsetDateTime;

//@JsonSerialize(using = DogSerializer.class)
public class Dog {

    @JsonDeserialize(using = UTCDateTimeDeserializer.class)
    private OffsetDateTime birthDate;
    private String name;
    @JsonIgnore
    private Integer ignored;

    public OffsetDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(OffsetDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //    @JsonValue
    public String value() {
        return "Simple value";
    }
}
