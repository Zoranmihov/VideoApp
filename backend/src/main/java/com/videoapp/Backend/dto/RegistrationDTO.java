package com.videoapp.Backend.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class RegistrationDTO {

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 5, max = 16, message = "Username must be between 5 and 16 characters")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Nullable
    private byte[] avatarBytes;

    @Nullable
    private MultipartFile avatarFile;

    public RegistrationDTO(){
        super();
    }

    public RegistrationDTO(String username, String password, byte[] avatarBytes, MultipartFile avatarFile){
        this.username = username;
        this.password = password;
        this.avatarBytes = avatarBytes;
        this.avatarFile = avatarFile;
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

    @Nullable
    public byte[] getAvatarBytes() {
        return avatarBytes;
    }

    public void setAvatarBytes(@Nullable byte[] avatarBytes) {
        this.avatarBytes = avatarBytes;
    }

    @Nullable
    public MultipartFile getAvatarFile() {
        return avatarFile;
    }

    public void setAvatarFile(@Nullable MultipartFile avatarFile) {
        this.avatarFile = avatarFile;
    }
}
