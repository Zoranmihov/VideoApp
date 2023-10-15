package com.videoapp.Backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LeaveCommentDTO {
    
    @NotNull(message = "Video id is missing details")
    private Integer videoId;
    @NotBlank(message = "Comment content is missing details")
    @Size(max = 2000, message = "Comment must be at most 2000 characters")
    private String content;
    @NotBlank(message = "Username is missing details")
    private String commentedBy;

    public LeaveCommentDTO() {
        super();
    }

    public LeaveCommentDTO(Integer videoId, String content, String commentedBy) {
        this.videoId = videoId;
        this.content = content;
        this.commentedBy = commentedBy;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public String getContent() {
        return content;
    }

    public String getCommentedBy() {
        return commentedBy;
    }
}
