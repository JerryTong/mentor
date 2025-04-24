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
import com.jerry.mentor.model.GeminiContentModel;
import com.jerry.mentor.model.GeminiContentModel.Content;
import com.jerry.mentor.model.GeminiContentModel.Part;
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

    public EvaluationResult evaluateAnswer(String studentAnswer, QuestionModel question, String model) {
        Question promptQuestion = questionService.getById(2);

        String prompt = promptQuestion.getAnswerText();
        prompt = prompt.replace("{{studentAnswer}}", studentAnswer);
        prompt = prompt.replace("{{questionText}}", question.getQuestionText());
        prompt = prompt.replace("{{expectedAnswerText}}", question.getExpectedAnswerText());

        EvaluationResult result = evaluateAnswer(prompt, model);
        result.getChatHistory().get(0).setStudentAnswer(studentAnswer);

        return result;
    }

    public EvaluationResult evaluateQuestionPrompt(Question question) {
        Question promptQuestion = questionService.getById(2);

        String prompt = promptQuestion.getAnswerText();
        prompt = prompt.replace("{{expectedAnswerText}}", question.getAnswerText());

        return evaluateAnswer(prompt, "gemini-2.5-flash-preview-04-17");
    }

    public EvaluationResult evaluateAnswer(Question question, String studentAnswer, List<ChatHistory> chatHishtory,
            String model) {
        Question promptQuestion = questionService.getById(2);

        String prompt = promptQuestion.getAnswerText();
        prompt = prompt.replace("{{expectedAnswerText}}", question.getAnswerText());

        chatHishtory.get(0).setStudentAnswer(prompt);

        GeminiContentModel contentModel = new GeminiContentModel();
        contentModel.setContents(new ArrayList<>());
        for (ChatHistory chatHistory : chatHishtory) {
            Content userContent = new Content();
            userContent.setRole("user");

            Part userPart = new Part();
            userPart.setText(chatHistory.getStudentAnswer());
            userContent.setParts(new ArrayList<>());
            userContent.getParts().add(userPart);

            Content modelContent = new Content();
            modelContent.setRole("model");

            Part modelPart = new Part();
            modelPart.setText(chatHistory.getAiFeedback());
            modelContent.setParts(new ArrayList<>());
            modelContent.getParts().add(modelPart);

            contentModel.getContents().add(userContent);
            contentModel.getContents().add(modelContent);
        }

        Content userContent = new Content();
        userContent.setRole("user");

        Part userPart = new Part();
        userPart.setText(studentAnswer);
        userContent.setParts(new ArrayList<>());
        userContent.getParts().add(userPart);

        contentModel.getContents().add(userContent);

        EvaluationResult result = evaluateAnswer(contentModel, model);
        result.setPrompt(prompt);
        result.getChatHistory().get(0).setStudentAnswer(studentAnswer);

        List<ChatHistory> newChatHistory = new ArrayList<>();
        newChatHistory.addAll(chatHishtory);
        newChatHistory.add(result.getChatHistory().get(0));

        result.setChatHistory(newChatHistory);

        return result;
    }

    public EvaluationResult evaluateAnswer(GeminiContentModel contentModel, String model) {
        GeminiResponse resultMap = geminiAIService.generateContent(contentModel, model);

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

    public EvaluationResult evaluateAnswer(String prompt, String model) {
        GeminiResponse resultMap = geminiAIService.generateContent(prompt, model);
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
