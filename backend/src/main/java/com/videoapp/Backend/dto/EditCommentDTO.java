package com.videoapp.Backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EditCommentDTO {

    @NotNull
    private Integer commentId;
    @NotBlank(message = "Video title cannot be blank")
    @Size(max = 2000, message = "Comment must be at most 2000 characters")
    private String newContent;

    @NotBlank(message = "Video title cannot be blank")
    private String username;

    @NotBlank(message = "Video title cannot be blank")
    private String commentedBy;

    @NotBlank(message = "Video title cannot be blank")
    private String role;

    public EditCommentDTO(){
        super();
    }

    public EditCommentDTO(Integer commentId, String newContent, String username, String commentedBy, String role) {
        this.commentId = commentId;
        this.newContent = newContent;
        this.username = username;
        this.commentedBy = commentedBy;
        this.role = role;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public String getNewContent() {
        return newContent;
    }

    public String getUsername() {
        return username;
    }

    public String getCommentedBy() {
        return commentedBy;
    }

    public String getRole() {
        return role;
    }
}
