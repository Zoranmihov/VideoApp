package com.videoapp.Backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/user")
public class UserController {

    @GetMapping
    public ResponseEntity<String> test() {
        return new ResponseEntity<String>("Hello", HttpStatus.OK);
    }
}
