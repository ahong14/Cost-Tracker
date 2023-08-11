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


import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        User testUser3 = new User(3, "Test", "Test Lastname", "test3@mail.com", "test password");


        when(userRepository.findUserByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(userRepository.findUserByEmail(testUser2.getEmail())).thenReturn(Optional.ofNullable(null));
        when(userRepository.save(any(User.class))).then(returnsFirstArg());
        when(userRepository.findById(testUser3.getId())).thenReturn(Optional.of(testUser3));
    }


    @Test
    public void shouldThrowExceptionWhenCreatingUserFoundEmail() {
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
        assertNotEquals(createdUser, null);
    }

    @Test
    public void createUserSuccess() {
        User testUser3 = new User(3, "Test", "Test Lastname", "test3@mail.com", "test password");
        User savedUser = userService.createUser(testUser3);
        assertNotEquals(savedUser, null);
        assertEquals(savedUser.getEmail(), testUser3.getEmail());
        assertEquals(savedUser.getFirst_name(), testUser3.getFirst_name());
        assertEquals(savedUser.getLast_name(), testUser3.getLast_name());
    }

    @Test
    public void shouldThrowExceptionWhenLoginUserNotFound() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            User testUser2 = new User(2, "Test", "Test Lastname", "test2@mail.com", "test password");
            userService.loginUser(testUser2.getEmail(), testUser2.getPassword());
            verify(userRepository, times(1)).findUserByEmail(testUser2.getEmail());
        });

        assertEquals("User email not found", exception.getMessage());
    }

    @Test
    public void shouldGetFoundUserById() {
        User testUser3 = new User(3, "Test", "Test Lastname", "test3@mail.com", "test password");
        User foundUser = userService.getUser(testUser3.getId());
        verify(userRepository, times(1)).findById(testUser3.getId());
        assertNotEquals(foundUser, null);
        assertEquals(foundUser.getEmail(), testUser3.getEmail());
        assertEquals(foundUser.getFirst_name(), testUser3.getFirst_name());
        assertEquals(foundUser.getLast_name(), testUser3.getLast_name());
    }

    @Test
    public void shouldThrowExceptionWhenUserNotFound() {
        assertThrows(NoSuchElementException.class, () -> {
            User testUser3 = new User(4, "Test4", "Test Lastname", "test4@mail.com", "test password");
            userService.getUser(testUser3.getId());
        });
    }
}
