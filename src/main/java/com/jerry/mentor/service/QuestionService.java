package com.jerry.mentor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerry.mentor.entity.Question;
import com.jerry.mentor.mapper.QuestionMapper;

@Service
public class QuestionService extends ServiceImpl<QuestionMapper, Question> {

        @Autowired
        QuestionMapper questionMapper;

        public Question getFirstQuestion() {
                QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
                queryWrapper.orderByAsc("questionid");
                queryWrapper.last("LIMIT 1");
                return questionMapper.selectOne(queryWrapper);
        }

        public Question getQuestionById(int id) {
                return questionMapper.selectById(id);
        }

        public List<Question> getAllQuestions() {
                QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
                queryWrapper.orderByAsc("questionid");
                return questionMapper.selectList(queryWrapper);
        }

        public void deleteQuestionById(Integer questionid) {
                questionMapper.deleteById(questionid);
        }
}
