package com.cost_tracker.cost_tracker.models;

public class BatchCostRequest implements BatchCostRequestInterface {
    private String fileName;
    public BatchCostRequest() {}

    public BatchCostRequest(String fileName) {
        this.fileName = fileName;
    }
    public String getFileName() {
        return this.fileName;
    }
}
