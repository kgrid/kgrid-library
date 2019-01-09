<template>
  <v-app>
    <v-toolbar app>
      <v-toolbar-title>
        <router-link to='/'>
        <svg id="Layer_1" width="55" height="55" data-name="Layer 1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 40.32 27.4"><defs><svg:style>.cls-1{fill:#0075bc;}</svg:style></defs><title>Artboard 3</title><path class="cls-1" d="M8.54,11l-3,2a.45.45,0,0,0,0,.74l5.66,3.78a.57.57,0,0,1,0,1L9.42,19.78a.81.81,0,0,1-.91,0L.22,14.14a.49.49,0,0,1,0-.82L8.51,7.68a.81.81,0,0,1,.91,0l5.74,3.91a.81.81,0,0,1,.35.71L15.43,14a.59.59,0,0,1-.92.46L9.44,11A.81.81,0,0,0,8.54,11Z"/><path class="cls-1" d="M29,21.66,20.8,27.21a1.08,1.08,0,0,1-1.22,0l-8.16-5.55a.48.48,0,0,1,0-.79l1.89-1.29a.93.93,0,0,1,1,0l5.34,3.56a.81.81,0,0,0,.89,0l3-2a.45.45,0,0,0,0-.75l-3.45-2.31a.81.81,0,0,1-.36-.7l0-1.22a.68.68,0,0,1,.72-.66l.44,0a.44.44,0,0,1,.22.08L29,20.87A.48.48,0,0,1,29,21.66Z"/><path class="cls-1" d="M20.27,11.17l.3-7.23a.51.51,0,0,0-.8-.45L15.84,6.17a.81.81,0,0,0-.35.63l-.07,1.37a.63.63,0,0,1-1,.49L11.38,6.56a.45.45,0,0,1,0-.74L19.73.14a.81.81,0,0,1,.91,0l8.29,5.64a.49.49,0,0,1,0,.82l-7.59,5.17A.69.69,0,0,1,20.27,11.17Z"/><path class="cls-1" d="M40.07,14.2l-8.21,5.59a.81.81,0,0,1-.91,0l-3.58-2.44a.62.62,0,0,1,.38-1.13l3.15.15a.81.81,0,0,0,.48-.13l3.56-2.31a.45.45,0,0,0-.22-.82l-7.13-.43-1.81-.11a.63.63,0,0,1-.32-1.16L31,7.66a.77.77,0,0,1,.87,0l8.23,5.6A.56.56,0,0,1,40.07,14.2Z"/></svg>
        <span class='toolbar_title_text'>Library</span>
        </router-link>
      </v-toolbar-title>
      <router-link class='toolbar_link' to='/about'>About</router-link>
      <router-link class='toolbar_link' to='/faq'>FAQ</router-link>
      <router-link class='toolbar_link' to='/contactus'>Contact Us</router-link>

      <v-spacer></v-spacer>
      <v-btn
        flat
        outline
        color='#0075bc'
        v-on:click='login_click'
        disabled
      >
      Sign In
      </v-btn>
    </v-toolbar>

    <v-content>
      <router-view/>
      <div v-bind:is='currentOLView'  v-if='showOverlay.show'></div>
    </v-content>
  </v-app>
</template>

<script>
import login from './components/login';
import ioviewer from './components/ioviewer';
import objcreator from './components/objcreator';
import objact from './components/objact';
export default {
  name: 'App',
  data() {
    return {
      showOverlay: { show: false },
      showSecOverlay:{show:false},
      currentOLView: 'login',
      request: {name:"",statement:"q",content:"",btnText:"OK"}
    };
  },
  created: function(){
    var self = this
    this.$http.get("./static/json/metadataschema.json").then(response=> {
      self.$store.commit('setmetaschema',response.data);
    }).catch(e=>{
      console.log(e)
    })
    this.$eventBus.$on("viewio", function(s){
      self.currentOLView="ioviewer";
      self.showOverlay.show=true;
      document.body.classList.toggle('noscroll', true);
    });
    this.$eventBus.$on("addobj", function(s){
      self.currentOLView="objcreator";
      self.showOverlay.show=true;
      document.body.classList.toggle('noscroll', true);
    });
    this.$eventBus.$on("objactivation", function(s){
      self.currentOLView="objact";
      self.showOverlay.show=true;
      document.body.classList.toggle('noscroll', true);
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
  },
  methods: {
    login_click: function () {
      console.log(this.currentOLView)
      this.showOverlay.show = true;
      this.currentOLView = 'login';
      document.body.classList.toggle('noscroll', true);
    }   ,
  },
  components: {
    login,
    ioviewer,
    objcreator,
    objact
  }
};
</script>

<style>
.theme--light.application {
  background: #e6e6e6;
}
.theme--light.primary--text {
  color: #0075bc;
  /* caret-color: #0075bc; */
}
.toolbar_title_text {
  position: absolute;
  color: #0075bc;
  font-size: 1.4em;
  font-weight: 700;
  display: inline;
  padding-left: 12px;
  line-height: 60px;
}
.toolbar_link {
  min-width:140px;
  text-align: center;
  line-height: 60px;
  font-size: 1.2em;
  padding: 0 1em;
  color: #333;
  text-decoration: none;
  transition: all ease 0.4s;
}
.toolbar_link:hover {
  color: #0075bc;
}
.toolbar_link:first-of-type {
  margin-left: 150px;
}
/* CSS for InfoGrid */
.infogrid {
  background-color: transparent;
  width: 1028px;
  font-family: 'Open Sans', sans-serif;
  padding: 50px 0px;
  margin-top: 60px;
  min-height: 740px;
}
.block_title {
  width: 100%;
  /* color: #555555; */
  padding-top: 25px;
  padding-bottom: 20px;
}
.line {
  display: inline-block;
  width: 50px;
  padding: 0px 16px;
  vertical-align: middle;
  color: #b3b3b3;
}
.inline {
  display: inline-block;
  margin:0px 0px 0px 50px;
  vertical-align: middle;
}
.line hr {
  border: 1px solid #fff;
  border-bottom-color: #555555;
  color: #fff;
}
.infoblock {
  padding-top: 10px;
  padding-bottom: 30px;
}
.block_info {
  line-height: 2em;
}
h4 {
  color: #555555;
  font-weight: 400;
  font-size: 14px;
  padding: 16px 16px;
  margin: 0 auto;
}
a {
  text-decoration: none;
}
</style>
