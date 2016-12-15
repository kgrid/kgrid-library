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
			<div class='col-md-5'></div>
			<div class='col-md-4'>
			<div class='col-md-4'></div>
			<div class='col-md-4'>
						<label class='ot-pub radio-inline'><input type='radio' value='false' v-on:click='unpublish'/>
							<span v-if='isPublic'>PRIVATE</span>
							<span class='active middleout' v-else>PRIVATE</span>
						</label>
			</div>
			<div class='col-md-4'>
						<label class='ot-pub radio-inline'><input type='radio' value='true' v-on:click='publish'/><img src='../assets/images/LittleGreenDot.png'>
							<span class='active middleout'  v-if='isPublic'>PUBLIC</span>
							<span v-else>PUBLIC</span>
						</label>
			</div>
				
			</div>
			</div>
					<div class='row ot-detail-titlerow'>
			<div id='ko-title'>
				<div style='width: 10px; hieght: 60px; display: inline-block;'>
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
				<div class='labels iconBtn accessLevelOne'>
					<img class='hover-out' src='../assets/images/More_Icon_Light-01.png' /> 
					<img class='hover-in' style='display: none;' src='../assets/images/More_Icon_Dark-01.png' />
				</div>
		</a></li>
	</ul>
					<div class='inline editwrapper accessLevelOne' id='objBtns'
						style='top: 10px; right: 10px; position: absolute;'>
						<button class='edit' id='metadataeditBtn' v-on:click='editObj'>EDIT</button>
						<button class='edit' id='deleteButton'
							v-on:click='deleteObject'>DELETE</button>
						<a class='edit' id='downloadButton' v-bind:href='downloadLink'>DOWNLOAD</a>
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
				$('ul#tabs li.middleout').removeClass('middleout');
				$('ul#tabs li.active').addClass('middleout');
				//this.autoresize();
				//console.log(this.activeTab);
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
			}); 
	    	$('ul#tabs li.active').addClass('middleout');
	    	$("html, body").animate({
	        	scrollTop: 0
	    	}, 200);
	    	//
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
				var sh = autosize.scrollHeight+15;
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
.nav-tabs>li.active>a, .nav-tabs>li.active>a:focus, .nav-tabs>li.active>a:hover {
    color: #666666;
    cursor: pointer;
    background-color: #fff;
    border: 1px solid #fff;
    border-bottom-color: transparent;
}
ul#tabs li.middleout:after {
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
}
#backButton:hover{
	background-image: url("../assets/images/Back_Icon_Dark-01.png");
		background-repeat: no-repeat;
background-position: left top;
color:#666666;
}
	</style>