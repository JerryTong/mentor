package com.jerry.mentor.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jerry.mentor.entity.Question;
import com.jerry.mentor.service.QuestionService;

/*
 * Controller class for handling question-related requests.
 * It provides endpoints to retrieve questions by ID and to get the first question.
 */
@RestController
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    /*
     * Endpoint to get the all question.
     * 
     * @return ResponseEntity containing the first question or 404 if not found.
     */
    @GetMapping("/question/all")
    public ResponseEntity<List<Question>> getAllQuestions() {
        List<Question> questions = questionService.getAllQuestions();
        if (questions == null || questions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(questions);
    }

    /*
     * Endpoint to get a question by ID.
     * 
     * @param id The ID of the question to retrieve.
     * 
     * @return ResponseEntity containing the question or 404 if not found.
     */
    @GetMapping("/question/{id}")
    public ResponseEntity<Question> getQuestion(@PathVariable int id) {
        Question question = (id == 0) ? questionService.getFirstQuestion() : questionService.getQuestionById(id);

        if (question == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(question);
    }

    @PostMapping("/question")
    public ResponseEntity<Question> postMethodName(@RequestBody Question question) {
        boolean isSave = questionService.saveOrUpdate(question);
        if (!isSave) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(question);
    }

    @PutMapping("/question/{id}")
    public ResponseEntity<Integer> putMethodName(@RequestBody Question question, @PathVariable int id) {
        question.setQuestionId(id);
        question.setUpdatedAt(new Date(System.currentTimeMillis()));
        boolean isSave = questionService.saveOrUpdate(question);
        if (!isSave) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(question.getQuestionId());
    }
}
