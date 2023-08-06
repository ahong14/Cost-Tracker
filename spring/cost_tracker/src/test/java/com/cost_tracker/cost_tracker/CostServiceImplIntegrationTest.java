package com.cost_tracker.cost_tracker;

import com.cost_tracker.cost_tracker.kafka.BatchCostMessageProducer;
import com.cost_tracker.cost_tracker.models.Cost;
import com.cost_tracker.cost_tracker.repositories.CostRepository;
import com.cost_tracker.cost_tracker.services.CostService;
import com.cost_tracker.cost_tracker.services.CostServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class CostServiceImplIntegrationTest {

    @TestConfiguration
    static class CostServiceImplTestContextConfiguration {
        @Bean
        public CostService costService() {
            return new CostServiceImpl();
        }
    }

    @Autowired
    private CostService costService;

    @MockBean
    private CostRepository costRepository;

    @MockBean
    private BatchCostMessageProducer batchCostMessageProducer;

    @Before
    public void setUp() {
        when(costRepository.save(ArgumentMatchers.any(Cost.class))).then(returnsFirstArg());
    }

    @Test
    public void createCostSuccess() {
        LocalDate localDate = LocalDate.now();
        Cost testCost = new Cost(1, 10.00, localDate, 0, "test cost", 1, 1);
        Cost createdCost = costService.createCost(testCost);
        verify(costRepository, times(1)).save(createdCost);
        assertNotEquals(createdCost, null);
        assertEquals(createdCost.getId(), testCost.getId());
        assertEquals(createdCost.getTitle(), testCost.getTitle());
        assertEquals(createdCost.getAmount(), testCost.getAmount());
        assertEquals(createdCost.getQuantity(), testCost.getQuantity());
        assertEquals(createdCost.getUser_id(), testCost.getUser_id());
        assertNotEquals(testCost.getDate(), null);
    }
}
