<template id="fieldtile-template">
	<div class="addtext" :id="fieldname" style='position:relative;'>
		<input type="text" class="metaEdit ft-sz-16" :disabled='isDisabled' v-model='fieldvalue' v-if='schemaProp.attrs.type=="text"' />
		<a v-if='fieldname=="service"' @click='viewfile' style='position:absolute; top:12px; left:-15px;display:inline-block'>
			<div><icon color="#0075bc" name="eye" v-if='false'></icon></div>
		</a>
		<textarea ref='ta' spellcheck=false class="metaEdit ft-sz-16" :disabled='isDisabled' v-model='fieldvalue' v-if='schemaProp.attrs.type=="textarea"' v-bind:style="{ height: taheight }"></textarea>
	</div>
</template>
<script>
	require('lodash')
	import 'vue-awesome/icons/eye'
	export default {
				name: "texteditor",
				props : [ 'schemaProp', 'fieldname', 'section', "isDisabled"],
				created:function(){
					var self = this
					this.fieldvalue = this.value
					if(this.section=='metadata'){
						this.$eventBus.$on('revertEdit', function(){
							self.undoEdit()
						})
					}
				},
				data:function(){
						return {
							fieldvalue:'',
							taheight:'50px'
						}
				},
				mounted:function(){
					if(this.schemaProp.attrs.type=='textarea'){
					 var h = this.$refs.ta.scrollHeight+2
					 this.taheight =h +'px'
				  }
					this.fieldvalue = this.value
				},
				watch:{
					fieldvalue:function(){
					  this.updatevalue()
						if(this.schemaProp.attrs.type=='textarea'){
							this.resize()
						}
					},
					objuri:function(){
						this.fieldvalue=this.value
					}
				},
				beforeDestroy:function(){
					this.$eventBus.$off('revertEdit')
				},
				components:{},
				computed : {
					objuri:function(){
						return this.$store.getters.getcurrenturi
					},
					parentobject:function(){
						return this.$store.getters.getobject.metadata
					},
						value : function() {
							var propertyValue = "";
							switch (this.section) {
							case "metadata":
								propertyValue = this.parentobject[this.fieldname];
								if(propertyValue==null){
									propertyValue=''
								}
								break;
							case "model":
								propertyValue = this.parentobject[this.section][this.fieldname];
								break;
							default:
								propertyValue = this.parentobject[this.fieldname];
								break;
							}
							return propertyValue;
						},
						edited: function(){
							switch (this.section) {
							case "metadata":
								return this.fieldvalue != this.parentobject[this.fieldname]
							default:
								return this.fieldvalue != this.parentobject[this.fieldname]
							}
						},
						editable:function(){
							return this.section=='metadata'
						}
					},
				methods:{
					viewfile: function(){
						if(this.fieldname=='service') this.$eventBus.$emit('viewio','');
					},
					undoEdit:function(){
						console.log('Undo Edit'+this.section+' '+this.fieldname)
						if(this.section=='metadata'){
							this.fieldvalue = this.parentobject[this.fieldname]
							var obj={}
							obj[this.fieldname]=this.fieldvalue
							this.$emit('valuechange',obj)
						}
					},
					updatevalue:_.debounce(function(){
							var obj={}
							obj[this.fieldname]=this.fieldvalue
							this.$emit('valuechange',obj)
					}, 300),
					resize:_.debounce(function(){
						this.taheight='auto'
						var h = this.$refs.ta.scrollHeight+2
						this.taheight = h+'px'
					}, 50),
				}
				};
</script>
<style scoped>
input.ft-sz-16, div.ft-sz-16 {
	font-size: 16px;
}
</style>
