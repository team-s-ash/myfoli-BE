package com.slash.project.myfoli.domain.like.controller;

import com.slash.project.myfoli.domain.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}")
public class LikeController {

    private final LikeService likeService;
    
    @PostMapping("/like")
    public ResponseEntity<Void> likePost(
            @PathVariable("postId") Long postId,
            @RequestHeader("User-Id") Long userId) {
        likeService.likePost(postId, userId);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/like")
    public ResponseEntity<Void> unlikePost(
            @PathVariable("postId") Long postId,
            @RequestHeader("User-Id") Long userId) {
        likeService.unlikePost(postId, userId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/like/status")
    public ResponseEntity<Boolean> isPostLikedByUser(
            @PathVariable("postId") Long postId,
            @RequestHeader("User-Id") Long userId) {
        return ResponseEntity.ok(likeService.isPostLikedByUser(postId, userId));
    }
    
    @GetMapping("/like/count")
    public ResponseEntity<Integer> getLikeCount(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(likeService.getLikeCount(postId));
    }
}
