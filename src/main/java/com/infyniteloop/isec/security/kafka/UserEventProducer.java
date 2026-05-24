package com.infyniteloop.isec.security.kafka;


import dtos.event.UserEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class UserEventProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserEventProducer.class);

    private NewTopic topic;

    KafkaTemplate<String, UserEvent> kafkaTemplate;

    public UserEventProducer(NewTopic newTopic, KafkaTemplate<String, UserEvent> kafkaTemplate) {
        this.topic = newTopic;
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendEvent(UserEvent userEvent) {

        LOGGER.info("\n\n User event ******************** {}", userEvent);

        Message<UserEvent> message = MessageBuilder
                .withPayload(userEvent)
                .setHeader(KafkaHeaders.TOPIC, topic.name())
                .build();

        kafkaTemplate.send(message);

    }
}
