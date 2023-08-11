package com.cost_tracker.cost_tracker;

import com.cost_tracker.cost_tracker.controllers.CostTrackerController;
import com.cost_tracker.cost_tracker.models.Cost;
import com.cost_tracker.cost_tracker.models.GetUserCostsRequest;
import com.cost_tracker.cost_tracker.services.CSVServiceImpl;
import com.cost_tracker.cost_tracker.services.CostServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CostTrackerController.class)
public class CostTrackerControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CostServiceImpl costService;

    @MockBean
    private CSVServiceImpl csvService;


    @Test
    public void getUserCosts() throws Exception {
        LocalDate localDate = LocalDate.now();
        Cost testCost = new Cost(1, 10.00, localDate, 0, "test cost", 1, 1);
        List<Cost> costs = Arrays.asList(testCost);
        given(costService.getUserCosts(any(GetUserCostsRequest.class))).willReturn(Optional.ofNullable(costs));

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get("/api/cost?userId=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andReturn();

        MockHttpServletResponse mockResponse = result.getResponse();
        String contentType = mockResponse.getContentType();
        assertEquals(contentType, MediaType.APPLICATION_JSON.toString());
    }

    @Test
    public void createUserCost() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        LocalDate localDate = LocalDate.now();
        Map<String, Object> body = new HashMap<>();
        body.put("amount", "10.00");
        body.put("quantity", "1");
        body.put("title", "test cost");
        body.put("date", localDate.toString());
        body.put("user_id", 1);


        Cost testCost = new Cost(1, 10.00, localDate, 0, "test cost", 1, 1);
        given(costService.createCost(any(Cost.class))).willReturn(testCost);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post("/api/cost")
                        .content(objectMapper.writeValueAsString(body))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse mockResponse = result.getResponse();
        String contentType = mockResponse.getContentType();
        assert (contentType == MediaType.APPLICATION_JSON.toString());
    }

    @Test
    public void deleteUserCost() throws Exception {
        doNothing().when(costService).deleteCost(anyInt(), anyInt());
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .delete("/api/cost?userId=1&costId=1")
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse mockResponse = result.getResponse();
        assert (mockResponse.getContentType().equals("text/plain;charset=UTF-8"));
        assert (mockResponse.getContentAsString().equals("Cost deleted successfully"));
    }

    @Test
    public void deleteUserCostNotFound() throws Exception {
        doThrow(new NoSuchElementException()).when(costService).deleteCost(anyInt(), anyInt());
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .delete("/api/cost?userId=1&costId=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value("404"))
                .andReturn();

        MockHttpServletResponse mockResponse = result.getResponse();
        String contentType = mockResponse.getContentType();

        assert (contentType == MediaType.APPLICATION_JSON.toString());
    }
}
