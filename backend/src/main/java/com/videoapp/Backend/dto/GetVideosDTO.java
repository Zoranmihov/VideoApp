package com.videoapp.Backend.dto;

import com.videoapp.Backend.models.Video;

import java.time.LocalDateTime;

public class GetVideosDTO {

    private Integer videoId;
    private String title;

    private String videoDescription;
    private String uploadedBy;

    private LocalDateTime uploadedAt;

    private byte[] thumbnail;

    public GetVideosDTO(){
        super();
    }

    public GetVideosDTO(Integer videoId, String title, String videoDescription, String uploadedBy, LocalDateTime uploadedAt, byte[] thumbnail){
        this.videoId = videoId;
        this.title = title;
        this.videoDescription = videoDescription;
        this.uploadedBy = uploadedBy;
        this.uploadedAt = uploadedAt;
        this.thumbnail = thumbnail;
    }

    public static GetVideosDTO fromVideo(Video video) {
        return new GetVideosDTO(
                video.getVideoId(),
                video.getTitle(),
                video.getDescription(),  // Assuming videoDescription in DTO maps to description in Video
                video.getUploadedBy(),
                video.getTimestamp(),    // Assuming uploadedAt in DTO maps to timestamp in Video
                video.getThumbnail()
        );
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

    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }
}
