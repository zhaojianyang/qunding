package com.jshx.kafka;

import org.springframework.kafka.core.KafkaTemplate;

/**
 * 生产者线程
 *
 */
public class ProducerThread implements Runnable{

    String topicName;
    private String message;
    KafkaTemplate kafkaTemplate;

    public ProducerThread(String topicName, String message, KafkaTemplate kafkaTemplate) {
        this.topicName = topicName;
        this.message = message;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void run() {
        kafkaTemplate.send(topicName, message);
    }
}
