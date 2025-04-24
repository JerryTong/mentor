package com.jerry.mentor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jerry.mentor.entity.Question;
import com.jerry.mentor.model.EvaluationResult;
import com.jerry.mentor.model.QuestionModel;
import com.jerry.mentor.service.GeminiEvaluationService;
import com.jerry.mentor.service.QuestionService;

@Controller
public class DemoController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private GeminiEvaluationService evaluationService;

    @GetMapping("/demo")
    public String showDemoPage(@RequestParam(required = false) Integer id, Model model) {
        // Add a sample question to the model
        QuestionModel question = (id == null) ? trasnsQuestionModel(questionService.getFirstQuestion())
                : trasnsQuestionModel(questionService.getQuestionById(id));

        List<QuestionModel> allQuestions = trasnsQuestionModel(questionService.getAllQuestions());

        model.addAttribute("question", question);
        model.addAttribute("allQuestions", allQuestions);
        return "demo";
    }

    @GetMapping("/select")
    public String selectQuestion(@RequestParam int id, Model model) {
        QuestionModel question = trasnsQuestionModel(questionService.getQuestionById(id));
        List<QuestionModel> allQuestions = trasnsQuestionModel(questionService.getAllQuestions());
        model.addAttribute("question", question);
        model.addAttribute("allQuestions", allQuestions);
        return "demo";
    }

    // @PostMapping("/evaluate")
    public String evaluate(@RequestParam int questionId,
            @RequestParam String studentAnswer,
            Model model) {

        QuestionModel question = trasnsQuestionModel(questionService.getQuestionById(questionId));
        List<QuestionModel> allQuestions = trasnsQuestionModel(questionService.getAllQuestions());

        EvaluationResult result = evaluationService.evaluateAnswer(studentAnswer, question, "gemini-2.0-flash");

        model.addAttribute("studentAnswer", studentAnswer);
        model.addAttribute("result", result);
        model.addAttribute("question", question);
        model.addAttribute("allQuestions", allQuestions);
        return "demo";
    }

    private List<QuestionModel> trasnsQuestionModel(List<Question> questions) {
        if (questions != null && !questions.isEmpty()) {
            return questions.stream().map(this::trasnsQuestionModel).toList();
        }
        // 如果問題列表為 null 或空，則返回 null 或者拋出異常，根據需求決定
        return null;
    }

    private QuestionModel trasnsQuestionModel(Question question) {
        if (question != null) {
            return QuestionModel.builder()
                    .id(question.getQuestionId())
                    .questionText(question.getQuestionText())
                    .expectedAnswerText(question.getAnswerText())
                    .build();
        }
        // 如果問題為 null，則返回 null 或者拋出異常，根據需求決定
        return null;
    }
}
