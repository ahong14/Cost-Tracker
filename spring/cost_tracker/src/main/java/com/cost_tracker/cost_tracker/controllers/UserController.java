package com.cost_tracker.cost_tracker.controllers;

import com.cost_tracker.cost_tracker.exception.ErrorMessage;
import com.cost_tracker.cost_tracker.services.UserServiceImpl;
import com.cost_tracker.cost_tracker.models.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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


    @Operation(summary = "get user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "got user successfully",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "get user unsuccessful",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "user not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))})
    })

    /**
     * userId, user id of user
     *
     * @return response, corresponding user found
     */
    @GetMapping
    public ResponseEntity getUser(@RequestParam int userId) {
        logger.info("Getting user for user id: " + userId);
        User foundUser = this.userServiceImpl.getUser(userId);
        return new ResponseEntity<>(foundUser, HttpStatus.OK);
    }
}
