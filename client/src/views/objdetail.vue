<template>
  <v-container fluid>
    <v-layout row >
      <v-flex xs2 px-1 py-5 class='treepanel' :style='panelStyle' offset-xs2>
        <v-treeview
          :active.sync='active'
          v-model="tree"
          :items="items"
          activatable
          hoverable
          item-key="name"
          open-all
          expand-icon=""
        >
        </v-treeview>
      </v-flex>
      <v-flex xs6 class='displaypanel' :style='panelStyle'>
        <v-container class='white' fluid>
          <v-layout row wrap my-2 >
            <v-flex xs8 offset-xs4 class='text-xs-right'>
                 <span class='caption'>CONTEXT </span><a :href="currentObject['@context'][0]" target='_blank'><span class='body-2'>{{currentObject['@context'][0]}}</span></a>
            </v-flex>
          </v-layout>
          <v-layout row wrap mt-4 >
            <v-flex xs12 mb-4>
              <span class='display-1'>{{currentObject.title}}</span>
            </v-flex>
            <v-flex xs8 align-start my-3>
              <v-layout row wrap>
                <v-flex xs7 class='overflow-x-hidden' style='text-overflow:ellipsis;'>
                  <span class='body-1'>OBJECT ID</span><br>
                  <v-tooltip top>
                    <span slot="activator"  class='subheading'>{{currentObject['identifier']}}</span>
                    <span>{{currentObject['identifier']}}</span>
                  </v-tooltip>
                </v-flex>
                <v-flex xs5 class='overflow-x-hidden' style='text-overflow:ellipsis;'>
                  <span class='body-1'>OBJECT TYPE</span><br>
                  <v-tooltip top>
                    <span slot="activator"  class='subheading'>{{currentObject['@type']}}</span>
                    <span>{{currentObject['@type']}}</span>
                  </v-tooltip>
                </v-flex>
            </v-layout>
            </v-flex>
            <v-flex xs4 class='text-xs-right' align-start mt-2>
              <v-tooltip bottom content-class='actioniconcap'>
                <v-btn slot="activator" outline small fab color="#0075bc" :disabled='inEditMode'>
                  <a :href='htmldownloadlink' :download='downloadFile'>
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
                <v-tooltip bottom content-class='actioniconcap' v-if='isImplementation'>
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
        tree: [],
        active:[],
        windowHeight:0,
        inEditMode:false,
        modelObject:{},
      }
    }
    , created : function() {
      this.active=[]
      this.active.push(this.currentdashname)
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
        store.dispatch('fetchkolist').then(function(){
  				store.commit('setCurrentKO',to.params.uri);
          store.dispatch('fetchko', {'uri':to.params.uri}).then(function(){
              next()
          }).catch(e=>{
            console.log(e)
          })
        }).catch(e=>{
          console.log(e)
        })
    },
    watch: {
      selected: function(){
        console.log("Selected a new one:"+this.selected);
        this.$store.commit('setCurrentKO',this.selected);
        var uri=this.selected.replace('-','/')
        this.$store.dispatch('fetchko', {'uri':uri});
        this.$eventBus.$emit("objectSelected", uri);
      },
      active: function() {
        if(this.active.length==0){
          this.active.push(this.currentdashname)
        }
      },
      currentObject () {
        this.modelObject=JSON.parse(JSON.stringify(this.$store.getters.getCurrentObject))
      }
    },
    computed: {
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
      downloadFile : function() {
        return this.currentKOId.naan+'-'+this.currentKOId.name+'.zip'
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
      isImplementation: function() {
        return this.currentObject['@type']=='koio:Implementation'
      },
      currentdashname () {
        var id = this.currentKOId.naan+'-'+this.currentKOId.name
        if(this.currentKOId.version!=''){
          id=id+'/'+this.currentKOId.version
        }
        return id
      },
      currenturi () {
        var id = this.currentKOId.naan+'/'+this.currentKOId.name
        if(this.currentKOId.version!=''){
          id=id+'/'+this.currentKOId.version
        }
        return id
      },
      items () {
        var l= []
        var o = {'name':'','children':[]}
        o.name = this.currentKOId.naan+'-'+this.currentKOId.name
    	  if(Array.isArray(this.currentKO.hasImplementation)){
          this.currentKO.hasImplementation.forEach(function(e){
            var imp = {}
            imp.name=e
            o.children.push(imp)
          })
        }else {
          o.children.push({'name':this.currentKO.hasImplementation})
        }
        l.push(o)
        return l
      },
      selected () {
        var id = this.currentdashname
        if (this.active.length>0) {
          id = this.active[0]
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
          bool = (key=='@id'||key=='@type'||key=='@context'||key=='identifier'||key=='hasImplementation'||key=='hasServiceSpecification'||key=='hasDeploymentSpecification'||key=='hasPayload')
        }
        return bool
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
        return (key=='hasServiceSpecification')||(key=='hasDeploymentSpecification')||(key=='hasPayload')||(key=='hasImplementation')
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
								self.$store.dispatch('fetchko', {"uri":uri})
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
        var key = this.findKey(entry)
        console.log(key)
        switch (key){
          case 'hasImplementation':
            this.active.splice(0)
            this.active.push(entry)
            break;
          default:
            var yamlurl = this.baseurl+this.currenturi+'/../'+entry
    				this.$store.commit('setkoiourl',yamlurl)
            this.$eventBus.$emit('viewio',yamlurl)
            break;
        }
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
.v-treeview-node__label, .v-treeview-node__label:hover {
  background-color: transparent;
}
.v-treeview-node__root {
  /* background-color: #fff; */
  /* border: 1px solid #eee; */
  padding-left: 2px;
  margin: 5px 0px;
  border-left: 2px solid transparent;
  transition: all 0.8s ease;
}
.v-treeview-node {
  color: #0075bc;
}
.v-treeview-node--active>.v-treeview-node__root {
  border-left: 2px solid #0075bc;
}

.theme--light.v-treeview .v-treeview-node--active {
  color: #333;
  background-color: transparent;

}
.v-treeview-node--active .v-treeview-node {
  color: #0075bc;
}
.v-treeview-node--leaf {
  margin-left: 30px;
}
.theme--light.v-treeview--hoverable .v-treeview-node__root:hover {
  background-color: transparent;
  border-left: 2px solid #0075bc;
}
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
