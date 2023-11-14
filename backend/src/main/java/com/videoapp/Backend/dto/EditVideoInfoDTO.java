package com.videoapp.Backend.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class EditVideoInfoDTO {

    @NotNull(message = "Video id is missing")
    private Integer videoId;

    @NotBlank(message = "Action missing")
    private String action;

    @NotBlank(message = "Username is missing")
    private String username;

    @Nullable
    private String data;

    @Nullable
    private MultipartFile thumbnail;

    @Nullable
    private byte[] thumbnailBytes;

    public EditVideoInfoDTO() {
        super();
    }

    public EditVideoInfoDTO(Integer videoId, String action, String username, @Nullable String data, @Nullable MultipartFile thumbnail, @Nullable byte[] thumbnailBytes) {
        this.videoId = videoId;
        this.action = action;
        this.username = username;
        this.data = data;
        this.thumbnail = thumbnail;
        this.thumbnailBytes = thumbnailBytes;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Nullable
    public String getData() {
        return data;
    }

    public void setData(@Nullable String data) {
        this.data = data;
    }

    @Nullable
    public MultipartFile getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(@Nullable MultipartFile thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Nullable
    public byte[] getThumbnailBytes() {
        return thumbnailBytes;
    }

    public void setThumbnailBytes(@Nullable byte[] thumbnailBytes) {
        this.thumbnailBytes = thumbnailBytes;
    }
}
