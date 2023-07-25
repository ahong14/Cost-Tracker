package com.cost_tracker.cost_tracker.services;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class CSVServiceImpl implements CSVService {
    private static final Logger logger = LogManager.getLogger(CSVServiceImpl.class);

    public static final String CSV_CONTENT_TYPE = "text/csv";

    /**
     * @param contentType, content type of uploaded file
     * @throws IllegalArgumentException
     */
    public void isCsvFile(String contentType) throws IllegalArgumentException {
        if (!contentType.equals(CSV_CONTENT_TYPE)) {
            logger.error("File is not a csv.");
            throw new IllegalArgumentException("File is not a csv.");
        }
    }

    /**
     * @param inputStream, input stream of csv file uploaded
     * @return Iterable<CSVRecord>, list of csv records parsed
     * @throws IOException
     */
    public Iterable<CSVRecord> parseBatchCostCsv(InputStream inputStream) throws IOException {
        // InputStreamReader, reads bytes and converts to characters
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        // BufferedReader, buffers characters for efficient reading
        BufferedReader fileReader = new BufferedReader(inputStreamReader);
        // CSVParser, pass file reader as param, buffer of characters from csv input stream
        // CSVFormat.DEFAULT, sets delimiter to , quote to ", record separator to \n
        // first record set to header
        CSVFormat csvFormat = CSVFormat.Builder.create().setHeader().setSkipHeaderRecord(true).setIgnoreHeaderCase(true).setTrim(true).build();
        CSVParser csvParser = new CSVParser(fileReader, csvFormat);
        // return list of csv records parsed
        Iterable<CSVRecord> csvRecords = csvParser.getRecords();
        return csvRecords;
    }
}
