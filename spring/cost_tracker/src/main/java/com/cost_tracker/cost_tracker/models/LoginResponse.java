package com.cost_tracker.cost_tracker.models;

public class LoginResponse {
    private String loginToken;

    private Boolean success;

    public LoginResponse(String loginToken, Boolean success) {
        this.loginToken = loginToken;
        this.success = success;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
