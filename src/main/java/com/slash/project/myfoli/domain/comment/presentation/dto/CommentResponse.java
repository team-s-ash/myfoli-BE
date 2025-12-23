package com.slash.project.myfoli.domain.comment.presentation.dto;


import com.slash.project.myfoli.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponse {
    private Long commentId;
    private Long userId;
    private String username;
    private Long postId;
    private String content;


    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .userId(comment.getUser().getUserId())
                .username(comment.getUser().getUsername())
                .postId(comment.getPost().getPostId())
                .content(comment.getContent())
                .build();
    }
}