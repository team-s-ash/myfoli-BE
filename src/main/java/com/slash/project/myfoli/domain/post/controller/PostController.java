package com.slash.project.myfoli.domain.post.controller;

import com.slash.project.myfoli.domain.post.dto.PortfolioResponse;
import com.slash.project.myfoli.domain.post.dto.PostCreateRequest;
import com.slash.project.myfoli.domain.post.dto.PostResponse;
import com.slash.project.myfoli.domain.post.dto.PostUpdateRequest;
import com.slash.project.myfoli.domain.post.service.PostService;
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
public class PostController {

    private final PostService postService;

    /**
     * 비공개 저장 (작성자만 볼 수 있음)
     * POST /api/post/save/{id}
     */
    @PostMapping("/save/{id}")
    public ResponseEntity<PostResponse> savePrivate(
            @PathVariable("id") Long userId,
            @Valid @RequestBody PostCreateRequest request) {
        PostResponse response = postService.savePrivate(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 공개 저장 (모든 사용자에게 공개)
     * POST /api/post/upload/{id}
     */
    @PostMapping("/upload/{id}")
    public ResponseEntity<PostResponse> savePublic(
            @PathVariable("id") Long userId,
            @Valid @RequestBody PostCreateRequest request) {
        PostResponse response = postService.savePublic(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 포트폴리오 수정
     * PUT /api/post/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable("id") Long postId,
            @Valid @RequestBody PostUpdateRequest request,
            @RequestHeader("User-Id") Long userId) {
        return ResponseEntity.ok(postService.updatePost(postId, request, userId));
    }

    /**
     * 포트폴리오 열람 (상세 조회)
     * GET /api/post/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<PortfolioResponse> getPortfolio(
            @PathVariable("id") Long postId,
            @RequestHeader(value = "User-Id", required = false) Long currentUserId) {
        return ResponseEntity.ok(postService.getPortfolioById(postId, currentUserId));
    }

    /**
     * 공개된 모든 포트폴리오 조회
     * GET /api/post/public
     */
    @GetMapping("/public")
    public ResponseEntity<List<PortfolioResponse>> getPublicPortfolios() {
        return ResponseEntity.ok(postService.getPublicPortfolios());
    }

    /**
     * 내 포트폴리오 목록 조회
     * GET /api/post/my
     */
    @GetMapping("/my")
    public ResponseEntity<List<PostResponse>> getMyPosts(
            @RequestHeader("User-Id") Long userId) {
        return ResponseEntity.ok(postService.getMyPosts(userId));
    }

    /**
     * 특정 사용자의 공개 포트폴리오 조회
     * GET /api/post/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PortfolioResponse>> getUserPublicPortfolios(
            @PathVariable Long userId) {
        return ResponseEntity.ok(postService.getUserPublicPortfolios(userId));
    }

    /**
     * 포트폴리오 삭제
     * DELETE /api/post/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable("id") Long postId,
            @RequestHeader("User-Id") Long userId) {
        postService.deletePost(postId, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 비공개로 변경
     * POST /api/post/{id}/private
     */
    @PostMapping("/{id}/private")
    public ResponseEntity<Map<String, String>> changeToPrivate(
            @PathVariable("id") Long postId,
            @RequestHeader("User-Id") Long userId) {
        postService.changeToPrivate(postId, userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "비공개로 변경되었습니다.");
        return ResponseEntity.ok(response);
    }

    /**
     * 공개로 변경
     * POST /api/post/{id}/public
     */
    @PostMapping("/{id}/public")
    public ResponseEntity<Map<String, String>> changeToPublic(
            @PathVariable("id") Long postId,
            @RequestHeader("User-Id") Long userId) {
        postService.changeToPublic(postId, userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "공개로 변경되었습니다.");
        return ResponseEntity.ok(response);
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

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, String>> handleSecurityException(SecurityException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }
}
