package com.cost_tracker.cost_tracker.services;

import com.cost_tracker.cost_tracker.models.User;

import java.util.Optional;

public interface UserService {
    User createUser(User newUser);

    User getUser(int userId);

    Optional<String> loginUser(String email, String password);
}
