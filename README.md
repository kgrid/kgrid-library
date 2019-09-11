# KGrid Library
[![CircleCI](https://circleci.com/gh/kgrid/kgrid-library.svg?style=svg)](https://circleci.com/gh/kgrid/kgrid-library)
[![GitHub release](https://img.shields.io/github/release/kgrid/kgrid-library.svg)](https://github.com/kgrid/kgrid-library/releases/)

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

### Docker Image/Container


#### Build Image
We are using [Spotify's Dockerfile Maven plug](https://github.com/spotify/dockerfile-maven) for docker image build and push.  
to create a docker image of the current activator project simply run the dockerfile:build after s build.  

```
mvn clean package
mvn dockerfile:build -pl application
```

After this run you will have a docker image 
```
~/kgrid-library $ docker images
REPOSITORY                  TAG                   IMAGE ID            CREATED             SIZE
kgrid/library               latest                fbe2de94cfa9        3 minutes ago       149MB

```
##### Running the Library 

```
docker run -p 8080:8080 --name library kgrid/library
```

Mapped to local shelf and running in the background

```
docker run -p 8080:8080 -v /mydirectory/shelf:/home/kgrid/shelf --name library -d  kgrid/library 
```

##### Stop and Start Actvator

```
docker stop library
```

```
docker start library
```


#### Good to Know

1. View Container Logs  ```docker logs library```
1. Access container ```docker exec -it library sh```

### Push New Image

Library images are stored on [DockerHub](https://cloud.docker.com/u/kgrid/repository/docker/kgrid/library) 

``
`mvn dockerfile:push -Ddockerfile.tag=1.0.4-rc1 -s /Users/me/.m2/my_settings.xml 
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
