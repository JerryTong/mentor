package com.jerry.mentor.entity;

import java.sql.Date;

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
    private int questionid;
    private String questiontext;
    private String answertext; // Optional: to store the answer text if needed
    private Date created_at; // Optional: to store the expected answer text if needed
    private Date updated_at; // Optional: to store the expected answer text if needed
}
