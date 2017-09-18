<template>
	<olpane layerid=0>
		<div slot="ol-title"><h3>Add a Knowledge Object to the Library </h3></div>
		<div slot="ol-form">
			<div class="addtext">
				<h4>Please enter a title for the new knowledge object, then	click on "Create Object".</h4>
					<input type="text" maxlength="140" class="metaEdit inEdit"
						v-model="newobjModel.object.metadata.title" /> <span  v-bind:class="{ nearmax:newobjModel.object.metadata.title.length>=130 }" >{{newobjModel.object.metadata.title.length}}/140</span>
					<h4 class='kgl-subtitle'> - Or - </h4>
				</div>
				<div>
					<fileuploader section="NEW_OBJECT" v-on:filechange="updatedisplay" :src='jsonobj'></fileuploader>
				</div>

			</div>
			<div slot='buttons'>
								<button class="kg-btn-secondary" style='display:none;' v-on:click="closeOverlay">Cancel</button>
								<button class="kg-btn-primary"  v-on:click="createObj">Create Object</button>
			</div>
		<div slot="ol-processing"><span>Processing...</span></div>
		<div slot="ol-success"><span>New object succesfully created!!! Object ID:{{newobjModel.object.uri}}</span></div>
		<div slot="ol-failure"><span>Creation of new object Failed! Please try again later!</span></div>
		<div slot="ol-warning">Warning !!!</div>
	</olpane>
</template>
	<script>
	import olpane from '../components/olpane';
	import eventBus from '../components/eventBus.js';
	import fileuploader from './fileuploader.vue';
	import { objModel, editObjModel, sections, userModel } from '../components/models.js'
	export default {
		name:"objcreator",
		data:function(){
			return {
				newobjModel:{object:{metadata:{title:""},uri:"arkid"}},
				newtitle:"",
				jsonobj:""
			}
		},
	created:function(){

	},
	components: {
		olpane:olpane,
		fileuploader:fileuploader
		},
	computed:{
		newid:function(){
			if(!this.newobjModel.object.uri){
				return "";
			}else{
			return this.newobjModel.object.uri;
			}
		}
	},
	methods:{
	closeOverlay: function(){
		eventBus.$emit('exit');
	},
		createObj:function(){
			var self=this;
			var text = JSON.stringify(self.newobjModel.object);
			$("div.processing").fadeIn(300);
			$.ajax({
				beforeSend : function(xhrObj) {
					xhrObj.setRequestHeader("Content-Type", "application/json");
					xhrObj.setRequestHeader("Accept", "application/json");
				},
				type : "POST",
				url : "knowledgeObject",
				data : text,
				dataType : "json",
				success : function(response) {
					console.log("After creation response:");
					console.log(response);
					if ((response != 'empty') && (response != null)) {
						var test = JSON.stringify(response);
						var obj = JSON.parse(test);
	//					console.log(test);
						$.extend(true, self.newobjModel.object, obj);
					}
					$("div.processing").fadeOut(200);
					$("div.success").fadeIn(300).delay(2000).fadeOut(400, function(){
							eventBus.$emit("objcreated",response);
					});
				},
				error : function(response) {
	//				console.log(response);
					$("div.processing").fadeOut(200);
					$("div.warning").text(response.status+"   "+response.statusText);
					$("div.warning").show();

					//test code, to be removed
					eventBus.$emit("objcreated",{"metadata":{"title":"error"}});
				}
			});
		},
		 updatedisplay : function(sec, msg){
	//	    	console.log("Section:"+sec+" Msg:"+msg);
		    	var fullobj={};
		    	if(msg!=""){
		    	try{
		    		fullobj=JSON.parse(msg);
		    		$.extend(true, this.newobjModel.object, fullobj);
				}catch(e){
					$("div.warning").text(e.message);
					$("div.warning").show();
				}
		    	}else{
		    		$.extend(true, this.newobjModel.object, {"metadata":{"title":""}});
		    	}
		    },

	}
};
	</script>
	<style scoped>
	.addtext {
	    position: relative;
	    width: 100%;
	    padding: 0;
	    margin: 5px 0px 15px 0px;
	    color: #666666;
	font-size: 11px;
	}
	.addtext span{
		position: relative;
		color: #0075bc;
	top: -10px;
	left: 940px;
	}
	.addtext span.nearmax {
		color: #ec2526;
	}
	.done_btn {
    height: 55px;
    width: 310px;
     right: 30px;
    margin: 55px auto;
    background-color: #fff;
    color: #0075bc;
    border-radius: 0px;
		border: 1px solid #0075bc;

    position: absolute;
		transition: all 0.6s ease;
}
.done_btn:hover {
background-color: #0075bc;
color: #fff;
}
	input[type=text] {
	    width: 970px;
	    height: 55px;
	    padding: 0px 16px;
	    border: 1px solid #e5e5e5;
	    border-radius: 0px;
	    margin: 2px 0;
	    font-size: 14px;
	    color: #666666;
	    font-weight: 400;
	}
	.kgl-subtitle {
		margin: 0 auto;
	text-align: center;
	}
	</style>
