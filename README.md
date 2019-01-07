# KGrid Library
[![CircleCI](https://circleci.com/gh/kgrid/kgrid-library.svg?style=svg)](https://circleci.com/gh/kgrid/kgrid-library)
[![GitHub release](https://img.shields.io/github/release/kgrid/kgrid-library.svg)](https://github.com/kgrid/kgrid-library/releases/)

More information on using Knowledge Objects in the Activator and integration with other systems 
can be found here: [Getting Started with the KGrid Library](http://kgrid.org/kgrid-library/).


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

## Running the tests

#### Automated tests 
Unit and Integration tests can be executed via
```
mvn clean test
mvn clean verify
```

## Publish Documentation

Running Local Dev Docs Publish
```
npm install
npm run docs:dev
```

Build dist directory ready for publish

```
npm run docs:build`
```

CircleCi publishes the documentation use [VuePress](https://vuepress.vuejs.org/) and 
the ```.circleci/vuepress_deploy.sh``` script.  The gh-pages branch is used for the publishing process and setup in the
GitHub repository's GitHub Pages.