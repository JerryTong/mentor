package com.jerry.mentor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerry.mentor.entity.Question;
import com.jerry.mentor.mapper.QuestionMapper;
import com.jerry.mentor.model.QuestionModel;

@Service
public class QuestionService extends ServiceImpl<QuestionMapper, Question> {

        @Autowired
        QuestionMapper questionMapper;

        public Question getFirstQuestion() {
                QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
                queryWrapper.orderByAsc("question_id");
                queryWrapper.last("LIMIT 1");
                return questionMapper.selectOne(queryWrapper);
        }

        public Question getQuestionById(int id) {
                return questionMapper.selectById(id);
        }

        public List<Question> getAllQuestions() {
                QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
                queryWrapper.orderByAsc("question_id");
                return questionMapper.selectList(queryWrapper);
        }

        public void deleteQuestionById(Integer questionid) {
                questionMapper.deleteById(questionid);
        }

        public List<QuestionModel> trasnsQuestionModel(List<Question> questions) {
                if (questions != null && !questions.isEmpty()) {
                        return questions.stream().map(this::trasnsQuestionModel).toList();
                }
                // 如果問題列表為 null 或空，則返回 null 或者拋出異常，根據需求決定
                return null;
        }

        public QuestionModel trasnsQuestionModel(Question question) {
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
