package ru.nshi.model;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
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
