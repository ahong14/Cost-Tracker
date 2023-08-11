package com.cost_tracker.cost_tracker.controllers;

import com.cost_tracker.cost_tracker.exception.ErrorMessage;
import com.cost_tracker.cost_tracker.models.CreateUserResponse;
import com.cost_tracker.cost_tracker.models.LoginRequest;
import com.cost_tracker.cost_tracker.models.LoginResponse;
import com.cost_tracker.cost_tracker.models.User;
import com.cost_tracker.cost_tracker.services.UserServiceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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


    @Operation(summary = "create user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user created successfully",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserResponse.class))}),
            @ApiResponse(responseCode = "400", description = "create user unsuccessful",
                    content = @Content)
    })
    /**
     * @param newUser, user object containing properties to create new user
     * @return response, status code and body
     */
    @PostMapping(path = "/signup")
    public ResponseEntity createNewUser(@RequestBody User newUser) throws JsonProcessingException {
        // set date created property of new user being created
        newUser.setDateCreated(LocalDate.now());
        User createNewUserResult = userServiceImpl.createUser(newUser);

        // construct response of new user created and message
        CreateUserResponse createUserResponse = new CreateUserResponse(createNewUserResult, true);
        return new ResponseEntity<>(createUserResponse, HttpStatus.OK);
    }

    @Operation(summary = "login user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "login user successful",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))}),
            @ApiResponse(responseCode = "400", description = "login user unsuccessful",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "login user not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))})
    })
    /**
     * @param loginRequest, contains email and password of user logging in
     * @return response, status code and body, header with loginToken
     */
    @PostMapping(path = "/login")
    public ResponseEntity loginUser(@RequestBody LoginRequest loginRequest) {
        Optional<String> loginToken = userServiceImpl.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        String loginTokenCookie = loginToken.get();
        // create JSON body response with login token
        LoginResponse loginResponse = new LoginResponse(loginTokenCookie, true);
        HttpHeaders loginHeaders = new HttpHeaders();

        // add header to login response containing JWT
        loginHeaders.add("loginToken", loginTokenCookie);

        return new ResponseEntity<>(loginResponse, loginHeaders, HttpStatus.OK);
    }
}
