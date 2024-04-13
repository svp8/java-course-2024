CREATE TABLE chat
(
    id         bigint primary key,
    created_at TIMESTAMP WITH TIME ZONE
);
CREATE TABLE link
(
    id              serial primary key,
    name            text unique,
    created_at      TIMESTAMP WITH TIME ZONE,
    last_updated_at TIMESTAMP WITH TIME ZONE

);
CREATE TABLE chat_link
(
    chat_id bigint REFERENCES chat (id),
    link_id int REFERENCES link (id),
    CONSTRAINT chat_link_pkey PRIMARY KEY (chat_id, link_id)
);
-- CREATE TABLE repository (
-- 	id int8 NOT NULL,
-- 	branch_count int4 NULL,
-- 	pull_count int4 NULL,
-- 	CONSTRAINT repository_pkey PRIMARY KEY (id)
-- );
CREATE TABLE pull_request
(
    id      int8 NOT NULL,
    title   text NOT NULL,
    link_id int  NOT NULL REFERENCES link (id)  ON DELETE CASCADE,
    CONSTRAINT pull_request_pkey PRIMARY KEY (id)
);
CREATE TABLE branch
(
    name    text   NOT NULL,
    link_id int    NOT NULL REFERENCES link (id)  ON DELETE CASCADE,
    CONSTRAINT branch_pkey PRIMARY KEY (name,link_id)
);
CREATE TABLE answer
(
    id         int8 NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE,
    link_id int  NOT NULL REFERENCES link (id)  ON DELETE CASCADE,
    CONSTRAINT answer_pkey PRIMARY KEY (id)
);
CREATE TABLE comment
(
    id            int8 NOT NULL,
    creation_date TIMESTAMP WITH TIME ZONE,
    link_id int  NOT NULL REFERENCES link (id)  ON DELETE CASCADE,
    CONSTRAINT comment_pkey PRIMARY KEY (id)
);
-- CREATE TABLE question
-- (
--     id            int8 NOT NULL,
--     comment_count int4 NULL,
--     answer_count  int4 NULL,
--     CONSTRAINT question_pkey PRIMARY KEY (id)
-- );
