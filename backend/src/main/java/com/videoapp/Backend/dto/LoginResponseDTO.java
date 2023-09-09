package com.videoapp.Backend.dto;

import com.videoapp.Backend.models.ApplicationUser;

public class LoginResponseDTO {
    private String username;
    private String jwt;

    public LoginResponseDTO(){
        super();
    }

    public LoginResponseDTO(String username, String jwt){
        this.username = username;
        this.jwt = jwt;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJwt() {
        return this.jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

}
