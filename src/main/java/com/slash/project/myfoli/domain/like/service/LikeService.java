package com.slash.project.myfoli.domain.like.service;

import com.slash.project.myfoli.domain.like.entity.Like;
import com.slash.project.myfoli.domain.like.repository.LikeRepository;
import com.slash.project.myfoli.domain.post.entity.Post;
import com.slash.project.myfoli.domain.post.repository.PostRepository;
import com.slash.project.myfoli.domain.user.entity.User;
import com.slash.project.myfoli.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void likePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));

        if (likeRepository.existsByPostAndUser(post, user)) {
            throw new IllegalStateException("이미 좋아요를 누른 게시글입니다.");
        }

        Like like = Like.of(post, user);
        likeRepository.save(like);
        post.incrementLikeCount();
    }

    @Transactional
    public void unlikePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));

        Like like = likeRepository.findByPostAndUser(post, user)
                .orElseThrow(() -> new IllegalStateException("좋아요를 누르지 않은 게시글입니다."));

        likeRepository.delete(like);
        post.decrementLikeCount();
    }

    @Transactional(readOnly = true)
    public boolean isPostLikedByUser(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));

        return likeRepository.existsByPostAndUser(post, user);
    }

    @Transactional(readOnly = true)
    public int getLikeCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));
        return likeRepository.countByPost(post);
    }
}