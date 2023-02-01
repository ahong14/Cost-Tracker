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

    private final int MAX_RESULTS = 50;

    @Autowired
    public CostTrackerController(CostService costService) {
        this.costService = costService;
    }

    @GetMapping()
    public ResponseEntity getUserCosts(@RequestParam int userId,
                                       @RequestParam(required = false) String title,
                                       @RequestParam(required = false) Integer fromDate,
                                       @RequestParam(required = false) Integer toDate,
                                       @RequestParam(required = false) String sortDir) {
        Optional<List<Cost>> userCosts = costService.getUserCosts(userId, 0, MAX_RESULTS, title, fromDate, toDate, sortDir);
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

    @DeleteMapping
    public ResponseEntity deleteCost(@RequestParam int userId, @RequestParam int costId) {
        boolean deleteCostResult = costService.deleteCost(userId, costId);
        if (!deleteCostResult) {
            return new ResponseEntity<>("Failed to delete cost.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Cost deleted successfully", HttpStatus.OK);
    }

}
