--liquibase formatted sql

--changeset user:1
CREATE TABLE user_info (
  id    bigserial PRIMARY KEY,
  email varchar(255) NOT NULL
);

--changeset user:2
CREATE TABLE user_base (
  id             bigserial PRIMARY KEY,
  username       varchar(255) NOT NULL,
  password       varchar(512) NOT NULL,

  mail_activated boolean      NOT NULL,

  user_info_id   bigint
);

ALTER TABLE user_base
  ADD CONSTRAINT FK_user_base__user_info
FOREIGN KEY (user_info_id) REFERENCES user_info (id);


--changeset user:3
CREATE TABLE user_role (
  role_type    varchar(50) NOT NULL,
  user_base_id bigint      NOT NULL
);

ALTER TABLE user_role
  ADD CONSTRAINT PK_user_role
PRIMARY KEY (role_type, user_base_id);

ALTER TABLE user_role
  ADD CONSTRAINT FK_user_role__user_base
FOREIGN KEY (user_base_id) REFERENCES user_base (id);