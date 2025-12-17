package com.slash.project.myfoli.domain.post.repository;

import com.slash.project.myfoli.domain.post.entity.Post;
import com.slash.project.myfoli.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    long countByUser(User user);
}
