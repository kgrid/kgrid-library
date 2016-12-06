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
import { editTabNav } from './ot.js';
export default {
  name: 'app1',
  data: function () {
    return {
      showOverlay: { show: false },
      userModel: {user: {username: '', passwd: '', id: -1, first_name: '', last_name: '', role: ''}},  // eslint-disable-line
      currentOLView: 'login'
    };
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
  },
  components: {
    navbar,
    login,
    objeditor,
    objcreator
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
