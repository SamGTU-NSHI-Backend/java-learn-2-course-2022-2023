package ru.nshi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Data
public class Message {
    @EqualsAndHashCode.Include
    private Integer id;
    @ToString.Include
    private String value;

    public Message(String value) {
        this.value = value;
    }
}
