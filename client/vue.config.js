var path = require('path')
var library = process.env.LIBRARY || 'http://localhost:8080'
var splitRegExp = new RegExp(
        '^' +
            '(?:' +
            '([^:/?#.]+)' +                         // scheme - ignore special characters
                                                    // used by other URL parts such as :,
                                                    // ?, /, #, and .
            ':)?' +
            '(?://' +
            '(?:([^/?#]*)@)?' +                     // userInfo
            '([\\w\\d\\-\\u0100-\\uffff.%]*)' +     // domain - restrict to letters,
                                                    // digits, dashes, dots, percent
                                                    // escapes, and unicode characters.
            '(?::([0-9]+))?' +                      // port
            ')?' +
            '([^?#]+)?' +                           // path
            '(?:\\?([^#]*))?' +                     // query
            '(?:#(.*))?' +                          // fragment
            '$');
var split = library.match(splitRegExp)
var splitpath = split[5] || ''
var ppath = splitpath

var ops={
    target: library,
    secure: false,
    changeOrigin: true,
    onProxyRes(proxyRes, req, res) {
      // proxyRes.headers['Set-Cookie']=proxyRes.headers['set-cookie'];
      var existingCookies = proxyRes.headers['set-cookie'];
      var rewrittenCookies = [];
      if (existingCookies !== undefined) {
        if (!Array.isArray(existingCookies)) {
          existingCookies = [existingCookies];
        }
        for (let i = 0; i < existingCookies.length; i++) {
          var re = /ath=(.*);/;
          var match=existingCookies[i].match(re);
          if(match!=null){
              rewrittenCookies.push(existingCookies[i].replace(match[1], "/"));
          }else{
            rewrittenCookies.push(existingCookies[i]);
          }

        }
        proxyRes.headers['set-cookie'] = rewrittenCookies;
      }
      Object.keys(proxyRes.headers).forEach(function (key) {
        var newkey = key.replace(/((?:^|-)[a-z])/g, function(val) { return val.toUpperCase(); });
        // custom hack for X-Parse-Os-Version ==> X-Parse-OS-Version
        newkey = newkey.replace(/(-Os-)/g, function(val) { return val.toUpperCase(); });
        proxyRes.headers[newkey] = proxyRes.headers[key];

        res.append(newkey, proxyRes.headers[newkey]);
      });
    },
    onProxyReq(proxyReq, req, res) {
      Object.keys(req.headers).forEach(function (key) {
        proxyReq.setHeader(key, req.headers[key]);
      });
    }
  }

module.exports = {
  assetsDir: 'static',
  devServer: {
    proxy: {
      '/user':ops,
      '/login':ops,
      '/logout':ops,
      '/knowledgeObject':ops,
      '/info':ops,
      '/shelf':ops
    }
  }
}
