# kgrid-libraryfront

The project provides a front-end reference web app for Knowledge Grid Library. It works with Kgrid-library 2.0 Backend and still under development.

### Installation

To try it, clone this Repository.

`Node.js` is required.

Run `npm install`


### Start Library Backend

You need to build the latest kgrid library backend. (`https://github.com/kgrid/kgrid-library`)

Start the back end:

```
java -jar target\kgrid-library-0.2.4-SNAPSHOT.war --cors.url=http://localhost:3000 --shelf.location=shelf
```

### Start Library Front End app

Run `npm run dev`. A webpack dev-server will start at http://localhost:3000
