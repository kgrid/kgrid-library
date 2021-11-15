function normalizeUrl(url){
  return new URL(url).origin
}

module.exports = {
  normalizeUrl: normalizeUrl
}
