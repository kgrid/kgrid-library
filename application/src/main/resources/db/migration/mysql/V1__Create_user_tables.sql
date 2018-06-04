/*
This script creates the minimal standard spring security user tables, plus
a user_profile table for any extra user information.

*/

-- Assume database/schema exists, and flyway user exists, with rights to that database

DROP TABLE IF EXISTS authorities, user_profiles, users;

-- Create user schema

CREATE TABLE users
(
  username VARCHAR(50) PRIMARY KEY NOT NULL,
  password VARCHAR(50)             NOT NULL,
  enabled  BOOLEAN                 NOT NULL DEFAULT TRUE
);

CREATE TABLE authorities
(
  username  VARCHAR(50) NOT NULL PRIMARY KEY,
  authority VARCHAR(50) NOT NULL,
  CONSTRAINT fk_authorities_users
    FOREIGN KEY (username)
    REFERENCES users (username)
    ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE user_profiles
(
  id         MEDIUMINT NOT NULL AUTO_INCREMENT,
  username   VARCHAR(50)           NOT NULL,
  first_name VARCHAR(50)           NOT NULL,
  last_name  VARCHAR(50)           NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_user_profiles_users
    FOREIGN KEY (username)
    REFERENCES users (username) 
    ON DELETE CASCADE ON UPDATE CASCADE
);

