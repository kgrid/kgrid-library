<template>
<div>
	<olpane layerid=0> 
		<div slot="ol-title"><h3>{{editObjModel.object.metadata.title}}</h3>
			<div id="barcontainer">
				<ul class="inEdit" id="edittabs">
					<li class="labels active">METADATA</li>
					<li class="labels">PAYLOAD</li>
					<li class="labels">INPUT</li>
					<li class="labels">OUTPUT</li>
				</ul>
				<div id="editWrapper">
					<button class="edit" v-on:click="undoEdit" id="cancelBtn">UNDO</button>
					<button class="edit" id="saveObjButton" v-on:click="saveObj">SAVE&CLOSE</button>
								</div>
			</div>
		</div>
		<div slot="ol-form">
		<ul class="inEdit" id="edittab">
			<li class="active current-tab" id="metadata">
				<fieldset class="fieldcontainer" id="first">
					<div id="metadata_fields">
						<div class="addtext">
							<h4>TITLE</h4>
							<input type="text" class="metaEdit" v-model="editObjModel.object.metadata.title" />
							</div>
						<div class="addtext">
							<h4>DESCRIPTION</h4>
							<textarea class="metaEdit" v-model="editObjModel.object.metadata.description"></textarea>
						</div>
						<div class="addtext">
							<h4>KEYWORDS</h4>
							<input type="text" class="metaEdit" v-model="editObjModel.object.metadata.keywords" />
						</div>
						<div class="addtext">
							<h4>OWNERS</h4>
							<input type="text" class="metaEdit" v-model="editObjModel.object.metadata.owner" />
						</div>
						<div class="addtext">
							<h4>CONTRIBUTORS</h4>
							<input type="text" class="metaEdit" v-model="editObjModel.object.metadata.contributors" />
						</div>
						<div class="addtext">
							<h4>LICENSE</h4>
							<div class="addBtn" v-if="!editObjModel.object.metadata.license.licenseName"><button class="mediumgreenroundbutton" v-on:click="addLicense"><img src="../assets/Plus_Icon.png" width="12px"></button></div>
							<div class="row" v-else>
								<div class="col-md-9">
									<linkedfieldtile v-bind:link="editObjModel.object.metadata.license.licenseLink"
										v-bind:value="editObjModel.object.metadata.license.licenseName"></linkedfieldtile>
								</div>
								<div class="col-md-3">
									<button class="edit" v-on:click="selectLicense(editObjModel.object.metadata.license)">EDIT</button>
									<button class="edit" v-on:click="deleteLicense">DELETE</button>
								</div>
							</div>
						</div>
						<div class="addtext">
							<h4>CITATIONS</h4>
							<button class="mediumgreenroundbutton" v-on:click="addCitation"><img src="../assets/Plus_Icon.png" width="12px"></button>
							<div class='entryArea'>
								<ul id="citationlist" class="list">
									<li v-for="(citation,index) in editObjModel.object.metadata.citations" >
										<div class="row">
											<div class="col-md-10">
												<linkedfieldtile :value="citation.citation_title" :link="citation.citation_at"></linkedfieldtile>
											</div>
											<div class="col-md-2">
												<button class="edit" v-on:click="selectCitation(citation)">EDIT</button>
												<button class="edit" v-on:click="deleteCitation(citation)">DELETE</button>
											</div>
										</div>	
									</li>
								</ul>
							</div>
						</div>
					</fieldset>
				</li>
				<li id="payload">
						<div id="payload_fields">
							<div class="addtext">
								<h4> FUNCTION NAME (REQUIRED)
								</h4>
								<div class="addtext">
									<input class="textbox inEdit" type="text" v-model="editObjModel.object.payload.functionName"
										placeholder="one instance only" maxlength="140"><span>140/140</span>
								</div>
								</div>
								<div class="addtext">
									<h4>ENGINE TYPE
									</h4>
									<select required class="options" v-model="editObjModel.object.payload.engineType">
										<option value="Python">PYTHON</option>
									</select> <br>
								</div>
								<fileuploader section="payload" v-on:filechange="updatedisplay" :src='editObjModel.object.payload.content'></fileuploader>
								</div>


						</li>
					<li id="input">
 							<div id="input_fields">
								<fileuploader section="inputMessage" v-on:filechange="updatedisplay" :src='editObjModel.object.inputMessage'></fileuploader>
							</div>
					</li>
					<li id="output">
					<div id="output_fields">
							<fileuploader section="outputMessage" v-on:filechange="updatedisplay" :src='editObjModel.object.outputMessage'></fileuploader>
					</div>
				</li>
			</ul>
		</div>
		<div slot="ol-processing">Processing...</div>
		<div slot="ol-success">Update Successful !!!</div>
		<div slot="ol-failure">Update Failure! </div>
		<div slot="ol-warning">Warning !!!</div>
	</olpane>
	<div v-if="showSecOverlay.show">
			<linkedfieldeditor v-bind:inedit="inEdit" v-bind:srcfield="srcFieldModel.object" v-on:slideout="update"></linkedfieldeditor>
	</div>
 </div>
</template>
<script>
import olpane from '../components/olpane';
import eventBus from '../components/eventBus.js';
import fileuploader from './fileuploader.vue';
import linkedfieldeditor from './linkedfieldeditor.vue'
import linkedfieldtile from './linkedfield.vue'
import { objModel, editObjModel, sections, userModel } from '../components/models.js'
import { editTabNav } from '../ot.js';
export default {
  	name: "objeditor",
	data:function(){
		return {
			editObjModel:editObjModel,
			sections:sections,
			isDisabled:false,
			citationIndex:0,
			showSecOverlay:{show:false},
			srcFieldModel:{object:{title:"",link:""},originalObject:{title:"",link:""}},
			inEdit:"License",
		}
	},
	components: {
	olpane:olpane,
	fileuploader:fileuploader,
	linkedfieldeditor:linkedfieldeditor
	},
	created:function(){
		var self=this;
		$(".ol_pane").css("width","1100px");
		console.log('Object Editor initiated!');
		eventBus.$on('slideout',function(id){
			if(id==1) self.showSecOverlay.show=false;
			self.resetSrcField();
		});
	},
	updated:function(){
	},
	computed:{

	},
	mounted:function(){
		$('ul#edittabs li:first').addClass('active'); 
	    $('ul#edittab li:first').addClass('active'); 
		$("ul#edittabs li.active").addClass("middleout");
		editTabNav();
	},
	updated : function() {


	},
	component:{
		  fileuploader:fileuploader
	  },
	methods: {
		 updatedisplay : function(sec, msg){
//			    	console.log("Section:"+sec+" Msg:"+msg);
			    	switch(sec) {
			    		case "payload":
			    			this.editObjModel.object.payload.content=msg;
			    			break;
			    		case "inputMessage":
			    			this.editObjModel.object.inputMessage=msg;
			    			break;
			    		case "outputMessage":
			    			this.editObjModel.object.outputMessage=msg;
			    			break;
			    		
			    	}
			    },
		saveObj:function(){
			var text = JSON.stringify(this.editObjModel.object);
			var self =this;
//			console.log(text);
			$("div.processing").fadeIn(300);
			$.ajax({
				beforeSend : function(xhrObj) {
					xhrObj.setRequestHeader("Content-Type", "application/json");
					xhrObj.setRequestHeader("Accept", "application/json");
				},
				type : "PUT",
				url : "../knowledgeObject/"+this.editObjModel.object.uri,
				data : text,
				dataType : "json",
				success : function(response) {
//					console.log(response);
					if ((response != 'empty') && (response != null)) {
						var test = JSON.stringify(response);
						var obj = JSON.parse(test);
					}
					eventBus.$emit("objSaved",self.editObjModel.object);
					$("#addObj_f").find("ul#tabs li").each(function() {
//						console.log("UL li text:" + $(this).text());
						$(this).text($(this).text().replace("*", ""));
					});
					$("div.processing").fadeOut(200);
					$("div.success").fadeIn(300).delay(2000).fadeOut(400, function(){
							
						});
					
				},
				error : function(response) {
//					console.log(response);
					$("div.processing").fadeOut(200);
					$("div.failure").text(response.status+"   "+ response.statusText);
					$("div.faiure").fadeIn(300).delay(1500).fadeOut(400);
					
				}
			});
		},
		undoEdit: function(){
			editObjModel.object = this.$parent.getObject();
			if(editObjModel.object.outputMessage==null){
				editObjModel.object.outputMessage="";
			}
			if(editObjModel.object.inputMessage==null){
				editObjModel.object.inputMessage="";
			}
		},
		setSrcField:function(obj){
			$.extend(true, this.srcFieldModel.object, obj);
			$.extend(true, this.srcFieldModel.originalObject,obj);
		},
		getOriginalField:function(){
			return $.extend(true, {}, this.srcFieldModel.originalObject);
		},
		resetSrcField:function(){
			this.srcFieldModel.object={title:"",link:""};
		},
		addLicense: function(){
			var srcObj = {title:"",link:""};
			this.setSrcField(srcObj);
			this.showSecOverlay.show=true;
			this.inEdit="License";
		},
		selectLicense:function(obj){
			var srcObj = {title:"",link:""};
			srcObj.title=obj.licenseName;
			srcObj.link=obj.licenseLink;
			this.setSrcField(srcObj);
			this.showSecOverlay.show=true;
			this.inEdit="License";
			},
		deleteLicense:function(){
			editObjModel.object.metadata.license={};
		},
		addCitation:function(){
			if(!editObjModel.object.metadata.citations){
				editObjModel.object.metadata.citations=[];
			}
			this.citationIndex=editObjModel.object.metadata.citations.length;
			editObjModel.object.metadata.citations.push({citation_title:"",citation_at:""});
			this.srcFieldModel.object={title:"",link:""};
			this.showSecOverlay.show=true;
			this.inEdit="Citation";

		},
		selectCitation:function(obj){
			var srcObj = {title:"",link:""};
			srcObj.title=obj.citation_title;
			srcObj.link=obj.citation_at;
			this.setSrcField(srcObj);
			this.showSecOverlay.show=true;
			this.inEdit="Citation";
			this.citationIndex=editObjModel.object.metadata.citations.indexOf(obj);
//			console.log("selected index:"+this.citationIndex);
		},
		deleteCitation:function(obj){
			editObjModel.object.metadata.citations.splice(editObjModel.object.metadata.citations.indexOf(obj), 1);
		},
		update:function(obj){
			
			this.showSecOverlay.show=false;
			this.setSrcField(obj);
	//		console.log(JSON.stringify(obj));
			switch(this.inEdit){
			case "License":
				editObjModel.object.metadata.license.licenseName=obj.title;
				editObjModel.object.metadata.license.licenseLink=obj.link;
				break;
			case "Citation":
				if(editObjModel.object.metadata.citations.length==0){
//					editObjModel.object.metadata.citations=[];
					editObjModel.object.metadata.citations.push({citation_title:obj.title,citation_at:obj.link});
				}else{			
					editObjModel.object.metadata.citations[this.citationIndex].citation_title=obj.title;
					editObjModel.object.metadata.citations[this.citationIndex].citation_at=obj.link;
				}
				break;
			}
		}
	}
	
};
</script>
<style>
.ol_narrow_pane{
	position: absolute;
    right:0px;
    width: 540px;
     margin: 0px auto;
    background-color: #fff;
    height: 100%;
    transition: all .8s ease;
}

.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, .5);
  display: table;
  transition: opacity .5s ease;
}
.sidebar_close {
    background-color:#fff;
    position:absolute;
    height: 100%;
    width:60px;
    text-align:center;
    border-right: 1px solid #e6e6e6;
}

.sidebar_close h3{
    position:absolute;
    line-height:60px;
    vertical-align:middle;
    top:60px;
    left:10px;
    color:#666666;
    font-size:14px;
    -webkit-transform: rotate(270deg);
    -moz-transform: rotate(270deg);
    -ms-transform: rotate(270deg);
    -o-transform: rotate(270deg);
    transform: rotate(270deg);
}
#close_overlay {
    position: relative;
    top:10px;
    width:35px;
    height:35px;
}

.overlay-top {
    height: 120px;
    width: 480px;
    position: absolute;
    top: 0;
    left: 60px;
    background-color: #fff;

}
.overlay-title {
	padding:0px 22px;
}
.overlay-board {
    position: absolute;
    background: white;
    width: 480px;
    top: 120px;
    left: 60px;
    padding: 0px 0px;
    border-left: 1px solid #e6e6e6;
}

.overlay-alert {
    height: 60px;
    width: 480px;
    position: absolute;
    bottom:0px;
    left: 60px;
    background-color: #fff;

}
.alert-box {
	position:absolute;
	left:100px;
	bottom:0px;
	padding: 15px;
    margin-bottom: 20px;
    border: 1px solid transparent;
    border-radius: 10px;
    width:240px;
}

.success {
    color: #3c763d;
    background-color: #dff0d8;
    border-color: #d6e9c6;
    display: none;
}

.processing{
	   border-color: #d6e9c6;
    display: none;

}
.failure {
    color: #a94442;
    background-color: #f2dede;
    border-color: #ebccd1;
    display: none;
}

.warning {
    color: #8a6d3b;
    background-color: #fcf8e3;
    border-color: #faebcc;
    display: none;
}
.entryform {
	height: 600px;
    padding: 16px 22px;
    margin-top:0px;
    }
.noscroll { overflow: hidden; }
.greenroundbutton {
    width: 41px;
    height: 41px;
    border-radius: 100%;
    color: white;
    background-color: #39b54a;
    border: none;
    font-size: 20px;
    padding: 0;
}
</style>
