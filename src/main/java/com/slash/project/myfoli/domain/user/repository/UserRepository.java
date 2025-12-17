package com.slash.project.myfoli.domain.user.repository;

import com.slash.project.myfoli.domain.user.entity.User;
import com.slash.project.myfoli.domain.user.presentation.dto.MyInfoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // 사용자 정보와 관련 카운트를 한 번의 쿼리로 조회하는 메서드
    @Query("SELECT new com.slash.project.myfoli.domain.user.presentation.dto.MyInfoResponse(" +
            "u.username, " +
            "u.email, " +
            "(SELECT COUNT(p) FROM Post p WHERE p.user = u), " +
            "(SELECT COUNT(f) FROM Follow f WHERE f.following = u), " +
            "(SELECT COUNT(f) FROM Follow f WHERE f.follower = u)) " +
            "FROM User u WHERE u.email = :email")
    Optional<MyInfoResponse> getMyInfoByEmail(@Param("email") String email);
}