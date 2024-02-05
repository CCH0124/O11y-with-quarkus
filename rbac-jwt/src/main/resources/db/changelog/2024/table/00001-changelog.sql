-- liquibase formatted sql
CREATE TABLE roles (
   id UUID DEFAULT uuid_generate_v4() NOT NULL,
   name erole,
   CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE users (
   id UUID DEFAULT uuid_generate_v4() NOT NULL,
   username VARCHAR(20) NOT NULL,
   email VARCHAR(50) NOT NULL,
   password VARCHAR(120) NOT NULL,
   birth_date timestamp(0) NOT NULL,
   created_time timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
   CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE user_roles (
   role_id UUID DEFAULT uuid_generate_v4() NOT NULL,
   user_id UUID DEFAULT uuid_generate_v4() NOT NULL,
   CONSTRAINT pk_user_roles PRIMARY KEY (role_id, user_id)
);

ALTER TABLE users ADD CONSTRAINT uc_74165e195b2f7b25de690d14a UNIQUE (email);

ALTER TABLE users ADD CONSTRAINT uc_77584fbe74cc86922be2a3560 UNIQUE (username);

ALTER TABLE user_roles ADD CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE user_roles ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id);