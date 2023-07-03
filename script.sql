DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS orders;

CREATE TABLE users
(
    id bigserial PRIMARY KEY ,
    username VARCHAR(128) unique ,
    firstname VARCHAR(128),
    lastname VARCHAR(128),
    birth_date DATE,
    role VARCHAR(32)
);

CREATE TABLE orders
(
    id bigserial PRIMARY KEY ,
    user_Id bigint ,
    value bigint ,
    creation_date DATE
);