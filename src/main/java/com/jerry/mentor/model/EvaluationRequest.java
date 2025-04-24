package com.jerry.mentor.model;

import java.util.List;

import lombok.Data;

@Data
public class EvaluationRequest {

    private List<ChatHistory> chatHistory;

    private int questionId;

    private String studentAnswer;

    private String model = "gemini-2.0-flash"; // 預設模型名稱
}
