package com.cost_tracker.cost_tracker.controllers;

import com.cost_tracker.cost_tracker.models.Cost;
import com.cost_tracker.cost_tracker.services.CostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/cost")
public class CostTrackerController {
    private final CostService costService;

    @Autowired
    public CostTrackerController(CostService costService) {
        this.costService = costService;
    }

    @GetMapping()
    public ResponseEntity getUserCosts(@RequestParam Integer userId) {
        Optional<List<Cost>> userCosts = costService.getUserCosts(userId);
        if (userCosts == null) {
            return new ResponseEntity<>("Failed to get user costs.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(userCosts, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity createCost(@RequestBody Cost newCost) {
        boolean createCostResult = costService.createCost(newCost);
        if (!createCostResult) {
            return new ResponseEntity<>("Failed to create cost.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Cost created successfully", HttpStatus.OK);
    }

}
