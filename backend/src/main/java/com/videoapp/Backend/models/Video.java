package com.videoapp.Backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "videos")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "video_id")
    private Integer videoId;

    private String title;
    @Column(name = "description", columnDefinition="TEXT")
    private String description;

    @Column(name = "video_path")
    private String videoPath;
    @Column(name = "thumbnail", columnDefinition = "longblob")
    private byte[] thumbnail;
    @Column(name = "uploaded_by")
    private String uploadedBy;
    @JsonIgnore
    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    private LocalDateTime timestamp;


    public Video(){
        super();
    }

    public Video(String title, String description, String videoPath, byte[] thumbnail, String uploadedBy, LocalDateTime timestamp){
        this.title = title;
        this.description = description;
        this.videoPath = videoPath;
        this.thumbnail = thumbnail;
        this.uploadedBy = uploadedBy;
        this.timestamp = timestamp;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
