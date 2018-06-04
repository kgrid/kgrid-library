-- add test users (may conflict with migrated user w/ same username)

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

