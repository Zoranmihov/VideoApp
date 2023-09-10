package com.videoapp.Backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class VideoUploadDTO {

    @NotBlank(message = "Video title cannot be blank")
    @Size(max = 255, message = "Video title must be at most 255 characters")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;

    @NotNull(message = "Video file cannot be null")
    private MultipartFile video;

    @NotNull(message = "Thumbnail file cannot be null")
    private MultipartFile thumbnail;

    public VideoUploadDTO() {
        super();
    }

    public VideoUploadDTO(String title, String description, MultipartFile video, MultipartFile thumbnail) {
        this.title = title;
        this.description = description;
        this.video = video;
        this.thumbnail = thumbnail;
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

    public MultipartFile getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(MultipartFile thumbnail) {
        this.thumbnail = thumbnail;
    }

}
