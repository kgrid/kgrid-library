
## Library Quick Start

These instructions will get the Kgrid Library running with sample set of Knowledge Objects.

### Prerequisites

For running the application you need:

- [Java 8 or higher](https://www.oracle.com/java/)

### Running the Library

Download the latest library jar from GitHub [Latest Activator Release](https://github.com/kgrid/kgrid-library/releases/latest).

1. Create a _library_ directory
1. Download [kgrid-library-#.#.#.jar](https://github.com/kgrid/kgrid-library/releases/latest)  
1. Place the _kgrid-library-#.#.#.jar_ into the _library_ 
1. Create a directory named _shelf_ in the new _library_ directory 

Directory structure should look similar to the following

```text 
 ├── library
     └── shelf  
     └── kgrid-library-#.#.#.jar
```

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
   userDetailService: {
     status: "UP",
     number of users: 2
    }  ,
    ezidService: {
      status: "UP",
      ezid.base.url: "https://ezid.lib.purdue.edu/",
      ezid.mock: "false"
    },
    shelf: {
      status: "UP",
      kgrid.shelf.cdostore.url: "/Users/me/library/shelf"
    },
    diskSpace: {
      status: "UP",
      total: 402672611328,
      free: 269428576256,
      threshold: 10485760
    },
      db: {
      status: "UP",
      database: "H2",
      hello: 1
    }
}
 
```

Now simply navigate to the [KGrid Library](http://localhost:8080).

## Adding the Hello World KO on the Library 

The Library allows you to take deposit a Knowledge Object archive (zip file).  On the KGrid Library 
main page you will see a _Deposit Knowledge Object_. 

1. Download [hello-world.zip](https://github.com/kgrid-objects/example-projects/releases/latest)
1. Navigate to the [KGrid Library](http://localhost:8080) site.
1. Follow this screen flow
<ol type="A">
<li>
<div>
<a target="_blank" href="./assets/img/AddKOScreenShot1.png">
  <img src="./assets/img/AddKOScreenShot1.png" alt="" width=250>
</a>
<div>Click on <i>Deposit Knowledge Object</i> in the right side of the screen</div>
</div></li>
<li>
<div>
<a target="_blank" href="./assets/img/AddKOScreenShot2.png">
  <img src="./assets/img/AddKOScreenShot2.png" alt=""  width=250>
</a>
<div>Click in the grey box to bring up a file select window.</div>
</div></li>
<li>
<div>
<a target="_blank" href="./assets/img/AddKOScreenShot3.png">
  <img src="./assets/img/AddKOScreenShot3.png" alt=""  width=250>
</a>
<div>Select the hello-world.zip you just download<br>
Click <i>Deposit Object</i> in the lower right of the screen.</div>
</div></li>
<li>
<div>
<a target="_blank" href="./assets/img/AddKOScreenShot4.png">
  <img src="./assets/img/AddKOScreenShot4.png" alt=""  width=250>
</a>
<div> The file will be upload to the 
     library and you return to library main screen.</div>
</div></li>
</ol>
