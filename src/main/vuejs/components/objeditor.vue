<template>
<div>
	<olpane layerid=0> 
		<div slot="ol-title"><h3>{{editObjModel.object.metadata.title}}</h3>
			<div id="barcontainer">
				<ul class="inEdit" id="edittabs">
					<li class="labels active">METADATA<span v-show='changed.metadata'>*</span></li>
					<li class="labels">PAYLOAD<span v-show='changed.payload'>*</span></li>
					<li class="labels">INPUT<span v-show='changed.input'>*</span></li>
					<li class="labels">OUTPUT<span v-show='changed.output'>*</span></li>
				</ul>
				<div id="editWrapper">
					<button class="edit" v-on:click="undoEdit" id="cancelBtn">UNDO</button>
					<button class="edit" id="saveObjButton" v-on:click="saveObj">SAVE & CLOSE</button>
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
							<input type="text" class="metaEdit" v-model="editObjModel.object.metadata.title" maxlength="140" />
							<span  v-bind:class="{ nearmax:editObjModel.object.metadata.title.length>=130 }" >{{editObjModel.object.metadata.title.length}}/140</span>
							</div>
						<div class="addtext">
							<h4>DESCRIPTION</h4>
							<textarea class="metaEdit" v-model="editObjModel.object.metadata.description"></textarea>
							<span  v-bind:class="{ nearmax:editObjModel.object.metadata.description.length>=480 }" >{{editObjModel.object.metadata.description.length}}/500</span>
						</div>
						<div class="addtext">
							<h4>KEYWORDS</h4>
							<input type="text" class="metaEdit" v-model="editObjModel.object.metadata.keywords" maxlength="140"/>
							<span  v-bind:class="{ nearmax:editObjModel.object.metadata.keywords.length>=130 }" >{{editObjModel.object.metadata.keywords.length}}/140</span>
						</div>
						<div class="addtext">
							<h4>OWNERS</h4>
							<input type="text" class="metaEdit" v-model="editObjModel.object.metadata.owner" maxlength="140" />
							<span  v-bind:class="{ nearmax:editObjModel.object.metadata.owner.length>=130 }" >{{editObjModel.object.metadata.owner.length}}/140</span>
						</div>
						<div class="addtext">
							<h4>CONTRIBUTORS</h4>
							<input type="text" class="metaEdit" v-model="editObjModel.object.metadata.contributors" maxlength="140" />
							<span  v-bind:class="{ nearmax:editObjModel.object.metadata.contributors.length>=130 }" >{{editObjModel.object.metadata.contributors.length}}/140</span>
						</div>
						<div class="addtext">
							<h4>LICENSE  </h4>
							<div class="addBtn" v-if="!editObjModel.object.metadata.license.licenseName"><button class="mediumgreenroundbutton" v-on:click="addLicense"><img src="../assets/images/Plus_Icon.png" width="12px"></button></div>
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
							<button class="mediumgreenroundbutton" v-on:click="addCitation"><img src="../assets/images/Plus_Icon.png" width="12px"></button>
							<div class='entryArea'>
								<ul id="citationlist" class="list">
									<li v-for="(citation,index) in editObjModel.object.metadata.citations" >
										<div class="row">
											<div class="col-md-9">
												<linkedfieldtile :value="citation.citation_title" :link="citation.citation_at"></linkedfieldtile>
											</div>
											<div class="col-md-3">
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
										placeholder="one instance only" maxlength="140">
									
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
import { editTabNav, overlayHeightResize } from '../ot.js';
export default {
  	name: "objeditor",
	data:function(){
		return {
			objModel:objModel,
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
	linkedfieldeditor:linkedfieldeditor,
	linkedfieldtile:linkedfieldtile
	},
	created:function(){
		var self=this;
		this.objModel.object = this.$parent.getObject();
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
		changed: function(){
			var changeCheck={metadata:false,payload:false,input:false,moutput:false};
			changeCheck.metadata= !(_.isEqual(this.editObjModel.object.metadata,this.objModel.object.metadata));
			changeCheck.payload= !(_.isEqual(this.editObjModel.object.payload,this.objModel.object.payload));
			changeCheck.input= !(_.isEqual(this.editObjModel.object.input,this.objModel.object.input));
			changeCheck.output= !(_.isEqual(this.editObjModel.object.output,this.objModel.object.output));
			return changeCheck;
	}
	},
	mounted:function(){
		$('ul#edittabs li:first').addClass('active'); 
	    $('ul#edittab li:first').addClass('active'); 
		$("ul#edittabs li.active").addClass("middleout");
		editTabNav();
		overlayHeightResize();
	},
	updated : function() {


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
				url : "/ObjectTeller/knowledgeObject/"+this.editObjModel.object.uri,
				data : text,
				dataType : "json",
				success : function(response) {
				console.log(response);
					if ((response != 'empty') && (response != null)) {
						eventBus.$emit("objSaved",response);
					}
					
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
<style scoped>
h3 {
	padding-left: 24px;
}
#barcontainer {
    height: 36px;
    border-bottom: 1px solid #e3e3e3;
    width: 91%;
	margin-top: 60px;
margin-left:24px;
}
#barcontainer ul li:first-child {
	margin-left: 10px; 
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
	position: absolute;
	color: #39b45a;
top: 68px;
right: 55px;
}
.addtext textarea+span{
	top: 108px;
}
.addtext span.nearmax {
	color: #ec2526;
}

.fieldcontainer {
    display: block;
    position: relative;
    height:100%;
    overflow: auto;
margin: 0 auto;
padding: 0px 20px;
}

.addtext textarea {
    width: 96%;
    height: 80px;
    resize: none;
    padding: 8px 16px 8px 16px;
border: 1px solid #e6e6e6;
border-radius: 10px;
    color: #666666;
    margin: 0;
    font-size: 14px;
    overflow-y: auto;
    line-height: 1.4em;
    text-overflow: clip;
    white-space: pre-wrap;
    word-break: break-word;
    background-color: #fff;
    font-weight: 400;
}
.addtext a {
    display: block;
    border: none;
    white-space: nowrap;
    overflow: hidden;
width:100%;
    text-overflow: ellipsis;
    height: 38px;
    padding: 10px 0px 0px 16px;
    border-radius: 0px;
    margin: 0px;
    font-size: 14px;
    color: #666666;
    font-weight: 400;
}
#editWrapper{
	width: 300px;
	display: inline-block;
	position: relative;
	right: -305px;
	top: 9px;
}
select {
    width: 96%;
background-position: 98%;
}
input[type=text] {
	width: 96%;
}
</style>
