package com.cost_tracker.cost_tracker.repositories;

import com.cost_tracker.cost_tracker.models.Cost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CostRepository extends JpaRepository<Cost, Integer> {
    // get costs by user id ordered by date desc
    @Query(value = "SELECT * FROM cost WHERE user_id = :userId",nativeQuery = true)
    Optional<List<Cost>> findCostsByUserId(Integer userId, Pageable pageable);


    // get costs between dates
    @Query(value = "SELECT * FROM cost WHERE user_id = :userId AND date_unix BETWEEN :fromDate AND :toDate",nativeQuery = true)
    Optional<List<Cost>> findCostsByUserIdAndDateUnix(Integer userId, Integer fromDate, Integer toDate, Pageable pageable);

    // get costs by title
    @Query(value = "SELECT * FROM cost WHERE user_id = :userId AND title LIKE %:title%", nativeQuery = true)
    Optional<List<Cost>> findCostsByUserIdAndTitle(Integer userId, String title, Pageable pageable);

    // get costs by title and date between
    @Query(value = "SELECT * FROM cost WHERE user_id = :userId AND title LIKE %:title% AND date_unix BETWEEN :fromDate AND :toDate", nativeQuery = true)
    Optional<List<Cost>> findCostsByUserIdAndTitleAndDate(Integer userId, String title, Integer fromDate, Integer toDate, Pageable pageable);
}
