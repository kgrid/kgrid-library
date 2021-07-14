# KGrid Library
[![CircleCI](https://circleci.com/gh/kgrid/kgrid-library.svg?style=svg)](https://circleci.com/gh/kgrid/kgrid-library)
[![GitHub release](https://img.shields.io/github/release/kgrid/kgrid-library.svg)](https://github.com/kgrid/kgrid-library/releases/)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

### Prerequisites
For building and running the application you need:

- [Java 8 or higher](https://www.oracle.com/java/)
- [Maven 3](https://maven.apache.org)

### Clone
To get started you can simply clone this repository using git:
```
git clone https://github.com/kgrid/kgrid-library.git
cd kgrid-library
```

### Quick start
This quick start will run the activator and load two example knowledge objects for testing.  This objects are located
in the _shelf_ directory at the root of the project. By default application will start up and PORT 8080.
```
$ mvn clean package
$ java -jar applicatoin/target/kgrid-activator*.jar
```

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```
mvn clean spring-boot:run
```

Once Running access the [Activators Health Endpoint](http://localhost:8080/health).  All _statuses_ reported should be **UP**

```$xslt
"status": "UP",
    "shelf": {
        "status": "UP",
  
```

The default list of activators that the library can push objects to can be overridden by setting the environment variable `VUE_APP_KGRID_ACTIVATOR_URLS` with a semicolon-separated list of urls.

## Running the tests

#### Automated tests 
Unit and Integration tests can be executed via
```
mvn clean test
mvn clean verify
```

### Docker Image/Container
## [Pull from DockerHub](https://docs.docker.com/engine/reference/commandline/pull/)
  ```bash
  docker pull kgrid/kgrid-library
  ```
## [Running the Image](https://docs.docker.com/engine/reference/commandline/run)

- Running in a container mapped to port 8080 (default port for the library)

```bash
  docker run -p 8080:8080 --name library kgrid/kgrid-library
```

- [Mapped to a local shelf](https://docs.docker.com/engine/reference/commandline/run/#mount-volume--v---read-only)

```bash
  docker run -p 8080:8080 -v ${PWD}/shelf:/applications/shelf --name library -d kgrid/kgrid-library 
```

- Example:

```bash
  docker run -it --rm --network host -p 8080:8080 -v ${PWD}/shelf:/application/shelf --name library kgrid/kgrid-library:latest
```
- This example has a few things going on:
    - `--network host` [Running with a network bridge](https://docs.docker.com/engine/reference/commandline/run/#connect-a-container-to-a-network---network) (if your containerized activator needs to talk to the network, i.e. you're running an external runtime in another container)
    - `-it --rm` Running interactive and Removing the Container when stopped. can be found in the [options](https://docs.docker.com/engine/reference/commandline/run/#options)
   
- Once created, you can stop and start the container using `docker stop library` and `docker start library`.
#### Good to Know

1. View Container Logs  ```docker logs library```
1. Access container ```docker exec -it library sh```
