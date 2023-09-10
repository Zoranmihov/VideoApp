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
            if (videoUploadDTO.getVideo().isEmpty() || videoUploadDTO.getThumbnail().isEmpty()) {
                return new ResponseEntity<>("Uploaded files are empty.", HttpStatus.BAD_REQUEST);
            }

            // Convert the relative path to an absolute path
            Path absoluteTempUploadPath = Paths.get(tempUploadPath).toAbsolutePath();
            System.out.println("Absolute Temp directory path: " + absoluteTempUploadPath);

            Path tempVideoPath = absoluteTempUploadPath.resolve(videoUploadDTO.getVideo().getOriginalFilename());
            Path tempThumbnailPath = absoluteTempUploadPath.resolve(videoUploadDTO.getThumbnail().getOriginalFilename());

            videoUploadDTO.getVideo().transferTo(tempVideoPath.toFile());
            videoUploadDTO.getThumbnail().transferTo(tempThumbnailPath.toFile());

            String username = auth.getName(); // Get the authenticated username
            Future<String> futureResult = videoService.videoUpload(videoUploadDTO, username);

            // Immediately return a response
            return new ResponseEntity<>("Upload in progress", HttpStatus.ACCEPTED);

        } catch (Exception e) {
            return new ResponseEntity<>("Error uploading video: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
