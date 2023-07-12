package com.cost_tracker.cost_tracker;

import com.cost_tracker.cost_tracker.controllers.CostTrackerController;
import com.cost_tracker.cost_tracker.models.Cost;
import com.cost_tracker.cost_tracker.models.GetUserCostsRequest;
import com.cost_tracker.cost_tracker.services.CSVServiceImpl;
import com.cost_tracker.cost_tracker.services.CostServiceImpl;
import jakarta.servlet.ServletContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
        Cost testCost = new Cost();
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
        assert (contentType == MediaType.APPLICATION_JSON.toString());
    }
}
