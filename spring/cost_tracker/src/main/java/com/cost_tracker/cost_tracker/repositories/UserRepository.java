package com.cost_tracker.cost_tracker.repositories;

import com.cost_tracker.cost_tracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// repository for User type
// mapped by id of type Integer
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByEmail(String email);
}
