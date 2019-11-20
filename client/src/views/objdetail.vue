<template>
  <v-container fluid>
    <v-layout row >
      <v-flex xs2 pl-2 py-5 class='treepanel' :style='panelStyle' offset-xs2>
        <v-flex xs12 px-2 py-3 my-3 class='subheading'>{{currentObject.identifier}}</v-flex>
        <v-flex xs12 ml-2 pl-3 py-2 my-2 v-for='ver in versionList' class='subheading entry' :class='{current:isCurrent(ver)}'>
          <span @click='setVersion(ver)'>{{ver}}</span>
        </v-flex>
      </v-flex>
      <v-flex xs6 class='displaypanel' :style='panelStyle'>
        <v-container class='white' fluid>
          <v-layout row wrap my-2 >
            <v-flex xs8 offset-xs4 class='text-xs-right'>
              <span class='caption'>CONTEXT </span>
              <a :href="currentObject['@context'][0]" target='_blank'><span class='body-2'>{{currentObject['@context'][0]}}</span></a>
            </v-flex>
          </v-layout>
          <v-layout row wrap mt-4 >
            <v-flex xs12 mb-4>
              <span class='display-1'>{{currentObject.title}}</span>
            </v-flex>
            <v-flex xs8 align-start my-3>
              <v-layout row wrap>
                <v-flex xs7 class='overflow-x-hidden' style='text-overflow:ellipsis;'>
                  <span class='body-1'>KO ID</span><br>
                  <v-tooltip top>
                    <span slot="activator"  class='subheading'>{{currentObject['identifier']}}</span>
                    <span>{{currentObject['identifier']}}</span>
                  </v-tooltip>
                </v-flex>
                <v-flex xs5 class='overflow-x-hidden' style='text-overflow:ellipsis;'>
                  <span class='body-1'>KO VERSION</span><br>
                  <v-tooltip top>
                    <span slot="activator"  class='subheading'>{{currentObject['version']}}</span>
                    <span>{{currentObject['@type']}}</span>
                  </v-tooltip>
                </v-flex>
            </v-layout>
            </v-flex>
            <v-flex xs4 class='text-xs-right' align-start mt-2>
              <v-tooltip bottom content-class='actioniconcap'>
                <v-btn slot="activator" outline small fab color="#0075bc" :disabled='inEditMode'>
                  <a :href='htmldownloadlink'>
                    <v-icon>get_app</v-icon>
                  </a>
                </v-btn>
                <span>DOWNLOAD</span>
              </v-tooltip>
              <!-- <v-tooltip bottom content-class='actioniconcap'>
                <v-btn outline slot="activator" small fab color="#0075bc" :disabled='inEditMode'>
                    <v-icon>assignment</v-icon>
                </v-btn>
                <span>COPY URL</span>
              </v-tooltip> -->
              <!-- <v-tooltip bottom content-class='actioniconcap'>
                <v-btn outline slot="activator" small fab color="#0075bc" :disabled='inEditMode'>
                    <v-icon>delete</v-icon>
                </v-btn>
                <span>DELETE</span>
              </v-tooltip> -->
              <v-tooltip bottom content-class='actioniconcap'>
                <v-btn outline slot="activator" small fab color="#0075bc" :disabled='inEditMode' @click.stop='inEditMode=true'>
                    <v-icon>edit</v-icon>
                  </v-btn>
                  <span>EDIT</span>
                </v-tooltip>
                <v-tooltip bottom content-class='actioniconcap'>
                  <v-btn outline slot="activator" small fab color="#0075bc" :disabled='inEditMode' @click.stop='sendtoactivator'>
                      <v-icon>play_arrow</v-icon>
                  </v-btn>
                  <span>LIVE DEMO</span>
                </v-tooltip>
            </v-flex>
          </v-layout>
          <v-divider></v-divider>
          <v-layout row wrap my-2 >
            <v-flex xs12 v-for='(value, key) in metaSchema' v-if='isDisplayable(key)' >
              <div v-if='isViewable(key)' class='viewable' :class="{dim:inEditMode}">
                <span class='caption font-weight-light'>{{upper(key)}}</span><br>
                <p v-for='(entry, index) in valuearray(key)' @click='viewfile(entry)' class='subheading'>{{entry}} </p>
              </div>
              <div v-if='!isViewable(key)&&value.type!="array"'>
                <v-text-field
                  v-model='modelObject[key]'
                  :label="upper(key)"
                  :disabled='isDisabled(key)'
                  :box="inEditMode"
                  v-if='value.attrs.type=="text"'
                >
                </v-text-field>
                <v-textarea
                  v-model='modelObject[key]'
                  :label="upper(key)"
                  :disabled='isDisabled(key)'
                  rows=1
                  auto-grow
                  :box="inEditMode"
                  style="min-height: 60px"
                  v-if='value.attrs.type=="textarea"'
                >
                </v-textarea>
              </div>
              <div v-if='!isViewable(key)&&value.type=="object"' class='viewable object'>
                <span class='caption font-weight-light'>{{upper(key)}}</span><br>
                <v-layout row wrap v-for='(entry, objkey) in modelObject[key]'
                   v-if='inEditMode'>
                  <v-text-field
                    v-model='modelObject[key][objkey]'
                    :label="upper(objkey)"
                    :box="inEditMode"
                    :disabled='isDisabled(key)'
                    hide-details
                  >
                  </v-text-field>
                </v-layout>
                <v-layout row wrap v-if='!inEditMode'>
                  <a :href='objlink(key)' target="_blank">{{objtitle(key)}}</a>
                </v-layout>
              </div>
              <div v-if='!isViewable(key)&&value.type=="array"' class='viewable multientry'>
                <span class='caption font-weight-light'>{{upper(key)}}</span><br>
                <v-layout row wrap v-for='(entry, index) in valuearray(key)'
                  @click='openlink(entry)'>
                  <v-flex xs11>
                    <v-text-field
                      v-model='modelObject[key][index]'
                      :label="upper(key)"
                      :disabled='isDisabled(key)'
                      :box="inEditMode"
                      single-line
                      hide-details
                    >
                    </v-text-field>
                  </v-flex>
                  <v-flex xs1>
                    <v-btn
                     fab
                     dark
                     small
                     color="#0075bc"
                     outline
                     @click='removeentry(entry)'
                     v-if='inEditMode'
                    >
                     <v-icon>clear</v-icon>
                    </v-btn>
                  </v-flex>
                </v-layout>
                <v-layout row wrap>
                  <v-flex xs1 offset-xs11>
                    <v-btn
                     fab
                     dark
                     small
                     color="#0075bc"
                     outline
                     @click='addentry(key)'
                     v-if='inEditMode'
                    >
                     <v-icon>add</v-icon>
                    </v-btn>
                  </v-flex>
                </v-layout>
              </div>
            </v-flex>
          </v-layout>
          <v-layout row wrap mt-5 v-if='inEditMode'>
            <v-flex xs12 class='text-xs-right'>
              <v-btn
               color="#0075bc"
               right
               bottom
               relative
               outline
               @click='cancelEdit'
               >
               <span>Cancel</span>
             </v-btn>
              <v-btn
               color="#0075bc"
               right
               bottom
               relative
               outline
               @click='saveMeta'
               >
               <span>Save</span>
             </v-btn>
           </v-flex>
         </v-layout>
        </v-container>
      </v-flex>
    </v-layout>
  </v-container>
</template>
<script>
  import store from '../store.js'
  export default {
    name:'objDetail',
    data : function() {
      return {
        windowHeight:0,
        inEditMode:false,
        modelObject:{},
      }
    }
    , created : function() {
      this.modelObject=JSON.parse(JSON.stringify(this.$store.getters.getCurrentObject))
    }
    , mounted: function() {
      var self=this
      this.windowHeight = window.innerHeight
      window.addEventListener('resize', () => {
          self.windowHeight = window.innerHeight
        });
    }
    ,	beforeRouteEnter (to, from, next) {
        console.log(to.params.uri)
        var koid = store.getters.getCurrentKOId
        var id = koid.naan+'/'+koid.name+'/'+koid.version
        if(id==to.params.uri){
          next()
        }else{
          store.dispatch('fetchko', {'uri':to.params.uri}).then(function(){
              next()
          }).catch(e=>{
            console.log(e)
          })
        }
    },
    watch: {
      currentObject () {
        this.modelObject=JSON.parse(JSON.stringify(this.$store.getters.getCurrentObject))
      }
    },
    computed: {
      versionList: function(){
        var vl = []
        this.currentKO.forEach(function(e){
          vl.push(e.version)
        })
        return vl
      },

      baseurl: function() {
        return this.$store.getters.getbaseurl
      },
      panelheight: function(){
        return this.windowHeight - 120
      },
      panelStyle () {
        return {
          height: `${this.panelheight}px`,
        }
      },
      currentKOId: function(){
        return this.$store.getters.getCurrentKOId
      },
      htmldownloadlink: function(){
        var link = this.baseurl+this.currentKOId.naan+'/'+this.currentKOId.name
        if(this.currentKOId.version!=''){
          link = link +'/'+this.currentKOId.version
        }
        return link+'?format=zip'
      },
      currentKO: function(){
        return this.$store.getters.getCurrentKO
      },
      currentObject: function(){
        return this.$store.getters.getCurrentObject
      },
      currenturi () {
        var id = this.currentKOId.naan+'/'+this.currentKOId.name
        if(this.currentKOId.version!=''){
          id=id+'/'+this.currentKOId.version
        }
        return id
      },
      sectionschema: function(){
        var obj = this.$store.getters.getschemabysection('metadata')
        var schematemplate={
          "$schema": "http://json-schema.org/draft-04/schema#",
          "type": "object",
          "title": "Metadata",
          "properties": {}}
        if(obj.properties){
          schematemplate.properties=JSON.parse(JSON.stringify(obj.properties))
        }
        return schematemplate
      },
      metaSchema: function(){
        return this.sectionschema.properties
      }
    },
    methods: {
      isDisabled: function(key) {
        var bool = true;
        if(this.inEditMode){
          bool = (key=='@id'||key=='@type'||key=='@context'||key=='identifier'||key=='hasServiceSpecification'||key=='hasDeploymentSpecification'||key=='hasPayload')
        }
        return bool
      },
      setVersion: function(ver){
        console.log(ver)
        var vIndex = this.versionList.indexOf(ver)
        this.$store.commit('setCurrentObject', this.currentKO[vIndex])
        this.$eventBus.$emit("objectSelected",  this.currentKO[vIndex]);
      },
      isCurrent: function(ver){
        return ver == this.currentObject.version
      },
      displayMeta: function(key) {
        var bool = true;
        bool = !(key=='@id'||key=='@type'||key=='@context'||key=='identifier')
        return bool
      },
      isDisplayable: function(key) {
        return (this.currentObject[key]!=null)
      },
      isViewable: function(key) {
        return (key=='hasServiceSpecification')||(key=='hasDeploymentSpecification')||(key=='hasPayload')
      },
      isEditable: function(key) {
        return (key=='title')||(key=='keywords')||(key=='description')||(key=='contributors')
      },
      valuearray: function(key) {
        var array =[]
        if(Array.isArray(this.modelObject[key])){
          this.modelObject[key].forEach(function(e){
            array.push(e)
          })
        } else {
          array.push(this.modelObject[key])
        }
        return array
      },
      saveMeta: function() {
        var self = this
        var uri = this.currenturi
        this.$store.dispatch('saveMetadata', JSON.stringify(this.modelObject)).then(function(){
								self.$store.dispatch('fetchkoversion', {"uri":uri})
                self.inEditMode =false
							})
      },
      cancelEdit: function(){
        this.modelObject = JSON.parse(JSON.stringify(this.currentObject))
        this.inEditMode =false
      },
      upper: s => s.toUpperCase(),
      findKey: function(value){
        return _.findKey(this.modelObject, function(item) { return item.indexOf(value) !== -1; })
      },
      viewfile: function(entry){
        console.log(entry)
        var yamlurl = this.baseurl+this.currenturi+'/'+entry
				this.$store.commit('setkoiourl',yamlurl)
        this.$eventBus.$emit('viewio',yamlurl)
      },
      addentry: function(key) {
        this.modelObject[key].push('')
      },
      removeentry: function(entry) {
        console.log(entry)
        var key = this.findKey(entry)
        console.log(key)
        var index = this.modelObject[key].indexOf(entry)
        console.log(index)
        this.modelObject[key].splice(index,1)
      },
      openlink:function(entry) {
        console.log(entry)
        if(!this.inEditMode){
          window.open(entry, '_blank')
        }
      },
      sendtoactivator : function(){
        this.$eventBus.$emit('objactivation','')
      },
      objlink: s => this.modelObject[s].linkfield,
      objtitle: s => this.modelObject[s].titlefield
    },
    components: {
    }
  }
</script>
<style>
.treepanel {
  overflow-y:auto;
  background-color: #fafafa;
}
.displaypanel{
  background-color: #fff;
  overflow-y:auto;
}
.actioniconcap {
  background: #fff;
  color: #0075bc;
  border-radius: 12px;
  border: 1px solid #0075bc;
  font-size: 0.8rem;
}
.entry {
  transition: all 0.5s ease;
}
.entry span {
  color: #0075bc;
  cursor: pointer;
}
.entry.current span {
  color: #000000de;
  cursor: default;
}
.entry.current {
  border-left: 2px solid #0075bc;
  background-color: #fff;
}
.entry:not(.current) {
  border-left: 2px solid #fafafa;
}
.theme--light.v-input--is-disabled input, .theme--light.v-input--is-disabled textarea {
  color: #333;

}

.theme--light.v-input--is-disabled.v-text-field>.v-input__control>.v-input__slot:before {
  border-image: none;
  border-style: none;
}
div.viewable {
  margin: 10px 0px;
}
div.viewable p {
  margin: 4px 0px;
  color: #0075bc;
  cursor: pointer;
}
div.viewable .theme--light.v-input--is-disabled input {
  pointer-events:auto;
  color: #0075bc;
  cursor: pointer;
}
div.viewable span {
  color: #aeaeae;
}
div.multientry .v-text-field {
  padding-top: 2px;
  margin-top: 2px;
}
div.dim {
  opacity:0.5;
}
</style>
