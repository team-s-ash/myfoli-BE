package com.slash.project.myfoli.domain.post.repository;

import com.slash.project.myfoli.domain.post.entity.Post;
import com.slash.project.myfoli.domain.post.entity.Status;
import com.slash.project.myfoli.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    long countByUser(User user);

    List<Post> findByUser_UserId(Long userId);

    List<Post> findByStatus(Status status);

    List<Post> findByUserUserId(Long userId);
}
