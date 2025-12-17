package com.slash.project.myfoli.domain.user.service;

import com.slash.project.myfoli.domain.auth.exception.UsernameAlreadyUsedException;
import com.slash.project.myfoli.domain.follow.repository.FollowRepository;
import com.slash.project.myfoli.domain.post.repository.PostRepository;
import com.slash.project.myfoli.domain.user.entity.User;
import com.slash.project.myfoli.domain.user.presentation.dto.MyInfoResponse;
import com.slash.project.myfoli.domain.user.presentation.dto.UpdateMyInfoRequest;
import com.slash.project.myfoli.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FollowRepository followRepository;

    /**
     * 내 정보 조회
     * 컨트롤러에서 @AuthenticationPrincipal을 통해 받은 email을 사용하여 조회합니다.
     *
     * @param email - 인증된 사용자의 이메일
     * @return MyInfoResponse - 조회된 사용자 정보 DTO
     */
    @Transactional(readOnly = true)
    public MyInfoResponse getMyInfo(String email) {
        // email을 사용하여 데이터베이스에서 사용자 정보를 조회합니다.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        long postCount = postRepository.countByUser(user);
        long followerCount = followRepository.countByFollowing(user);
        long followingCount = followRepository.countByFollower(user);

        return MyInfoResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .postCount(postCount)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .build();
    }

    /**
     * 내 정보 수정
     */
    @Transactional
    public void updateMyInfo(UpdateMyInfoRequest dto) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("인증된 사용자를 찾을 수 없습니다."));

        String newUsername = dto.getUsername();

        if (newUsername == null || newUsername.isBlank()) {
            throw new IllegalArgumentException("사용자 이름은 비어 있을 수 없습니다.");
        }

        // 기존과 동일하면 아무 작업 안 함
        if (user.getUsername().equals(newUsername)) {
            return;
        }

        // 중복 검사
        if (userRepository.existsByUsername(newUsername)) {
            throw new UsernameAlreadyUsedException("이미 사용 중인 이름입니다.");
        }

        user.updateUsername(newUsername);
    }
}
