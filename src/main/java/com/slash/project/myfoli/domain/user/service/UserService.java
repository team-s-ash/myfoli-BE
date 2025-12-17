package com.slash.project.myfoli.domain.user.service;

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

    /**
     * 내 정보 조회
     * SecurityContext에서 인증된 사용자의 이메일을 가져온 후,
     * UserRepository에 추가한 커스텀 쿼리를 호출하여 사용자 정보와 관련 카운트들을 한 번에 조회합니다.
     * 이 방식은 여러 번의 DB 조회를 한 번으로 줄여 성능을 개선합니다.
     *
     * @return MyInfoResponse - 조회된 사용자 정보 DTO
     */
    @Transactional(readOnly = true)
    public MyInfoResponse getMyInfo() {
        // SecurityContextHolder에서 현재 인증된 사용자의 정보를 가져옵니다.
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String email = userDetails.getUsername();

        // email을 사용하여 데이터베이스에서 사용자 정보를 DTO로 직접 조회합니다.
        return userRepository.getMyInfoByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
}
