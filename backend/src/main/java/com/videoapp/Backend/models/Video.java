package com.videoapp.Backend.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "videos")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "video_id")
    private Integer videoId;

    private String title;

    private String description;

    @Column(name = "video_path")
    private String videoPath;
    @Column(name = "thumbnail_path")
    private String thumbnailPath;
    @Column(name = "uploaded_by")
    private String uploadedBy;

    private LocalDateTime timestamp;

    public Video(){
        super();
    }

    public Video(String title, String description, String videoPath, String thumbnailPath, String uploadedBy, LocalDateTime timestamp){
        this.title = title;
        this.description = description;
        this.videoPath = videoPath;
        this.thumbnailPath = thumbnailPath;
        this.uploadedBy = uploadedBy;
        this.timestamp = timestamp;
    }

    public Integer getVideoId() {
        return videoId;
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

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
