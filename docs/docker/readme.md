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
