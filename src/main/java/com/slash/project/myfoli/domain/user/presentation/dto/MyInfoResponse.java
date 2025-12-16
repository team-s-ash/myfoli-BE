package com.slash.project.myfoli.domain.user.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

// 내 정보 조회를 위한 DTO
@Getter
@Builder
@AllArgsConstructor
public class MyInfoResponse {
    // 사용자 이름
    private String username;
    // 사용자 이메일
    private String email;
    // 사용자가 작성한 게시물 수
    private long postCount;
    // 팔로워 수
    private long followerCount;
    // 팔로잉 수
    private long followingCount;
}
