package com.videoapp.Backend.repositories;

import com.videoapp.Backend.models.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {
     List<Video> findTop20ByOrderByTimestampDesc();
     Page<Video> findByTitleContainingIgnoreCase(String searchTerm, Pageable pageable);

     Page<Video> findByUploadedBy(String uploadedBy, Pageable pageable);

     List<Video> findByUploadedBy(String uploadedBy);
}
