package com.slash.project.myfoli.domain.comment.dto;

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
    private String createdAt;
    private String updatedAt;

    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .userId(comment.getUserId())
                .username(comment.getUser().getUsername())
                .postId(comment.getPostId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
