import Vue from 'vue';
import VueRouter from 'vue-router';
import VueResource from 'vue-resource';

require('es6-promise').polyfill();
//import { configRouter } from './config/routes';

// Bootstrap 4
require('bootstrap');

// debug mode
Vue.config.debug = false;

// install vue-resource
Vue.use(VueResource);

// install router
Vue.use(VueRouter);

// create router
const routes = [ 
                { path : '/', component : require('./components/home.vue')	}, 
                { path : '/about', name : 'object', component : require('./components/about.vue')} 
                ];

const router = new VueRouter({
	routes : routes,
  history: true,
  hashbang : false, 
});

const eventBus = new Vue();

var vm = new Vue({
	router : router,
	data : {
		currentOLView:'objeditor',
		koModel:objModel,
		showOverlay:{show:false},
		showSecOverlay:{show:false},
		userModel:userModel
	},
	components:{
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
		getCurrentUser(function(response) {
			if(response!="")
				$.extend(true, self.userModel.user, response);
		})
	},
	created: function(){
		var self=this;
		eventBus.$on('editObj', function(obj){
			self.currentOLView='objeditor';
			self.showOverlay.show=true;
			document.body.classList.toggle('noscroll', true);
		});
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
