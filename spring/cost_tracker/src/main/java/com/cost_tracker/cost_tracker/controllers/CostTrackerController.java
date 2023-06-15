package com.cost_tracker.cost_tracker.controllers;

import com.cost_tracker.cost_tracker.models.Cost;
import com.cost_tracker.cost_tracker.models.GetUserCostsRequest;
import com.cost_tracker.cost_tracker.services.CSVServiceImpl;
import com.cost_tracker.cost_tracker.services.CostServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/cost")
public class CostTrackerController {

    private static final Logger logger = LoggerFactory.getLogger(CostTrackerController.class);

    private final CostServiceImpl costServiceImpl;

    private final CSVServiceImpl csvServiceImpl;

    private final int MAX_RESULTS = 50;

    @Autowired
    public CostTrackerController(CostServiceImpl costServiceImpl, CSVServiceImpl csvServiceImpl) {
        this.costServiceImpl = costServiceImpl;
        this.csvServiceImpl = csvServiceImpl;
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
            Optional<List<Cost>> userCosts = costServiceImpl.getUserCosts(getUserCostsRequest);
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
            Cost createCostResult = costServiceImpl.createCost(newCost);
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

    /**
     * @param userId, user id
     * @param costId, cost id of cost being deleted
     * @return response, status code and body
     */
    @DeleteMapping
    public ResponseEntity deleteCost(@RequestParam int userId, @RequestParam int costId) {
        try {
            costServiceImpl.deleteCost(userId, costId);
            return new ResponseEntity<>("Cost deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("Failed to delete cost: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * @param file, MultipartFile, batch csv file of costs
     * @return
     */
    @PostMapping(path = "batchCost")
    public ResponseEntity createBatchCost(@RequestParam MultipartFile file) {
        try {
            logger.info("file: " + file.getOriginalFilename());
            logger.info("file size: " + file.getSize());
            logger.info("file content type: " + file.getContentType());

            // check if file uploaded is a csv
            csvServiceImpl.isCsvFile(file.getContentType());

            // get input stream of file, obtains input bytes from a file
            InputStream inputStream = file.getInputStream();

            // parse and get csv records from input stream
            // Iterable, interface represents collection that can be iterated with for loop
            Iterable<CSVRecord> csvRecords = csvServiceImpl.parseBatchCostCsv(inputStream);

            // publish batch records to kafka topic
            costServiceImpl.publishCostsKafka(csvRecords);
            return new ResponseEntity<>("Batch costs processed successfully.", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("Failed to process batch csv: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
