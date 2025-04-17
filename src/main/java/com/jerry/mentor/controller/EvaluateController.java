package com.jerry.mentor.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jerry.mentor.entity.Question;
import com.jerry.mentor.model.ChatHistory;
import com.jerry.mentor.model.EvaluationRequest;
import com.jerry.mentor.model.EvaluationResult;
import com.jerry.mentor.service.GeminiEvaluationService;
import com.jerry.mentor.service.QuestionService;

@RestController
public class EvaluateController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private GeminiEvaluationService evaluationService;

    @GetMapping("/evaluation/prompt/{id}")
    public ResponseEntity<EvaluationResult> getQuestionPrompt(@PathVariable int id) {
        Question question = (id == 0) ? questionService.getFirstQuestion() : questionService.getQuestionById(id);

        if (question == null) {
            return ResponseEntity.notFound().build();
        }

        EvaluationResult result = evaluationService.evaluateQuestionPrompt(question);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/evaluation")
    public ResponseEntity<EvaluationResult> evaluate(@RequestBody EvaluationRequest request) {
        // Validate request
        if (request.getQuestionId() <= 0 || request.getStudentAnswer() == null
                || request.getStudentAnswer().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Question question = questionService.getQuestionById(request.getQuestionId());

        // 取最後五筆問答當作上下文
        List<ChatHistory> chatHishtory = request.getChatHistory();
        int size = chatHishtory.size();
        int startIndex = Math.max(0, size - 5); // 確保起始索引不小於 0

        List<ChatHistory> subList = new ArrayList<>();
        subList.add(request.getChatHistory().get(0));
        subList.addAll(chatHishtory.subList(startIndex, size));

        EvaluationResult result = evaluationService.evaluateAnswer(question, request.getStudentAnswer(),
                subList);
        if (result == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(result);
    }
}
