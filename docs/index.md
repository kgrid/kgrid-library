---
layout: page
navtitle: Quick Start 
---
## Library Quick Start

These instructions will get the Kgrid Library running with sample set of Knowledge Objects.

### Prerequisites

For running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

### Running the Activator

Download the latest activator jar from GitHub [Latest Activator Release](https://github.com/kgrid/kgrid-library/releases/latest).

1. Download [kgrid-library-#.#.#.jar](https://github.com/kgrid/kgrid-library/releases/latest)  

Directory structure should look similar to the following

The library is executable jar and can be run from the command line.  Open a terminal window and navigate to the directory where the jar and shelf are located.  

Type in the following. 

```bash
 java -jar kgrid-library-#.#.#.jar 
```

By default the activator will run on port 8080. You can validate the activator is up and running using 
the [library health endpoint](http://localhost:8080/health).  The health of the Activator should display a status of **UP**.  

```yaml
{
   status: "UP",
   shelf: {
      status: "UP",
      kgrid.shelf.cdostore.url: "shelf"
   },
   activationService: {
      status: "UP",
      Knowledge Objects found: 1,
      Adapters loaded: [
        "JAVASCRIPT",
        "PROXY"
       ],
   EndPoints loaded: [
        "hello/world/v0.0.1/welcome"
   ]
   },
   diskSpace: {
      status: "UP",
      total: 499963170816,
      free: 415911948288,
      threshold: 10485760
   }
 }
 
```

## Adding the Hello World KO on the Library 

The Hello World is a very simple KO with a Javascript based service that takes in a name and displays 
 a _Welcome to the Knowledge Grid_ message. 
 


