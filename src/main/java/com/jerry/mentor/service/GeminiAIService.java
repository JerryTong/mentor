package com.jerry.mentor.service;

import java.util.List;
import java.util.Map;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jerry.mentor.model.GeminiContentModel;
import com.jerry.mentor.model.GeminiResponse;

@Service
public class GeminiAIService {

    private final RestTemplate restTemplate;

    private String googleGeminiApiKey = "AIzaSyA8mJTIqO9J3Glq0hKuQELAWSNFsngPBnY";

    // https://generativelanguage.googleapis.com/v1beta/models?key=AIzaSyA8mJTIqO9J3Glq0hKuQELAWSNFsngPBnY
    // private final String geminiApiUrl =
    // "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-preview-04-17:generateContent?key="
    // + googleGeminiApiKey;

    private final String geminiApiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key="
            + googleGeminiApiKey;

    public GeminiAIService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public GeminiResponse generateContent(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, List<Map<String, List<Map<String, String>>>>> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", prompt)))));

        HttpEntity<Map<String, List<Map<String, List<Map<String, String>>>>>> requestEntity = new HttpEntity<>(
                requestBody, headers);

        try {
            ResponseEntity<GeminiResponse> responseEntity = restTemplate.exchange(
                    geminiApiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    GeminiResponse.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            System.err.println("Error calling Google Gemini API: " + e.getMessage());
            return null; // 或者拋出異常
        }
    }

    public GeminiResponse generateContent(GeminiContentModel contentModel, String model) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String geminiApiUrl = "https://generativelanguage.googleapis.com/v1beta/models/" + model
                + ":generateContent?key=" + googleGeminiApiKey;

        HttpEntity<GeminiContentModel> requestEntity = new HttpEntity<>(contentModel, headers);

        try {
            ResponseEntity<GeminiResponse> responseEntity = restTemplate.exchange(
                    geminiApiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    GeminiResponse.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            System.err.println("Error calling Google Gemini API: " + e.getMessage());
            return null; // 或者拋出異常
        }
    }

    public GeminiResponse generateContent(String prompt, String model) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String geminiApiUrl = "https://generativelanguage.googleapis.com/v1beta/models/" + model
                + ":generateContent?key=" + googleGeminiApiKey;

        GeminiContentModel requestBody = new GeminiContentModel();
        GeminiContentModel.Content content = new GeminiContentModel.Content();
        GeminiContentModel.Part part = new GeminiContentModel.Part();
        part.setText(prompt);
        content.setParts(List.of(part));
        requestBody.setContents(List.of(content));

        HttpEntity<GeminiContentModel> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<GeminiResponse> responseEntity = restTemplate.exchange(
                    geminiApiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    GeminiResponse.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            System.err.println("Error calling Google Gemini API: " + e.getMessage());
            return null; // 或者拋出異常
        }
    }
}
