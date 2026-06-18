package com.metrowest.repo;

import com.metrowest.entity.UserEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntry, Long>
{
    Optional<UserEntry> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
