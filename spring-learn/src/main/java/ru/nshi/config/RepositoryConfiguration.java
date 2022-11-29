package ru.nshi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.nshi.model.Message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class RepositoryConfiguration {
    @Bean
    AtomicInteger atomicIntegerAutoId(@Value("#{ ${application.auto-id.start:0} / 10}")
                                      Integer startId) {
        return new AtomicInteger(startId);
    }

    @Bean
    Map<Integer, Message> messageMap() {
        return new ConcurrentHashMap<>();
    }
}
