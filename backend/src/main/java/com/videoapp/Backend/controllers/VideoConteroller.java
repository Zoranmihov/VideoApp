package com.videoapp.Backend.controllers;

import com.videoapp.Backend.dto.GetHomeVideosDTO;
import com.videoapp.Backend.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/videos")
public class VideoConteroller {

    @Autowired
    private VideoService videoService;

    @GetMapping("/all")
    public List<GetHomeVideosDTO> getRecent() {
        return videoService.getRecentVideos();
    }
}
