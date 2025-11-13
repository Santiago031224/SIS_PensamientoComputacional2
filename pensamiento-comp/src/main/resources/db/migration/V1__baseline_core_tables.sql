-- Baseline core tables aligned with JPA entity names for login
-- Users, Roles, User_Role join

CREATE TABLE users (
  id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name VARCHAR2(100) NOT NULL,
  last_name VARCHAR2(100) NOT NULL,
  email VARCHAR2(100) NOT NULL,
  profile_picture VARCHAR2(255),
  password VARCHAR2(100) NOT NULL,
  document_type VARCHAR2(40),
  document VARCHAR2(60),
  status VARCHAR2(20),
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  deleted_at TIMESTAMP,
  last_login TIMESTAMP
);

CREATE UNIQUE INDEX users_email_un ON users (email);

CREATE TABLE roles (
  id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name VARCHAR2(50) NOT NULL
);
CREATE UNIQUE INDEX roles_name_un ON roles (name);

CREATE TABLE user_role (
  user_id NUMBER NOT NULL,
  role_id NUMBER NOT NULL,
  CONSTRAINT user_role_pk PRIMARY KEY (user_id, role_id),
  CONSTRAINT user_role_user_fk FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT user_role_role_fk FOREIGN KEY (role_id) REFERENCES roles(id)
);
