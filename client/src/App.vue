<template>
  <v-app>
    <v-toolbar app>
      <router-link to='/'>
        <svg id="Layer_1" width="55" height="55" data-name="Layer 1" xmlns="http://www.w3.org/2000/svg"
             viewBox="0 0 40.32 27.4">
          <defs>
            <svg:style>.cls-1{fill:#0075bc;}</svg:style>
          </defs>
          <title>Home</title>
          <path class="cls-1"
                d="M8.54,11l-3,2a.45.45,0,0,0,0,.74l5.66,3.78a.57.57,0,0,1,0,1L9.42,19.78a.81.81,0,0,1-.91,0L.22,14.14a.49.49,0,0,1,0-.82L8.51,7.68a.81.81,0,0,1,.91,0l5.74,3.91a.81.81,0,0,1,.35.71L15.43,14a.59.59,0,0,1-.92.46L9.44,11A.81.81,0,0,0,8.54,11Z"/>
          <path class="cls-1"
                d="M29,21.66,20.8,27.21a1.08,1.08,0,0,1-1.22,0l-8.16-5.55a.48.48,0,0,1,0-.79l1.89-1.29a.93.93,0,0,1,1,0l5.34,3.56a.81.81,0,0,0,.89,0l3-2a.45.45,0,0,0,0-.75l-3.45-2.31a.81.81,0,0,1-.36-.7l0-1.22a.68.68,0,0,1,.72-.66l.44,0a.44.44,0,0,1,.22.08L29,20.87A.48.48,0,0,1,29,21.66Z"/>
          <path class="cls-1"
                d="M20.27,11.17l.3-7.23a.51.51,0,0,0-.8-.45L15.84,6.17a.81.81,0,0,0-.35.63l-.07,1.37a.63.63,0,0,1-1,.49L11.38,6.56a.45.45,0,0,1,0-.74L19.73.14a.81.81,0,0,1,.91,0l8.29,5.64a.49.49,0,0,1,0,.82l-7.59,5.17A.69.69,0,0,1,20.27,11.17Z"/>
          <path class="cls-1"
                d="M40.07,14.2l-8.21,5.59a.81.81,0,0,1-.91,0l-3.58-2.44a.62.62,0,0,1,.38-1.13l3.15.15a.81.81,0,0,0,.48-.13l3.56-2.31a.45.45,0,0,0-.22-.82l-7.13-.43-1.81-.11a.63.63,0,0,1-.32-1.16L31,7.66a.77.77,0,0,1,.87,0l8.23,5.6A.56.56,0,0,1,40.07,14.2Z"/>
        </svg>
      </router-link>
      <v-menu :nudge-width="100" :disabled='!serverSelection'>
        <template v-slot:activator="{ on }">
          <v-toolbar-title v-on="on">
            <span class='toolbar_title_text'
                  v-if='currentserver!=null'>{{ currentserver.type }} <small><small>{{
                currentserver.name
              }}</small></small></span>
            <v-icon dark>arrow_drop_down</v-icon>
          </v-toolbar-title>
        </template>
        <v-list>
          <v-list-tile
            v-for="(item, index) in servers"
            :key="index"
            @click="selectserver(index)"
          >
            <v-list-tile-title v-text="item.type.concat(' - ',item.name)" v-if='item.name!=""'></v-list-tile-title>
            <v-list-tile-title v-text="item.type.concat(' (Current Hosting Server) ')" v-else></v-list-tile-title>
          </v-list-tile>
          <hr>
          <v-list-tile
            :key="servers.length"
            @click="dialog = true"
          >
            <v-list-tile-title>Add a server</v-list-tile-title>
          </v-list-tile>
        </v-list>
      </v-menu>
      <v-spacer></v-spacer>
      <v-toolbar-items class="hidden-sm-and-down">
        <router-link class='toolbar_link' to='/about'>About</router-link>
        <router-link class='toolbar_link' to='/faq'>FAQ</router-link>
        <router-link class='toolbar_link' to='/contactus'>Contact Us</router-link>
      </v-toolbar-items>
    </v-toolbar>
    <v-content>
      <router-view/>
      <div v-bind:is='currentOLView' v-if='showOverlay.show'></div>
    </v-content>
    <v-dialog v-model="dialog" max-width="800px">
      <v-card>
        <v-card-title>
          <span class="headline">Add a Server and Connect</span>
        </v-card-title>
        <v-card-text>
          <v-layout row wrap my-2>
            <v-flex xs12>
              <v-text-field
                label="Server Name"
                box
                v-model='server.name'
              ></v-text-field>
            </v-flex>
          </v-layout>
          <v-layout row wrap my-2>
            <v-flex xs12>
              <v-text-field
                label="Server URL"
                box
                v-model='server.host'
              ></v-text-field>
            </v-flex>
          </v-layout>
          <hr>
          <v-radio-group v-model="server.type" row>
            <template v-slot:label>
              <div>Server Type:</div>
            </template>
            <v-radio label="Library" value="Library"></v-radio>
            <v-radio label="Activator" value="Activator"></v-radio>
          </v-radio-group>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" flat @click="doneConfig">Done</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-app>
</template>
<script>
import login from './components/login';
import ioviewer from './components/ioviewer';
import objuploader from './components/objuploader';
import objact from './components/objact';

export default {
  name: 'App',
  data() {
    return {
      showOverlay: {show: false},
      currentOLView: 'login',
      dialog: false,
      servers: [
        {
          "host": ".",
          "shelf": "/kos",
          "type": "Library",
          "name": ""
        },
        {
          "host": ".",
          "shelf": "/kos",
          "type": "Activator",
          "name": ""
        }
      ],
      server: {
        host: ".",
        shelf: "/kos",
        type: "Library",
        name: ""
      },
      serverSelection: false
    };
  },
  created: function () {
    console.log();
    var self = this
    Promise.all([
      this.$http.get("./static/json/metadataschema.json"),
      this.$http.get("./static/json/config.json")
    ]).then(function (responses) {
      let activatorUrlList = process.env.VUE_APP_KGRID_ACTIVATOR_URLS;
      let activatorUrls = activatorUrlList ? activatorUrlList.split(";") :
        ["http://localhost:8080", "https://kgrid-activator.herokuapp.com"];

      self.$store.commit('setactivatorurls', activatorUrls)
      self.$store.commit('setdefaultactivator', 0)
      self.$store.commit('setmetaschema', responses[0].data);
      self.$store.commit('setdemourl', responses[1].data.demourl);
      self.serverSelection = responses[1].data.serverSelection
      self.$store.commit('setservers', self.servers);
      self.$store.commit('setcurrentServerIndex', 0);
      self.serverSelection = false;
    }).catch(error => {
      console.log(error)
    })
    this.$eventBus.$on("viewio", function (s) {
      self.currentOLView = "ioviewer";
      self.showOverlay.show = true;
    });
    this.$eventBus.$on("addobj", function (s) {
      self.currentOLView = "objuploader";
      self.showOverlay.show = true;
    });
    this.$eventBus.$on("objactivation", function (s) {
      self.currentOLView = "objact";
      self.showOverlay.show = true;
    });
    this.$eventBus.$on('hideOverlay', function (layerid) {
      switch (layerid) {
        case '0':
          self.showOverlay.show = false;
          break;
        case '9':
          self.showOverlay.show = false;
          break;
      }
    });
    this.$eventBus.$on('objAdded', function (obj) {
      self.showOverlay.show = false;
      self.$router.go(self.$router.currentRoute)
    });
  },
  methods: {
    login_click: function () {
      console.log(this.currentOLView)
      this.showOverlay.show = true;
      this.currentOLView = 'login';
    },
    doneConfig: function () {
      this.dialog = false
      if (this.server.type == 'Library') {
        this.server.shelf = '/kos'
      } else {
        this.server.shelf = '/'
      }
      this.servers.push(this.server)
      this.$store.commit('setservers', this.servers)
      this.$store.commit('setcurrentServerIndex', this.servers.length - 1)
      this.$router.push('/')
    },
    selectserver: function (index) {
      console.log("Selected Server: " + index + "    ===> " + this.servers[index].name)
      this.$store.commit('setcurrentServerIndex', index)
      this.$store.commit('setErrorStatus', '')
      this.server = JSON.parse(JSON.stringify(this.currentserver))
      this.$router.push('/')
    }
  },
  computed: {
    currentserver: function () {
      return this.$store.getters.getCurrentServer
    }
  },
  components: {
    login,
    ioviewer,
    objuploader,
    objact
  }
};
</script>
<style>
.noscroll {
  overflow: hidden;
  height: 100%;
}

html {
  height: 100%;
}

.theme--light.application {
  background: #e6e6e6;
}

.theme--light.primary--text {
  color: #0075bc;
}

.toolbar_title_text {
  color: #0075bc;
  font-size: 1.4em;
  font-weight: 700;
  display: inline;
  padding-left: 12px;
  line-height: 60px;
}

.toolbar_link {
  min-width: 140px;
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
  margin: 0px 0px 0px 50px;
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

::-webkit-scrollbar {
  width: 10px;
}

::-webkit-scrollbar-track {
  -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.1);
  border-radius: 0px;
}

::-webkit-scrollbar-thumb {
  border-radius: 0px;
}

::-webkit-scrollbar-track {
  background-color: #fff;
}

/* the new scrollbar will have a flat appearance with the set background color */
::-webkit-scrollbar-thumb {
  background-color: #d5d5d5;
}

/* this will style the thumb, ignoring the track */
::-webkit-scrollbar-button {
  display: none;
  background-color: #d5d5d5;
}

/* optionally, you can style the top and the bottom buttons (left and right for horizontal bars) */
::-webkit-scrollbar-corner {
  background-color: #fff;
}

/* if both the vertical and the horizontal bars appear, then perhaps the right bottom corner also needs to be styled */
</style>
