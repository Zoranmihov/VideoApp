package com.videoapp.Backend.dto;

import jakarta.annotation.Nullable;

public class UserInfoDTO {

    private String username;
    private String role;
    @Nullable
    private String jwt;
    @Nullable
    private byte[] avatar;

    @Nullable
    private  String message;

    public UserInfoDTO(){
        super();
    }

    public UserInfoDTO(String username, String role,String jwt ,byte[] avatar, String message){
        this.username = username;
        this.role = role;
        this.jwt = jwt;
        this.avatar = avatar;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Nullable
    public String getJwt() {
        return jwt;
    }

    public void setJwt(@Nullable String jwt) {
        this.jwt = jwt;
    }

    @Nullable
    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    public void setMessage(@Nullable String message) {
        this.message = message;
    }
}
