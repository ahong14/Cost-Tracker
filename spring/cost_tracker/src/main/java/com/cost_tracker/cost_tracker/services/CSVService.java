package com.cost_tracker.cost_tracker.services;

import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;

public interface CSVService {
    void isCsvFile(String contentType);

    Iterable<CSVRecord> parseBatchCostCsv(InputStream inputStream) throws IOException;
}
