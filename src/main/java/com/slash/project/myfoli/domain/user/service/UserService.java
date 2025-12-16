package com.slash.project.myfoli.domain.user.service;

import com.slash.project.myfoli.domain.follow.repository.FollowRepository;
import com.slash.project.myfoli.domain.post.repository.PostRepository;
import com.slash.project.myfoli.domain.user.entity.User;
import com.slash.project.myfoli.domain.user.presentation.dto.MyInfoResponse;
import com.slash.project.myfoli.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FollowRepository followRepository;

    // 내 정보 조회
    @Transactional(readOnly = true)
    public MyInfoResponse getMyInfo() {
        // SecurityContextHolder에서 현재 인증된 사용자의 정보를 가져옵니다.
        // JwtAuthenticationFilter에서 username 대신 email을 저장했으므로, getUsername()은 email을 반환합니다.
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String email = userDetails.getUsername();

        // email을 사용하여 데이터베이스에서 사용자 정보를 조회합니다.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 사용자가 작성한 게시물 수 조회
        long postCount = postRepository.countByUser(user);

        // 팔로워 및 팔로잉 수 조회
        long followerCount = followRepository.countByFollowing(user);
        long followingCount = followRepository.countByFollower(user);

        // DTO로 변환하여 반환
        return MyInfoResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .postCount(postCount)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .build();
    }
}
