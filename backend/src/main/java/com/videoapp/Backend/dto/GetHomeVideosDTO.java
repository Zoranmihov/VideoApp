package com.videoapp.Backend.dto;

import java.time.LocalDateTime;

public class GetHomeVideosDTO {

    private Integer videoId;
    private String title;

    private String videoDescription;
    private String uploadedBy;

    private LocalDateTime uploadedAt;

    private byte[] thumbnail;

    public GetHomeVideosDTO(){
        super();
    }

    public GetHomeVideosDTO(Integer videoId,String title, String videoDescription, String uploadedBy, LocalDateTime uploadedAt,  byte[] thumbnail){
        this.videoId = videoId;
        this.title = title;
        this.videoDescription = videoDescription;
        this.uploadedBy = uploadedBy;
        this.uploadedAt = uploadedAt;
        this.thumbnail = thumbnail;
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
