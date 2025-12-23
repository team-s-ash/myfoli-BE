package com.slash.project.myfoli.domain.like.repository;

import com.slash.project.myfoli.domain.like.entity.Like;
import com.slash.project.myfoli.domain.post.entity.Post;
import com.slash.project.myfoli.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByPostAndUser(Post post, User user);
    Optional<Like> findByPostAndUser(Post post, User user);
    int countByPost(Post post);
}
