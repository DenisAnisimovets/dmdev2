--liquibase formatted sql

--changeset danis:1
DROP TABLE IF EXISTS good_in_bucket;
DROP TABLE IF EXISTS good_in_order;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS good;
DROP TABLE IF EXISTS users;

--changeset danis:2
CREATE TABLE users
(
    id bigserial PRIMARY KEY ,
    username VARCHAR(128) unique ,
    password VARCHAR(128) ,
    firstname VARCHAR(128),
    lastname VARCHAR(128),
    birth_date DATE,
    role VARCHAR(32)
);
--rollback DROP IF EXISTS TABLE users;

--changeset danis:3
CREATE TABLE good
(
    id BIGSERIAL PRIMARY KEY ,
    good_name VARCHAR(128) unique ,
    price INTEGER,
    quantity INTEGER,
    img VARCHAR(256)
);
--rollback DROP IF EXISTS TABLE good;

--changeset danis:4
CREATE TABLE orders
(
    id BIGSERIAL PRIMARY KEY ,
    user_id BIGINT ,
    sum BIGINT ,
    creation_date TIMESTAMP
);
--rollback DROP IF EXISTS TABLE orders;

--changeset danis:5
CREATE TABLE good_in_order
(
    id BIGSERIAL PRIMARY KEY ,
    orders_id BIGINT REFERENCES orders(id) ,
    good_id BIGINT REFERENCES good(id),
    quantity INTEGER ,
    price INTEGER ,
    creation_date TIMESTAMP,
    UNIQUE (orders_Id, good_Id)
);
--rollback DROP IF EXISTS TABLE good_in_order;

--changeset danis:6
CREATE TABLE good_in_bucket
(
    id BIGSERIAL PRIMARY KEY ,
    user_Id BIGINT REFERENCES users(id) ,
    good_Id BIGINT REFERENCES good(id) ,
    quantity INTEGER ,
    price INTEGER ,
    creation_date TIMESTAMP,
    UNIQUE (user_id, good_Id)
);
--rollback DROP IF EXISTS TABLE good_in_bucket;