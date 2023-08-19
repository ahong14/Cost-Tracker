package com.cost_tracker.cost_consumer_service.cost_consumer_service.repositories;

import com.cost_tracker.cost_consumer_service.cost_consumer_service.models.Cost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CostRepository extends JpaRepository<Cost, Integer> {
}
