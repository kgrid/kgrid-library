<template>
<div>
	<olpane layerid=0>
		<div slot="ol-title">
			<div  id='arkid'><span>Object ID: {{editObjModel.object.uri}}</span><span class='float-r'>Last Updated: {{formattedUpdateDate}}</span></div>
			<h2>{{editObjModel.object.metadata.title}}</h2>
			<div id="barcontainer">
				<ul class="inEdit" id="edittabs">
					<li id='METADATA' class="labels">METADATA<span v-show='changed.metadata'>*</span></li>
					<li id='PAYLOAD' class="labels">PAYLOAD<span v-show='changed.payload'>*</span></li>
					<li id='INPUT' class="labels">INPUT<span v-show='changed.input'>*</span></li>
					<li id='OUTPUT' class="labels">OUTPUT<span v-show='changed.output'>*</span></li>
				</ul>

			</div>
		</div>
		<div slot="ol-form">
			<ul class="inEdit" id="edittab">
				<li class="current-tab" id="METADATA">
					<fieldset class="fieldcontainer" id="first">
						<div id="metadata_fields">
							<div class="addtext">
								<label>TITLE</label>
								<input type="text" class="metaEdit" v-model="editObjModel.object.metadata.title" maxlength="140" />
								<span  v-bind:class="{ nearmax:editObjModel.object.metadata.title.length>=130 }" >{{editObjModel.object.metadata.title.length}}/140</span>
							</div>
							<div class="addtext">
								<label>DESCRIPTION</label>
								<textarea class="metaEdit" v-model="editObjModel.object.metadata.description"></textarea>
								<span  v-bind:class="{ nearmax:editObjModel.object.metadata.description.length>=480 }" >{{editObjModel.object.metadata.description.length}}/500</span>
							</div>
							<div class="addtext">
								<label>KEYWORDS</label>
								<input type="text" class="metaEdit" v-model="editObjModel.object.metadata.keywords" maxlength="140"/>
								<span  v-bind:class="{ nearmax:editObjModel.object.metadata.keywords.length>=130 }" >{{editObjModel.object.metadata.keywords.length}}/140</span>
							</div>
							<div class="addtext">
								<label>OWNERS</label>
								<input type="text" class="metaEdit" v-model="editObjModel.object.metadata.owner" maxlength="140" />
								<span  v-bind:class="{ nearmax:editObjModel.object.metadata.owner.length>=130 }" >{{editObjModel.object.metadata.owner.length}}/140</span>
							</div>
							<div class="addtext">
								<label>CONTRIBUTORS</label>
								<input type="text" class="metaEdit" v-model="editObjModel.object.metadata.contributors" maxlength="140" />
								<span  v-bind:class="{ nearmax:editObjModel.object.metadata.contributors.length>=130 }" >{{editObjModel.object.metadata.contributors.length}}/140</span>
							</div>
							<div class="addtext">
								<label>LICENSE  </label>
								<div class="addBtn" v-if="!editObjModel.object.metadata.license.licenseName">
								<div class='kg-roundbtn kg-newlf' v-on:click='addLicense'>
									<div class='btnContent'>
										<i class='fa fa-plus kg-fg-color'></i>
										</div>
										</div>
								</div>
								<div class="row" v-else>
									<div class="col-md-9">
										<linkedfieldtile v-bind:link="editObjModel.object.metadata.license.licenseLink" v-bind:value="editObjModel.object.metadata.license.licenseName"></linkedfieldtile>
									</div>
									<div class="col-md-3">
										<button class="edit" v-on:click="selectLicense(editObjModel.object.metadata.license)">EDIT</button>
										<button class="edit" v-on:click="deleteLicense">DELETE</button>
									</div>
								</div>
							</div>
							<div class="addtext">
								<label>CITATIONS</label>
								<div class='kg-roundbtn kg-newlf' v-on:click='addCitation'>
									<div class='btnContent'>
										<i class='fa fa-plus kg-fg-color'></i>
										</div>
										</div>
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
						</div>
					</fieldset>
				</li>
				<li id="PAYLOAD">
						<div id="payload_fields">

							<div class="addtext">
								<label>ENGINE TYPE</label>
								<select required class="options" v-model="editObjModel.object.payload.engineType">
										<option value="Python">PYTHON</option>
								</select> <br>
							</div>
							<div class="addtext">
								<label> FUNCTION NAME (REQUIRED)
								</label>
								<div class="addtext">
									<input class="textbox inEdit" type="text" v-model="editObjModel.object.payload.functionName"
										placeholder="one instance only" maxlength="140">
								</div>
							</div>
							<div class="addtext">
								<label>PAYLOAD CONTENT</label>
								<div class="addtext">
							<fileuploader section="payload" v-on:filechange="updatedisplay" :src='editObjModel.object.payload.content'></fileuploader>
							</div>
							</div>
						</div>
					</li>
					<li id="INPUT">
							<div class="addtext" id="input_fields">
								<label>INPUT CONTENT</label>
								<div class="addtext">
									<fileuploader section="inputMessage" v-on:filechange="updatedisplay" :src='editObjModel.object.inputMessage'></fileuploader>
								</div>
							</div>
					</li>
					<li id="OUTPUT">
						<div class="addtext" id="output_fields">
							<label>INPUT CONTENT</label>
							<div class="addtext">
							<fileuploader section="outputMessage" v-on:filechange="updatedisplay" :src='editObjModel.object.outputMessage'></fileuploader>
						</div>
						</div>
					</li>
				</ul>
		</div>
		<div slot='buttons'>
							<button class="kg-btn-secondary" v-on:click="undoEdit" id="cancelBtn">CANCEL</button>
							<button class="kg-btn-primary" id="saveObjButton" v-on:click="saveObj">SAVE & CLOSE</button>
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
			activeTab:'METADATA',
			undorequest:{name:"undoedit",statement:"All unsaved changes will be discarded!"},
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
		eventBus.$on('hideOverlay',function(id){
			if(id==1) {
			 	self.showSecOverlay.show=false;
				if((self.inEdit=='Citation')&&(self.srcFieldModel.object.title==''))
		  		editObjModel.object.metadata.citations.pop();
				self.resetSrcField();
			}
		});
		eventBus.$on('confirm', function (data) {
			console.log(data);
			if(data.name=="undoedit"){
				self.undoEdit();
			}
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
	},
	formattedUpdateDate : function() {
		if(!this.editObjModel.object.metadata.lastModified || this.editObjModel.object.metadata.lastModified=="" ){
			return ""
		}
		else
			{return new Date(this.editObjModel.object.metadata.lastModified)
				.format("mediumDate")}
		}
	},
	mounted:function(){
		this.activeTab=this.$store.getters.getactivetab;
		var el='ul#edittabs li#'+this.activeTab;
		var el1 = 'ul#edittab li#'+this.activeTab
		$('ul#edittabs li.active').removeClass('active');
		$("ul#edittabs li.middleout").removeClass("middleout");
		$(el).addClass('active middleout');
				$(el1).addClass('active');
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
				url : "knowledgeObject/"+this.editObjModel.object.uri,
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
		undo:function(){
			eventBus.$emit("confirmRequest",this.undorequest);
		},
		undoEdit: function(){
			editObjModel.object = this.$parent.getObject();
			if(editObjModel.object.metadata.keywords==null){
				editObjModel.object.metadata.keywords='';
			}
			if(editObjModel.object.metadata.owner==null){
				editObjModel.object.metadata.owner='';
			}
			if(editObjModel.object.metadata.contributors==null){
				editObjModel.object.metadata.contributors='';
			}
			if(editObjModel.object.metadata.description==null){
				editObjModel.object.metadata.description='';
			}
			if(editObjModel.object.metadata.license==null){
				editObjModel.object.metadata.license={licenseName:"",licenseLink:""};
			}
			if(editObjModel.object.metadata.citations==null){
				editObjModel.object.metadata.citations=[];
			}
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
div#arkid {
	margin:-35px 0px 10px 0px;
	font-size: 12px;
}

#barcontainer {
    height: 36px;
    border-bottom: 1px solid #e3e3e3;
		margin-top: 30px;

}
#barcontainer ul li:first-child {
	margin-left: 30px;
}
.addtext {
    position: relative;
    width: 100%;
    padding: 0;
    margin: 0px 0px 15px 0px;
    color: #666666;
font-size: 11px;
}
.addtext label {

}
.addtext span{
	position: absolute;
	color: #0075bc;
top: 92px;
right: 25px;
}
.addtext textarea+span{
	top: 140px;
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
		overflow-x:hidden;
}

.addtext textarea {
    width: 970px;
    height: 100px;
    resize: none;
    padding: 8px 16px 8px 16px;
border: 1px solid #e5e5e5;
border-radius: 0px;
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

select {
    width: 970px;
		height:48px;
		background-position: 98%;
		border:1px solid #e5e5e5;
		padding:0px 16px;
}
input[type=text] {
	width:970px;
}
.kg-newlf{
	width: 24px;
	height: 24px;
	position:absolute;
	top: 6px;
  left:80px;
  margin:0 auto;
  z-index:500;
}
.kg-newlf span {
position: absolute;
top:4px;
left:-90px;
}
.kg-newlf i {
position: absolute;
top: 6px;
left:7px;
}
</style>
