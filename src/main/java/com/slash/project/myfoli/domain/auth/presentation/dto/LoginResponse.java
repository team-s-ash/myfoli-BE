package com.slash.project.myfoli.domain.auth.presentation.dto;


import com.slash.project.myfoli.domain.user.presentation.dto.UserDto;
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
