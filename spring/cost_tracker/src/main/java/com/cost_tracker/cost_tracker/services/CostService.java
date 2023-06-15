package com.cost_tracker.cost_tracker.services;

import com.cost_tracker.cost_tracker.models.Cost;
import com.cost_tracker.cost_tracker.models.GetUserCostsRequest;
import org.apache.commons.csv.CSVRecord;

import java.util.List;
import java.util.Optional;

public interface CostService {
    Optional<List<Cost>> getUserCosts(GetUserCostsRequest getUserCostsRequest);

    Cost createCost(Cost newCost);

    void deleteCost(int userId, int costId);

    void publishCostsKafka(Iterable<CSVRecord> csvRecords);
}
