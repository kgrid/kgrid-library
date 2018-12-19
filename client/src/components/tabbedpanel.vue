	<template id= "tab-panel-template">
		<div class="tab-content view-obj">
			<ul>
				<li class="kgl-viewlist" v-for="(value,key) in sectionschema.properties">
					<div :class='{bg:!isDisabled}'><span class='ft-sz-12 text-cap' >{{value.title}}</span><span v-if='(!isDisabled&&editable)&&(edited[key])'>*</span></div>
					<fieldtile :schemaProp='value' :fieldname='key' :section="section"   :object="parentobject" :isDisabled='isDisabled' v-if='value.type=="array"' @valuechange='updateBundle'></fieldtile>
					<texteditor :schemaProp='value' :fieldname='key' :section="section" :object='object' index=-1 :isDisabled='isDisabled' v-if='getinputtype(value)==="text" | getinputtype(value)=="textarea"' @valuechange='updateBundle'></texteditor>
					<linkedfieldinput :schemaProp='value' :fieldname='key' :section="section" index=-1 :object='object' :isDisabled='isDisabled' v-if='getinputtype(value)==="linked"' @valuechange='updateBundle'></linkedfieldinput>
				</li>
			</ul>
			<tree-menu v-if='section=="assets"' :label="tree.label" :nodes="tree.nodes" :depth="0"></tree-menu>
		</div>
	</template>
	<script>
	import fieldtile from './fieldtile.vue'
	import texteditor from './texteditor.vue'
	import linkedfieldinput from './linkedfieldinput.vue'
	import TreeMenu from "./treemenu.vue"
	export default {
		name:'tabpane',
		props:['section','object','isDisabled'],
		data: function(){
			return {
				tree:{},
				bundle:{},
				model:{},
				edited:{}
			}
		},
		created:function(){
			var self = this
			// if(this.section=="assets"){
			// 	this.tree=JSON.parse(JSON.stringify(this.$store.getters.getkotree))
			// 	var ko=JSON.parse(JSON.stringify(this.tree.nodes[0]))
			// 	this.tree.label=this.parentobject.metadata.arkId.fedoraPath
			// 	this.tree.nodes[0].label=this.parentobject.metadata.version
			// }
			if(this.section!="logData"&&this.section!="versions"){
				this.refreshbundle()
			}
			if(this.section=='metadata'){
				this.$eventBus.$on('saveMetadata', function(){
						if(self.section=='metadata'){
							self.$store.dispatch('saveMetadata', JSON.stringify(self.bundle)).then(function(){
								self.$store.dispatch('fetchko', self.$route.params)
							})
						}
				})
			}
			// if(self.section!="logData"&&self.section!="versions"){
			// 	this.$eventBus.$on('objectSelected', function(t){
			// 		self.refreshbundle()
			// });
		// }
		},
		beforeDestroy: function(){
				if(this.section=="metadata"){
					this.$eventBus.$off('saveMetadata')
				}
		},
		components: {
			fieldtile,
			texteditor,
			linkedfieldinput,
			TreeMenu,
		},
		computed : {
			parentobject:function(){
				return this.$store.getters.getobject
			},
			bundledebug:function(){
				return this.bundle
			},
			editable:function(){
				return this.section=='metadata'
			},
			fields_json : function(){
				return this.$store.getters.getfields
				},
			filteredFields :function(){
				var section = this.section;
				return this.fields_json.fields.filter(function(field){
					return (field.section==section)
				})
			},
			sectionschema: function(){
				var obj = this.$store.getters.getschemabysection(this.section)
				var schematemplate={
				  "$schema": "http://json-schema.org/draft-04/schema#",
				  "type": "object",
				  "title": this.section+" Metadata",
				  "properties": {}}
				if(obj.properties){
					schematemplate.properties=JSON.parse(JSON.stringify(obj.properties))
				}
				return schematemplate
			},
		},
		methods:{
			refreshbundle:function(){
				this.bundle.section=this.section
				this.bundle.data=JSON.parse(JSON.stringify(this.parentobject))
				if(this.section=='metadata'){
					if(this.bundle.data.model)  { delete this.bundle.data.model }
					if(this.bundle.data.children)  { delete this.bundle.data.children }
				}
				for(var key in this.parentobject[this.section]){
					this.edited[key]=(this.bundle.data[this.section][key]!=this.parentobject[this.section][key])
				}
			},
			updateBundle:function(pair){
				if(this.section!='logData'&&this.section!='versions'){
					console.log(pair)
					switch(this.section){
						case 'metadata':
							for(var key in pair){
								if(key!='index'){
									this.bundle.data[key]=JSON.parse(JSON.stringify(pair[key]))
									this.edited[key]=(this.bundle.data[key]!=this.parentobject[key])
								}
							}
							break
						default:
							for(var key in pair){
								this.bundle.data[this.section][key]=JSON.parse(JSON.stringify(pair[key]))
								this.edited[key]=(this.bundle.data[this.section][key]!=this.parentobject[this.section][key])
							}
							break
						}
					this.$forceUpdate()
				}
			},
			getinputtype:function(obj){
					var inputtype =''
					if(obj.attrs){
						if(obj.attrs.type){
							inputtype = obj.attrs.type
						}
					}
					return inputtype
			},
			valuecheck:function(key){
				console.log("Comparing "+key)
				console.log(this.bundle.data[this.section][key])
				console.log(this.parentobject[this.section][key])
				console.log(this.bundle.data[this.section][key]==this.parentobject[this.section][key])
				return this.bundle.data[this.section][key]!=this.parentobject[this.section][key]
			}
		}
	};
	</script>
<style>
.kgl-viewlist div.bg {
	background-color:#e3e3e3;
	padding:2px;
}
input[type=text]:disabled, textarea:disabled {
	border: none;
	background-color: #fff;
	color: #666666;
	font-weight: 400;
}
input[type=text] {
		height:40px;
		padding: 4px 6px;
		width:100%;
}
.kgl-viewlist {
	list-style:none;
	padding: 10px 16px 16px 16px;
}
textarea {
	width:100%;
	border: 1px solid #e5e5e5;
	border-radius: 0px;
	/* margin:2px 0; */
	font-size: 14px;
	color:#666666;
	font-weight:400;
	min-height:40px;
	padding: 0px 6px;
	resize:none;
}
.kgl-viewlist input, .kgl-viewlist div, .kgl-viewlist h4{
	background-color: #fff;
	/* padding: 0px; */
	color: #555555;
	font-weight: 400;
	font-size: 14px;
	border-radius: 0px;
	margin: 0px;
}
.kgl-viewlist span {
	margin-left:6px;
}
</style>
