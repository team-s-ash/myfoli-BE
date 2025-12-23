package com.slash.project.myfoli.domain.post.dto;

import com.slash.project.myfoli.domain.post.entity.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostCreateRequest {
    @NotBlank(message = "제목은 필수 입력값입니다.")
    @Size(max = 100, message = "제목은 100자 이하로 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    // 이미지 파일 목록 (업로드용)
    private List<MultipartFile> imageFiles = new ArrayList<>();
    
    // 이미지 URL 목록 (이미 업로드된 이미지 URL)
    private List<String> imageUrls = new ArrayList<>();

    public PostCreateRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
