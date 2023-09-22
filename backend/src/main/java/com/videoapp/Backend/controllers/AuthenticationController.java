package com.videoapp.Backend.controllers;

import com.videoapp.Backend.dto.LoginResponseDTO;
import com.videoapp.Backend.dto.RegistrationDTO;
import com.videoapp.Backend.dto.UserInfoDTO;
import com.videoapp.Backend.services.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @GetMapping("/auth")
    public ResponseEntity<String> auth(){
        return new ResponseEntity<String>("Hello", HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegistrationDTO registrationDTO){
        String status = authenticationService.registerUser(registrationDTO.getUsername(), registrationDTO.getPassword());
        switch (status) {
            case "Success":
                return ResponseEntity.ok("User registered successfully");

            case "Username taken":
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username is already taken");

            case "Error: Role not found":
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Role not found");

            case "Database error":
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error occurred");

            case "Error occurred":
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
    

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody RegistrationDTO registrationDTO, HttpServletResponse response){
        LoginResponseDTO status = authenticationService.loginUser(registrationDTO.getUsername(), registrationDTO.getPassword());
        if (status.getJwt().equals("Failed")){
            return new ResponseEntity<LoginResponseDTO>(new LoginResponseDTO("", "", ""), HttpStatus.UNAUTHORIZED);
        }

        response.setHeader("Set-Cookie", "JWT=" + status.getJwt() + "; Domain=localhost; Max-Age=86400; Path=/; SameSite=None; Secure; HttpOnly");
        return new ResponseEntity<LoginResponseDTO>(new LoginResponseDTO(status.getUsername(), status.getRole().toLowerCase(), status.getJwt()), HttpStatus.OK);
    }

    @PostMapping("/userinfo")
    public UserInfoDTO   getInfo(Authentication auth) {
        return  authenticationService.userInfo(auth.getName());
    }
}
