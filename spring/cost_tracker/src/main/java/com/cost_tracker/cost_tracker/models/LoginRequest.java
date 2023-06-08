package com.cost_tracker.cost_tracker.models;



public class LoginRequest implements LoginRequestInterface {
    private String email;
    private String password;

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }
}
