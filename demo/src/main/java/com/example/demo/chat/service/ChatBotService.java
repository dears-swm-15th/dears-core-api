package com.example.demo.chat.service;

import com.example.demo.chat.domain.Question;
import com.example.demo.chat.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatBotService {

    private final ChatClient chatClient;

    private final QuestionRepository questionRepository;

    private static final String PERSONA_PROMPTING = "넌 한국의 웨딩플래너 전문가야. 질문자의 의도를 분석해서 제안하는 답변을 해줘.";
    private static final String CHAIN_OF_THOUGHT = "우선 지금까지 학습된 커뮤니티 질의응답 데이터를 바탕으로 비슷한 질문이 있는지 찾아보고, 댓글 중 적절한 답변을 정리해서 추려줘.";
    private static final String RULE_BASED_PROMPTING = "비속어는 무조건 필터링 혹은 순화해서 표현해야 해. 웨딩과 너무 연관 없는 질문은 '웨딩에 대한 질문이 아닌 것 같아요. 다른 질문이 있으신가요?'와 같은 답변으로 대화를 이어가야 해.";
    private static final String ADDITIONAL_INFORMATION = "질문자가 원하는 답변을 찾지 못했을 경우에는 '질문을 이해하지 못 했어요. 다시 질문해주세요.'라고 말해줘." +
            "혹은 사용자가 '고마워요' 등 대화를 종료하는 말을 하면, '도움이 필요할 때 언제든지 말씀해주세요!' 와 같은 답변으로 대화를 자연스럽게 끝맺어줘.";

    public String getAnswer(String message) {
        String answer = chatClient.prompt()
                .system(
                        PERSONA_PROMPTING + "\n" +
                                CHAIN_OF_THOUGHT + "\n" +
                                RULE_BASED_PROMPTING + "\n" +
                                ADDITIONAL_INFORMATION
                )
                .user(message)
                .call()
                .content();

        Question question = Question.builder()
                .question(message)
                .answer(answer)
                .build();

        questionRepository.save(question);

        return answer;
    }
}
