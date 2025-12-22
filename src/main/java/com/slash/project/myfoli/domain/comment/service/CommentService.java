package com.slash.project.myfoli.domain.comment.service;

import com.slash.project.myfoli.domain.comment.dto.CommentRequest;
import com.slash.project.myfoli.domain.comment.dto.CommentResponse;
import com.slash.project.myfoli.domain.comment.entity.Comment;
import com.slash.project.myfoli.domain.comment.repository.CommentRepository;
import com.slash.project.myfoli.domain.post.entity.Post;
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
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        String now = LocalDateTime.now().format(formatter);
        
        Comment comment = Comment.builder()
                .userId(user.getUserId())
                .postId(post.getPostId())
                .content(request.getContent())
                .createdAt(now)
                .updatedAt(now)
                .build();

        Comment savedComment = commentRepository.save(comment);
        return CommentResponse.from(savedComment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId).stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, Long userId, CommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if (!comment.getUserId().equals(userId)) {
            throw new IllegalStateException("You are not authorized to update this comment");
        }

        comment.updateContent(request.getContent());
        comment.setUpdatedAt(LocalDateTime.now().format(formatter));
        
        return CommentResponse.from(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if (!comment.getUserId().equals(userId)) {
            throw new IllegalStateException("You are not authorized to delete this comment");
        }

        commentRepository.delete(comment);
    }
}
