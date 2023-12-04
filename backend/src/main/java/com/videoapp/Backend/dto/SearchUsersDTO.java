package com.videoapp.Backend.dto;

public class SearchUsersDTO {
    private String username;

    private byte[] avatar;

    public SearchUsersDTO(){
        super();
    }

    public SearchUsersDTO(String username, byte[] avatar) {
        this.username = username;
        this.avatar = avatar;
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
}
