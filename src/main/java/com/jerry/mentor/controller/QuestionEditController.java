package com.jerry.mentor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jerry.mentor.entity.Question;
import com.jerry.mentor.service.QuestionService;

@Controller
@RequestMapping("/questionEdit")
public class QuestionEditController {

    @Autowired
    private QuestionService questionService;

    @GetMapping({ "/", "" })
    public String showQuestionEditPage(Model model) {

        model.addAttribute("questions", questionService.getAllQuestions());
        return "questionEdit";
    }

    @GetMapping({ "edit" })
    public String showQuestionEditPage(Integer questionid, Model model) {

        Question question = questionService.getQuestionById(questionid);

        model.addAttribute("selectedQuestion", question);
        model.addAttribute("questions", questionService.getAllQuestions());
        return "questionEdit";
    }

    @PostMapping("/save")
    public String saveQuestion(@ModelAttribute Question question) {
        questionService.saveOrUpdate(question);
        return "redirect:/questionEdit";
    }

    @PostMapping("/delete")
    public String deleteQuestion(Integer questionid) {
        questionService.deleteQuestionById(questionid);
        return "redirect:/questionEdit";
    }
}
