## Fedora Repository Tests

[Fedora Repository](https://wiki.duraspace.org/display/FF) in 
terms of [JSON LD](https://json-ld.org/) and our [KOIO](http://kgrid.org/koio) ontology.  


### Testing with Fedora Repository
We use lite [Fedora image](https://hub.docker.com/r/kgrid/fcrepo/) based on [Fedora Docker](https://hub.docker.com/r/yinlinchen/fcrepo4-docker/) 
which is part of [Fedora Labs](https://github.com/fcrepo4-labs). 
```
npm install

```

The _test-it_ starts up the fcreop docker container and executes the postman tests.  Once completed the 
fcrepo container is destroyed.  

#### Starting a Fedora Repository
You can run the fcrepo and keep it running via the ```npm start```  command.  
This starts up the fcrepo at http:n//localhost:8080/fcrepo/rest/.  


