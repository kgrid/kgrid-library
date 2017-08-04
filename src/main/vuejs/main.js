import Vue from 'vue';
import VueRouter from 'vue-router';
import VeeValidate from 'vee-validate';
import Vuex from 'vuex';
import App from './App';
import store from './store';
import eventBus from './components/eventBus.js';
import { objModel} from './components/models.js'

require('es6-promise').polyfill();
// Bootstrap 4
require('jquery');
require('tether');
require('bootstrap');
require('lodash');

// debug mode
Vue.config.debug = false;

// install router
Vue.use(VueRouter);

// install Vee-Validate
Vue.use(VeeValidate);

//install vuex
Vue.use(Vuex);

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
                 { path : '/*', component : require('./components/notfound.vue')	},

                	    ];

const router = new VueRouter({
	routes : routes,
  history: true,
  hashbang : false,
});

Vue.directive(
  'click-outside', {
    bind: function(el, binding, vNode) {
    if (typeof binding.value !== 'function') {
        const compName = vNode.context.name
        let warn = `[Vue-click-outside:] provided expression '${binding.expression}' is not a function, but has to be`
        if (compName) { warn += `Found in component '${compName}'` }

        console.warn(warn)
      }
      // Define Handler and cache it on the element
      const bubble = binding.modifiers.bubble
      const handler = (e) => {
        if (bubble || (!el.contains(e.target) && el !== e.target)) {
          binding.value(e)
        }
      }
      el.__vueClickOutside__ = handler

      // add Event Listeners
      document.addEventListener('click', handler)
    },

    unbind: function(el, binding) {
      // Remove Event Listeners
      document.removeEventListener('click', el.__vueClickOutside__)
      el.__vueClickOutside__ = null

    }
  }
)

var vm = new Vue({
	router : router,
	el: '#app',
  store,
	data : {

	},
	components:{
		App: App,
	},
	created: function(){
		var self=this;
    console.log('Knowledge Grid Library Web Application');
    console.log('Build 20170804B');
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
	},

	}).$mount('#app');
