package ru.nshi.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    public static final String VALUE_PROPERTY = "value";
    @JsonProperty(value = VALUE_PROPERTY)
    private String value;

    public Message(String value) {
        this.value = value;
    }

    public Message() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
