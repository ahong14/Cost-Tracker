package com.cost_tracker.cost_tracker.repositories;

import com.cost_tracker.cost_tracker.models.Cost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// repository of type Cost
// mapped by id of type Integer
@Repository
public interface CostRepository extends JpaRepository<Cost, Integer> {
    @Query(value = "SELECT * FROM cost WHERE user_id = :userId AND id = :id", nativeQuery = true)
    Optional<Cost> findCostByUserIdAndId(Integer userId, Integer id);

    // get costs by user id ordered by date desc
    @Query(value = "SELECT * FROM cost WHERE user_id = :userId", nativeQuery = true)
    Optional<List<Cost>> findCostsByUserId(Integer userId, Pageable pageable);


    // get costs between dates
    @Query(value = "SELECT * FROM cost WHERE user_id = :userId AND date_unix BETWEEN :fromDate AND :toDate", nativeQuery = true)
    Optional<List<Cost>> findCostsByUserIdAndDateUnix(Integer userId, Integer fromDate, Integer toDate, Pageable pageable);

    // get costs by title
    @Query(value = "SELECT * FROM cost WHERE user_id = :userId AND title LIKE %:title%", nativeQuery = true)
    Optional<List<Cost>> findCostsByUserIdAndTitle(Integer userId, String title, Pageable pageable);

    // get costs by title and date between
    @Query(value = "SELECT * FROM cost WHERE user_id = :userId AND title LIKE %:title% AND date_unix BETWEEN :fromDate AND :toDate", nativeQuery = true)
    Optional<List<Cost>> findCostsByUserIdAndTitleAndDate(Integer userId, String title, Integer fromDate, Integer toDate, Pageable pageable);

    // delete user cost by cost id
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM cost WHERE user_id = :userId AND id = :costId", nativeQuery = true)
    void deleteUserCost(Integer userId, Integer costId);
}
