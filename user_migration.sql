/*
The script migrates users in the existing user table (USER_DETAILS) to the new schema and
performs a simple test for migration success. There is an optional section that can
be configured to create test users.

The script DOES NOT remove the original data

The script is intended to be run manually. See inline comments for details.
*/

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

