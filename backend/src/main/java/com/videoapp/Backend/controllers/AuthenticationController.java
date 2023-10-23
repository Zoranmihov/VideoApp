package com.videoapp.Backend.controllers;

import com.videoapp.Backend.dto.LoginResponseDTO;
import com.videoapp.Backend.dto.RegistrationDTO;
import com.videoapp.Backend.dto.UserInfoDTO;
import com.videoapp.Backend.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Enumeration;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @GetMapping("/auth")
    public ResponseEntity<String> auth(){
        return new ResponseEntity<String>("Hello", HttpStatus.OK);
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerUser(@Valid @ModelAttribute RegistrationDTO registrationDTO) throws IOException {

        if (registrationDTO.getAvatarFile() != null) {
            byte[]  avatarBytes = registrationDTO.getAvatarFile().getBytes();
            registrationDTO.setAvatarBytes(avatarBytes);
        }

        String status = authenticationService.registerUser(registrationDTO);
        return switch (status) {
            case "Success" -> ResponseEntity.ok("User registered successfully");
            case "Username taken" -> ResponseEntity.status(HttpStatus.CONFLICT).body("Username is already taken");
            case "Error: Role not found" ->
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Role not found");
            case "Database error" ->
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error occurred");
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        };
    }
    

    @PostMapping("/login")
    public ResponseEntity<UserInfoDTO> loginUser(@RequestBody RegistrationDTO registrationDTO, HttpServletResponse response){
        UserInfoDTO status = authenticationService.loginUser(registrationDTO.getUsername(), registrationDTO.getPassword());
        if (status.getJwt().equals("Failed")){
            return new ResponseEntity<UserInfoDTO>(new UserInfoDTO("", "", "", null, "Invalid credentials"), HttpStatus.UNAUTHORIZED);
        }

        response.setHeader("Set-Cookie", "JWT=" + status.getJwt() + "; Domain=localhost; Max-Age=86400; Path=/; SameSite=None; Secure; HttpOnly");
        return new ResponseEntity<UserInfoDTO>(status, HttpStatus.OK);
    }

    @PostMapping("/userinfo")
    public UserInfoDTO getInfo(Authentication auth, HttpServletRequest request) {
        return  authenticationService.userInfo(auth.getName());
    }
}
