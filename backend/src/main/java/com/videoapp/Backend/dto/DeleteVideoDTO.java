package com.videoapp.Backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DeleteVideoDTO {

    @NotNull(message = "videoId is missing")
    private Integer videoId;

    @NotBlank(message = "uploadedBy is missing")
    private String uploadedBy;

    @NotBlank(message = "userName is missing")
    private String userName;

    @NotBlank(message = "userROle is missing")
    private String userRole;

    public DeleteVideoDTO (){
        super();
    }

    public DeleteVideoDTO(Integer videoId,  String uploadedBy, String userName, String userRole) {
        this.videoId = videoId;
        this.uploadedBy = uploadedBy;
        this.userName = userName;
        this.userRole = userRole;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserRole() {
        return userRole;
    }
}
