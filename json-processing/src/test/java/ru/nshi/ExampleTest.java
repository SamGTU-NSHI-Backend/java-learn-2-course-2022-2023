package ru.nshi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ExampleTest {
    ObjectMapper mapper = new ObjectMapper();
    @Test
    void testJackson() throws JsonProcessingException {
        ExampleModel exampleModel = new ExampleModel(123L);
        String actual = mapper.writeValueAsString(exampleModel);
        Assertions.assertEquals("{\"prop\":123}", actual);
    }

    public static class ExampleModel{
        @JsonProperty(value = "prop")
        private Long id;

        public ExampleModel() {
        }

        public ExampleModel(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }
}
