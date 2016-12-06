<template>
  <div>
    <navbar></navbar>
 	<router-view></router-view>
    <div v-bind:is='currentOLView'  v-if='showOverlay.show'></div>
  </div>
</template>
<script>
import navbar from './components/navbar.vue';
import login from './components/login';  // eslint-disable-line
import olnpane from './components/olnpane'; // eslint-disable-line
import objeditor from './components/objeditor'; 
import objcreator from './components/objcreator'; 
import eventBus from './components/eventBus.js';
import { editTabNav, getCurrentUser, overlayHeightResize, retrieveObject, retrieveObjectList, otScroll} from './ot.js';
import { objModel, editObjModel, sections, userModel } from './components/models.js'

export default {
  name: 'app1',
  data: function () {
    return {
      showOverlay: { show: false },
      userModel: {user: {username: '', passwd: '', id: -1, first_name: '', last_name: '', role: ''}},  // eslint-disable-line
      showSecOverlay:{show:false},
      koModel:objModel,
      currentOLView: 'login'
    };
  },
  beforeCreate:function(){
		var self=this;
		getCurrentUser(function(response) {
			if(response!="")
				$.extend(true, self.userModel.user, response);
		},function(response) {
			console.log(response);
		});
	},
  created: function () {
    var self = this  // eslint-disable-line
    eventBus.$on('openOverlay', function () {
      self.showOverlay.show = true;
      self.currentOLView = 'login';
      document.body.classList.toggle('noscroll', true);
    });
    eventBus.$on('hideOverlay', function (layerid) {
      switch (layerid) {
        case '0':
          self.showOverlay.show = false;
          document.body.classList.toggle('noscroll', false);
          break;
        case '1':
          self.showSecOverlay.show = false;
          break;
      }
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
	eventBus.$on('editObj', function(obj){
		console.log("Edit Obj Button Clicked...");
		self.currentOLView='objeditor';
		self.showOverlay.show=true;
		document.body.classList.toggle('noscroll', true);
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
		eventBus.$on("open", function(x){
			self.showOverlay.show=true;
			self.currentOLView='login';
			document.body.classList.toggle('noscroll', true);
		}); 
  },
  components: {
    navbar,
    login,
    objeditor,
    objcreator
  },
	computed:{
		isLoggedIn:function(){
			var loggedin =false;
			loggedin = (this.userModel.user.username!="");
			return loggedin;
		}
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
		}
	}
};
</script>
<style>
#app {
  font-family: 'Open Sans', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #777;
  margin-top: 0px;
}
</style>
