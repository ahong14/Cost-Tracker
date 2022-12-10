package com.cost_tracker.cost_tracker.repositories;

import com.cost_tracker.cost_tracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
