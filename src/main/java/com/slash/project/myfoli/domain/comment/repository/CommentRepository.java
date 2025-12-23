package com.slash.project.myfoli.domain.comment.repository;

import com.slash.project.myfoli.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * 게시물 ID를 기준으로 댓글 목록을 댓글 ID 내림차순으로 조회합니다.
     * (Post 객체 안의 postId 필드를 기준으로 검색)
     */
    List<Comment> findByPost_PostIdOrderByCommentIdDesc(Long postId);

    /**
     * 게시물 ID를 기준으로 댓글 수를 조회합니다.
     * (Post 객체 안의 postId 필드를 기준으로 카운트)
     */
    long countByPost_PostId(Long postId);

    /**
     * 게시물 ID를 기준으로 모든 댓글을 벌크 삭제합니다.
     */
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.post.postId = :postId")
    void deleteAllByPostId(@Param("postId") Long postId);
}
