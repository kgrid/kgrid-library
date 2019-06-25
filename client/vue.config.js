// const path = require('path');

const library = process.env.LIBRARY || 'http://localhost:8080';
// const splitRegExp = new RegExp(
//   '^'
//             + '(?:'
//             + '([^:/?#.]+)' // scheme - ignore special characters
//   // used by other URL parts such as :,
//   // ?, /, #, and .
//             + ':)?'
//             + '(?://'
//             + '(?:([^/?#]*)@)?' // userInfo
//             + '([\\w\\d\\-\\u0100-\\uffff.%]*)' // domain - restrict to letters,
//   // digits, dashes, dots, percent
//   // escapes, and unicode characters.
//             + '(?::([0-9]+))?' // port
//             + ')?'
//             + '([^?#]+)?' // path
//             + '(?:\\?([^#]*))?' // query
//             + '(?:#(.*))?' // fragment
//             + '$',
// );
// const split = library.match(splitRegExp);
// const splitpath = split[5] || '';
// const ppath = splitpath;

const ops = {
  target: library,
  secure: false,
  changeOrigin: true,
  onProxyRes(proxyRes, req, res) {
    // proxyRes.headers['Set-Cookie']=proxyRes.headers['set-cookie'];
    let existingCookies = proxyRes.headers['set-cookie'];
    const rewrittenCookies = [];
    if (existingCookies !== undefined) {
      if (!Array.isArray(existingCookies)) {
        existingCookies = [existingCookies];
      }
      for (let i = 0; i < existingCookies.length; i += 1) {
        const re = /ath=(.*);/;
        const match = existingCookies[i].match(re);
        if (match != null) {
          rewrittenCookies.push(existingCookies[i].replace(match[1], '/'));
        } else {
          rewrittenCookies.push(existingCookies[i]);
        }
      }
      proxyRes.headers['set-cookie'] = rewrittenCookies;
    }
    Object.keys(proxyRes.headers).forEach((key) => {
      let newkey = key.replace(/((?:^|-)[a-z])/g, val => val.toUpperCase());
      // custom hack for X-Parse-Os-Version ==> X-Parse-OS-Version
      newkey = newkey.replace(/(-Os-)/g, val => val.toUpperCase());
      proxyRes.headers[newkey] = proxyRes.headers[key];
      res.append(newkey, proxyRes.headers[newkey]);
    });
  },
  onProxyReq(proxyReq, req) {
    Object.keys(req.headers).forEach((key) => {
      proxyReq.setHeader(key, req.headers[key]);
    });
  },
};

module.exports = {
  assetsDir: 'static',
  publicPath:'./',
  devServer: {
    proxy: {
      '/user': ops,
      '/login': ops,
      '/logout': ops,
      '/knowledgeObject': ops,
      '/info': ops,
      '/shelf': ops,
      '/kos':ops
    },
  },
  chainWebpack: (config) => {
    config.module.rules.delete('eslint');
  },
};
