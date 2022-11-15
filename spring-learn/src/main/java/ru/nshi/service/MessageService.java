package ru.nshi.service;

import ru.nshi.model.Message;

import java.util.List;

public interface MessageService {
    List<Message> getMessages();

    Message getById(Integer id);

    Message save(Message message);

    Message updateById(Integer id, Message message);

    Message deleteById(Integer id);

    Message doHandleMessage(Message message);
}
