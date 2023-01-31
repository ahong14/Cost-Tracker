package com.cost_tracker.cost_tracker.repositories;

import com.cost_tracker.cost_tracker.models.Cost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CostRepository extends JpaRepository<Cost, Integer> {
    // get costs by user id ordered by date desc
    @Query(value = "SELECT * FROM cost WHERE user_id = :userId LIMIT :limit OFFSET :offset",nativeQuery = true)
    Optional<List<Cost>> findCostsByUserIdOrderByDateDesc(Integer userId, Integer limit, Integer offset);


    // get costs between dates
    @Query(value = "SELECT * FROM cost WHERE user_id = ?1 AND date_unix BETWEEN ?2 AND ?3 ORDER BY date_unix ASC",nativeQuery = true)
    Optional<List<Cost>> findCostsByUserIdAndDateUnixBetweenOrderByDateUnixAsc(Integer userId, Integer fromDate, Integer endDate);

    // get costs by title
    @Query(value = "SELECT * FROM cost WHERE user_id = :userId AND title LIKE %:title% ORDER BY date_unix ASC", nativeQuery = true)
    Optional<List<Cost>> findCostsByUserIdAndTitleContainingIgnoreCase(Integer userId, String title);

    // get costs by title and date between
    @Query(value = "SELECT * FROM cost WHERE user_id = :userId AND title LIKE %:title% AND date_unix BETWEEN :fromDate AND :toDate ORDER BY date_unix ASC", nativeQuery = true)
    Optional<List<Cost>> findCostsByUserIdAndTitleAndDate(Integer userId, String title, Integer fromDate, Integer toDate);
}
