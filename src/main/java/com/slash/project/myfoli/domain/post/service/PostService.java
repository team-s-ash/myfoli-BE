package com.slash.project.myfoli.domain.post.service;

import com.slash.project.myfoli.domain.comment.repository.CommentRepository;
import com.slash.project.myfoli.domain.post.dto.PortfolioResponse;
import com.slash.project.myfoli.domain.post.dto.PostCreateRequest;
import com.slash.project.myfoli.domain.post.dto.PostResponse;
import com.slash.project.myfoli.domain.post.dto.PostUpdateRequest;
import com.slash.project.myfoli.domain.post.entity.Post;
import com.slash.project.myfoli.domain.post.entity.Status;
import com.slash.project.myfoli.domain.post.repository.PostRepository;
import com.slash.project.myfoli.domain.user.entity.User;
import com.slash.project.myfoli.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    /**
     * 비공개 저장 (작성자만 볼 수 있음)
     */
    @Transactional
    public PostResponse savePrivate(PostCreateRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));

        
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .viewCount(0)
                .status(Status.PRIVATE)
                .user(user)
                .build();

        Post savedPost = postRepository.save(post);
        return new PostResponse(savedPost);
    }
    
    /**
     * 공개 저장 (모든 사용자에게 공개)
     */
    @Transactional
    public PostResponse savePublic(PostCreateRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));
        
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .viewCount(0)
                .status(Status.PUBLIC)
                .user(user)
                .build();

        Post savedPost = postRepository.save(post);
        return new PostResponse(savedPost);
    }
    
    /**
     * 포트폴리오 수정 (상태 유지)
     */
    @Transactional
    public PostResponse updatePost(Long postId, PostUpdateRequest request, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));
        
        if (!post.getUser().getUserId().equals(userId)) {
            throw new SecurityException("본인의 게시글만 수정할 수 있습니다.");
        }
        
        post.update(request.getTitle(), request.getContent(), request.getStatus());
        
        return new PostResponse(post);
    }
    
    /**
     * 공개된 모든 포트폴리오 조회
     */
    @Transactional(readOnly = true)
    public List<PortfolioResponse> getPublicPortfolios() {
        return postRepository.findByStatus(Status.PUBLIC).stream()
                .map(PortfolioResponse::new)
                .collect(Collectors.toList());
    }
    
    /**
     * 내 포트폴리오 목록 조회 (모든 상태)
     */
    @Transactional(readOnly = true)
    public List<PostResponse> getMyPosts(Long userId) {
        return postRepository.findByUserUserId(userId).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }
    
    /**
     * 특정 사용자의 공개 포트폴리오 조회
     */
    @Transactional(readOnly = true)
    public List<PortfolioResponse> getUserPublicPortfolios(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));
        
        return postRepository.findByUserUserId(userId).stream()
                .filter(post -> post.getStatus() == Status.PUBLIC)
                .map(PortfolioResponse::new)
                .collect(Collectors.toList());
    }
    
    /**
     * 포트폴리오 상세 조회
     */
    @Transactional
    public PortfolioResponse getPortfolioById(Long postId, Long currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));
        
        // 비공개 포트폴리오는 작성자만 볼 수 있음
        if (post.getStatus() == Status.PRIVATE) {
            if (currentUserId == null || !post.getUser().getUserId().equals(currentUserId)) {
                throw new IllegalStateException("이 포트폴리오를 볼 권한이 없습니다.");
            }
        }
        
        // 공개 포트폴리오면 조회수 증가
        if (post.getStatus() == Status.PUBLIC) {
            post.incrementViewCount();
        }
        
        return new PortfolioResponse(post);
    }
    
    /**
     * 포트폴리오 삭제
     */
    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));
        
        if (!post.getUser().getUserId().equals(userId)) {
            throw new SecurityException("본인의 게시글만 삭제할 수 있습니다.");
        }
        
        // 댓글도 함께 삭제
        commentRepository.deleteAllByPostId(postId);
        postRepository.delete(post);
    }
    
    /**
     * 비공개로 변경
     */
    @Transactional
    public void changeToPrivate(Long postId, Long userId) {
        changePostStatus(postId, userId, Status.PRIVATE);
    }
    
    /**
     * 공개로 변경
     */
    @Transactional
    public void changeToPublic(Long postId, Long userId) {
        changePostStatus(postId, userId, Status.PUBLIC);
    }
    
    /**
     * 상태 변경 헬퍼 메서드
     */
    private void changePostStatus(Long postId, Long userId, Status status) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));
        
        if (!post.getUser().getUserId().equals(userId)) {
            throw new SecurityException("본인의 게시글만 상태를 변경할 수 있습니다.");
        }
        
        post.setStatus(status);
    }
    
    @Transactional(readOnly = true)
    public PostResponse getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));
        return new PostResponse(post);
    }
}
