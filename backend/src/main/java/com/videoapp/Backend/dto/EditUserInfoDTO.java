package com.videoapp.Backend.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public class EditUserInfoDTO {
    @NotBlank
    private String target;

    @NotBlank
    private String username;

    private String password;

    @NotBlank
    private String userRole;

    @Nullable
    private String data;

    @Nullable
    private MultipartFile avatar;

    @Nullable
    private byte[] avatarBytes;

    public  EditUserInfoDTO(){
        super();
    }

    public EditUserInfoDTO(String target, String username, String password, String userRole, @Nullable String data, @Nullable MultipartFile avatar, @Nullable byte[] avatarBytes) {
        this.target = target;
        this.username = username;
        this.password = password;
        this.userRole = userRole;
        this.data = data;
        this.avatar = avatar;
        this.avatarBytes = avatarBytes;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @Nullable
    public String getData() {
        return data;
    }

    public void setData(@Nullable String data) {
        this.data = data;
    }

    @Nullable
    public MultipartFile getAvatar() {
        return avatar;
    }

    public void setAvatar(@Nullable MultipartFile avatar) {
        this.avatar = avatar;
    }

    @Nullable
    public byte[] getAvatarBytes() {
        return avatarBytes;
    }

    public void setAvatarBytes(@Nullable byte[] avatarBytes) {
        this.avatarBytes = avatarBytes;
    }
}
