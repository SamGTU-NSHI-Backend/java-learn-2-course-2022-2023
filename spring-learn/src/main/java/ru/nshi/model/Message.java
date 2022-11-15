package ru.nshi.model;

public class Message {
    private String value;
    private Integer id;

    public Message() {
    }

    public Message(String value) {
        this.value = value;
    }

    public Message(String value, Integer id) {
        this.value = value;
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
