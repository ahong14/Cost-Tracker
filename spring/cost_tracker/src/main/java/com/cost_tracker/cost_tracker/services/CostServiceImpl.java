package com.cost_tracker.cost_tracker.services;

import com.cost_tracker.cost_tracker.kafka.BatchCostMessageProducer;
import com.cost_tracker.cost_tracker.models.Cost;
import com.cost_tracker.cost_tracker.models.GetUserCostsRequest;
import com.cost_tracker.cost_tracker.repositories.CostRepository;

import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CostServiceImpl implements CostService {
    private static final Logger logger = LogManager.getLogger(CostServiceImpl.class);
    private CostRepository costRepository;
    private BatchCostMessageProducer batchCostMessageProducer;

    private static final String SORT_PROPERTY = "date_unix";

    @Autowired
    public void setCostRepository(CostRepository repository) {
        this.costRepository = repository;
    }

    @Autowired
    public void setBatchCostMessageProducer(BatchCostMessageProducer producer) {
        this.batchCostMessageProducer = producer;
    }

    /**
     * @param getUserCostsRequest, object containing params to get user requests
     * @return list of costs or null, Optional<List < Cost>>
     */
    public Optional<List<Cost>> getUserCosts(GetUserCostsRequest getUserCostsRequest) {
        // return list of user costs or null
        Optional<List<Cost>> userCosts;

        // extract values from getUserCostsRequest
        int userId = getUserCostsRequest.getUserId();
        Optional<String> title = getUserCostsRequest.getTitle();
        Optional<String> sortDir = getUserCostsRequest.getSortDir();
        Optional<Integer> fromDate = getUserCostsRequest.getFromDate();
        Optional<Integer> toDate = getUserCostsRequest.getToDate();


        // setup sort direction based on sortDir param
        Sort.Direction sortDirection = Sort.Direction.ASC;
        if (sortDir.isPresent() && sortDir.get().trim().equals("desc")) {
            sortDirection = Sort.Direction.DESC;
        }
        // create PageRequest
        // params: offset, limit, sort direction, sort property
        PageRequest pageRequest = PageRequest.of(getUserCostsRequest.getOffset(), getUserCostsRequest.getLimit(), sortDirection, SORT_PROPERTY);

        // TODO fix pagination and limit
        if (title.isEmpty() && fromDate.isPresent() && toDate.isPresent()) {
            // find costs between from/to dates
            logger.info("finding costs by user id, from/to date ***");
            userCosts = costRepository.findCostsByUserIdAndDateUnix(getUserCostsRequest.getUserId(), getUserCostsRequest.getFromDate().get(), getUserCostsRequest.getToDate().get(), pageRequest);
        } else if (title.isPresent() && fromDate.isEmpty() && toDate.isEmpty()) {
            // find costs by title and user id
            logger.info("finding costs by user id, title ***");
            userCosts = costRepository.findCostsByUserIdAndTitle(userId, title.get(), pageRequest);
        } else if (title.isPresent() && fromDate.isPresent() && toDate.isPresent()) {
            // find costs by title, from date, and end date
            logger.info("finding costs by user id, title, from/to date ***");
            userCosts = costRepository.findCostsByUserIdAndTitleAndDate(userId, title.get(), fromDate.get(), toDate.get(), pageRequest);
        } else {
            logger.info("finding costs by user id ***");
            userCosts = costRepository.findCostsByUserId(userId, pageRequest);
        }

        return userCosts;
    }

    /**
     * @param newCost, param of new cost being created
     * @return Cost, newly created cost
     */
    public Cost createCost(Cost newCost) {
        // convert date time to unix timestamp
        LocalDate costDate = newCost.getDate();
        newCost.setDate_unix(costDate.toEpochSecond(LocalTime.NOON, ZoneOffset.MIN));
        return costRepository.save(newCost);
    }

    /**
     * @param userId, user id
     * @param costId, id of cost being deleted
     */
    public void deleteCost(int userId, int costId) {
        Optional<Cost> foundUserCost = costRepository.findCostByUserIdAndId(userId, costId);
        if (foundUserCost.isEmpty()) {
            logger.error("Cost not found for user: " + userId);
            throw new NoSuchElementException("Cost not found for user");
        }
        costRepository.deleteUserCost(userId, costId);
    }

    /**
     * @param csvRecords, collection of csv records to be published to kafka topic
     */
    public void publishCostsKafka(Iterable<CSVRecord> csvRecords) {
        // iterate through csv records
        for (CSVRecord csvRecord : csvRecords) {
            // extract values from csv record, convert to corresponding data types for Cost
            Double csvCostAmount = Double.parseDouble(csvRecord.get("amount"));
            LocalDate csvCostDate = LocalDate.parse(csvRecord.get("date"));
            long unixTimestamp = csvCostDate.toEpochSecond(LocalTime.NOON, ZoneOffset.MIN);
            String csvCostTitle = csvRecord.get("title");
            Integer csvCostQuantity = Integer.parseInt(csvRecord.get("quantity"));
            Integer csvCostUserId = Integer.parseInt(csvRecord.get("userId"));

            // create Cost object from extracted values
            Cost newCost = new Cost(
                    csvCostAmount,
                    csvCostDate,
                    unixTimestamp,
                    csvCostTitle,
                    csvCostQuantity,
                    csvCostUserId
            );

            // publish Cost record to kafka topic
            // TODO create service that will read from kafka topics to process messages
            this.batchCostMessageProducer.sendMessage(newCost);
        }
    }
}
