package com.slash.project.myfoli.domain.follow.repository;

import com.slash.project.myfoli.domain.follow.entity.Follow;
import com.slash.project.myfoli.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    // 특정 사용자를 팔로우하는 사용자 수 (팔로워 수)
    long countByFollowing(User user);

    // 특정 사용자가 팔로우하는 사용자 수 (팔로잉 수)
    long countByFollower(User user);
}
