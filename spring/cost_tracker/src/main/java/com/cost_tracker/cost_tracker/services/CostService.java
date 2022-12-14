package com.cost_tracker.cost_tracker.services;

import com.cost_tracker.cost_tracker.models.Cost;
import com.cost_tracker.cost_tracker.repositories.CostRepository;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Service
public class CostService {
    private static final Logger logger = LogManager.getLogger(CostService.class);
    private final CostRepository costRepository;

    @Autowired
    public CostService(CostRepository costRepository) {
        this.costRepository = costRepository;
    }

    public Optional<List<Cost>> getUserCosts(Integer userId, int offset, int limit) {
        try {
            // TODO fix pagination and limit
            int page = offset / limit;
            Optional<List<Cost>> userCosts = costRepository.findCostsByUserIdOrderByDateDesc(userId);
            return userCosts;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public boolean createCost(Cost newCost) {
        try {
            // convert date time to unix timestamp
            LocalDate costDate = newCost.getDate();
            newCost.setDate_unix(costDate.toEpochSecond(LocalTime.NOON, ZoneOffset.MIN));
            costRepository.save(newCost);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}
