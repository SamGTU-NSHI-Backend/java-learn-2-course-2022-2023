package ru.nshi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.nshi.error.MessageException;
import ru.nshi.error.MessageNotFoundException;
import ru.nshi.error.MessageValidationException;
import ru.nshi.model.Error;
import ru.nshi.model.Message;
import ru.nshi.service.MessageService;

import java.util.List;

@RestController
public class MessageControllerImpl implements MessageController {
    @Autowired
    private MessageService service;

    @Override
    public List<Message> getMessages() {
        return service.getMessages();
    }

    @Override
    public Message getMessageById(Integer id) {
        checkId(id);
        return service.getById(id);
    }

    @Override
    public Message createMessage(Message message) {
        checkMessage(message);
        return service.save(message);
    }

    @Override
    public Message updateMessage(Integer id, Message message) {
        checkId(id);
        checkMessage(message);
        return service.updateById(id, message);
    }

    @Override
    public Message deleteById(Integer id) {
        checkId(id);
        return service.deleteById(id);
    }

    @ExceptionHandler(MessageValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleValidationException(MessageValidationException ex) {
        return new Error(ex.getMessage());
    }

    @ExceptionHandler(MessageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleNotFoundException(MessageNotFoundException ex) {
        return new Error(ex.getMessage());
    }

    @ExceptionHandler(MessageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleNotFoundException(MessageException ex) {
        return new Error(ex.getMessage());
    }

    void checkId(Integer id) {
        if (id == null) {
            throw new MessageValidationException("message id cannot by null");
        }
        if (id < 1) {
            throw new MessageValidationException("message id cannot be less than 1");
        }
    }

    void checkMessage(Message message) {
        if (message == null || message.getValue() == null) {
            throw new MessageValidationException("message or message value cannot be null");
        }
        String strip = message.getValue().strip();
        if (strip.isEmpty()) {
            throw new MessageValidationException("message cannot be empty");
        }
        message.setValue(strip);
    }
}
