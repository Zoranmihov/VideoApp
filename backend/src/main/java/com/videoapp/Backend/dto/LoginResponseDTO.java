package com.videoapp.Backend.dto;

public class LoginResponseDTO {
    private String username;

    private String role;
    private String jwt;



    public LoginResponseDTO(){
        super();
    }

    public LoginResponseDTO(String username, String role, String jwt){
        this.username = username;
        this.role = role;
        this.jwt = jwt;
    }

    public String getUsername() {
        return this.username;
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

    public String getJwt() {
        return this.jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

}
