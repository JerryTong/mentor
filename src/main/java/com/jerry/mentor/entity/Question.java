package com.jerry.mentor.entity;

import java.sql.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * questionid serial4 NOT NULL,
 * questiontext varchar(500) NOT NULL,
 * answertext varchar(500) NOT NULL,
 * created_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
 * updated_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
 * CONSTRAINT question_pkey PRIMARY KEY (questionid)
 */
@Data
@TableName("dblqy.question")
public class Question {

    @TableId
    @TableField(value = "question_id")
    private int questionId;

    @TableField(value = "question_text")
    private String questionText;

    @TableField(value = "answer_text")
    private String answerText; // Optional: to store the answer text if needed

    @TableField(value = "created_at")
    private Date createdAt; // Optional: to store the expected answer text if needed

    @TableField(value = "updated_at")
    private Date updatedAt; // Optional: to store the expected answer text if needed
}
