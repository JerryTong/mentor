package com.jerry.mentor.service;

import java.util.ArrayList;
import java.util.List;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jerry.mentor.entity.Question;
import com.jerry.mentor.model.ChatHistory;
import com.jerry.mentor.model.EvaluationResult;
import com.jerry.mentor.model.GeminiResponse;
import com.jerry.mentor.model.QuestionModel;

@Service
public class GeminiEvaluationService {

    private static final Logger logger = LoggerFactory.getLogger(GeminiEvaluationService.class);

    private final Parser parser = Parser.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().build();

    @Autowired
    GeminiAIService geminiAIService;

    @Autowired
    QuestionService questionService;

    public EvaluationResult evaluateAnswer(String studentAnswer, QuestionModel question) {
        Question promptQuestion = questionService.getById(2);

        String prompt = promptQuestion.getAnswerText();
        prompt = prompt.replace("{{studentAnswer}}", studentAnswer);
        prompt = prompt.replace("{{questionText}}", question.getQuestionText());
        prompt = prompt.replace("{{expectedAnswerText}}", question.getExpectedAnswerText());

        EvaluationResult result = evaluateAnswer(prompt);
        result.getChatHistory().get(0).setStudentAnswer(studentAnswer);

        return result;
    }

    public EvaluationResult evaluateQuestionPrompt(Question question) {
        Question promptQuestion = questionService.getById(2);

        String prompt = promptQuestion.getAnswerText();
        prompt = prompt.replace("{{expectedAnswerText}}", question.getAnswerText());

        return evaluateAnswer(prompt);
    }

    public EvaluationResult evaluateAnswer(Question question, String studentAnswer, List<ChatHistory> chatHishtory) {
        Question promptQuestion = questionService.getById(2);

        String prompt = promptQuestion.getAnswerText();
        prompt = prompt.replace("{{expectedAnswerText}}", question.getAnswerText());

        StringBuilder promptBuilder = new StringBuilder(prompt);
        for (ChatHistory chat : chatHishtory) {
            promptBuilder.append("\n <學生>").append(chat.getStudentAnswer()).append("\n <AI>：")
                    .append(chat.getAiFeedback());
        }
        promptBuilder.append("\n <學生>").append(studentAnswer);
        promptBuilder.append("\n 請根據學生的回答，提出下一個引導性問題。");
        prompt = promptBuilder.toString();

        EvaluationResult result = evaluateAnswer(prompt);
        result.setPrompt(prompt);
        result.getChatHistory().get(0).setStudentAnswer(studentAnswer);

        List<ChatHistory> chatHistory = new ArrayList<>();
        chatHishtory.addAll(chatHistory);
        chatHishtory.addAll(result.getChatHistory());
        result.setChatHistory(chatHishtory);

        return result;
    }

    public EvaluationResult evaluateAnswer(String prompt) {
        GeminiResponse resultMap = geminiAIService.generateContent(prompt);
        if (resultMap == null) {
            logger.error("Failed to get response from Gemini AI service.");
            return null;
        }

        EvaluationResult result = new EvaluationResult();
        ChatHistory chatHishtory = new ChatHistory();
        chatHishtory.setAiFeedback(resultMap.getCandidates().get(0).getContent().getParts().get(0).getText());

        Node document = parser.parse(chatHishtory.getAiFeedback());
        chatHishtory.setAiFeedbackHtml(renderer.render(document));

        result.setChatHistory(new ArrayList<>());
        result.getChatHistory().add(chatHishtory);
        return result;
    }
}
