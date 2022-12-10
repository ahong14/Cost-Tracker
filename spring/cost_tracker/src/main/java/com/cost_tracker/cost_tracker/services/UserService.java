package com.cost_tracker.cost_tracker.services;

import com.cost_tracker.cost_tracker.models.User;
import com.cost_tracker.cost_tracker.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean createUser(User newUser) {
        try {
            Optional<User> foundUser = userRepository.findUserByEmail(newUser.getEmail());
            if (foundUser.isPresent()) {
                logger.info("Found user: " + foundUser);
                logger.error("Email found for user, unable to create new user.");
                return false;
            }
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            userRepository.save(newUser);
            logger.info("New user created successfully for: " + newUser);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }


//    public Optional<String> loginUser(String email, String password) {
//        Optional<User> foundUser = userRepository.findUserByEmail(email);
//        if (foundUser.isEmpty()) {
//            logger.error("User email not found");
//            return null;
//        }
//
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String hashedPassword = passwordEncoder.encode(password);
//        User getFoundUser = foundUser.get();
//
//        if (getFoundUser.getPassword() != hashedPassword) {
//            logger.error("Passwords do not match");
//            return null;
//        }
//    }
}
