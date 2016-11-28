import Vue from 'vue';
import app from './app';

/* eslint-disable no-new */
var eventBus = new Vue ({}) // eslint-disable-line
var userModel = {user: {username: '', passwd: '', id: -1, first_name: '', last_name: '', role: ''}} // eslint-disable-line
var vm = new Vue({ // eslint-disable-line
  el: '#app',
  template: '<App/>',
  components: { app }
});
