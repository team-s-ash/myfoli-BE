package com.slash.project.myfoli.domain.ai.service;

import com.slash.project.myfoli.domain.post.entity.Post;
import com.slash.project.myfoli.domain.post.repository.PostRepository;
import com.slash.project.myfoli.domain.user.entity.User;
import com.slash.project.myfoli.domain.user.repository.UserRepository;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AIChatService {
    private final OpenAiService openAiService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public String chat(String message, Long userId) {
        try {
            // 사용자 포트폴리오 정보 가져오기
            String portfolioContext = buildPortfolioContext(userId);
            
            // 바이브 코딩 스타일 시스템 프롬프트
            String enhancedSystemPrompt = buildEnhancedSystemPrompt(portfolioContext);
            
            // 메시지 리스트 생성
            List<ChatMessage> messages = new ArrayList<>();
            messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), enhancedSystemPrompt));
            messages.add(new ChatMessage(ChatMessageRole.USER.value(), message));
            
            // ChatCompletionRequest 생성
            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                    .model("gpt-3.5-turbo")
                    .messages(messages)
                    .maxTokens(1000)
                    .temperature(0.7)
                    .build();
            
            // OpenAI API 호출
            return openAiService.createChatCompletion(chatCompletionRequest)
                    .getChoices()
                    .get(0)
                    .getMessage()
                    .getContent();
                    
        } catch (Exception e) {
            return "죄송합니다. AI 서비스에 문제가 발생했습니다: " + e.getMessage();
        }
    }
    
    private String buildPortfolioContext(Long userId) {
        if (userId == null) {
            return "현재 로그인하지 않은 사용자입니다.";
        }
        
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return "사용자 정보를 찾을 수 없습니다.";
        }
        
        List<Post> userPosts = postRepository.findByUserUserId(userId);
        
        if (userPosts.isEmpty()) {
            return String.format("사용자: %s\n아직 작성된 포트폴리오가 없습니다.", user.getUsername());
        }
        
        StringBuilder context = new StringBuilder();
        context.append("=== 사용자 정보 ===\n");
        context.append(String.format("이름: %s\n", user.getUsername()));
        context.append(String.format("이메일: %s\n\n", user.getEmail()));
        context.append(String.format("=== 포트폴리오 목록 (총 %d개) ===\n\n", userPosts.size()));
        
        for (int i = 0; i < userPosts.size(); i++) {
            Post post = userPosts.get(i);
            context.append(String.format("[프로젝트 %d]\n", i + 1));
            context.append(String.format("제목: %s\n", post.getTitle()));
            context.append(String.format("내용: %s\n", post.getContent()));
            context.append(String.format("상태: %s\n", post.getStatus()));
            context.append(String.format("작성일: %s\n\n", post.getCreatedAt()));
        }
        
        return context.toString();
    }
    
    private String buildEnhancedSystemPrompt(String portfolioContext) {
        return String.format("""
                너는 MyFoli 플랫폼의 AI 코딩 멘토야. 친근하고 편안한 바이브로 대화하면서 개발자들에게 실질적인 도움을 주는 게 목표야.
                
                **너의 역할:**
                - 🎯 포트폴리오 기반 맞춤형 조언 제공
                - 💡 프로젝트 아이디어와 개선 방안 제안
                - 🔧 기술 스택 선택 및 코드 리뷰
                - 🚀 커리어 성장을 위한 가이드
                
                **대화 스타일:**
                - 반말 사용하되 존중하는 톤 유지
                - 이모지 적절히 활용해서 친근함 표현
                - 어려운 개념은 쉽게 풀어서 설명
                - 실용적이고 구체적인 예시 제공
                - 격려와 동기부여도 잊지 않기
                
                **사용자의 현재 포트폴리오:**
                %s
                
                사용자의 포트폴리오를 참고해서 그들의 관심사, 기술 수준, 프로젝트 방향성을 파악하고
                그에 맞는 맞춤형 조언을 해줘. 단순히 일반적인 답변이 아니라
                사용자의 상황에 딱 맞는 구체적이고 실용적인 답변을 제공해야 해!
                
                필요하면 코드 예시도 제공하고, 다음 단계로 무엇을 해야 할지 명확히 가이드해줘.
                개발자로서 성장할 수 있도록 영감과 동기부여도 함께 전달해! 🔥
                """, portfolioContext);
    }
}
