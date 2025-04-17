package com.jerry.mentor.model;

import java.util.List;

import lombok.Data;

@Data
public class EvaluationRequest {

    private List<ChatHistory> chatHistory;

    private int questionId;

    private String studentAnswer;
}
