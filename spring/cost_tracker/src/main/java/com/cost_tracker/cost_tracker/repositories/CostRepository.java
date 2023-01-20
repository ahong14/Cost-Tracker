package com.cost_tracker.cost_tracker.repositories;

import com.cost_tracker.cost_tracker.models.Cost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CostRepository extends JpaRepository<Cost, Integer> {
    // get costs by user id ordered by date desc
    Optional<List<Cost>> findCostsByUserIdOrderByDateDesc(Integer userId);


    // get costs between dates
    @Query(value = "SELECT * FROM cost WHERE user_id = ?1 AND date_unix BETWEEN ?2 AND ?3 ORDER BY date_unix ASC",nativeQuery = true)
    Optional<List<Cost>> findCostsByUserIdAndDateUnixBetweenOrderByDateUnixAsc(Integer userId, Integer fromDate, Integer endDate);

    // get costs by title
    Optional<List<Cost>> findCostsByUserIdAndTitleContainingIgnoreCase(Integer userId, String title);


    // TODO
    // get costs by title and date between
//    Optional<List<Cost>> findCostsByUserIdAndTitleContainingIgnoreCaseAndDate_unixGreaterThanAndDate_unixLessThan(Integer userId, String title, Integer fromDate, Integer toDate);
}
