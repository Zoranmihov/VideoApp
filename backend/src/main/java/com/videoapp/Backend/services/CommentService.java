package com.videoapp.Backend.services;

import com.videoapp.Backend.dto.DeleteCommentDTO;
import com.videoapp.Backend.dto.EditCommentDTO;
import com.videoapp.Backend.dto.LeaveCommentDTO;
import com.videoapp.Backend.models.Comment;
import com.videoapp.Backend.models.Video;
import com.videoapp.Backend.repositories.CommentRepository;
import com.videoapp.Backend.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private VideoRepository videoRepository;

    public ResponseEntity<String> leaveComment(LeaveCommentDTO leaveCommentDTO){
        Optional<Video> optionalVideo = videoRepository.findById(leaveCommentDTO.getVideoId());
        if (optionalVideo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Video not found");
        }

        Video video = optionalVideo.get();

        Comment comment = new Comment(video, leaveCommentDTO.getContent(), leaveCommentDTO.getCommentedBy(), LocalDateTime.now());
        commentRepository.save(comment);

        return ResponseEntity.ok("Done");
    }

    public ResponseEntity<Page<Comment>> getcomments(Integer videoId,Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Video video = videoRepository.findById(videoId).get();
        Page<Comment> commentsPage = commentRepository.findByVideo(video, pageable);
        return ResponseEntity.ok(commentsPage);
    }

    public ResponseEntity<String> deleteComment(DeleteCommentDTO deleteCommentDTO){
        if (deleteCommentDTO.getUsername().equals(deleteCommentDTO.getCommentedBy()) || deleteCommentDTO.getUserRole().equals("admin"))
        {
            commentRepository.findById(deleteCommentDTO.getCommentID()).ifPresent(comment -> commentRepository.delete(comment));
            return ResponseEntity.status(HttpStatus.OK).body("Deleted");
        } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You don't have permission to do this");

    }

    public ResponseEntity<String> editComment(EditCommentDTO editCommentDTO){
        if (editCommentDTO.getUsername().equals(editCommentDTO.getCommentedBy()) || editCommentDTO.getRole().equals("admin"))
        {
            commentRepository.findById(editCommentDTO.getCommentId()).ifPresent(comment -> {
                comment.setContent(editCommentDTO.getNewContent());
                commentRepository.save(comment);
            });
            return ResponseEntity.ok("Done");
        }
        return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body("An error occured or comment was not found");
    }
}
