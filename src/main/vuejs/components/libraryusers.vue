<template>
  <olpane layerid=0>
	<div slot="ol-title"><h3>Libray Users</h3></div>
	  <div slot="ol-form">
		<div class="row addtext">
		  <div class='col-md-6'>
			<div class='ot-sub'>USERS IN THIS LIBRARY</div>
			  <div id='uList'>
		        <ul>
		          <li v-for='(user,index) in umodel.userList' v-bind:key='index'><usercard :user='user' :you='userModel.user'
		      						:tileindex='index' v-on:remove='orderedList.splice(index, 1)'></usercard></li>
		        </ul>
		      </div>
		    </div>
	        <div class='col-md-6'>
			    <div class='ot-sub'>BASIC INFORMATION</div>
		   </div>     
		 </div>
			</div>
		</div>
		<div slot="ol-processing">Processing...</div>
		<div slot="ol-success">New object succesfully created!!! Object ID:{{newobjModel.object.uri}}</div>
		<div slot="ol-failure">Creation of new object Failed! </div>
		<div slot="ol-warning">Warning !!!</div>
	</olpane>
</template>
	<script>
	import olpane from '../components/olpane';
	import eventBus from '../components/eventBus.js';
	import {getCurrentUser, retrieveUserList, overlayHeightResize, retrieveObject, retrieveObjectList, setenddate, setstartdate, otScroll, setBannerbkSize} from '../ot.js';
	import usercard from '../components/usercard';
	import { objModel, editObjModel, sections, userModel } from '../components/models.js'
	export default {
		name:"libraryusers",
		data:function(){
			return {
			    umodel:{userList:[
			              {username: 'gqmeng@umich.edu', passwd: '', id: 2, first_name: 'George', last_name: 'Meng', role: 'Informatician'},
			              {username: 'jrampton@umich.edu', passwd: '', id: 3, first_name: 'James', last_name: 'Rampton', role: 'Designer'}
			           ]},
				newobjModel:{object:{metadata:{title:""},uri:"arkid"}},
				newtitle:"",
				jsonobj:"",
				userModel:{user:{}}
			}
		},
		beforeCreate: function(){
			var self=this;
			retrieveUserList(function(response) {
				self.umodel.userList = response;
			},function(response){
				console.log(response);
			});
			getCurrentUser(function(response) {
				if(response!="")
					$.extend(true, self.userModel.user, response);
			},function(response) {
				console.log(response);
			});
		},
	created:function(){
			var self =this;
			
		  	
			eventBus.$on('userdeleted',function(userid){
			      console.log(userid)
			      var dIndex = self.umodel.userList.map(function(e) {return e.id}).indexOf(userid);
			      self.umodel.userList.splice(dIndex,1);
			    })
	},
	components: {
		olpane:olpane,
		usercard
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
		createObj:function(){
			var self=this;
			var text = JSON.stringify(self.newobjModel.object);
//			console.log("data to sent:"+text);
			$.ajax({
				beforeSend : function(xhrObj) {
					xhrObj.setRequestHeader("Content-Type", "application/json");
					xhrObj.setRequestHeader("Accept", "application/json");
				},
				type : "POST",
				url : "/ObjectTeller/knowledgeObject",
				data : text,
				dataType : "json",
				success : function(response) {
					console.log(response);
					if ((response != 'empty') && (response != null)) {
						var test = JSON.stringify(response);
						var obj = JSON.parse(test);
	//					console.log(test);
						$.extend(true, self.newobjModel.object, obj);
					}
					$("div.processing").fadeOut(200);
					$("div.success").fadeIn(300).delay(2000).fadeOut(400, function(){
							eventBus.$emit("objcreated",obj);
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
	ul {
		list-style: none;
	}
	#uList {
		padding: 0px 10px;
	}
	#uList li {
		margin-top : 20px;
	}
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
		color: #39b45a;
	top: 5px;
	left: 850px;
	}
	.addtext span.nearmax {
		color: #ec2526;
	}
	.done_btn {
    height: 55px;
    width: 910px;
    /* right: 100px; */
    margin: 55px auto;
    background-color: #39b45a;
    color: #fff;
    border-radius: 10px;
    position: absolute;
}
	.ot-sub {
		border-bottom: 1px solid #b3b3b3;
		width: 385px;
	    padding: 5px 0px;
		margin: 0px 10px;
	font-size:14px;
	}
	
	</style>