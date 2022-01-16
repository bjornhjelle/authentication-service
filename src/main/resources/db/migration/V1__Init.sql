--create schema if not exists authdb;
-- Other script tags

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users
(
    id          UUID    NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    first_name  varchar NULL,
    last_name   varchar NULL,
    email       varchar NOT NULL,
    phone       varchar NOT NULL,
    nick_name   varchar NULL,
    password    varchar NOT NULL,
    user_status varchar NOT NULL,
    role varchar(16) NOT NULL,
    created_at timestamp with time zone NOT NULL default (now()),
    updated_at timestamp with time zone NOT NULL default (now()),
    UNIQUE (email),
    UNIQUE (phone)
);