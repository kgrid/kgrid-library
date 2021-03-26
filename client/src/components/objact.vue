<template>
  <olpane layerid=0 :stage='stage'>
    <div slot="oltitle">
      <div class='row'>
        <span class='body-1'>Knowledge Object</span><br>
        <span class='headline' v-if='!objactivated'> {{ url }}</span>
        <span class='headline' v-else><a :href="targeturl" target="_blank"
                                         style="padding: 3px;position:relative;font-size:1.2em;text-align:center;"><span>{{
            url
          }}</span></a></span>
        <span v-if='objpackaged'
              style="background-color:#0075bc;font-size:1em;color:#fff; padding:1px 4px; margin:0px 3px 5px 30px;">packaged</span>
        <span v-if='objactivated'
              style="background-color:#0075bc;font-size:1em;color:#fff; padding:1px 4px; margin:0px 3px 5px 10px;">activated</span>
      </div>
    </div>
    <div slot="ol-form">
      <div id="activator_pane" v-if='objpackaged && !objactivated'>
        <v-layout row wrap my-2>
          <span class='body-1'>The knowledge object has been packaged. Please enter or select the activator to send the KO.</span>
        </v-layout>
        <v-layout row wrap my-5>
          <v-flex xs3>
            <span class='title'>Activator Site URL</span>
          </v-flex>
          <v-flex xs7>
            <v-text-field
              label="Activator Site URL"
              box
              solo
              v-model='activatorurl'
            ></v-text-field>
          </v-flex>
          <v-flex xs2>
            <div class='col-md-2'>
              <v-btn
                color="#0075bc"
                outline
                @click='addactivatorentry'
                v-if='activatorurlindex==-1'
              >
                <span>Save URL</span>
              </v-btn>
              <v-btn
                color="#0075bc"
                outline
                @click='setdefaultactivator'
                v-if='!isDefaultActivator'
              >
                <span>Set as Default</span>
              </v-btn>
            </div>
          </v-flex>
        </v-layout>

        <v-layout row wrap my-5>
          <v-flex xs3>
            <span class='title'>Available Activator Sites</span>
          </v-flex>
          <v-flex xs9>
            <v-radio-group v-model='activatorurlselect' :mandatory="false">
              <v-layout row wrap v-for="(entry,index) in activatorurls" :key='entry' my-1 py-2 align-center fill-height>
                <v-flex xs10>
                  <v-radio :label="entry" :value="entry"></v-radio>
                </v-flex>
                <v-flex xs2>
                  <v-btn
                    fab
                    small
                    color="#0075bc"
                    outline
                    @click='deleteactentry(index)'
                    :disabled='(index==defaultactivatorindex)||(index==activatorurlindex)'
                  >
                    <v-icon>clear</v-icon>
                  </v-btn>
                </v-flex>
                <v-divider></v-divider>
              </v-layout>
            </v-radio-group>
          </v-flex>
        </v-layout>
      </div>
      <div id="demo_pane" v-if='objactivated' style=" padding: 100px 0px; margin:30px 0px;">
        <div class='card' style=" padding: 50px 0px;">
          <div class='row'>
            <h2 style=" padding: 10px 3px;position:relative;text-align:center;"><a :href="swaggereditorurl"
                                                                                   target="_blank"
                                                                                   style="padding: 3px;position:relative;font-size:1.2em;text-align:center;"><span>Go to Swagger Editor to try the KO '{{
                uri
              }}'</span></a>
            </h2>
          </div>
        </div>
      </div>
    </div>
    <div slot='buttons'>
      <v-btn
        color="#0075bc"
        right
        bottom
        relative
        outline
        @click='sendzip'
        v-if='(stage=="ready"||stage=="error")&&!objactivated'
        :disabled="btndisabled"
      >
        <span>Send KO to Activator</span>
      </v-btn>
    </div>
    <div slot="ol-processing"><span>{{ processMsg }}</span></div>
    <div slot="ol-success"><span>The Knowledge Object has been succesfully deployed. </span></div>
    <div slot="ol-failure"><span>{{ errorMsg }}</span></div>
    <div slot="ol-warning"><span>Warning !!!</span></div>
  </olpane>
</template>
<script>
import olpane from '../components/olpane';

export default {
  name: "objact",
  data: function () {
    return {
      stage: '',
      zipfile: '',
      objpackaged: false,
      objactivated: false,
      activatorurls: [],
      activatorurl: '',
      activatorurlselect: '',
      demourl: '',
      btndisabled: false,
      shelfurl: '/',
      securedactivator: false
    }
  },
  created: function () {
    var self = this
    this.activatorurls = this.$store.getters.getactivatorurls
    if (this.currentserver.type == 'Activator') {
      self.stage = 'ready'
    } else {
      self.stage = 'processing'
      setTimeout(function () {
        self.$http.get(
          self.htmldownloadlink,
          {responseType: 'blob'}
        ).then(response => {
          self.stage = 'ready'
          console.log("reading zip...")
          self.zipfile = new File([response.data], self.downloadFile, {type: 'application/zip'})
          console.log(self.zipfile.size)
          setTimeout(function () {
            self.objpackaged = true
          }, 500)
        }).catch(e => {
          self.stage = 'error'
          console.log(e)
        })
      }, 2500)
    }
  },
  mounted: function () {
    this.demourl = this.$store.getters.getdemourl
    this.activatorurl = this.activatorurls[this.defaultactivatorindex]
    this.activatorurlselect = this.activatorurl
    this.objactivated = this.currentserver.type == 'Activator'
    this.objpackaged = this.currentserver.type == 'Activator'
  },
  components: {
    olpane
  },
  watch: {
    activatorurlselect() {
      // Need to check with the activator for the shelf endpoint

      var self = this
      this.$http.get(this.activatorurlselect + '/kos')
        .then(() => {
          self.activatorurl = self.activatorurlselect
          self.shelfurl = '/kos/'
        })
        .catch((err) => {
          self.activatorurl = self.activatorurlselect
          self.shelfurl = '/'
        })
    }
  },
  computed: {
    currentserver: function () {
      return this.$store.getters.getCurrentServer
    },
    processMsg: function () {
      if (this.objpackaged) {
        return 'Deploying KO to the activator ...'
      } else {
        return 'Packaging the KO ... '
      }
    },
    errorMsg: function () {
      if (this.objpackaged) {
        if (this.securedactivator) {
          return 'Cannot deploy KO to secured activator. Check the URL and try again.'
        }
        return 'Error in deploying the KO! Check the URL and try again.'
      } else {
        return 'Error in packaging the KO!'
      }
    },
    url: function () {
      return this.$store.getters.getcurrenturl
    },
    currentKOId: function () {
      return this.$store.getters.getCurrentKOId
    },
    htmldownloadlink: function () {
      // var url = this.$store.getters.getbaseurl
      return this.$store.getters.getbaseurl + this.currentKOId.naan + '/' + this.currentKOId.name + '/' + this.currentKOId.version + '?format=zip'
    },
    downloadFile: function () {
      return this.currentKOId.naan + '-' + this.currentKOId.name + '-' + this.currentKOId.version + '.zip'
    },
    defaultactivatorindex: function () {
      return this.$store.getters.getdefaultactivatorurlindex
    },
    activatorurlindex: function () {
      return this.activatorurls.indexOf(this.activatorurl)
    },
    uri: function () {
      return this.currentKOId.naan + '/' + this.currentKOId.name + '/' + this.currentKOId.version
    },
    isDefaultActivator: function () {
      if (this.activatorurlindex != -1) {
        return (this.defaultactivatorindex == this.activatorurlindex)
      } else {
        return true
      }
    },
    targeturl: function () {
      return this.activatorurl + this.shelfurl
    },
    swaggereditorurl: function () {
      return this.demourl + "?url=" + this.targeturl + this.uri + '/' + this.$store.getters.getCurrentObject.hasServiceSpecification
    },
    currentObject: function () {
      return this.$store.getters.getCurrentObject
    }
  },
  methods: {
    sendzip: function () {
      var formData = new FormData();
      var self = this;
      formData.append('ko', this.zipfile);
      this.stage = 'processing';
      setTimeout(function () {
        self.$http.post(
          self.targeturl,
          formData,
          {headers: {}}
        ).then(function (resp) {
          self.$http.get(self.activatorurl + '/actuator/activation/reload/'+self.uri)
            .then(function (response) {})
            .catch(function (error) {});
          setTimeout(function () {
            self.$http.get(self.activatorurl + '/endpoints').then(function (response) {
              let activated = false
              response.data.forEach(function(e, index){
                activated = activated || (e.knowledgeObject.includes(self.uri)&&(e.status=="ACTIVATED"))
              })
              self.stage = activated ? 'success' :'error'
              setTimeout(function () {
                self.objactivated = activated
                self.stage = 'ready'
              }, 1500)
            }).catch(function (error) {
              self.stage = 'error'
              console.log(error);
              setTimeout(function () {
                self.stage = 'ready'
              }, 5000)
            });
          }, 2000)
        }).catch(function (err) {
          self.stage = 'error'
          console.log(err);
          self.securedactivator = err.message.endsWith("401")
          setTimeout(function () {
            self.stage = 'ready'
          }, 5000)
        });
      }, 1000)
    },
    addactivatorentry: function () {
      this.activatorurls.push(this.activatorurl)
      this.activatorurlselect = this.activatorurl
      this.$store.commit('setactivatorurls', this.activatorurls)
    },
    deleteactentry: function (i) {
      this.activatorurls.splice(i, 1)
      let defaultActivatorIndex = this.$store.getters.getdefaultactivatorurlindex;
      if (defaultActivatorIndex > i) {
        this.$store.commit("setdefaultactivatorurlindex", defaultActivatorIndex - 1)
      }
      this.$store.commit('setactivatorurls', this.activatorurls)
    },
    setdefaultactivator: function () {
      console.log(this.activatorurlindex)
      this.$store.commit("setdefaultactivatorurlindex", this.activatorurlindex)
    }
  }
};
</script>
<style>
#activator_pane {
  padding: 10px;
  border: none;
}

.alert-box div span {
  color: #fff;
}

.card {
  border: 2px solid #eee;
  margin-top: 30px;
  margin-right: 5px;
  box-shadow: 5px 5px 1px 1px #ddd;
}

.entryform .v-input {
  font-size: 1.8em;
}

.entryform .v-input--selection-controls .v-input__control {
  flex-grow: 1;
}

.entryform .v-input--selection-controls {
  margin-top: 0;
}
</style>
