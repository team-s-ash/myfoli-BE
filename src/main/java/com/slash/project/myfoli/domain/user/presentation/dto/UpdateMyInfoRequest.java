package com.slash.project.myfoli.domain.user.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 내 정보 수정을 위한 DTO
@Getter
@NoArgsConstructor
public class UpdateMyInfoRequest {
    private String username;
}
