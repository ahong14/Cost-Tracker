package com.cost_tracker.cost_tracker.repositories;

import com.cost_tracker.cost_tracker.models.Cost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CostRepository extends JpaRepository<Cost, Integer> {
    // get costs by user id ordered by date desc
    Optional<List<Cost>> findCostsByUserIdOrderByDateDesc(Integer userId);
}
