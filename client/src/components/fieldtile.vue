<template id="fieldtile-template">
	<div class="addtext" :id="fieldname">
		<div class='entryDisplayArea' >
			<ul class="list">
				<li v-for="(entry,index) in fieldvalue" style="position:relative;">
							<div style="background-color:#fff; text-align:left;position:absolute;left:0%;z-index:180;padding:4px;margin:1px;"  @click='removeentry(index)'>
								<icon color="#0075bc" name="times" v-if='!isDisabled'></icon>
							</div>
							<linkedfieldinput :schemaProp='schemaProp' :fieldname='fieldname' :section="section" :object='fieldvalue' :entry='entry' :index='index' :isDisabled='isDisabled' @valuechange='updatearray'></linkedfieldinput>
				</li>
			</ul>
			<div style='position:relative;'>
			<div style="background-color:#fff; text-align:left;position:absolute;left:0%;z-index:180;padding:4px;margin:1px;"  @click='addentry'>
				<icon color="#0075bc" name="plus" v-if='!isDisabled&&moreentry'></icon>
			</div>
		</div>
		</div>
	</div>
</template>
<script>
	import linkedfieldinput from './linkedfieldinput.vue'
	export default {
				name: "fieldtile",
				props : [ 'schemaProp', 'fieldname', 'section', "isDisabled"],
				created:function(){
					var self = this
					this.fieldvalue = JSON.parse(JSON.stringify(this.value))
					this.$eventBus.$on('revertEdit', function(){
							self.undoEdit()
						})
					if(this.isCitation){
						this.entrymax=25
					}
				},
				data:function(){
						return {
							fieldvalue:[],
							entrymax:1
						}
				},
				components:{
					linkedfieldinput
				},
				watch:{
					objuri:function(){
						this.fieldvalue=JSON.parse(JSON.stringify(this.value))
					}
				},
				computed : {
					moreentry:function(){
						return this.fieldvalue.length<this.entrymax
					},
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
								propertyValue = this.parentobject[this.section][this.fieldname];
								if(propertyValue==null){
									propertyValue=''
								}
								break;
							case "models":
								propertyValue = this.parentobject[this.section][this.fieldname];
								break;
							default:
								propertyValue = this.parentobject[this.fieldname];
								break;
							}
							return propertyValue;
						},
						isCitation : function() {
							return this.fieldname == "citations";
						}
					},
				methods:{
					removeentry:function(index){
						// console.log(index)
						this.fieldvalue.splice(index,1)
						this.updatevalue()
					},
					addentry:function(){
						var obj={}
						if(this.isCitation){
							obj.citation_at=''
							obj.citation_title=''
						}
						this.fieldvalue.push(obj)
						this.updatevalue()
					},
					updatearray:_.debounce(function(obj){
						if(obj.index<this.fieldvalue.length && obj.index!=-1){
							// console.log('Update entry '+obj.index +JSON.stringify(obj))
							this.fieldvalue[obj.index]=JSON.parse(JSON.stringify(obj[this.fieldname]))
						}else {
							console.log('Error')
						}
						this.updatevalue()
					},500),
					updatevalue:function(){
							var obj={}
							obj[this.fieldname]=JSON.parse(JSON.stringify(this.fieldvalue))
							this.$emit('valuechange',obj)
					},
					undoEdit:function(){
						// console.log('Undo Edit'+this.section+' '+this.fieldname)
						this.fieldvalue = JSON.parse(JSON.stringify(this.parentobject[this.section][this.fieldname]))
					}
				}
				};
</script>
<style scoped></style>
