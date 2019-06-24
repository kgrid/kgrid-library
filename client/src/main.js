import '@babel/polyfill';
import Vue from 'vue';
import './plugins/vuetify';
import axios from 'axios';
import moment from 'moment';
import router from './router';
import store from './store';
import 'roboto-fontface/css/roboto/roboto-fontface.css';
import '@fortawesome/fontawesome-free/css/all.css';
import eventBus from './eventBus';
import 'prismjs/themes/prism.css';

import App from './App.vue';

require('lodash');
require('prismjs');
require('prismjs/components/prism-yaml');

Vue.config.productionTip = false;

Vue.prototype.$http = axios;
Vue.prototype.$moment = moment;
Vue.prototype.$eventBus = eventBus;
Vue.prototype.$DEBUG = true;

new Vue({
  router,
  store,
  created() {
    this.$eventBus.$on('objectSelected', (t) => {
      router.push({ name: 'object', params: { uri: t } });
    });
  },
  render: h => h(App),
}).$mount('#app');
