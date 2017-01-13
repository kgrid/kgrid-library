<template>
	<div class='content'>
		<applayout :nothelper='true'>
		<div slot='banner'>
		<div class='row ot-detail-spacer'></div>
		<div class='row ot-detail-smallrow'>
			<div class='col-md-3'>
			
			<div id='goback'>
			<div id='backButton'>
				 <a @click='returntolibrary'>BACK TO THE LIBRARY
				<div class='ot-r1-btn ot-back'>
				<div class='greyroundbutton'></div>
			       <div class='btnContent'><img src='../assets/images/Chevron.png' width="5px"/></div>
			       </div></a>
			</div>
				</div>
			</div>
			<div class='col-md-4'></div>
			<div class='col-md-5 pri-pub'>
			<div class='col-md-7' style="text-align:right;w">VIEW TYPE:</div>
			<div class='col-md-2'>
						<label class='ot-pub radio-inline'><input type='radio' value='false' v-on:click='unpublish'/>
							<span v-if='isPublic'>PRIVATE</span>
							<span class='active middleout' v-else>PRIVATE</span>
						</label>
			</div>
			<div class='col-md-3'>
						<label class='ot-pub radio-inline'><input type='radio' value='true' v-on:click='publish'/><img src='../assets/images/LittleGreenDot.png' width="6px">
							<span class='active middleout'  v-if='isPublic'>PUBLIC</span>
							<span v-else>PUBLIC</span>
						</label>
			</div>
				
			</div>
			</div>
					<div class='row ot-detail-titlerow'>
			<div id='ko-title'>
				<div id= 'type-status' >
					<img v-if='objModel.object.metadata.published'
						src='../assets/images/LittleGreenDot.png' width='10px'
						height='auto' />
				</div>
				<h1>
					<small id='page_title_v'>{{objModel.object.metadata.title}}</small>
				</h1>
			</div>
			</div>
					<div class='row ot-detail-daterow'>
							<div class='col-md-3'>
					<p class='date-title'>Object ID:</p>
					<p class='date-data'>
						<span>{{objModel.object.uri}}</span>
					</p>
				</div>
				<div class='col-md-2'>
					<p class='date-title'>Last Updated:</p>
					<p class='date-data'>
						<span v-text='formattedUpdateDate'></span>
					</p>
				</div>
				<div class='col-md-2'>
					<p class='date-title'>Created On:</p>
					<p class='date-data'>
						<span v-text='formattedCreateDate'></span>
					</p>
				</div>
				<div class='col-md-5'>
				</div>
						</div>
		</div>
		<div slot='header'>
		<ul class='nav nav-tabs view' role='tablist' id='tabs'>
		<li v-for='section in sections' v-bind:id='section.name' v-bind:class="{ active: activeTab === section.label }" v-on:click="selectTab(section)"><a >{{section.label}}</a></li>
		<li role='presentation' class='labels accessLevelOne'><a>
				<div class='labels iconBtn accessLevelOne' id ='more'>
				</div>
		</a></li>
	</ul>
					<div class='inline editwrapper accessLevelOne' id='objBtns'
						style='top: 10px; right: 40px; position: absolute;'>
						<button class='edit' id='metadataeditBtn' v-on:click='editObj'>EDIT</button>
						<button class='edit' id='deleteButton'
							v-on:click='deleteObject'>DELETE</button>
						<div id='downloadButton'>
							<a class='edit' v-bind:href='downloadLink'>DOWNLOAD
							<div class='ot-r1-btn ot-download'>
							<div class='greyroundbutton'></div>
						       <div class='btnContent'><img src='../assets/images/Down_Arrow_Dark.png' width="12px"/></div>
						       </div></a>
						</div>
			</div>
		</div>
			<div slot='maincontent'>
					<ul class='tab-content view' id='tab'>
						<li v-for='section in sections' v-bind:id='section.name'
								 v-bind:class="{ active: activeTab === section.label }">
								<tabpane v-bind:section='section.name' v-bind:object='objModel.object' ></tabpane>
						</li>
					</ul>
				</div>
			</applayout>
	</div>
	</template>
	<script>
	import applayout from './applayout.vue';
	import tabpane from './tabbedpanel.vue';
	import {tabNav, getCurrentUser, overlayHeightResize, retrieveObject, retrieveObjectList, otScroll, setBannerbkSize} from '../ot.js';
	import eventBus from '../components/eventBus.js';
	import { objModel, editObjModel, sections, userModel } from '../components/models.js'
	export default {
		name:'ko-detail', 
		data : function() {
			return {
				objModel : objModel,
				sections : sections,
				isDisabled: 1,
				isPublic:false,
				activeTab: 'METADATA'
			}
		},
			components:{
		'applayout':applayout,
		'tabpane':tabpane
		},
		created : function() {
			var self = this;
			//tabNav();
			eventBus.$on("objSaved",function(obj){
				$.extend(true, objModel.object, obj);
			});
			eventBus.$on('objectSelected',function(obj){
				$.extend(true, objModel.object, obj);	
			});
			var sessionObj = sessionStorage.getItem("otObj");
			if(sessionObj){
				$.extend(true, self.objModel.object, JSON.parse(sessionObj));
			}
		},
		mounted:function() {
			var self = this;
			retrieveObject(this.$route.params.uri, "complete", function(response) {
				self.objModel.object = response;
				self.isPublic = self.objModel.object.metadata.published;
				
			}, function(response){
				console.log("Error:");
				console.log(response);
				eventBus.$emit('404');
			}); 
	    	$('ul#tabs li.active').addClass('middleout');
	    	$("html, body").animate({
	        	scrollTop: 0
	    	}, 200);
	    	otScroll();
	    	$('.ot-banner').addClass('detail');
		},
		computed : {
			formattedUpdateDate : function() {
				if(!this.objModel.object.metadata.lastModified || this.objModel.object.metadata.lastModified=="" ){
					return ""
				}
				else
					{return new Date(this.objModel.object.metadata.lastModified)
						.format("mediumDate")}
				},
			formattedCreateDate : function() {
					if(!this.objModel.object.metadata.createdOn || this.objModel.object.metadata.createdOn=="" ){
						return ""
					}
					else
						{return new Date(this.objModel.object.metadata.createdOn)
							.format("mediumDate")}
					},
				
		
			downloadLink : function() {
				return '/ObjectTeller/knowledgeObject/'
					+ this.objModel.object.uri + '/complete.json'
			}
		},
		updated : function() {
			
		},
		methods:{
			selectTab: function(section){
				console.log("Clicked on "+section.label);
				this.activeTab=section.label;
				//this.$nextTick(this.autoresize(section.name));
		},
			editObj:function(){
				$.extend(true, editObjModel.object, this.objModel.object);
				if(editObjModel.object.outputMessage==null){
					editObjModel.object.outputMessage="";
				}
				if(editObjModel.object.inputMessage==null){
					editObjModel.object.inputMessage="";
				}
				eventBus.$emit('editObj', this.objModel.obj);
			},
			publish:function(){
				this.toggleObject(true);
			},
			unpublish:function(){
				this.toggleObject(false);
			},
			toggleObject:function(pub){
				var uri=this.objModel.object.uri;
				var published='';
				var self=this;
				if(pub){
					published='published';
				}else{
					published='unpublished';
				}
				$.ajax({
					beforeSend : function(xhrObj) {
						xhrObj.setRequestHeader("Content-Type", "application/json");
					},
					type : 'PUT',
					url : "/ObjectTeller/knowledgeObject/" + uri + "/" + published,
					success : function(response) {
						self.isPublic=pub;
						objModel.object.metadata.published=pub;
					},
					error : function(response, tStatus, xhr) {
						console.log(response);
					}
				});
			},
			deleteObject : function() {
				var self=this;
				var uri = this.objModel.object.uri;
				var txt;
				if (uri != "") {
					var r = confirm("Do you really want to delete the object ? ");
					if (r == true) {
						$.ajax({
								type : 'DELETE',
								url : "/ObjectTeller/knowledgeObject/"
										+ uri,
								success : function(
										response) {
									eventBus.$emit('return');
								}
							});
					}
				}
			},
			downloadObj: function() {
				var myWindow = window.open(this.downlaodLink, "myWindow");   // Opens a new window
				myWindow.focus();
			},
			returntolibrary: function(){
				eventBus.$emit("return");
			},
		}
};
	</script>
<style>
.ot-banner.detail {
	height: 240px; 
}
.ot-detail-smallrow {
	height: 20px;
	margin-left: -40px;
}
.ot-detail-spacer {
	height: 25px;
}
.ot-detail-titlerow {
	height: 80px;
	margin-left: -40px;
}
.ot-detail-daterow {
	height: 60px;
	margin-left: -37px;
	margin-top: 35px;
}

#type-status {
	width: 10px; 
	height: 42px; 
	display: inline-block;
}
#type-status img {
	position: absolute;
	top: 16px;
left: 5px;
}
#ko-title {
	position:relative;
	display: block;
	font-size: 28pt;
	margin-left:16px;
	margin-top:16px;
	width:860px;
	height:60px;
	background-color:#fff;
}

#ko-title h1{
	position: absolute;
    display: inline-block;
    font-size: 22px;
    left: 24px;
    margin: 0;
    text-align: left;
    padding: 0;
}



.nav-tabs>li.active>a, .nav-tabs>li.active>a:focus, .nav-tabs>li.active>a:hover {
    color: #666666;
    cursor: pointer;
    background-color: #fff;
    border: 1px solid #fff;
    border-bottom-color: transparent;
}
ul#tabs li.active:after {
    width: 100%;
    background: #39b45a;
}
ul#tabs li:after {
    content: '';
    display: block;
    margin: auto;
    height: 2px;
    width: 0px;
    background: transparent;
    transition: width .5s ease, background-color .5s ease;
}

ul#tab>li {
	display:none;	
}
ul#tab>li.active {
	display:block;
	padding: 36px 36px;
}


#goback {
	display: inline-block;
	font-size: 12px;
	color: #666666;
}

#backButton a{
	padding-left:26px;
	vertical-align:middle;
	color: #b3b3b3;
	line-height: 1.8em;
}

#backButton a:hover{
	color:#666666;
}

#backButton {
	display:inline-block;
	background-color: #fff;
	border: none;
	color: #b3b3b3;
	font-size: 12px;
	vertical-align:top;
	height:24px;
	background-size: 20px;
}
#backButton:hover{
	color:#666666;
	background-size: 24px;
	transition:  0.5s ease;
}
.date-title {
	font-size: 12px;
}
.date-data {
	font-size: 18px;
}
#more {
	opacity: 0.5;
	width: 25px;
	height: 20px;
	margin: auto;
	background-image : url('../assets/images/More_Icon_Dark-01.png');
	background-size: 20px;
    background-repeat: no-repeat;
	background-position-y: center;
	transition: opacity 0.5s ease;

}

#more:hover {	
	opacity: 1;
}

#downloadButton {
	display: inline-block;
}

#downloadButton:hover .greyroundbutton{
	  transform: scale(1.2);
      border: 1px solid #666666;
	  margin: auto;
	}
#downloadButton:hover .btnContent{
		 opacity: 1;
		}
.ot-download{
	width: 40px;
	height: 40px;
	position:absolute;
    bottom:-20px;
    right:-32px;
    margin:0 auto;
    z-index:500;
}

.ot-back{
	width: 40px;
	height: 40px;
	position:absolute;
    bottom:-18px;
    right:200px;
    margin:0 auto;
    z-index:500;
}

.ot-back .btnContent {
	position: relative;
    top: -22px;
    left:17px; 
}

#backButton:hover .greyroundbutton{
	  transform: scale(1.2);
	  border: 1px solid #666666;
	  margin: auto;
	}
#backButton:hover .btnContent{
		 opacity: 1;
		}
.pri-pub {
	font-size: 12px;
}
.pri-pub label {
	margin: 0px;
}

.pri-pub label span {
	color: #b3b3b3;
	transition: color 0.5s ease;
}

.pri-pub label span:hover {
	color: #666666;
}

.pri-pub label span.active {
	color: #666666;
}

</style>