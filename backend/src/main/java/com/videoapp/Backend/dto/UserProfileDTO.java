package com.videoapp.Backend.dto;

import com.videoapp.Backend.models.Video;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;

public class UserProfileDTO {
    private String username;

    @Nullable
    private byte[] avatar;

    @Nullable
    Page<Video> videos;

    public UserProfileDTO() {
        super();
    }

    public UserProfileDTO(String username, byte[] avatar, Page<Video> videos) {
        this.username = username;
        this.avatar = avatar;
        this.videos = videos;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public Page<Video> getVideos() {
        return videos;
    }

    public void setVideos(Page<Video> videos) {
        this.videos = videos;
    }
}
