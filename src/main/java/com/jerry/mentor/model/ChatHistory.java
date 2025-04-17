package com.jerry.mentor.model;

import lombok.Data;

@Data
public class ChatHistory {

    // 使用者回答的內容
    private String studentAnswer;
    // AI 回答的內容
    private String aiFeedback;
    // AI 回答的內容(html 格式)
    private String aiFeedbackHtml;

    private int score;
    private String feedback;
}
