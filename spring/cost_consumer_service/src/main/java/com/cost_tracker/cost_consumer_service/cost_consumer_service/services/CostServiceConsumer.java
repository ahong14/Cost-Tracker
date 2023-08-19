package com.cost_tracker.cost_consumer_service.cost_consumer_service.services;

import com.cost_tracker.cost_consumer_service.cost_consumer_service.models.Cost;

public interface CostServiceConsumer {
    Cost createCost(Cost newCost);
}
