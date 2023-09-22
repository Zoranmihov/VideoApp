package com.videoapp.Backend.controllers;

import com.videoapp.Backend.dto.VideoUploadDTO;
import com.videoapp.Backend.services.VideoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private VideoService videoService;

    @Value("${spring.servlet.multipart.location}")
    private String tempUploadPath;

    @GetMapping
    public ResponseEntity<String> test() {
        return new ResponseEntity<String>("Hello", HttpStatus.OK);
    }

    @PostMapping(value = "/uploadvideo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadVideo(@Valid @ModelAttribute VideoUploadDTO videoUploadDTO, Authentication auth) {
        try {
            if (videoUploadDTO.getVideo().isEmpty()) {
                return new ResponseEntity<>("Video file cannot be empty.", HttpStatus.BAD_REQUEST);
            }

            // Convert the relative path to an absolute path
            Path absoluteTempUploadPath = Paths.get(tempUploadPath).toAbsolutePath();

            // Get the authenticated username
            String username = auth.getName();

            // Create a folder with the user's username in the temp directory if it does not exist
            Path tempUserPath = absoluteTempUploadPath.resolve(username);
            if (!Files.exists(tempUserPath)) {
                Files.createDirectories(tempUserPath);
            }

            // Create paths to the video and thumbnail files in the temp user directory
            Path tempVideoPath = tempUserPath.resolve(videoUploadDTO.getVideo().getOriginalFilename());

            // Transfer the video file to the temp user directory
            videoUploadDTO.getVideo().transferTo(tempVideoPath.toFile());


            // Update the video and thumbnail paths in the DTO with the temp user directory paths
            videoUploadDTO.setVideoTempPath(tempVideoPath.toString());

            // Start the video upload process
            Future<String> futureResult = videoService.videoUpload(videoUploadDTO, username);

            // Immediately return a response
            return new ResponseEntity<>("Upload in progress", HttpStatus.ACCEPTED);

        } catch (Exception e) {
            return new ResponseEntity<>("Error uploading video: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
