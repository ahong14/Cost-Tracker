package com.cost_tracker.cost_tracker.kafka;

import com.cost_tracker.cost_tracker.models.Cost;
import com.cost_tracker.cost_tracker.services.CostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;

@Service
public class BatchCostMessageProducer {
    private static final Logger logger = LogManager.getLogger(BatchCostMessageProducer.class);
    private KafkaTemplate<String, Cost> kafkaTemplate;

    @Value(value = "${topic.name.producer}")
    private String topicName;

    @Autowired
    public BatchCostMessageProducer(KafkaTemplate<String, Cost> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(Cost costMessage) {
        logger.info("Sending kafka message: " + costMessage.toString());
        logger.info("topic name: " + topicName);
        kafkaTemplate.send(topicName, costMessage);
    }
}
