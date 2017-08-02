<template>
	<div class='content'>
		<applayout :nothelper='true'>
		<div slot='banner'>
		<div class='row ht-40'>
		</div>
		<div class='row ot-detail-titlerow'>
				<div id='ko-title' class='col-md-12 col-sm-12'>
					<div id= 'type-status' >
						<i v-if="objModel.object.metadata.published" class='fa fa-circle kg-fg-color ft-sz-12 '></i>
					</div>
					<h1>
						<small>{{objModel.object.metadata.title}}</small>
					</h1>
				</div>
		</div>
		<div class='row ht-40 pad-0'>
		<div class='col-md-8 col-sm-8 col-xs-8'></div>
		<div class="col-md-4 col-sm-4 col-xs-4 pad-0" >
			<div class='float-r'>
				<ul class="actioniconlist" v-if='true'>
					<li v-show='isLoggedIn'>
						<a  v-on:click='editObj'>
							<div class='actionicon'>
								<i class="fa fa-edit" aria-hidden="false"></i>
							</div>
							<span class='actioniconcap'>Edit</span>
						</a>
					</li>
					<li>
						<a v-bind:href='downloadLink' :download='downloadFile'>
							<div class='actionicon'><i class="fa fa-download" aria-hidden="true"></i></div>
							<div  class='actioniconcap'><span>Download</span></div>
						</a>
					</li>
					<li v-if='isLoggedIn'>
						<a v-on:click='deleteObject'>
							<div class='actionicon'><i class="fa fa-trash" aria-hidden="true"></i></div>
							<div  class='actioniconcap'><span>Delete</span></div>
						</a>
					</li>
				</ul>
			</div>
		</div>
		</div>
		<div class='row ot-detail-daterow'>
							<div class='col-md-3 col-sm-3 col-xs-3'>
					<p class='date-title'>Object ID:</p>
					<p class='date-data'>
						<span>{{objModel.object.uri}}</span>
					</p>
				</div>
				<div class='col-md-2 col-sm-2 col-xs-2'>
					<p class='date-title'>Last Updated:</p>
					<p class='date-data'>
						<span v-text='formattedUpdateDate'></span>
					</p>
				</div>
				<div class='col-md-2 col-sm-2 col-xs-2'>
					<p class='date-title'>Created On:</p>
					<p class='date-data'>
						<span v-text='formattedCreateDate'></span>
					</p>
				</div>
				<div class='col-md-5 col-sm-5 col-xs-5'>
					<div class='float-r mar-r-30'>
						<h6><span>View Type:</span></h6>

							<vselect :value.sync="kgselect.value" :options="optionlist" :searchable='false' :noDrop='!isLoggedIn' :loading='settingPubPri':onChange='selectCallback'></vselect>

					</div>
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
	var $ = require('jquery');
	import applayout from './applayout.vue';
	import tabpane from './tabbedpanel.vue';
	import {tabNav, overlayHeightResize, retrieveObject, retrieveObjectList, otScroll, setBannerbkSize} from '../ot.js';
	import eventBus from '../components/eventBus.js';
	import vselect from '../vendor/vue-select.vue';
	import { objModel, editObjModel, sections, userModel } from '../components/models.js'
	export default {
		name:'ko-detail',
		data : function() {
			return {
				objModel : objModel,
				sections : sections,
				isDisabled: 1,
				isPublic:false,
				settingPubPri:false,
				kgselect:{'value':''},
				optionlist:[{'label':'Public', 'value':'public'},{'label':'Private','value':'private'}],
				publishState: 'Unpublished',
				userModel:{user:{username:'',password:''}},
				activeTab: 'METADATA',
				confirmrequest:{name:"deleteObject",statement:"This Object will be deleted!"},
			}
		},
			components:{
		'applayout':applayout,
		'tabpane':tabpane,
		vselect
		},
		created : function() {
			var self = this;
			eventBus.$on("objSaved",function(obj){
				$.extend(true, self.objModel.object, obj);
			});
			eventBus.$on('objectSelected',function(obj){
				$.extend(true, self.objModel.object, obj);
				otScroll();
			});
			eventBus.$on('userloggedin',function(obj){
				self.isLoggedIn=true;
				$.extend(true, self.userModel.user,obj);
				//self.isAdmin = (self.userModel.user.role=='ADMIN');

			});
			eventBus.$on('confirm', function (data) {
				console.log(data);
				if(data.name=="deleteObject"){
				if(data.val==true){
					var uri = self.objModel.object.uri;
					var txt;
					if (uri != "") {
							$.ajax({
									type : 'DELETE',
									url : "knowledgeObject/"
											+ uri,
									success : function(
											response) {
										eventBus.$emit('objDeleted', self.objModel.object);
									}
								});
					}
				}
				}
			});
			eventBus.$on('logout', function(){
				if(!self.isPublic){
					eventBus.$emit('return');
				}
			});
			$('.dropdown').on('show.bs.dropdown', function(e){
				$(this).find('.dropdown-menu').first().stop(true, true).slideDown(300);
				});
			$('.dropdown').on('hide.bs.dropdown', function(e){
				$(this).find('.dropdown-menu').first().stop(true, true).slideUp(200);
				});
			},
		mounted:function() {
			var self = this;
			retrieveObject(this.$store.state.baseurl, this.$route.params.uri, "complete", function(response) {
				self.objModel.object = response;
				self.isPublic = self.objModel.object.metadata.published;
				if(self.isPublic){
					self.kgselect.value='Public';
				}else {
								self.kgselect.value='Private';
				}
			}, function(response){
				console.log("Error:");
				console.log(response);
				eventBus.$emit('404');
			});
	    	$('ul#tabs li.active').addClass('middleout');
	    	$("html, body").animate({
	        	scrollTop: 0
	    	}, 200);

	    	$('.ot-banner').addClass('detail');
				$(".header").wrap('<div class="theadwrapper"></div>');
				$(".theadwrapper").height($(".header").outerHeight(false));
	    	otScroll();
		},
		computed : {
			isLoggedIn:function(){
				return this.$store.getters.isLoggedIn;
		},
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
				return 'knowledgeObject/'
					+ this.objModel.object.uri+'.json'
			},
			downloadFile : function() {
				return this.objModel.object.uri + '.json'
			}
		},
		updated : function() {
			otScroll();
		},
		methods:{
		selectCallback: function(val){
			var value=val.value;
			 console.log(value);
			 switch(value) {
			 case 'public':
					this.toggleObject(true);
					break;
			 case 'private':
 					this.toggleObject(false);
					break;
					}
		},
		selectTab: function(section){
				console.log("Clicked on "+section.label);
				this.activeTab=section.label;
				if(section.label!='LOG DATA'){
				this.$store.commit('setactivetab',section.label);
				}
		},
			editObj:function(){
				$.extend(true, editObjModel.object, this.objModel.object);
				if(editObjModel.object.outputMessage==null){
					editObjModel.object.outputMessage="";
				}
				if(editObjModel.object.inputMessage==null){
					editObjModel.object.inputMessage="";
				}
				eventBus.$emit('editObj', {'object':this.objModel.obj,'atab':this.activeTab});
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
				self.settingPubPri=true;
				$.ajax({
					beforeSend : function(xhrObj) {
						xhrObj.setRequestHeader("Content-Type", "application/json");
					},
					type : 'PUT',
					url : "knowledgeObject/" + uri + "/" + published,
					success : function(response) {
						self.isPublic=pub;
						objModel.object.metadata.published=pub;
						self.settingPubPri=false;
					},
					error : function(response, tStatus, xhr) {
						console.log(response);
							self.settingPubPri=false;
					}
				});
			},
			deleteObject : function() {
					eventBus.$emit("confirmRequest",this.confirmrequest);
			}
		}
};
	</script>
<style>
.ot-banner.detail {
	height: 220px;
padding: 0px 32px 0px 48px;
margin:0 auto;
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
	margin-top: 0px;
}
.ot-detail-daterow div {
	padding-right: 0px;
}
.ot-detail-daterow .col-md-3 {
	width: 200px;
}
.ot-detail-daterow .col-md-2 {
	width: 120px;
	padding-left:0px;
}
.ot-detail-daterow .col-md-5 {
	width: 550px;
	line-hegiht:3em;
	padding-left:10px;
	margin-top:10px;
}
#type-status {
	width: 10px;
	height: 42px;
	display: inline-block;
	filter: grayscale(100%);
}
#type-status i {
	position: absolute;
	top: 9px;
left: 0px;
}
#ko-title {
	position:relative;
	display: block;
	margin-left:16px;
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
ul#tabs li.active:after, ul#tabs li:hover:after {
    width: 100%;
    background: #0075bc;
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

#actiondiv {
margin-top: 16px;
text-align: right;
padding-right: 0px;
}

#actionlist {
	position: absolute;
	top:199px;
 float:right;
 display:inline-block;
 text-align: left;
 right:-15px;
 height:80px;
 overflow-y: visible;
}
#goback {
	display: inline-block;
	font-size: 12px;
	color: #666666;
}

#backButton a{
	padding-left:30px;
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
	font-size: 11px;
margin-bottom: 0px;
}
.date-data {
	font-size: 14px;
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
#objBtns{
	top: 10px;
    right: 68px;
    position: absolute;
}

#more:hover {
	opacity: 1;
}

#downloadButton {
width:100%;
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
  bottom:-13px;
  margin:0 auto;
  z-index:500;
}
.ot-download .btnContent {
    position: relative;
    top: -29px;
    left: 16px;
    opacity: 0.5;
    transition: opacity 0.5s ease;
}
.ot-back{
	width: 40px;
	height: 40px;
	position:absolute;
    bottom:-17px;
    right:200px;
    margin:0 auto;
    z-index:500;
}

.ot-back .btnContent {
	position: relative;
    top: -22px;
    left:18px;
}

#backButton:hover .greyroundbutton{
	  transform: scale(1.2);
	  border: 1px solid #666666;
	  margin: auto;
	}
#backButton:hover .btnContent{
		 opacity: 1;
		}

.dropdown-menu {
position: absolute;
top: auto;
right: 0;
left: auto;
z-index: 10000;
float: left;
min-width: 160px;
display: block;
padding: 0;
margin: 2px 0 0;
font-size: 14px;
text-align: left;
list-style: none;
background-color: #fff;
-webkit-background-clip: padding-box;
background-clip: padding-box;
border: 1px solid #ccc;
border: 1px solid rgba(0,0,0,.15);
border-radius: 4px;
-webkit-box-shadow: 0 6px 12px rgba(0,0,0,.175);
box-shadow: 0 6px 12px rgba(0,0,0,.175);
z-index:99999;
}
.dropdown ul {
	background-color: #fff;
	color: #333;
}
.dropdown-menu > li > a, #downloadButton a {
    padding: 0px 35px;
    line-height: 2.5em;
		text-decoration: none;
    color: #b3b3b3;
    cursor: pointer;
    transition: color 0.5s ease;
		background-color:transparent;

		}

.dropdown-menu > li>a :hover, #downloadButton:hover, #downloadButton:hover a{

	color:#666666;
}
.nav>li>a {
    position: relative;
    display: block;
    padding: 0px 0px;
    margin: 5px 0px;
}
.nav-tabs>li>a, .nav-tabs>li>a:focus, .nav-tabs>li>a:hover {
    color: #666;
    cursor: pointer;
    background-color: #fff;
    border: 1px solid #fff;
    border-bottom-color: transparent;
}
ul.actioniconlist {
list-style: none;
}
ul.actioniconlist li {
  display: inline-block;
	width: 40px;
	padding: 0px 5px;
	text-align:center;
	overflow-x:visible;
}
ul.actioniconlist li span {
	font-size: 12px;
	opacity: 0;
	position: relative;
	margin: 0 auto;
	transition: opacity 0.5s ease;
}
ul.actioniconlist li:hover span {
	opacity: 1;
}
ul.actioniconlist li>a{
	position: relative;
}
div.actionicon {
	width:26px;
	height: 26px;
	border-radius:100%;
	margin: 0 auto;
	border: 1px solid #666666;
	postion: relative;
}
div.actioniconcap {
	width: 60px;
	margin:auto;
	position: relative;
	left:-50%;
}
div.actionicon i {
	margin: 6px;
	font-size: 14px;
}
</style>
