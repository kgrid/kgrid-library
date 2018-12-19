<template>
  <div>
    <navbar></navbar>
 	  <router-view></router-view>
    <div v-bind:is='currentOLView'  v-bind='request' v-if='showOverlay.show'></div>
  </div>
</template>
<script>
import navbar from './components/navbar.vue';
import login from './components/login';
import confirmdialog from './components/confirmdialog'; // eslint-disable-line
import objcreator from './components/objcreator';
import objdeposit from './components/objdeposit';
import ioviewer from './components/ioviewer';
import codeviewer from './components/codeviewer';
import objact from './components/objact';
import libraryusers from './components/libraryusers.vue';
export default {
  name: 'app1',
  data: function () {
    return {
      showOverlay: { show: false },
      showSecOverlay:{show:false},
      currentOLView: 'login',
      request: {name:"",statement:"q",content:"",btnText:"OK"}
    };
  },
  created: function () {
    var self = this  // eslint-disable-line
    //Test code to set default user to bypass LOGIN
    // this.$store.commit('setuser',{username: 'Guest', password: 'none', first_name:'Guest', admin: true})
    this.$http.get("./static/json/metadataschema.json").then(response=> {
      self.$store.commit('setmetaschema',response.data);
    }).catch(e=>{
      console.log(e)
    })
    // will move to objdetail and construct the tree using model children array
    // this.$http.get("./static/json/kotree.json").then(response=> {
    //   self.$store.commit('setkottree',response.data);
    // }).catch(e=>{
    //   console.log(e)
    // })
    // =======================================================================
    this.$eventBus.$on('openLogin', function () {
      self.showOverlay.show = true;
      self.currentOLView = 'login';
      document.body.classList.toggle('noscroll', true);
    });
    this.$eventBus.$on('confirmRequest', function (data) {
      self.request=JSON.parse(JSON.stringify(data));
      console.log(self.request);
      self.showOverlay.show = true;
      self.currentOLView = 'confirmdialog';
    });
    this.$eventBus.$on('openUserManagement', function () {
        self.showOverlay.show = true;
        self.currentOLView = 'libraryusers';
        document.body.classList.toggle('noscroll', true);
      });
      this.$eventBus.$on('confirm', function (data) {
        console.log(data);
        self.showOverlay.show = false;
        document.body.classList.toggle('noscroll', false);
        });
    this.$eventBus.$on('hideOverlay', function (layerid) {
      switch (layerid) {
        case '0':
          self.showOverlay.show = false;
          document.body.classList.toggle('noscroll', false);
          break;
        case '1':
          self.showSecOverlay.show = false;
          break;
        case '9':
          self.showOverlay.show = false;
          document.body.classList.toggle('noscroll', false);
          break;
      }
    });
	this.$eventBus.$on('objAdded', function(obj){
		self.showOverlay.show=false;
		document.body.classList.toggle('noscroll', false);
    self.$router.go(self.$router.currentRoute)
	});
	this.$eventBus.$on('slideout', function(layerid){
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
	this.$eventBus.$on("addobj", function(s){
		self.currentOLView="objcreator";
    // self.currentOLView="objdeposit";

		self.showOverlay.show=true;
		document.body.classList.toggle('noscroll', true);
	});
  this.$eventBus.$on("viewio", function(s){
    self.currentOLView="ioviewer";
    self.showOverlay.show=true;
    document.body.classList.toggle('noscroll', true);
  });
  this.$eventBus.$on("objactivation", function(s){
    self.currentOLView="objact";
    self.showOverlay.show=true;
    document.body.classList.toggle('noscroll', true);
  });
  this.$eventBus.$on("viewcode", function(s){
    self.currentOLView="codeviewer";
    self.showOverlay.show=true;
    document.body.classList.toggle('noscroll', true);
  });
 this.$eventBus.$on("userloggedin",function(obj){
		 self.showOverlay.show=false;
		 document.body.classList.toggle('noscroll', false);
	 });
  },
  components: {
    navbar,
    login,
    objcreator,
    objdeposit,
    ioviewer,
    objact,
    codeviewer,
    libraryusers,
    confirmdialog
  },
	computed:{
    user:function(){
      return this.$store.getters.getuser
    }
	}
};
</script>
<style src='bootstrap/dist/css/bootstrap.css'></style>
<style>
@import '../static/css/Kgrid-default.css';
@import '../static/css/theme-kglibrary.css';
#app {
  font-family: 'Open Sans', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #777;
  margin-top: 0px;
}
</style>
