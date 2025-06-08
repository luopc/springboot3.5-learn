DROP TABLE if EXISTS Person;
CREATE TABLE Person
(
    id    BIGINT PRIMARY KEY AUTO_INCREMENT,
    name  VARCHAR(50),
    age   VARCHAR(50),
    email VARCHAR(240)
);
--  
-- CREATE TABLE User
-- (
--     id  BIGINT PRIMARY KEY AUTO_INCREMENT,
--     name VARCHAR(20) NULL,
--     age  INT         NULL
-- );
--
-- CREATE TABLE User1
-- (
--     id  BIGINT PRIMARY KEY AUTO_INCREMENT,
--     name VARCHAR(20) NOT NULL
-- );
--
-- CREATE TABLE User2
-- (
--     id  BIGINT PRIMARY KEY AUTO_INCREMENT,
--     age INT    NOT NULL
-- );