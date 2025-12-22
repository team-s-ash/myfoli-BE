package com.slash.project.myfoli.domain.comment.controller;

import com.slash.project.myfoli.domain.comment.dto.CommentRequest;
import com.slash.project.myfoli.domain.comment.dto.CommentResponse;
import com.slash.project.myfoli.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long postId,
            @AuthenticationPrincipal Long userId,
            @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.createComment(postId, userId, request));
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getCommentsByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal Long userId,
            @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.updateComment(commentId, userId, request));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal Long userId) {
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.noContent().build();
    }
}
