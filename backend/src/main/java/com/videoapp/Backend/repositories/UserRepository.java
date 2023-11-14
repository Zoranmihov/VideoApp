package com.videoapp.Backend.repositories;

import com.videoapp.Backend.models.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Integer> {

    boolean existsByUsername(String username);

    Optional<ApplicationUser> findByUsername(String username);
}
