package com.slash.project.myfoli.domain.comment.service;

import com.slash.project.myfoli.domain.comment.dto.CommentRequest;
import com.slash.project.myfoli.domain.comment.dto.CommentResponse;
import com.slash.project.myfoli.domain.comment.entity.Comment;
import com.slash.project.myfoli.domain.comment.repository.CommentRepository;
import com.slash.project.myfoli.domain.post.entity.Post;
import com.slash.project.myfoli.domain.post.entity.Status;
import com.slash.project.myfoli.domain.post.repository.PostRepository;
import com.slash.project.myfoli.domain.user.entity.User;
import com.slash.project.myfoli.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public CommentResponse createComment(Long postId, Long userId, CommentRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));
        
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));

        // 공개된 게시글만 댓글 작성 가능
        if (post.getStatus() != Status.PUBLIC) {
            throw new IllegalStateException("공개된 게시글에만 댓글을 작성할 수 있습니다.");
        }

        String now = LocalDateTime.now().format(formatter);
        
        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(request.getContent())
                .createdAt(now)
                .updatedAt(now)
                .build();

        Comment savedComment = commentRepository.save(comment);
        return CommentResponse.from(savedComment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));

        // 공개된 게시글의 댓글만 조회 가능
        if (post.getStatus() != Status.PUBLIC) {
            throw new IllegalStateException("공개된 게시글의 댓글만 조회할 수 있습니다.");
        }

        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId).stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, Long userId, CommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다. ID: " + commentId));

        if (!comment.getUserId().equals(userId)) {
            throw new IllegalStateException("본인이 작성한 댓글만 수정할 수 있습니다.");
        }

        comment.updateContent(request.getContent());
        
        return CommentResponse.from(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다. ID: " + commentId));

        if (!comment.getUserId().equals(userId)) {
            throw new IllegalStateException("본인이 작성한 댓글만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public long getCommentCount(Long postId) {
        return commentRepository.countByPostId(postId);
    }
}
