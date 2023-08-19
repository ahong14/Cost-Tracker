package com.cost_tracker.cost_consumer_service.cost_consumer_service.services;

import com.cost_tracker.cost_consumer_service.cost_consumer_service.models.Cost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cost_tracker.cost_consumer_service.cost_consumer_service.repositories.CostRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;

@Service
public class CostServiceConsumerImpl implements CostServiceConsumer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private CostRepository costRepository;

    @Autowired
    public void setCostRepository(CostRepository costRepository) {
        this.costRepository = costRepository;
    }

    /**
     * @param newCost, param of new cost being created
     * @return Cost, newly created cost
     */
    public Cost createCost(Cost newCost) {
        logger.info("creating cost: " + newCost.toString());
        // convert date time to unix timestamp
        LocalDate costDate = newCost.getDate();
        newCost.setDate_unix(costDate.toEpochSecond(LocalTime.NOON, ZoneOffset.MIN));
        return costRepository.save(newCost);
    }
}
