package com.cost_tracker.cost_tracker;

import com.cost_tracker.cost_tracker.controllers.UserController;
import com.cost_tracker.cost_tracker.models.User;
import com.cost_tracker.cost_tracker.services.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserServiceImpl userService;

    @Test
    public void getUserTestSuccessful() throws Exception {
        User testUser = new User(1, "firstname", "lastname", "test@mail.com", "test123");
        given(userService.getUser(1)).willReturn(testUser);


        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name").value("firstname"))
                .andExpect(jsonPath("$.last_name").value("lastname"))
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andReturn();
        MockHttpServletResponse mockResponse = result.getResponse();
        String contentType = mockResponse.getContentType();

        assert (contentType == MediaType.APPLICATION_JSON.toString());
    }

    @Test
    public void getUserTestNotFound() throws Exception {
        given(userService.getUser(anyInt())).willThrow(new NoSuchElementException("no such element exception"));
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get("/api/user/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value("404"))
                .andReturn();
        MockHttpServletResponse mockResponse = result.getResponse();
        String contentType = mockResponse.getContentType();

        assert (contentType == MediaType.APPLICATION_JSON.toString());
    }
}
