package com.cost_tracker.cost_tracker;

import com.cost_tracker.cost_tracker.models.User;
import com.cost_tracker.cost_tracker.repositories.UserRepository;
import com.cost_tracker.cost_tracker.services.UserService;
import com.cost_tracker.cost_tracker.services.UserServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setUp() {
        User testUser = new User(1, "Test", "Test Lastname", "test@mail.com", "test password");
        User testUser2 = new User(2, "Test", "Test Lastname", "test2@mail.com", "test password");

        UserRepository mockUserRepository = mock(UserRepository.class);
        when(mockUserRepository.findUserByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(mockUserRepository.findUserByEmail(testUser2.getEmail())).thenReturn(Optional.ofNullable(null));
        when(mockUserRepository.save(any(User.class))).thenReturn(new User());
    }


    @Test
    public void creatingUserFoundEmailExceptionThrown() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Email found for user");
        User testUser = new User(1, "Test", "Test Lastname", "test@mail.com", "test password");
        userService.createUser(testUser);
    }

//    @Test
//    public void creatingUserNotFoundEmailCreated() {
//        User testUser2 = new User(2, "Test", "Test Lastname", "test2@mail.com", "test password");
//        User createdUser = userService.createUser(testUser2);
//        assert (createdUser != null);
//    }
}
