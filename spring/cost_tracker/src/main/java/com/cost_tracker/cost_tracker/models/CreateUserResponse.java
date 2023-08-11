package com.cost_tracker.cost_tracker.models;

public class CreateUserResponse {
    private User user;
    private Boolean success;

    public CreateUserResponse(User user, Boolean success) {
        this.user = user;
        this.success = success;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
