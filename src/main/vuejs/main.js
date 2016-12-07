import Vue from 'vue';
import VueRouter from 'vue-router';
//import VueResource from 'vue-resource';
import App from './App';
import eventBus from './components/eventBus.js';
import { objModel, editObjModel, sections, userModel } from './components/models.js'

require('es6-promise').polyfill();
// Bootstrap 4
require('bootstrap');
require('jquery');
require('tether');
require('lodash');
// require('./vendor/jquery-ui.js');

import {getCurrentUser, overlayHeightResize, retrieveObject, retrieveObjectList, otScroll} from './ot.js';
import login from './components/login';  
import objeditor from './components/objeditor'; 
import objcreator from './components/objcreator'; 

// debug mode
Vue.config.debug = false;

// install vue-resource
//Vue.use(VueResource);

// install router
Vue.use(VueRouter);

// create router
const routes = [
                { path : '/', component : require('./components/home.vue')	},
                { path : '/object/:uri', name : 'object', component : require('./components/objdetail.vue'), data: function(){
                	   	console.log("current URI"+ this.$route.params.uri);
                	    }	} 
                	    ];

const router = new VueRouter({
	routes : routes,
  history: true,
  hashbang : false,
});

var vm = new Vue({
	router : router,
	el: '#app',
	data : {
		currentOLView:'objeditor',
		koModel:objModel,
		showOverlay:{show:false},
		showSecOverlay:{show:false},
		userModel:userModel
	},
	components:{
		App: App,
	},
	created: function(){
		var self=this;
		 eventBus.$on("return", function(){
			router.push({ path: '/' });
		 });
		 eventBus.$on("objectSelected", function(obj){
			router.push({ name:'object' ,params: { uri: obj.uri }});
		 });
	}
	}).$mount('#app');

