package com.slash.project.myfoli.domain.comment.controller;

import com.slash.project.myfoli.domain.comment.dto.CommentRequest;
import com.slash.project.myfoli.domain.comment.dto.CommentResponse;
import com.slash.project.myfoli.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 작성
     */
    @PostMapping("/{id}/comment")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable("id") Long postId,
            @RequestHeader("User-Id") Long userId,
            @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.createComment(postId, userId, request));
    }

    /**
     * 게시글의 모든 댓글 조회
     */
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsByPostId(
            @PathVariable("id") Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }

    /**
     * 댓글 수 조회
     */
    @GetMapping("/{id}/comments/count")
    public ResponseEntity<Map<String, Long>> getCommentCount(
            @PathVariable("id") Long postId) {
        long count = commentService.getCommentCount(postId);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    /**
     * 댓글 수정
     */
    @PutMapping("/{id}/comment/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable("id") Long postId,
            @PathVariable Long commentId,
            @RequestHeader("User-Id") Long userId,
            @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.updateComment(commentId, userId, request));
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{id}/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable("id") Long postId,
            @PathVariable Long commentId,
            @RequestHeader("User-Id") Long userId) {
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 예외 처리
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }
}
