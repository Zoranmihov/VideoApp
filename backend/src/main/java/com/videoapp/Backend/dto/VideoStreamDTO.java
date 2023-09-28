package com.videoapp.Backend.dto;

import org.springframework.core.io.Resource;

public class VideoStreamDTO {
    private final int status;
    private final String contentRange;
    private final Resource resource;

    public VideoStreamDTO(int status, String contentRange, Resource resource) {
        this.status = status;
        this.contentRange = contentRange;
        this.resource = resource;
    }

    // Getters
    public int getStatus() {
        return status;
    }

    public String getContentRange() {
        return contentRange;
    }

    public Resource getResource() {
        return resource;
    }
}
