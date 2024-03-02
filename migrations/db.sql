CREATE TABLE chat
(
    id bigint primary key,
    created_at timestamp
);
CREATE TABLE link
(
    id serial primary key,
    name text unique,
    created_at timestamp,
    last_updated_at timestamp

);
CREATE TABLE chat_link
(
    chat_id bigint REFERENCES chat (id),
    link_id int REFERENCES link (id),
    CONSTRAINT chay_link_pkey PRIMARY KEY (chat_id, link_id)
);
CREATE TABLE repository (
	id int8 NOT NULL,
	branch_count int4 NULL,
	pull_count int4 NULL,
	CONSTRAINT repository_pkey PRIMARY KEY (id)
);
CREATE TABLE question (
	id int8 NOT NULL,
	comment_count int4 NULL,
	answer_count int4 NULL,
	CONSTRAINT question_pkey PRIMARY KEY (id)
);
