package com.cost_tracker.cost_tracker.models;

import java.util.Optional;

public class GetUserCostsRequest {
    private int userId;

    private int offset;

    private int limit;

    private Optional<Integer> fromDate;

    private Optional<Integer> toDate;

    private Optional<String> title;

    private Optional<String> sortDir;

    public GetUserCostsRequest(int userId, int offset, int limit, Optional<Integer> fromDate, Optional<Integer> toDate, Optional<String> title, Optional<String> sortDir) {
        this.userId = userId;
        this.offset = offset;
        this.limit = limit;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.title = title;
        this.sortDir = sortDir;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Optional<Integer> getFromDate() {
        return fromDate;
    }

    public void setFromDate(Optional<Integer> fromDate) {
        this.fromDate = fromDate;
    }

    public Optional<Integer> getToDate() {
        return toDate;
    }

    public void setToDate(Optional<Integer> toDate) {
        this.toDate = toDate;
    }

    public Optional<String> getTitle() {
        return title;
    }

    public void setTitle(Optional<String> title) {
        this.title = title;
    }

    public Optional<String> getSortDir() {
        return sortDir;
    }

    public void setSortDir(Optional<String> sortDir) {
        this.sortDir = sortDir;
    }
}
