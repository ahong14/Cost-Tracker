package com.cost_tracker.cost_tracker.controllers;

import com.cost_tracker.cost_tracker.models.LoginRequest;
import com.cost_tracker.cost_tracker.models.User;
import com.cost_tracker.cost_tracker.services.UserServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {
    private final UserServiceImpl userServiceImpl;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @Autowired
    public AuthController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }


    /**
     * @param newUser, user object containing properties to create new user
     * @return response, status code and body
     */
    @PostMapping(path = "/signup")
    public ResponseEntity createNewUser(@RequestBody User newUser) {
        try {
            // set date created property of new user being created
            newUser.setDateCreated(LocalDate.now());
            User createNewUserResult = userServiceImpl.createUser(newUser);

            // construct response of new user created and message
            Map<String, String> body = new HashMap<>();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            body.put("user", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(createNewUserResult));
            body.put("message", "User created successfully");
            return new ResponseEntity<>(body, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("Failed to create new user: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @param loginRequest, contains email and password of user logging in
     * @return response, status code and body, header with loginToken
     */
    @PostMapping(path = "/login")
    public ResponseEntity loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            Optional<String> loginToken = userServiceImpl.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
            Map<String, String> body = new HashMap<>();
            String loginTokenCookie = loginToken.get();
            // create JSON body response with login token
            body.put("loginToken", loginTokenCookie);
            body.put("message", "Login successful");
            HttpHeaders loginHeaders = new HttpHeaders();
            loginHeaders.add("loginToken", loginTokenCookie);

            return new ResponseEntity<>(body, loginHeaders, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error logging in: " + e.getMessage());
            return new ResponseEntity<>("Error logging in: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
