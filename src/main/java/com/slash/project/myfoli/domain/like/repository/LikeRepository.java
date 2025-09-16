package com.slash.project.myfoli.domain.like.repository;

import com.slash.project.myfoli.domain.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
