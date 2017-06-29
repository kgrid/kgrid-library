#### Getting Started
## Setting up a Local Kgrid Library

### What you'll need

The [latest release of the Kgrid Library](https://github.com/kgrid/object-teller/releases/latest) (packaged as a WAR file). It runs on Mac OSx, recent versions of Windows, and most flavors of Linux.

Java 1.8 runtime version. ([Update to the latest Java runtime](https://www.java.com/en/).)

A local database. It's quick and easy to install [MySQL Community Server](https://dev.mysql.com/doc/mysql-getting-started/en/). See [Setting up the database](#setting-up-the-database) for more info.

A Fedora repository. This is a little involved, so it's easiest to use the [fcrepo-vagrant](https://github.com/fcrepo4-exts/fcrepo4-vagrant/releases) project from the Fedora folks (which in turn requires **Vagrant** and **VirtualBox**.) The KGrid library is designed to run with a plain vanilla deployment of the Fedora repository. The fedora endpoints use `localhost:8080` and `localhost:9080` but this can be changed by editing the configuration in the `Vagrantfile` found in the `fcrepo-vagrant` root directory.


### Setting up the database

Create a database and a user with privileges for that database.

```mysql
create database library; -- Create the new database
create user 'lib_user'@'localhost' identified by 'lib123'; -- Creates the user
grant all on library.* to 'lib_user'@'localhost'; -- Gives all the privileges to the new user on the newly created database
```

The only tables used by the Library are for user management:

```mysql
/*
This script creates the minimal standard spring security user tables, plus
a user_profile table for any extra user information.

The script is intended to be run manually. See inline comments for details.
*/


-- set to your schema if different

USE library;

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
  id         MEDIUMINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  username   VARCHAR(50)           NOT NULL,
  first_name VARCHAR(50)           NOT NULL,
  last_name  VARCHAR(50)           NOT NULL,
  CONSTRAINT fk_user_profiles_users
    FOREIGN KEY (username)
    REFERENCES users (username) 
    ON DELETE CASCADE ON UPDATE CASCADE 
);

-- Uncomment to add test users (may conflict with migrated user w/ same username)

  INSERT INTO users (username, password)
  VALUES
    ('user@umich.edu', 'user'),
    ('admin@umich.edu', 'admin');

  INSERT INTO authorities (username, authority)
  VALUES
    ('admin@kgrid.org', 'ADMIN'),
    ('user@kgrid.org', 'USER');

  INSERT INTO user_profiles (username, first_name, last_name)
  VALUES
    ('user@kgrid.org', 'Ulrich', 'User'),
    ('admin@kgrid.org', 'Alvin', 'Admin');

  commit;

```

And you need to tell Spring what datasource to use (in `application.properties`. See ()[Configuring application properties](#configuring-application-properties):

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/library
spring.datasource.username=lib_user
spring.datasource.password=lib123
```



### Configuring application properties










The ObjectTeller application uses a local MySQL database for managing user info and library connection info.

One way to get MySQL up and running is to download and install a recent version. You can download manually, or use homebrew on a Mac. (In the end this is what I chose.)

_Another possibility_ (more complicated): Install a virtual machine preconfigured w/ MySQL, using VirtualBox and/or vagrant. This probably requires some kind of SSH tunnelling/port forwarding to get it to work on `localhost:3306`. Here's a hint on setting it up with vagrant, https://gielberkers.com/create-mysql-ssh-tunnel-within-vagrant/. (I tried out the Scotch Box vagrant LAMP box.)


Object IDs:

If you are running a local instance of the ObjectTeller and pointing to a remote instance of a (Fedora) library, you need to make sure you don't have object id collisions. There is an initial object id set at the end of the db create script that needs to be in a range outside of the library's current range. Since the library started low, I set mine to 10,000, which worked fine.
