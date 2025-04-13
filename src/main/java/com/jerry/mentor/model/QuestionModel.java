package com.jerry.mentor.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionModel {
    private int id;
    private String questionText;
    // 學生回答
    private String answerText; // Optional: to store the answer text if needed
    // 期待的回答
    private String expectedAnswerText; // Optional: to store the expected answer text if needed
}
