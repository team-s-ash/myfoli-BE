package com.slash.project.myfoli.domain.auth.presentation.dto;


import com.slash.project.myfoli.domain.user.dto.UserDto;
import com.slash.project.myfoli.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

    @NotNull
    private UserDto userDto;
    @NotBlank
    private String refreshToken;
    @NotBlank
    private String accessToken;
}
