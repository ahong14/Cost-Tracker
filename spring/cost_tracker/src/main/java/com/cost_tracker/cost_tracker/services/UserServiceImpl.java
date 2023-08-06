package com.cost_tracker.cost_tracker.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cost_tracker.cost_tracker.models.User;
import com.cost_tracker.cost_tracker.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;

    @Value("${jwt_secret}")
    private String jwt_secret;

    public UserServiceImpl() {
    }

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    /**
     * @param newUser, object containing user properties
     * @return newly created user
     */
    public User createUser(User newUser) {
        Optional<User> foundUser = userRepository.findUserByEmail(newUser.getEmail());
        if (foundUser.isPresent()) {
            logger.error("Email found for user, unable to create new user.");
            throw new IllegalArgumentException("Email found for user");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return userRepository.save(newUser);
    }

    @Override
    /**
     * @param email,    email of user
     * @param password, password of user
     * @return JWT loginToken
     * @throws IllegalArgumentException
     */
    public Optional<String> loginUser(String email, String password) {
        Optional<User> foundUser = userRepository.findUserByEmail(email);
        logger.info("found user: " + foundUser);
        if (foundUser.isEmpty()) {
            logger.error("User email not found");
            throw new IllegalArgumentException("User email not found");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User getFoundUser = foundUser.get();
        if (!passwordEncoder.matches(password, getFoundUser.getPassword())) {
            logger.error("Passwords do not match");
            throw new IllegalArgumentException("Passwords do not match");
        }

        return Optional.ofNullable(JWT.create().withSubject("User")
                .withClaim("Email", email)
                .withClaim("id", getFoundUser.getId())
                .withClaim("FirstName", getFoundUser.getFirst_name())
                .withClaim("LastName", getFoundUser.getLast_name())
                .withIssuedAt(new Date())
                .withIssuer("Cost Tracker Issuer")
                .sign(Algorithm.HMAC256(jwt_secret)));
    }

    @Override
    /**
     * @param userId, int user id of user being searched for
     * @return User, found user based on user id
     * @throws NoSuchElementException
     */
    public User getUser(int userId) {
        return userRepository.findById(userId).get();
    }
}
