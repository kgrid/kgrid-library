import Vue from 'vue'
import axios from 'axios'

export default {
  get (url, config) {
    return axios.get(url, config)
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  post (url, data,config) {
    return axios.post(url, data, config)
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  patch (url, data,config) {
    return axios.patch(url, data,config)
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  put (url, data,config) {
    return axios.put(url, data,config)
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  delete (url, config) {
    return axios.delete(url, config)
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  formpost (url, data){
    var formBody = [];
    for (var property in data) {
      var encodedKey = encodeURIComponent(property);
      var encodedValue = encodeURIComponent(data[property]);
      formBody.push(encodedKey + "=" + encodedValue);
    }
    formBody = formBody.join("&");
    return  axios({
      method: 'POST',
      url: url,
      headers: {'Content-Type': 'application/x-www-form-urlencoded',
      },
      data: formBody,
    }).then((response) => Promise.resolve(response))
    .catch((error) => Promise.reject(error))
  }
}
