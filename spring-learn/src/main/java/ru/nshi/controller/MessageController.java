package ru.nshi.controller;

import org.springframework.web.bind.annotation.*;
import ru.nshi.model.Message;

import java.util.List;

@RequestMapping(MessageController.MAPPING)
public interface MessageController {
    String MAPPING = "/message";

    @GetMapping
    List<Message> getMessages();

    @GetMapping("/{id}")
    Message getMessageById(@PathVariable Integer id);

    @PostMapping
    Message createMessage(@RequestBody Message message);

    @PutMapping("/{id}")
    Message updateMessage(@PathVariable Integer id, @RequestBody Message message);

    @DeleteMapping("/{id}")
    Message deleteById(@PathVariable Integer id);
}
