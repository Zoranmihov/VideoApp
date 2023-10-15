package com.videoapp.Backend.dto;

import java.sql.Array;
import java.time.LocalDateTime;

public class VideoInfoDTO {


    private final Integer id;
    private final String description;
    private final String title;
    private final String uploadedBy;
    private final LocalDateTime uploadedAt;


    public VideoInfoDTO(Integer id, String description, String title, String uploadedBy, LocalDateTime uploadedAt) {
        this.id = id;
        this.description = description;
        this.title = title;
        this.uploadedBy = uploadedBy;
        this.uploadedAt = uploadedAt;
    }

    public Integer getid() {
        return id;
    }

    public String getdescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getuploadedBy() {
        return uploadedBy;
    }

    public LocalDateTime getuploadedAt() {
        return uploadedAt;
    }
}
