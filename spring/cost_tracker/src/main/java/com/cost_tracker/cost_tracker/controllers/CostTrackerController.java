package com.cost_tracker.cost_tracker.controllers;

import com.cost_tracker.cost_tracker.models.BatchCostRequest;
import com.cost_tracker.cost_tracker.models.Cost;
import com.cost_tracker.cost_tracker.models.GetUserCostsRequest;
import com.cost_tracker.cost_tracker.services.CostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/cost")
public class CostTrackerController {
    private final CostService costService;

    private static final Logger logger = LoggerFactory.getLogger(CostTrackerController.class);

    private final int MAX_RESULTS = 50;

    @Autowired
    public CostTrackerController(CostService costService) {
        this.costService = costService;
    }

    /**
     * @param userId,   user id
     * @param title,    title of cost
     * @param fromDate, from date of costs
     * @param toDate,   to date of costs
     * @param sortDir,  sort direction
     * @return response, status code and body
     */
    @GetMapping()
    public ResponseEntity getUserCosts(@RequestParam int userId,
                                       @RequestParam(required = false) String title,
                                       @RequestParam(required = false) Integer fromDate,
                                       @RequestParam(required = false) Integer toDate,
                                       @RequestParam(required = false) String sortDir) {
        try {
            logger.info("building getUserCostsRequest");
            GetUserCostsRequest getUserCostsRequest = new GetUserCostsRequest(userId, 0, MAX_RESULTS, Optional.ofNullable(fromDate), Optional.ofNullable(toDate), Optional.ofNullable(title), Optional.ofNullable(sortDir));
            logger.info(getUserCostsRequest.toString());
            Optional<List<Cost>> userCosts = costService.getUserCosts(getUserCostsRequest);
            return new ResponseEntity<>(userCosts, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception getting costs: " + e.getMessage());
            return new ResponseEntity<>("Exception getting costs: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @param newCost, request body converted to Cost object
     * @return response, status code and body
     */
    @PostMapping
    public ResponseEntity createCost(@RequestBody Cost newCost) {
        try {
            Cost createCostResult = costService.createCost(newCost);
            // create response with cost and message
            Map<String, String> body = new HashMap<>();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            body.put("cost", objectMapper.writeValueAsString(createCostResult));
            body.put("message", "Cost created successfully");
            return new ResponseEntity<>(body, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("Failed to create cost: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    public ResponseEntity deleteCost(@RequestParam int userId, @RequestParam int costId) {
        boolean deleteCostResult = costService.deleteCost(userId, costId);
        if (!deleteCostResult) {
            return new ResponseEntity<>("Failed to delete cost.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Cost deleted successfully", HttpStatus.OK);
    }

    @PostMapping(path = "batchCost")
    public ResponseEntity createBatchCost(@RequestBody BatchCostRequest batchCostRequest) {
        boolean createBatchCostResult = costService.createBatchCost(batchCostRequest);
        if (!createBatchCostResult) {
            return new ResponseEntity<>("Failed to create batch cost.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Create batch cost message queued successfully.", HttpStatus.ACCEPTED);
    }
}
