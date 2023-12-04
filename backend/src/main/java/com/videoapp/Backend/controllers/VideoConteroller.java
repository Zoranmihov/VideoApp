package com.videoapp.Backend.controllers;

import com.videoapp.Backend.dto.*;
import com.videoapp.Backend.models.Comment;
import com.videoapp.Backend.models.Video;
import com.videoapp.Backend.services.CommentService;
import com.videoapp.Backend.services.VideoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/videos")
public class VideoConteroller {

    @Autowired
    private VideoService videoService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/all")
    public List<GetVideosDTO> getRecent() {
        return videoService.getRecentVideos();
    }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<Resource> streamVideo(
            @PathVariable Integer videoId,
            @RequestHeader(value = "Range", required = false) String rangeHeader)
            throws IOException, ExecutionException, InterruptedException {

        VideoStreamDTO videoStream = videoService.streamVideo(videoId, rangeHeader);
        return ResponseEntity.status(videoStream.getStatus())
                .header(HttpHeaders.CONTENT_TYPE, "video/mp4")
                .header(HttpHeaders.CONTENT_RANGE, videoStream.getContentRange())
                .body(videoStream.getResource());
    }

    @GetMapping("/video/info/{videoId}")
    public ResponseEntity<VideoInfoDTO> getVideoInfo(@PathVariable Integer videoId){
        return  videoService.getVidInfo(videoId);
    }

    @GetMapping("/getcomments/{videoId}")
    public ResponseEntity<Page<Comment>> getVideoComments(@PathVariable Integer videoId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return commentService.getcomments(videoId, page, size);
    }

    @GetMapping("/search/{term}")
    public ResponseEntity<Page<GetVideosDTO>> searchVideos(@PathVariable String term, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return videoService.searchVideos(term, page, size);
    }

}
