package com.jerry.mentor.model;

import java.util.List;

import lombok.Data;

@Data
public class GeminiContentModel {

    private List<Content> contents;

    @Data
    public static class Content {
        private List<Part> parts;

        private String role;
    }

    @Data
    public static class Part {
        private String text;
    }
}
