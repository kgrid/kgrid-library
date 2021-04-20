

## Configuration
There are several settings that you can control on the Activator.

**Library Knowledge Object Shelf Location**

By default the activator will look for a _shelf_ in jar execution directory but the location the _shelf_ can be configured:

```bash
java -jar kgrid-library-#.#.#.jar  --kgrid.shelf.cdostore.url=filesystem:file:///data/myshelf

java -jar kgrid-library-#.#.#.jar  --kgrid.shelf.cdostore.url=filesystem:file:///c:/Users/me/myshelf
```

**Activator Cross-Origin Resource Sharing (CORS)**
The Activator by default allows all origins access to the api. You can tighten that access via the
cors.url parameter.

To change the origins allowed:

```java -jarkgrid-library-#.#.#.jar  --cors.url=https://myservice.com```


**Activator Server Port** 

To change the port:

```java -jar kgrid-library-#.#.#.jar --server.port=9090```


**Activator Server Path** 

By default the endpoints of the activator at the root of the activator server.  To change the server root path:

```java -jar kgrid-library-#.#.#.jar  --server.contextPath=/library```

## Debug Configuration

### `logging.level`

- Specify the logging level for a particular package
    - Highest level is `logging.level.root` which will affect all classes
    - A particular package can be specified by adding the package location to the end like so:
        ```bash
        logging.level.org.kgrid.adapter.proxy
        ```
    - Default value: `INFO`
    - Possible Values: `INFO, DEBUG, WARN, ERROR`
    - Command line:
    ```bash
    java -jar kgrid-activator-#.#.#.jar --logging.level.org.kgrid.classToLog=DEBUG
    ```
    - Environment Variable:
    ```bash
    export logging.level.org.kgrid.classToLog=DEBUG
    ```
    - Note: This also works with Spring classes, like RestTemplate, which will allow you to see more info about particular rest calls.
        - Example: `logging.level.org.springframework.web.client.RestTemplate=DEBUG`
  