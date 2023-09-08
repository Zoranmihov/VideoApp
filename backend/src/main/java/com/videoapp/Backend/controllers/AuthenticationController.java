package com.videoapp.Backend.controllers;

import com.videoapp.Backend.dto.LoginResponseDTO;
import com.videoapp.Backend.dto.RegistrationDTO;
import com.videoapp.Backend.models.ApplicationUser;
import com.videoapp.Backend.services.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
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
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody RegistrationDTO registrationDTO, HttpServletResponse response){
        LoginResponseDTO status = authenticationService.loginUser(registrationDTO.getUsername(), registrationDTO.getPassword());
        if (status.getJwt().equals("Failed")){
            return new ResponseEntity<LoginResponseDTO>(new LoginResponseDTO("", ""), HttpStatus.UNAUTHORIZED);
        }

        response.setHeader("Set-Cookie", "JWT=" + status.getJwt() + "; Domain=localhost; Max-Age=86400; Path=/; SameSite=None; Secure; HttpOnly");
        return new ResponseEntity<LoginResponseDTO>(new LoginResponseDTO(status.getUsername(), status.getJwt()), HttpStatus.OK);
    }
}
