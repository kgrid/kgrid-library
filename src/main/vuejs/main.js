import Vue from 'vue';
import VueRouter from 'vue-router';
//import VueResource from 'vue-resource';
import App from './App';
import eventBus from './components/eventBus.js';

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

var objModel = { object : { metadata:{title:"",keywords:"",contributors:"",published:"",citations:[],license:{licenseName:"",licenseLink:""}}, payload:{functionName:"",engineType:"",content:""},inputMessage:"", outputMessage:"", uri:"",published:false,lastModified:0,createdOn:0} };
var editObjModel = { object : { metadata:{title:"Edit object",keywords:"",contributors:"",published:"",citations:[],license:{licenseName:"",licenseLink:""}}, payload:{functionName:"",engineType:"",content:""},inputMessage:"", outputMessage:"", uri:"ark"} };
var userModel= {user:{username:"",password:""}};
var sections = [{name:"metadata",id:"#metadata",label:"METADATA"},
                {name:"payload",id:"#payload",label:"PAYLOAD"},
                {name:"inputMessage",id:"#inputMessage", label:"INPUT"},
                {name:"outputMessage",id:"#outputMessage", label:"OUTPUT"},
                {name:"logData",id:"#logData", label:"LOG DATA"}];
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
		login : login,
		objeditor : objeditor,
		objcreator :objcreator
	},
	computed:{
		isLoggedIn:function(){
			var loggedin =false;
			loggedin = (this.userModel.user.username!="");
			return loggedin;
		}
	},
	updated:function(){
	},
	beforeCreate:function(){
		var self=this;
/*		getCurrentUser(function(response) {
			if(response!="")
				$.extend(true, self.userModel.user, response);
		},function(response) {
			console.log(response);
		});*/
	},
	created: function(){
		var self=this;
		eventBus.$on('objSaved', function(obj){
			self.updateObject(obj);
			self.showOverlay.show=false;
			document.body.classList.toggle('noscroll', false);
		});
		eventBus.$on('slideout', function(layerid){
			switch(layerid){
				case '0':
					self.showOverlay.show=false;
					document.body.classList.toggle('noscroll', false);
					break;
				case '1':
					self.showSecOverlay.show=false;
					break;
			}
		});
		eventBus.$on("objcreated",function(obj){
			$.extend(true, self.koModel.object, obj);
			$.extend(true, editObjModel.object, obj);
			self.currentOLView="objeditor";
			if(editObjModel.object.metadata.license==null){
				editObjModel.object.metadata.license={licenseName:"",licenseLink:""};
			}
		});
		eventBus.$on("addobj", function(s){
			self.currentOLView="objcreator";
			self.showOverlay.show=true;
			document.body.classList.toggle('noscroll', true);
		});
		 eventBus.$on("userloggedin",function(obj){
			 self.showOverlay.show=false;
			 document.body.classList.toggle('noscroll', false);
			 getCurrentUser(function(response) {
				if(response!="")
						$.extend(true, self.userModel.user, response);
			})
		 });
		 eventBus.$on("return", function(){
			router.push({ path: '/' });
		 });
		 eventBus.$on("objectSelected", function(obj){
				router.push({ name:'object' ,params: { uri: obj.uri }});
			 });
			 
		 
		 
			eventBus.$on("open", function(x){
				self.showOverlay.show=true;
				self.currentOLView='login';
				document.body.classList.toggle('noscroll', true);
			});
	},
	mounted:function(){
		overlayHeightResize();
	},
	methods:{
		createObj_click:function(){
			this.showOverlay.show=true;
			objURI="";
		},
		getObject:function(){
			return $.extend(true, {}, this.koModel.object);
		},
		updateObject:function(obj){
			$.extend(true, objModel.object, obj);
		},
	}}).$mount('#app');

