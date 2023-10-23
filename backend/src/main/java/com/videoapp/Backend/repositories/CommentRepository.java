package com.videoapp.Backend.repositories;

import com.videoapp.Backend.models.Comment;
import com.videoapp.Backend.models.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findByVideo(Video video, Pageable pageable);
    List<Comment> findBycommentedBy(String commentedBy);

}
