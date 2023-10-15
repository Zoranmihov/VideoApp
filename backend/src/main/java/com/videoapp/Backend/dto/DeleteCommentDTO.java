package com.videoapp.Backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DeleteCommentDTO {

    @NotNull(message = "Comment id is missing details")
    private Integer commentID;

    @NotBlank(message = "CommentedBy is missing details")
    private String commentedBy;

    @NotBlank(message = "Username is missing details")
    private String username;

    @NotBlank(message = "UserRole is missing details")
    private String userRole;

    public DeleteCommentDTO(){
        super();
    }

    public DeleteCommentDTO(Integer commentID, String commentedBy, String username, String userRole) {
        this.commentID = commentID;
        this.commentedBy = commentedBy;
        this.username = username;
        this.userRole = userRole;
    }

    public Integer getCommentID() {
        return commentID;
    }

    public String getCommentedBy() {
        return commentedBy;
    }

    public String getUsername() {
        return username;
    }

    public String getUserRole() {
        return userRole;
    }
}
