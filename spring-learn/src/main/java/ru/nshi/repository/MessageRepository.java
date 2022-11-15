package ru.nshi.repository;

import ru.nshi.model.Message;

import java.util.List;

public interface MessageRepository {
    List<Message> findAll();

    Message getById(Integer id);

    Message save(Message message);

    Message updateById(Integer id, Message message);

    Message deleteById(Integer id);
}
