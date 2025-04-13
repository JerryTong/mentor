-- dblqy.question definition

-- Drop table

-- DROP TABLE dblqy.question;

CREATE TABLE dblqy.question (
	questionid serial4 NOT NULL,
	questiontext varchar(500) NOT NULL,
	answertext varchar(500) NOT NULL,
	created_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	updated_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	CONSTRAINT question_pkey PRIMARY KEY (questionid)
);