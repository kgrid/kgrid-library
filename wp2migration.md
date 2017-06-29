
# Webpack2 Migration

The front end of ObjectTeller uses Vue.js as the framework and Webpack2 as the bundling tool.

For dev,

The default proxy server will be

`http://localhost:8080/ObjectTeller`

You can set the process environment variable of `LIBRARY` to the context path where you deploy the whole package.

For example, in Windows:

```
set LIBRARY=http://localhost:8000/ObjectTeller
npm run dev
```
