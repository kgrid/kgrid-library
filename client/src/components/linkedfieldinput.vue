<template id="fieldtile-template">
	<div class="addtext" :id="fieldname">
		<!-- <div :class='{bg:!isDisabled}'><span class='ft-sz-12 text-cap' >{{fieldname}}</span><span v-if='!isDisabled&&editable&&edited'>*</span></div> -->
		<div v-if='isDisabled'><a target="_blank" v-bind:href="value[linkfield]"><span>{{value[titlefield]}}</span></a></div>
		<div v-else class='editpane'>
			<div class='row'>
				<div class="col-md-2">
					<h4 title="license_title">TITLE</h4></div>
				<div class="col-md-10 pad-0">
						<input class="" v-model="fieldvalue[titlefield]" type="text" spellcheck="false" placeholder="Enter Title"	maxlength="140"/>
				</div>
			</div>
			<div class='row'>
				<div class="col-md-2" v-on:click="preview">
					<h4 title="license_title">LINK</h4><icon color="#0075bc" name="external-link-alt" ></icon></div>
				<div class="col-md-10 pad-0">
					<input class="" v-model="fieldvalue[linkfield]" type="text" spellcheck="false"	placeholder="Please provide the URL. "/>
				</div>
			</div>
		</div>
	</div>
</template>
<script>
	require('lodash')
	import 'vue-awesome/icons/external-link-alt'
	export default {
				name: "linkedfieldinput",
				props : ['schemaProp', 'fieldname', 'section', 'object','entry','index',"isDisabled"],
				created:function(){
					var self = this
					this.fieldvalue = this.value
					if(this.section=='metadata'){
						this.$eventBus.$on('revertEdit', function(){
							self.undoEdit()
						})
					}
			},
				mounted: function(){
				},
				data:function(){
						return {
							fieldvalue:''
						}
				},
				components:{
				},
				computed : {
					objuri:function(){
						return this.$store.getters.getcurrenturi
					},
					parentobject:function(){
						return this.$store.getters.getobject.metadata
					},
					value : function() {
							var propertyValue = {titlefield:'',linkfield:''};
							switch (this.section) {
							case "metadata":
								if(this.index==-1){
									if(this.parentobject[this.fieldname]){
										propertyValue = JSON.parse(JSON.stringify(this.parentobject[this.fieldname]));
									}
								}else {
									propertyValue = JSON.parse(JSON.stringify(this.entry));
								}
								break;
							case "model":
								propertyValue = JSON.parse(JSON.stringify(this.entry))
								break;
							default:
								propertyValue = this.parentobject[this.fieldname];
								break;
							}
							return propertyValue;
						},
						titlefield:function(){
							var t = 'licenseName'
							switch(this.fieldname){
								case 'license':
									t = 'licenseName'
									break;
								case 'citations':
									t='citation_title'
									break;
								case 'adapters':
									t='filename'
									break;
								default:
									t = 'licenseName'
									break
							}
								return t
						},
						linkfield:function(){
							var t = 'licenseLink'
							switch(this.fieldname){
								case 'license':
									t = 'licenseLink'
									break;
								case 'citations':
									t='citation_at'
									break;
								case 'adapters':
									t='download_url'
									break;
								default:
									t = 'licenseLink'
									break
							}
							return t
						},
						isLicense : function() {
							return this.fieldname == "license";
						},
						isCitation : function() {
							return this.fieldname == "citations";
						},
						edited: function(){
							switch (this.section) {
							case "metadata":

							return ((this.fieldvalue[this.linkfield]!=this.parentobject[this.fieldname][this.linkfield])
								| (this.fieldvalue[this.titlefield]!=this.parentobject[this.fieldname][this.titlefield]))
							default:
							return ((this.fieldvalue[this.linkfield]!=this.parentobject[this.section][this.fieldname][this.linkfield])
								| (this.fieldvalue[this.titlefield]!=this.parentobject[this.section][this.fieldname][this.titlefield]))
						}
					},
						editable:function(){
							return this.section=='metadata'
						}
					},
					watch:{
						fieldvalue:{
							handler: function(){
						  	this.updatevalue()
							},
							deep:true
						},
						objuri:function(){
								this.fieldvalue = JSON.parse(JSON.stringify(this.value))
						},
						object:{
							handler: function(){
								 this.fieldvalue = JSON.parse(JSON.stringify(this.value))
							},
							deep:true
						}
					},
				methods:{
					updatevalue:_.debounce(function(){
							var obj={}
							obj[this.fieldname]=JSON.parse(JSON.stringify(this.fieldvalue))
							obj.index=this.index
							console.log(JSON.parse(JSON.stringify(obj)))
							this.$emit('valuechange',obj)
					}, 300),
					preview: function(){
						var myWindow = window.open(this.value[this.linkfield], "myWindow", "width=800,height=600");   // Opens a new window
						myWindow.focus();
					},
					undoEdit:function(){
						console.log('Undo Edit'+this.section+' '+this.fieldname)
						if(this.index==-1){
							this.fieldvalue = JSON.parse(JSON.stringify(this.object.metadata[this.fieldname]))
						}else {
							this.fieldvalue = JSON.parse(JSON.stringify(this.parentobject.metadata[this.fieldname][this.index]))
						}
					}
				}
			};
</script>
<style scoped>
input.ft-sz-16, div.ft-sz-16 {
	font-size: 16px;
}
a {
	display: block;
	border: none;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	height: 38px;
	padding: 10px 0 0 1px;
	border-radius: 0;
	margin: 0;
	font-size: 16px;
	color: #666;
	font-weight: 400;
}
a, a:hover, a:visited {
	text-decoration: none;
	cursor: pointer;
	transition: color 0.5s ease;
	background-color: transparent;
	color: #666;
}
.editpane {
		border: 1px solid #e3e3e3;
		padding: 10px;
}
div.bg {
	background-color:#e3e3e3;
	padding:2px;
}
</style>
