package com.videoapp.Backend.controllers;

import com.videoapp.Backend.dto.GetHomeVideosDTO;
import com.videoapp.Backend.dto.VideoStreamDTO;
import com.videoapp.Backend.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @GetMapping("/video/{videoId}")
    public ResponseEntity<Resource> streamVideo(@PathVariable Integer videoId, @RequestHeader(value = "Range", required = false) String rangeHeader) throws IOException {
        VideoStreamDTO videoStream = videoService.streamVideo(videoId, rangeHeader);
        return ResponseEntity.status(videoStream.getStatus())
                .header(HttpHeaders.CONTENT_TYPE, "video/mp4")
                .header(HttpHeaders.CONTENT_RANGE, videoStream.getContentRange())
                .body(videoStream.getResource());
    }

}
