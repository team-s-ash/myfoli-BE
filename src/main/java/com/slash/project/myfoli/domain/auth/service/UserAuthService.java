package com.slash.project.myfoli.domain.auth.service;

import com.slash.project.myfoli.domain.auth.presentation.dto.SignUpRequest;
import com.slash.project.myfoli.domain.category.entity.Category;
import com.slash.project.myfoli.domain.category.repository.CategoryRepository;
import com.slash.project.myfoli.domain.interest.entity.UserInterest;
import com.slash.project.myfoli.domain.interest.repository.UserInterestRepository;
import com.slash.project.myfoli.domain.user.entity.User;
import com.slash.project.myfoli.domain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


@Service
public class UserAuthService {

    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private UserInterestRepository userInterestRepository;
    private PasswordEncoder passwordEncoder;

    public void signUp(SignUpRequest signUpRequest) throws Exception{
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new Exception("Email already in use");
        }
        if (userRepository.findByUsername(signUpRequest.getUsername())){
            throw new Exception("Username already in use");
        }
        if (!passwordChecker(signUpRequest)) {
            throw new Exception("Password are not correct");
        }

        User user = User.builder().
                username(signUpRequest.getUsername()).
                password(passwordEncoder.encode(signUpRequest.getPassword())).
                email(signUpRequest.getEmail()).
                build();

        userRepository.save(user);

        for (Long catId : signUpRequest.getCategory_id()) {
            Category category = categoryRepository.findById(catId)
                    .orElseThrow(() -> new Exception("Category not found"));
            UserInterest ui = UserInterest.builder()
                    .user(user)
                    .category(category)
                    .build();
            userInterestRepository.save(ui);

        }
    }

    public boolean passwordChecker(SignUpRequest signUpRequest) {
        return signUpRequest.getPassword().equals(signUpRequest.getPassword_check());
    }

    public void logIn(String username, String password) throws Exception{

    }
}
