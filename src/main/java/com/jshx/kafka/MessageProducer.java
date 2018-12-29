package com.jshx.kafka;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Kafka 消息生产者
 *
 */
@Component
public class MessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;
    private final Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeTypeAdapter()).create();
    private ExecutorService executor;

    @Value("${spring.kafka.producer.threadNum}")
    private Integer threadNum;

    @PostConstruct
    public void init(){
        executor = Executors.newFixedThreadPool(threadNum);
    }

    /**
     * 向指定的队列发送消息
     *
     * @param topic    队列
     * @param message  消息
     */
    public void sendMessage(String topic, Object message){
        String msg = gson.toJson(message);
        logger.debug("Sending {} to {}", msg, topic);
        ProducerThread producerThread = new ProducerThread(topic, msg, kafkaTemplate);
        executor.submit(producerThread);
    }

    /**
     * 向指定的队列发送csv字符串消息
     *
     * @param topic
     * @param message
     */
    public void sendDelimiterMessage(String topic, String message){
        logger.debug("Sending {} to {}", message, topic);
        ProducerThread producerThread = new ProducerThread(topic, message, kafkaTemplate);
        executor.submit(producerThread);
    }
}
