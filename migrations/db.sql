CREATE TABLE chat
(
    id bigserial primary key
);
CREATE TABLE link
(
    id bigserial primary key,
    name text unique

);
CREATE TABLE chat_link
(
    chat_id bigint REFERENCES chat (id),
    link_id bigint REFERENCES link (id),
    CONSTRAINT chay_link_pkey PRIMARY KEY (chat_id, link_id)
);
