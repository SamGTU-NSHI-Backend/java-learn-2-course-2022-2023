package ru.nshi.repository;

import org.springframework.stereotype.Repository;
import ru.nshi.error.MessageNotFoundException;
import ru.nshi.model.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class MessageRepositoryImpl implements MessageRepository {
    private final Map<Integer, Message> data = new ConcurrentHashMap<>();
    private final AtomicInteger autoId = new AtomicInteger(1);

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Message getById(Integer id) {
        Message result = data.get(id);
        if (result == null) {
            throw new MessageNotFoundException("message not found");
        }
        return result;
    }

    @Override
    public Message save(Message message) {
        int id = autoId.incrementAndGet();
        message.setId(id);
        data.put(id, message);
        return message;
    }

    @Override
    public Message updateById(Integer id, Message message) {
        Message oldValue = getById(id);
        message.setId(id);
        data.put(id, message);
        return message;
    }

    @Override
    public Message deleteById(Integer id) {
        Message result = data.remove(id);
        if (result == null) {
            throw new MessageNotFoundException("message not found");
        }
        return result;
    }
}
