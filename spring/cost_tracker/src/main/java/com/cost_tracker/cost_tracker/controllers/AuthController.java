package com.cost_tracker.cost_tracker.controllers;

import com.cost_tracker.cost_tracker.models.LoginRequest;
import com.cost_tracker.cost_tracker.models.User;
import com.cost_tracker.cost_tracker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/signup")
    public ResponseEntity createNewUser(@RequestBody User newUser) {
        newUser.setDateCreated(LocalDate.now());
        boolean createNewUserResult = userService.createUser(newUser);
        if (!createNewUserResult) {
            return new ResponseEntity<>("Failed to create new user", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Successfully created new user", HttpStatus.OK);
    }

    @PostMapping(path = "/login")
    public ResponseEntity loginUser(@RequestBody User loginRequest) {
        Optional<String> loginToken = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        if (loginToken == null || !loginToken.isPresent()) {
            return new ResponseEntity<>("Failed to login", HttpStatus.BAD_REQUEST);
        }

        String loginTokenCookie = loginToken.get();
        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.add("Set-Cookie", "loginToken=" + loginTokenCookie + " Path=/; Secure: HttpOnly");
        return new ResponseEntity<>("Login successful", loginHeaders, HttpStatus.OK);
    }
}
