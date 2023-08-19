package com.cost_tracker.cost_tracker.kafka;

import com.cost_tracker.cost_tracker.models.Cost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
public class BatchCostMessageProducer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${topic.name.producer}")
    private String topicName;

    @Autowired
    public BatchCostMessageProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(Cost costMessage) throws JsonProcessingException {
        // convert cost to JSON string
        ObjectWriter objectWriter = new ObjectMapper().registerModule(new JavaTimeModule()).writer();
        String costJson = objectWriter.writeValueAsString(costMessage);

        // publish JSON string to kafka topic
        logger.info("Sending kafka message: " + costJson);
        logger.info("topic name: " + topicName);
        kafkaTemplate.send(topicName, costJson);
    }
}
