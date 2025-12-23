package com.slash.project.myfoli.domain.comment.service;

import com.slash.project.myfoli.domain.comment.entity.Comment;
import com.slash.project.myfoli.domain.comment.presentation.dto.CommentRequest;
import com.slash.project.myfoli.domain.comment.presentation.dto.CommentResponse;
import com.slash.project.myfoli.domain.comment.repository.CommentRepository;
import com.slash.project.myfoli.domain.post.entity.Post;
import com.slash.project.myfoli.domain.post.repository.PostRepository;
import com.slash.project.myfoli.domain.user.entity.User;
import com.slash.project.myfoli.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    /**
     * 댓글 생성
     *
     * @param postId 댓글을 작성할 게시글 ID
     * @param request 댓글 내용
     * @return 생성된 댓글 정보
     */
    public CommentResponse createComment(Long postId, CommentRequest request) {
        // 인증된 사용자 정보 가져오기
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("인증된 사용자를 찾을 수 없습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(request.getContent())
                .build();

        Comment savedComment = commentRepository.save(comment);
        return CommentResponse.from(savedComment);
    }

    /**
     * 특정 게시글의 댓글 목록 조회
     *
     * @param postId 게시글 ID
     * @return 댓글 목록
     */
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPostId(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId);
        }

        // commentId를 기준으로 최신순 정렬 (이전에 createdAt을 사용했으나 삭제됨)
        return commentRepository.findByPost_PostIdOrderByCommentIdDesc(postId).stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 댓글 수정
     *
     * @param commentId 수정할 댓글 ID
     * @param request 수정할 댓글 내용
     * @return 수정된 댓글 정보
     */
    public CommentResponse updateComment(Long commentId, CommentRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("인증된 사용자를 찾을 수 없습니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다. ID: " + commentId));

        // 댓글 작성자 본인 확인
        if (!comment.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalStateException("본인이 작성한 댓글만 수정할 수 있습니다.");
        }

        comment.updateContent(request.getContent());
        // @Transactional에 의해 메서드 종료 시 자동으로 dirty checking 되어 DB에 반영
        return CommentResponse.from(comment);
    }

    /**
     * 댓글 삭제
     *
     * @param commentId 삭제할 댓글 ID
     */
    public void deleteComment(Long commentId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("인증된 사용자를 찾을 수 없습니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다. ID: " + commentId));

        // 댓글 작성자 본인 확인
        if (!comment.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalStateException("본인이 작성한 댓글만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }

    /**
     * 특정 게시글의 댓글 수 조회
     *
     * @param postId 게시글 ID
     * @return 댓글 수
     */
    @Transactional(readOnly = true)
    public long getCommentCount(Long postId) {
        return commentRepository.countByPost_PostId(postId);
    }
}
