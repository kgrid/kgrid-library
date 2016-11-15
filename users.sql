/*
This script creates the minimal standard spring security user tables, plus
a user_profile table for any extra user information.

The script migrates users in the existing user table (USER_DETAILS) to the new schema and
performs a simple test for migration success. There is an optional section that can
be configured to create test users.

The script DOES NOT remove the original data

The script is intended to be run manually. See inline comments for details.
*/


-- set to your schema if different

USE ObjectTellerDB;

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
);

CREATE TABLE user_profiles
(
  id         MEDIUMINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  username   VARCHAR(50)           NOT NULL,
  first_name VARCHAR(50)           NOT NULL,
  last_name  VARCHAR(50)           NOT NULL,
  CONSTRAINT fk_user_profiles_users
  FOREIGN KEY (username)
  REFERENCES users (username)
);

-- Migrate exist data from user_details

INSERT INTO users (username, password)
  SELECT
    username,
    passwd
  FROM USER_DETAILS;

INSERT INTO authorities (username, authority)
  SELECT
    username,
    role
  FROM USER_DETAILS;

INSERT INTO user_profiles (username, first_name, last_name)
  SELECT
    username,
    first_name,
    last_name
  FROM USER_DETAILS;

-- Create view like USER_DETAILS for testing

DROP VIEW IF EXISTS vw_user_details;

CREATE VIEW vw_user_details AS
  SELECT

    u.username,
    u.password  passwd,
    a.authority role,
    p.first_name,
    p.last_name
  FROM users u, authorities a, user_profiles p
  WHERE u.username = a.username AND u.username = p.username;

-- Compare the view and


SELECT
  username,
  passwd,
  role,
  first_name,
  last_name
FROM USER_DETAILS
WHERE username NOT IN (SELECT username
                       FROM vw_user_details)
UNION
SELECT
  username,
  passwd,
  role,
  first_name,
  last_name
FROM vw_user_details
WHERE username NOT IN (SELECT username
                       FROM USER_DETAILS);

-- Uncomment to add test users (may conflict with migrated user w/ same username)

/*
INSERT INTO users (username, password)
VALUES
  ('user', 'user'),
  ('admin', 'admin');

INSERT INTO authorities (username, authority)
VALUES
  ('admin', 'ADMIN'),
  ('user', 'USER');

INSERT INTO user_profiles (username, first_name, last_name)
VALUES
  ('user', 'fuser', 'luser'),
  ('admin', 'fadmin', 'ladmin');
*/


