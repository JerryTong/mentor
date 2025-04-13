package com.jerry.mentor.model;

import lombok.Data;

@Data
public class EvaluationResult {
    private String correctness;
    private String correctnessHtml;
    private String completeness;
    private String aiStyleDetected;
    private int score;
    private String feedback;
}
