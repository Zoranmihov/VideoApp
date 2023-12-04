package com.videoapp.Backend.repositories;

import com.videoapp.Backend.models.ApplicationUser;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Integer> {

    boolean existsByUsername(String username);

    @Query("SELECT u FROM ApplicationUser u WHERE " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))")
    Page<ApplicationUser> findByUsernameContainingIgnoreCaseCustom(String username, Pageable pageable);

    Optional<ApplicationUser> findByUsername(String username);
}
