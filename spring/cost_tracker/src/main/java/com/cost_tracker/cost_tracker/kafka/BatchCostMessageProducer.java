package com.cost_tracker.cost_tracker.kafka;

import com.cost_tracker.cost_tracker.models.BatchCostRequest;
import com.cost_tracker.cost_tracker.services.CostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;

@Service
public class BatchCostMessageProducer {
    private static final Logger logger = LogManager.getLogger(CostService.class);
    private KafkaTemplate<String, BatchCostRequest> kafkaTemplate;

    @Value(value = "${topic.name.producer}")
    private String topicName;

    @Autowired
    public BatchCostMessageProducer(KafkaTemplate<String, BatchCostRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(BatchCostRequest message) {
        logger.info("Sending kafka message: " + message.toString());
        logger.info("topic name: " + topicName);
        kafkaTemplate.send(topicName, message);
    }
}
