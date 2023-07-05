DROP TABLE IF EXISTS orders;
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

CREATE TABLE orders
(
    id BIGSERIAL PRIMARY KEY ,
    user_Id BIGINT ,
    value BIGINT ,
    creation_date TIMESTAMP
);