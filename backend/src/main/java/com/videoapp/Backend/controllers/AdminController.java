package com.videoapp.Backend.controllers;

import com.videoapp.Backend.dto.DeleteVideoDTO;
import com.videoapp.Backend.dto.EditVideoDTO;
import com.videoapp.Backend.services.VideoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private VideoService videoService;

    @GetMapping
    public ResponseEntity<String> test() {
        return new ResponseEntity<String>("Hello Admin", HttpStatus.OK);
    }

    @PostMapping("/delvideo")
    public ResponseEntity<String> delVideo(@Valid @RequestBody DeleteVideoDTO deleteVideoDTO) {
        return videoService.delVideo(deleteVideoDTO);
    }

    @PutMapping("/editvideo")
    public ResponseEntity<String> editVideo(@Valid @RequestBody EditVideoDTO editVideoDTO) {
        return videoService.editVideo(editVideoDTO);
    }
}