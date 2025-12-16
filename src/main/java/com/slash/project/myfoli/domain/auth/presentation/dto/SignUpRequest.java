package com.slash.project.myfoli.domain.auth.presentation.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class SignUpRequest {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String email;

    private List<Long> categoryId = new ArrayList<>();
}
