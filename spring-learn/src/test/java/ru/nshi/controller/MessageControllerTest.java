package ru.nshi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.nshi.model.Error;
import ru.nshi.model.Message;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MessageControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    @BeforeEach
    void setup() throws Exception {
        Assertions.assertEquals(0, getMessages().size());
    }

    @AfterEach
    void cleanup() throws Exception {
        for (Message message : getMessages()) {
            deleteMessageById(message.getId());
        }
    }

    @Test
    @DisplayName("Full test")
    void fullTest() throws Exception {
        String text = " test ";
        Message message = createMessage(new Message(text));
        Assertions.assertNotNull(message);
        Assertions.assertNotNull(message.getId());
        Assertions.assertEquals("test", message.getValue());

        Message messageById = getMessageById(message.getId());
        Assertions.assertNotNull(messageById);
        Integer id = messageById.getId();
        Assertions.assertEquals(message.getId(), id);
        Assertions.assertEquals(message.getValue(), messageById.getValue());

        Assertions.assertEquals(1, getMessages().size());

        String updateText = "test2";
        Message messageUpdate = updateMessage(id, new Message(updateText));
        Assertions.assertNotNull(messageUpdate);
        Assertions.assertEquals(id, messageUpdate.getId());
        Assertions.assertEquals(updateText, messageUpdate.getValue());

        Assertions.assertEquals(1, getMessages().size());

        Message messageAfterUpdate = getMessageById(message.getId());
        Assertions.assertNotNull(messageAfterUpdate);
        Assertions.assertEquals(id, messageAfterUpdate.getId());
        Assertions.assertEquals(updateText, messageAfterUpdate.getValue());

        Message deletedMessage = deleteMessageById(id);
        Assertions.assertNotNull(deletedMessage);
        Assertions.assertEquals(id, deletedMessage.getId());
        Assertions.assertEquals(updateText, deletedMessage.getValue());

        Error messageByIdError = getMessageByIdError(id, 404);
        Assertions.assertNotNull(messageByIdError);
        Assertions.assertNotNull(messageByIdError.getErrorMessage());
    }

    @ParameterizedTest
    @DisplayName("Create test with empty, blank or null text")
    @ValueSource(strings = {" "})
    @NullAndEmptySource
    void errorCreateTest(String text) throws Exception {
        Error error = createMessageError(new Message(text), 400);
        Assertions.assertNotNull(error);
        Assertions.assertNotNull(error.getErrorMessage());

        Assertions.assertEquals(0, getMessages().size());
    }

    @ParameterizedTest
    @DisplayName("Update test with empty, blank or null text")
    @ValueSource(strings = {" "})
    @NullAndEmptySource
    void errorUpdateTest(String text) throws Exception {
        Message message = createMessage(new Message("test"));
        Integer id = message.getId();

        Error error = updateMessageError(id, new Message(text), 400);
        Assertions.assertNotNull(error);
        Assertions.assertNotNull(error.getErrorMessage());

        Message messageAfterUpdate = getMessageById(id);
        Assertions.assertNotNull(messageAfterUpdate);
        Assertions.assertEquals(message.getId(), messageAfterUpdate.getId());
        Assertions.assertEquals(message.getValue(), messageAfterUpdate.getValue());
    }

    @Test
    @DisplayName("Update test with not existed message")
    void updateNotExistTest() throws Exception {
        Error error = updateMessageError(1, new Message("test"), 404);
        Assertions.assertNotNull(error);
        Assertions.assertNotNull(error.getErrorMessage());
    }

    @Test
    @DisplayName("Get by id with not existed message")
    void getByIdNotExistTest() throws Exception {
        Error error = getMessageByIdError(1, 404);
        Assertions.assertNotNull(error);
        Assertions.assertNotNull(error.getErrorMessage());
    }

    @Test
    @DisplayName("Delete by id with not existed message")
    void deleteByIdNotExistTest() throws Exception {
        Error error = deleteMessageByIdError(1, 404);
        Assertions.assertNotNull(error);
        Assertions.assertNotNull(error.getErrorMessage());
    }


    List<Message> getMessages() throws Exception {
        String response = mvc.perform(get(MessageController.MAPPING))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();
        return mapper.readValue(response, new TypeReference<List<Message>>() {
        });
    }

    Message getMessageById(Integer id) throws Exception {
        String response = mvc.perform(get(MessageController.MAPPING + "/" + id))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();
        return mapper.readValue(response, Message.class);
    }

    Error getMessageByIdError(Integer id, int expectedStatus) throws Exception {
        String response = mvc.perform(get(MessageController.MAPPING + "/" + id))
            .andExpect(status().is(expectedStatus))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();
        return mapper.readValue(response, Error.class);
    }

    Message createMessage(Message message) throws Exception {
        String response = mvc.perform(post(MessageController.MAPPING)
                .content(mapper.writeValueAsString(message))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();
        return mapper.readValue(response, Message.class);
    }

    Error createMessageError(Message message, int expectedStatus) throws Exception {
        String response = mvc.perform(post(MessageController.MAPPING)
                .content(mapper.writeValueAsString(message))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is(expectedStatus))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();
        return mapper.readValue(response, Error.class);
    }

    Message updateMessage(Integer id, Message message) throws Exception {
        String response = mvc.perform(put(MessageController.MAPPING + "/" + id)
                .content(mapper.writeValueAsString(message))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();
        return mapper.readValue(response, Message.class);
    }

    Error updateMessageError(Integer id, Message message, int expectedStatus) throws Exception {
        String response = mvc.perform(put(MessageController.MAPPING + "/" + id)
                .content(mapper.writeValueAsString(message))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is(expectedStatus))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();
        return mapper.readValue(response, Error.class);
    }

    Message deleteMessageById(Integer id) throws Exception {
        String response = mvc.perform(delete(MessageController.MAPPING + "/" + id))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();
        return mapper.readValue(response, Message.class);
    }

    Error deleteMessageByIdError(Integer id, int expectedStatus) throws Exception {
        String response = mvc.perform(delete(MessageController.MAPPING + "/" + id))
            .andExpect(status().is(expectedStatus))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();
        return mapper.readValue(response, Error.class);
    }
}
