package com.videoapp.Backend.controllers;

import com.videoapp.Backend.dto.LoginResponseDTO;
import com.videoapp.Backend.dto.RegistrationDTO;
import com.videoapp.Backend.models.ApplicationUser;
import com.videoapp.Backend.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationDTO registrationDTO){
            if(authenticationService.registerUser(registrationDTO.getUsername(), registrationDTO.getPassword())){
                return new ResponseEntity<String>("Welcome aboard", HttpStatus.OK);

            } else {
                return  new ResponseEntity<String>("Something went wrong please try again", HttpStatus.BAD_REQUEST);
            }
    }

    @PostMapping("/login")
    public LoginResponseDTO loginUser(@RequestBody RegistrationDTO registrationDTO){
        return authenticationService.loginUser(registrationDTO.getUsername(), registrationDTO.getPassword());
    }
}
