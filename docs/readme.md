# Knowledge Grid - Kgrid Library

## Quick Start

Install the [fcrepo4 vagrant virtual machine](https://github.com/fcrepo4-exts/fcrepo4-vagrant) and get it running

### Download and run an executable binary or war file

Download the [latest release of the library](https://github.com/kgrid/object-teller/releases/latest) 

Launch the war (running on port 8080 by default, change to 8081 so as not to conflict with the fcrepo vagrant instance):

```bash
java -jar kgrid-library-0.4.war --server.port=8081
```

Go to the site in your browser at http://localhost:8081


### To build from source code

    git clone https://github.com/kgrid/kgrid-library.git
    
    cd kgrid-library
    
### Build and deploy the library

```bash
mvn clean package
./target/kgrid-library-0.4.0.war
```

### Default configuration 

```properties
library.name=Library(Dev)
 
### SPRING BOOT CONFIG ###
# set profile based on system environment variable ('env' or 'ENV')
# if it exists -> loads application-${env}.properties
# Setting 'spring.profiles.active' directly works, too
spring.profiles.active=${env:test}
 
# Disable JMX export of all endpoints, or set 'endpoints.jmx.unique-names=true'
# if deploying multiple instances of the library
endpoints.jmx.enabled=false
 
# turn off for local development
management.security.enabled=false
 
logging.level.org.springframework.security: INFO
logging.level.org.springframework.boot.actuate.audit.listener.AuditListener: DEBUG
 
### EZID ###
# test naan and shoulder by default
naan=99999
ezid.shoulder=fk4
 
# The test account at Purdue-hosted ezid instance
ezid.base.url=https://ezid.lib.purdue.edu/
ezid.username=apitest
ezid.password=apitest
 
### USER DATABASE  ###
#local dev db settings (h2):
flyway.locations=db/migration/common,db/migration/{vendor}
 
# flyway uses spring.datasource.* values by default, can be configured separately
# flyway user must have elevated privileges
#flyway.url=
#flyway.user=
#flyway.password=
 
spring.datasource.url=jdbc:h2:file:${library.home:~/library}/users
spring.datasource.username=sa
spring.datasource.password=
 
# MySQl user db setup (assumes library schema and root access) ###
 
#spring.datasource.url=jdbc:mysql://localhost:3306/library
#spring.datasource.username=root
#spring.datasource.password=root123
 

### FEDORA COMMONS ###
# for fcrepo-vagrant environment, update Vagrantfile to change port from 8080
# or run the library server on another port using '... --server.port=XXXX'
 
fcrepo.port=8080
fedora.fcrepoUrl=http://localhost:${fcrepo.port}/fcrepo/rest/
fedora.fcrepoUsername=fedoraAdmin
fedora.fcrepoPassword=secret3
 
fedora.fusekiUrl=http://localhost:${fcrepo.port}/fuseki/test/query
```

