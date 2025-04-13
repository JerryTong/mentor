package com.jerry.mentor.service;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jerry.mentor.entity.Question;
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

        String prompt = promptQuestion.getAnswertext();
        prompt = prompt.replace("{{studentAnswer}}", studentAnswer);
        prompt = prompt.replace("{{questionText}}", question.getQuestionText());
        prompt = prompt.replace("{{expectedAnswerText}}", question.getExpectedAnswerText());

        return evaluateAnswer(prompt);
    }

    public EvaluationResult evaluateAnswer(String prompt) {
        GeminiResponse resultMap = geminiAIService.generateContent(prompt);
        if (resultMap == null) {
            logger.error("Failed to get response from Gemini AI service.");
            return null;
        }

        EvaluationResult result = new EvaluationResult();
        result.setCorrectness(resultMap.getCandidates().get(0).getContent().getParts().get(0).getText());

        Node document = parser.parse(result.getCorrectness());
        result.setCorrectnessHtml(renderer.render(document));

        return result;
    }
}
