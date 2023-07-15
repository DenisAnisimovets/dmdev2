DROP TABLE IF EXISTS good_in_bucket;
DROP TABLE IF EXISTS good_in_order;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS good;
DROP TABLE IF EXISTS users;


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

CREATE TABLE good
(
    id BIGSERIAL PRIMARY KEY ,
    good_name VARCHAR(128) unique ,
    price INTEGER,
    quantity INTEGER,
    img VARCHAR(256)
);

CREATE TABLE orders
(
    id BIGSERIAL PRIMARY KEY ,
    user_id BIGINT ,
    sum INTEGER ,
    creation_date TIMESTAMP
);

CREATE TABLE good_in_order
(
    id BIGSERIAL PRIMARY KEY ,
    order_id BIGINT REFERENCES orders(id) ,
    good_id BIGINT REFERENCES good(id),
    quantity INTEGER ,
    price INTEGER ,
    creation_date TIMESTAMP,
    UNIQUE (order_Id, good_Id)
);

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