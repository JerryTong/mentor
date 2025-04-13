package com.jerry.mentor.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jerry.mentor.entity.Question;

@Mapper
public interface QuestionMapper extends BaseMapper<Question> {

}
