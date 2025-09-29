package com.slash.project.myfoli.domain.auth.service;

import com.slash.project.myfoli.domain.auth.exception.EmailAlreadyUsedException;
import com.slash.project.myfoli.domain.auth.exception.UsernameAlreadyUsedException;
import com.slash.project.myfoli.domain.auth.presentation.dto.LoginRequest;
import com.slash.project.myfoli.domain.auth.presentation.dto.LoginResponse;
import com.slash.project.myfoli.domain.auth.presentation.dto.SignUpRequest;
import com.slash.project.myfoli.domain.category.entity.Category;
import com.slash.project.myfoli.domain.category.repository.CategoryRepository;
import com.slash.project.myfoli.domain.interest.entity.UserInterest;
import com.slash.project.myfoli.domain.interest.repository.UserInterestRepository;
import com.slash.project.myfoli.domain.user.dto.UserDto;
import com.slash.project.myfoli.domain.user.entity.User;
import com.slash.project.myfoli.domain.user.repository.UserRepository;
import com.slash.project.myfoli.global.auth.jwt.JwtProvider;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class UserAuthService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final UserInterestRepository userInterestRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public void signUp(SignUpRequest signUpRequest) throws Exception{
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) { //이미 존재하는 email인지 확인
            throw new EmailAlreadyUsedException("Email already in use"); // 커스텀 exception
        }
        if (userRepository.existsByUsername(signUpRequest.getUsername())){ // 이미 존재하는 username인지 확인
            throw new UsernameAlreadyUsedException("Username already in use");
        }

        User user = User.builder(). // Builder를 사용하여 DTO로 받아온 정보를 객체로 넘기기
                username(signUpRequest.getUsername()).
                password(passwordEncoder.encode(signUpRequest.getPassword())).
                email(signUpRequest.getEmail()).
                build();

        userRepository.save(user); // 객체를 DB에 저장



        // 회원가입시 같이 입력받은 Category는 User에 넘겨 줄 수 없으니 Interest에 추가하기 위한 for
        for (Long catId : signUpRequest.getCategoryId()) {
            Category category = categoryRepository.findById(catId) // 카테고리 정보 가져오기
                    .orElseThrow(() -> new Exception("Category not found"));
            UserInterest ui = UserInterest.builder()
                    .user(user)
                    .category(category)
                    .build();
            userInterestRepository.save(ui);

        }
    }


    public LoginResponse logIn(LoginRequest loginRequest) throws Exception{
        User user = userRepository.findByEmail(loginRequest.getEmail()).
                orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new AuthenticationException("이메일 또는 비밀번호가 일치하지 않습니다") {
            };
        }

        String accessToken = jwtProvider.createAccessToken(user.getUserId(), user.getEmail());
        String refreshToken = jwtProvider.createRefreshToken();

        jwtProvider.saveRefreshToken(user.getEmail(), refreshToken);

        log.info("login successful {}", user.getUsername());

        return LoginResponse.builder()
                .userDto(UserDto.from(user))
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
