package com.cost_tracker.cost_tracker;

import com.cost_tracker.cost_tracker.models.User;
import com.cost_tracker.cost_tracker.repositories.UserRepository;
import com.cost_tracker.cost_tracker.services.UserService;
import com.cost_tracker.cost_tracker.services.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import java.util.Date;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserServiceImplIntegrationTest {

    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {
        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
    }


    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Before
    public void setUp() {
        User testUser = new User(1, "Test", "Test Lastname", "test@mail.com", "test password");
        User testUser2 = new User(2, "Test", "Test Lastname", "test2@mail.com", "test password");
        User testUser3 = new User(2, "Test", "Test Lastname", "test3@mail.com", "test password");


        when(userRepository.findUserByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(userRepository.findUserByEmail(testUser2.getEmail())).thenReturn(Optional.ofNullable(null));
        when(userRepository.save(any(User.class))).then(returnsFirstArg());
    }


    @Test
    public void creatingUserFoundEmailExceptionThrown() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            User testUser = new User(1, "Test", "Test Lastname", "test@mail.com", "test password");
            userService.createUser(testUser);
        });
        assertEquals("Email found for user", exception.getMessage());
    }

    @Test
    public void creatingUserNotFoundEmailCreated() {
        User testUser2 = new User(2, "Test", "Test Lastname", "test2@mail.com", "test password");
        User createdUser = userService.createUser(testUser2);
        verify(userRepository, times(1)).findUserByEmail(testUser2.getEmail());
        verify(userRepository, times(1)).save(testUser2);
        assert (createdUser != null);
    }

    @Test
    public void createUserSuccess() {
        User testUser3 = new User(2, "Test", "Test Lastname", "test3@mail.com", "test password");
        User savedUser = userService.createUser(testUser3);
        assert (savedUser != null);
        assert (savedUser.getEmail() == testUser3.getEmail());
        assert (savedUser.getFirst_name().equals(testUser3.getFirst_name()));
        assert (savedUser.getLast_name().equals(testUser3.getLast_name()));
    }

    @Test
    public void loginUserNotFoundExceptionThrown() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            User testUser2 = new User(2, "Test", "Test Lastname", "test2@mail.com", "test password");
            userService.loginUser(testUser2.getEmail(), testUser2.getPassword());
            verify(userRepository, times(1)).findUserByEmail(testUser2.getEmail());
        });

        assertEquals("User email not found", exception.getMessage());
    }
}
