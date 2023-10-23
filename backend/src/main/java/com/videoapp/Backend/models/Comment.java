package com.videoapp.Backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_id")
    private Integer commentId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "video_id", referencedColumnName = "video_id")
    @JsonIgnore
    private Video video;

    @Column(name = "content", columnDefinition="TEXT")
    private String content;

    @Column(name = "commented_by")
    private String commentedBy;

    private LocalDateTime timestamp;

    public Comment() {
        super();
    }

    public Comment(Video video, String content, String commentedBy, LocalDateTime timestamp) {
        this.video = video;
        this.content = content;
        this.commentedBy = commentedBy;
        this.timestamp = timestamp;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommentedBy() {
        return commentedBy;
    }

    public void setCommentedBy(String commentedBy) {
        this.commentedBy = commentedBy;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
