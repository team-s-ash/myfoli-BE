package com.slash.project.myfoli.domain.post.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    PRIVATE("비공개", "작성자만 볼 수 있음"),
    PUBLIC("공개", "모든 사람이 볼 수 있음");

    private final String displayName;
    private final String description;
}
