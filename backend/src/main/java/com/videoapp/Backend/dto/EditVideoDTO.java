package com.videoapp.Backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EditVideoDTO {
    @NotNull(message = "VideoId is missing")
    private Integer videoId;

    @NotBlank(message = "Title is missing")
    private String title;

    @NotBlank(message = "Description is missing")
    private String description;

    @NotBlank(message = "UploadedBy is missing")
    private String uploadedBy;

    @NotBlank(message = "Username is missing")
    private String username;

    @NotBlank(message = "UserRole is missing")
    private String userRole;

    public EditVideoDTO(){
        super();
    }

    public EditVideoDTO(Integer videoId, String title, String description, String uploadedBy, String username, String userRole) {
        this.videoId = videoId;
        this.title = title;
        this.description = description;
        this.uploadedBy = uploadedBy;
        this.username = username;
        this.userRole = userRole;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public String getUsername() {
        return username;
    }

    public String getUserRole() {
        return userRole;
    }
}
