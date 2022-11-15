package ru.nshi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nshi.model.Message;
import ru.nshi.repository.MessageRepository;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository repository;

    @Override
    public List<Message> getMessages() {
        return repository.findAll();
    }

    @Override
    public Message getById(Integer id) {
        return repository.getById(id);
    }

    @Override
    public Message save(Message message) {
        return repository.save(message);
    }

    @Override
    public Message updateById(Integer id, Message message) {
        return repository.updateById(id, message);
    }

    @Override
    public Message deleteById(Integer id) {
        return repository.deleteById(id);
    }

    @Override
    public Message doHandleMessage(Message message) {
        message.setValue(message.getValue().toUpperCase());
        return message;
    }
}
