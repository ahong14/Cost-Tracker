package com.cost_tracker.cost_tracker.controllers;

import com.cost_tracker.cost_tracker.services.UserServiceImpl;
import com.cost_tracker.cost_tracker.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserServiceImpl userServiceImpl;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping
    public ResponseEntity getUser(@RequestParam int userId) {
        logger.info("Getting user for user id: " + userId);
        User foundUser = this.userServiceImpl.getUser(userId);
        return new ResponseEntity<>(foundUser, HttpStatus.OK);
    }
}
