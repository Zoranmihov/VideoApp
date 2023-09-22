package com.videoapp.Backend.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class VideoUploadDTO {

    @NotBlank(message = "Video title cannot be blank")
    @Size(max = 255, message = "Video title must be at most 255 characters")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;

    @NotNull(message = "Video file cannot be null")
    private MultipartFile video;

    @Nullable
    private byte[] thumbnail;

    @Nullable
    private String videoTempPath;

    @Nullable
    private String thumbnailTempPath;

    public VideoUploadDTO() {
        super();
    }

    public VideoUploadDTO(String title, String description, MultipartFile video, byte[] thumbnail, String videoTempPath, String thumbnailTempPath) {
        this.title = title;
        this.description = description;
        this.video = video;
        this.thumbnail = thumbnail;
        this.videoTempPath = videoTempPath;
        this.thumbnailTempPath = thumbnailTempPath;
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

    public MultipartFile getVideo() {
        return video;
    }

    public void setVideo(MultipartFile video) {
        this.video = video;
    }

    @Nullable
    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(@Nullable byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Nullable
    public String getVideoTempPath() {
        return videoTempPath;
    }

    public void setVideoTempPath(@Nullable String videoTempPath) {
        this.videoTempPath = videoTempPath;
    }

    @Nullable
    public String getThumbnailTempPath() {
        return thumbnailTempPath;
    }

    public void setThumbnailTempPath(@Nullable String thumbnailTempPath) {
        this.thumbnailTempPath = thumbnailTempPath;
    }
}
