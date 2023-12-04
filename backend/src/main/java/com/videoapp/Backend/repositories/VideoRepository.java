package com.videoapp.Backend.repositories;

import com.videoapp.Backend.models.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {
     List<Video> findTop20ByOrderByTimestampDesc();

     Page<Video> findByUploadedBy(String uploadedBy, Pageable pageable);

     List<Video> findByUploadedBy(String uploadedBy);

     // Custom method to search in both title and description
     @Query("SELECT v FROM Video v WHERE " +
             "LOWER(v.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
             "LOWER(v.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
     Page<Video> findByTitleOrDescriptionContainingIgnoreCase(String searchTerm, Pageable pageable);
}
