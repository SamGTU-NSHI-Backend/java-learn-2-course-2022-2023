package ru.nshi.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.nshi.error.MessageNotFoundException;
import ru.nshi.model.Message;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Repository
public class MessageRepositoryImpl implements MessageRepository {
    private final Map<Integer, Message> data;
    private final AtomicInteger autoId;

    @Value("${application.message.default-value:def.msg}")
    private String defaultMessage;

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
    public Message save(@NonNull Message message) {
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

    @PostConstruct
    public void setup() {
//        int id = autoId.incrementAndGet();
//        data.put(id, new Message(id, defaultMessage));
        System.out.println("Repository created");
    }

    @PreDestroy
    public void clean() {
        data.clear();
        System.out.println("Repository clean");
    }
}
