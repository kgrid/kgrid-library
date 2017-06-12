import Vue from 'vue';
import VueRouter from 'vue-router';
import VeeValidate from 'vee-validate';


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


//import { overlayHeightResize, retrieveObject, retrieveObjectList, otScroll} from './ot.js';
//import login from './components/login';  
//import objeditor from './components/objeditor'; 
//import objcreator from './components/objcreator'; 

// debug mode
Vue.config.debug = false;

// install vue-resource
//Vue.use(VueResource);

// install router
Vue.use(VueRouter);

// install Vee-Validate
Vue.use(VeeValidate);

// create router
const routes = [
                { path : '/', component : require('./components/home.vue')	},
                { path: '/about', component: require('./components/about.vue') },
                { path: '/faq', component: require('./components/faq.vue') },
                { path: '/contactus', component: require('./components/contactus.vue') },
                { path : '/object/:uri', name : 'object', component : require('./components/objdetail.vue'), data: function(){
                	   	console.log("current URI"+ this.$route.params.uri);
                	    }	} ,
                {path:'/soon', component: require('./components/comingsoon.vue')},
                 { path : '*', component : require('./components/notfound.vue')	},
                        
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
		eventBus.$on('404', function(){
			router.push({path:'*'});
		});
		eventBus.$on('soon', function(){
			router.push({path:'/soon'});
		});
		
		 eventBus.$on("return", function(){
			router.push({ path: '/' });
		 });
		 eventBus.$on("objectSelected", function(obj){
			router.push({ name:'object' ,params: { uri: obj.uri }});
		 });
		 eventBus.$on('objSaved', function(obj){
				self.updateObject(obj);
		});
		 eventBus.$on("objDeleted", function(obj){
				router.push({ path: '/' });
			 });
		 
	},
	methods: {
		updateObject:function(obj){
			$.extend(true, objModel.object, obj);
		},
	}
	}).$mount('#app');

