<template id="fieldtile-template">
	<div class="addtext" :id="fieldname">
		<div class='entryDisplayArea' >
			<ul class="list">
				<li v-for="(entry,index) in fieldvalue" style="position:relative;">
							<div style="background-color:#fff; text-align:left;position:absolute;right:0%;z-index:180;padding:4px;margin:1px;"  @click='removeentry(index)'>
								<icon color="#0075bc" name="times" v-if='!isDisabled'></icon>
							</div>
							<texteditor :schemaProp='schemaProp' :fieldname='fieldname' :section="section" :isDisabled='isDisabled' :object='fieldvalue' :entry='entry' :index='index' v-if='getinputtype(schemaProp)==="text" | getinputtype(schemaProp)=="textarea"' @valuechange='updatearray'></texteditor>
							<linkedfieldinput :schemaProp='schemaProp' :fieldname='fieldname' :section="section" :object='fieldvalue' :entry='entry' :index='index' v-if='getinputtype(schemaProp)==="linked"' :isDisabled='isDisabled' @valuechange='updatearray'></linkedfieldinput>
				</li>
			</ul>
			<div style='position:relative;'>
			<div style="background-color:#fff; text-align:left;position:absolute;right:0%;z-index:180;padding:4px;margin:1px;"  @click='addentry'>
				<icon color="#0075bc" name="plus" v-if='!isDisabled&&moreentry'></icon>
			</div>
		</div>
		</div>
	</div>
</template>
<script>
	import linkedfieldinput from './linkedfieldinput.vue'
	import texteditor from './texteditor.vue'
	export default {
				name: "fieldtile",
				props : [ 'schemaProp', 'fieldname', 'section', "isDisabled"],
				created:function(){
					var self = this
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
				mounted:function(){
					if(this.value){
						console.log("Fieldtile:"+JSON.stringify(this.value))
						this.fieldvalue = JSON.parse(JSON.stringify(this.value))
					}
				},
				components:{
					linkedfieldinput,
					texteditor
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
						return this.$store.getters.getobject
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
								propertyValue = this.parentobject[this.section][this.fieldname];
								break;
							}
							return propertyValue;
						},
						isCitation : function() {
							return this.fieldname == "citations";
						}
					},
				methods:{
					getinputtype:function(obj){
							var inputtype =''
							if(obj.items.attrs){
								if(obj.items.attrs.type){
									inputtype = obj.items.attrs.type
								}
							}
							return inputtype
					},
					removeentry:function(index){
						this.fieldvalue.splice(index,1)
						this.updatevalue()
					},
					addentry:function(){
						var inputtype = this.getinputtype(this.schemaProp)
						switch(inputtype) {
							case "text":
								var textentry =''
								this.fieldvalue.push(textentry)
								break
							case "linked":
								var obj = {}
								this.fieldvalue.push(obj)
								break
							}
						this.updatevalue()
					},
					updatearray:_.debounce(function(obj){
						if(obj.index<this.fieldvalue.length && obj.index!=-1){
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
						this.fieldvalue = JSON.parse(JSON.stringify(this.parentobject[this.fieldname]))
					}
				}
				};
</script>
<style scoped>
.entryDisplayArea {
	margin-bottom: 15px;
}
</style>
