<template>
	<div class='content'>
		<applayout :nothelper='true'>
		<div slot='banner'>
		<div class='row ot-detail-spacer'></div>
		<div class='row ot-detail-smallrow'>
			<div class='col-md-3'>
			<div id='goback'>
				<div id='backButton'>
					<a @click='returntolibrary'>BACK TO THE LIBRARY</a>
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
		<li v-for='section in sections' v-bind:class="{ active: activeTab === section.label }" v-on:click="selectTab(section.label)"><a >{{section.label}}</a></li>
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
							<a class='edit' v-bind:href='downloadLink'>DOWNLOAD</a>
							<div id='downloadButtonimg'></div>
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
	import {tabNav, getCurrentUser, overlayHeightResize, retrieveObject, retrieveObjectList, otScroll, autoresize, setBannerbkSize} from '../ot.js';
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
		watch:{
			activeTab:function(){
				console.log(this.activeTab);
			}
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
				return new Date(this.objModel.object.metadata.createdOn)
					.format("mediumDate")
			},
			downloadLink : function() {
				return '/ObjectTeller/knowledgeObject/'
					+ this.objModel.object.uri + '/complete.json'
			}
		},
		updated : function() {
			this.autoresize();
		},
		methods:{
			selectTab: function(tablabel){
				console.log("Clicked on "+tablabel);
				this.activeTab=tablabel;
				this.autoresize();
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
			autoresize:function(){
				var autosize = this.$el.querySelector("ul#tab>li.active .autosize");
				var sh = autosize.scrollHeight+5;
				autosize.style.height="0px";     //Reset height, so that it not only grows but also shrinks
				autosize.style.height = sh + 'px';    //Set new height
				console.log(this.activeTab+"New Height = "+sh);
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
			returntolibrary: function(){
				eventBus.$emit("return");
			},
		}
};
	</script>
<style>
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
	background-image: url("../assets/images/Back_Icon_Light-01.png");
	background-repeat: no-repeat;
	background-position: left top;
	display:inline-block;
	background-color: #fff;
	border: none;
	color: #b3b3b3;
	font-size: 12px;
	vertical-align:top;
	height:21px;
	background-size: 20px;
}
#backButton:hover{
	background-image: url("../assets/images/Back_Icon_Dark-01.png");
		background-repeat: no-repeat;
background-position: left top;
color:#666666;
}
.date-title {
	font-size: 12px;
}
.date-data {
	font-size: 18px;
}
#more {
	width: 25px;
	height: 20px;
	margin: auto;
	background-image : url('../assets/images/More_Icon_Light-01.png');
	background-size: 20px;
    background-repeat: no-repeat;
	background-position-y: center;
}

#more:hover {	
	background-image : url('../assets/images/More_Icon_Dark-01.png');
}

#downloadButton {
	display: inline-block;
background-image : url('../assets/images/Down_Arrow_Light.png');
background-size: 12px;
background-position-x: 98%;
background-position-y:center;
background-repeat: no-repeat;
}
#downloadButtonimg{
	display: inline-block;
	width: 14px;
	height: 14px;
	padding: 3px;
	border-radius: 100%;
	border: none

}
#downloadButton:hover {
	background-image : url('../assets/images/Down_Arrow_Dark.png');

}
.pri-pub {
	font-size: 12px;
}
.pri-pub label {
	margin: 0px;
}
</style>