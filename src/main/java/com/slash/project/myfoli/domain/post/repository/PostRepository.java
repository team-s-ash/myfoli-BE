package com.slash.project.myfoli.domain.post.repository;

import com.slash.project.myfoli.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
