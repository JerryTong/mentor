package com.jerry.mentor.model;

import java.util.List;

import lombok.Data;

@Data
public class EvaluationResult {
    private List<ChatHistory> chatHistory;

    private String prompt;
}
