/*
This script creates the minimal standard spring security user tables, plus
a user_profile table for any extra user information.

The script is intended to be run manually. See inline comments for details.
*/


-- set to your schema if different

CREATE SCHEMA IF NOT EXISTS library2;

USE library2;

-- Uncomment to allow script to be rerun, drop tables in this order to avoid fk constraints

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

-- Uncomment to add test users (may conflict with migrated user w/ same username)

  INSERT INTO users (username, password)
  VALUES
    ('user@kgrid.org', 'user123'),
    ('admin@kgrid.org', 'admin123');

  INSERT INTO authorities (username, authority)
  VALUES
    ('admin@kgrid.org', 'ADMIN'),
    ('user@kgrid.org', 'USER');

  INSERT INTO user_profiles (username, first_name, last_name)
  VALUES
    ('user@kgrid.org', 'Ulrich', 'User'),
    ('admin@kgrid.org', 'Alvin', 'Admin');

  commit;

