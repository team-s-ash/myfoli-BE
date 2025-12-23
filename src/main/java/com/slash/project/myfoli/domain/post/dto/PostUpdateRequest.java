package com.slash.project.myfoli.domain.post.dto;

import com.slash.project.myfoli.domain.post.entity.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class PostUpdateRequest {
    @NotBlank(message = "제목은 필수 입력값입니다.")
    @Size(max = 100, message = "제목은 100자 이하로 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "공개 여부를 선택해주세요.")
    private Status status;

    // 삭제할 이미지 URL 목록
    private List<String> deleteImageUrls = new ArrayList<>();
    
    // 새로 추가할 이미지 파일
    private List<MultipartFile> newImageFiles = new ArrayList<>();
    
    // 기존 이미지 URL 유지용
    private List<String> existingImageUrls = new ArrayList<>();
}
