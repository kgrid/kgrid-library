<template>
	<div class='content'>
		<applayout :nothelper='true'>
			<div slot='banner'>
				<div class='row ht-50 ft-sz-12 kg-fg-color'>		</div>
				<div class='row ht-90 kgl-detail-titlerow'>
					<div id='ko-title' class='col-md-11 col-sm-11 pad-0'>
						<div id= 'type-status' >
							<!-- <icon style='{height:0.5em;}' v-if="knowledgeobject.published" color="#0075bc" name="circle"></icon> -->
						</div>
						<h1>{{knowledgeobject.title}}</h1>
					</div>
				</div>
				<div class='row ht-60'>
					<div v-if='!isMutable' class='col-md-11 col-sm-11 col-xs-11'>
						<div class='objtag ft-sz-18'><span>Draft</span></div>
						<div class='mar-l-20 ft-sz-16 inlineblock'>
							<span v-if='versionList.length>0'>This is not the published version for this object! Go to <a @click='gotodefault'>default version </a></span>
							<span v-else>No published version yet.</span>
						</div>
					</div>
					<div v-else class='col-md-11 col-sm-11 col-xs-11'>
						<div class="inlineblock mar-t-10 ft-sz-24" ><span>{{uriarray[2]}}</span></div>
						<!-- <div class='objtag mar-l-20 ft-sz-18' v-if='isDefault'><span>Default</span></div>
						<div class='mar-l-20 ft-sz-16 inlineblock' v-else>
							<span>This is not the default version for this object! Go to <a @click='gotodefault'>default version </a></span>
						</div> -->
					</div>
					<div class="col-md-1 col-sm-1 col-xs-1 pad-0" >
					</div>
				</div>
				<div class='row ht-60'>

					<div class='inlineblock pad-l-18'>
						<p class='kg-label'>Object ID</p>
						<p class='date-data'>
							<span>{{knowledgeobject.arkId}}</span>
						</p>
					</div>

					<div class='inlineblock pad-l-50'>
						<p class='kg-label'>Last Updated</p>
						<p class='date-data'>
							<span v-text='formattedUpdateDate'></span>
						</p>
					</div>
					<div class='inlineblock pad-l-50'>
						<p v-if='isMutable' class='kg-label'>Created On</p>
						<p v-else class='kg-label'>Generated On</p>
						<p class='date-data'>
							<span v-text='formattedCreateDate'></span>
						</p>
					</div>
					<div class='inlineblock pad-l-50'>
						<div class='float-r wd-70'>
							<p class='kg-label'>View Type</p>
							<div v-if='isAdmin&&isMutable'>
								<div class="spinner" v-if="settingPubPri">
									<icon color="#fff" name="sync" spin></icon>
									<span class="sr-only">Loading...</span>
								</div>
								<div class='dropdown' id="pubDropdown" v-else style='height:15px;' v-click-outside='outside'>
									<a data-target='#' v-on:mouseenter='trigDropdown' v-on:mouseleave='checkDropdown'>
										<div class='row float-r mar-r-12 pad-t-2 lh-1'>
											<span class=' ft-sz-14 kg-fg-color'>{{pubState}}</span>
											<i id='pubdropdowniconimg' class='fa fa-caret-down kg-fg-color' :class="{down:showPubDropdown, up:!showPubDropdown}"></i>
										</div>
									</a>
									<ul class='dropdown-menu mar-top20' id='pubdropdown' v-if='showPubDropdown' v-on:mouseleave='leaveDropdown'>
										<li><a v-on:click='setPublic'><span>Public</span></a></li>
										<li><a v-on:click='setPrivate'><span>Private</span></a></li>
									</ul>
								</div>
							</div>
							<div v-else>
								<div class='row float-r mar-r-12 pad-t-2 lh-1'>
									<span class=' ft-sz-14'>{{pubState}}</span>
								</div>
							</div>
						</div>
					</div>
					<div class='pad-l-50 inlineblock float-r'>
									<ul class="actioniconlist float-r" v-if='true'>
										<li>
										<div>
											<a :href='htmldownloadlink' download>
												<div class='actionicon'><icon color="#fff" name="download"></icon></div>
											</a>
											<div class='actioniconcap'>
												<span>DOWNLOAD</span></div>
												</div>
										</li>
										<!-- <li>
										<div>
											<a @click='viewswagger'>
												<div class='actionicon'><icon color="#fff" name="eye"></icon></div>
											</a>
											<div class='actioniconcap'>
												<span>VIEW I/O</span></div>
												</div>
										</li> -->
										<li>
										<div>
											<a>
											<div class='actionicon' v-on:click='copyurlrequest'>
													<icon color="#fff" name="clipboard"></icon>
											</div>
											</a>
											<div class='actioniconcap'>
											<span >COPY URL</span>
											</div>
											</div>
										</li>
										<li>
										<div>
											<a>
											<div class='actionicon' v-on:click='sendtoactivator'>
													<icon color="#fff" name="play"></icon>
											</div>
											</a>
											<div class='actioniconcap'>
											<span >Live Demo</span>
											</div>
											</div>
										</li>
										<li v-if='isLoggedIn && !isMutable' v-show='false'>
										<div>
											<a>
											<div class='actionicon' v-on:click='cloneobj'>
													<icon color="#fff" name="clone"></icon>
											</div>
											</a>
											<div class='actioniconcap' >
											<span >CLONE</span>
											</div>
											</div>
										</li>
										<li v-show='false' v-if='isAdmin && !isMutable && !isDefault'>
											<div>
												<a>
													<div class='actionicon' v-if='isAdmin' v-on:click='setDefault'><icon color="#fff" name="anchor"></icon></div>
												</a>
												<div class='actioniconcap'>
													<span>SET as DEFAULT</span>
												</div>
											</div>
										</li>
										<li v-if='isLoggedIn'>
					<div>
						<a>
						<div class='actionicon'  v-on:click='deleteObject'><icon color="#fff" name="trash-alt"></icon></div>
						</a>
						<div class='actioniconcap'>
								<span>DELETE</span></div>
												</div>
					</li>
									</ul>
								</div>
				</div>
			</div>
			<div slot='header'>
				<div class='row'>
					<div class='col-md-8'>
				<ul class='nav nav-tabs view inline' role='tablist' id='tabs'>
					<li v-for='section in constSections' v-bind:id='section.name' v-if='section.name!="assets"' v-show='(section.name=="metadata"&&isEditMode)|(!isEditMode)' v-bind:class="{ active: activeTab === section.label }" v-on:click="selectTab(section)">
						<a >{{section.label}}</a>
							<div v-show='validobj' v-if='section.name=="metadata" && activeTab === section.label && !isEditMode &&isLoggedIn' style='position:absolute;top: 0px;right:-20px;' @click='seteditmode'>
								<icon color="#0075bc" name="regular/edit" ></icon>
							</div>
					</li>
					<li role='presentation' class='labels' v-show='false'>
						<a><icon color="#555" name="ellipsis-h"></icon></a>
					</li>
				</ul>
			</div>
			<div class='col-md-4' style='padding-left:0px;'>
				<button class='inline kg-btn' @click='clickcancel' v-if='isEditMode'>Cancel</button>
				<button class='inline kg-btn' @click='discardChange' v-if='isEditMode'>Undo</button>
				<button class='inline kg-btn' @click='saveChange' v-if='isEditMode'>Save</button>
		</div>
	</div>
			</div>
			<div slot='maincontent'>
					<ul class='tab-content view' id='tab'>
						<li v-for='section in constSections' v-bind:id='section.name' v-if='section.name!="versions"&&section.name!="assets"'
								 v-bind:class="{ active: activeTab === section.label }">
								<tabpane v-bind:section='section.name' v-bind:object='knowledgeobject' v-bind:isDisabled='!isEditMode'></tabpane>
						</li>
						<li id="versions" v-bind:class="{ active: activeTab === 'VERSIONS' }">
							<ul class=''>
								<li v-for='(ver,index) in versionList'>
									<vertile :object='knowledgeobject' :ver='ver'
																:tileindex='index' ></vertile>
								</li>
							</ul>
						</li>
					</ul>
				</div>
			</applayout>
	</div>
	</template>
	<script>
	import store from '../store'
	import axios from 'axios';
	import moment from 'moment';
	import applayout from './applayout.vue';
	import tabpane from './tabbedpanel.vue';
	import vertile from './vertile.vue';
	import 'vue-awesome/icons/anchor'
	import 'vue-awesome/icons/copy'
	import 'vue-awesome/icons/clipboard'
	import 'vue-awesome/icons/clone'
	import 'vue-awesome/icons/trash-alt'
	import 'vue-awesome/icons/ellipsis-h'
	import 'vue-awesome/icons/circle'
	import 'vue-awesome/icons/regular/edit'
	import 'vue-awesome/icons/download'
	import 'vue-awesome/icons/camera'
	import 'vue-awesome/icons/sync'
		import 'vue-awesome/icons/trash-alt'
			import 'vue-awesome/icons/play'
	export default {
		name:'ko-detail',
		data : function() {
			return {
				isDisabled: 1,
				showPubDropdown:false,
				pubpriwidth: '50px',
				isPublic:false,
				settingPubPri:false,
				activeTab: 'METADATA',
				confirmrequest:{name:"deleteObject",statement:"Are you sure that you want to delete this object?", content:'',btnText:"Delete"},
				showVersions:false,
				retrievingVersion:false,
				isEditMode:false
			}
		},
		components:{
			applayout,
			tabpane,
			vertile
		},
		created : function() {
			var self = this;
			this.$eventBus.$on("objSaved",function(obj){
				self.knowledgeobject.citations.splice(0,self.knowledgeobject.citations.length);
				self.knowledgeobject=JSON.parse(JSON.stringify(obj));
			});
			this.$eventBus.$on('objectSelected',this.objselectedlistener);
			this.$eventBus.$on('confirm', function (data) {
				console.log(data);
				if(data.val==true){
					if(data.name=="cloneobj"){
						var data=self.objectid+'/'+self.objectversion
						self.$store.dispatch('createobject', data);
					}
					if(data.name=="copyurl"){
						self.copyURL();
					}
					if(data.name=="snapshot"){
						self.addversion();
					}
					if(data.name=="deleteObject"){
						var uri = self.downloadLink.replace(/-/g,'.');
						var txt;
						if (uri != "") {
							$.ajax({
									type : 'DELETE',
									url : uri,
									success : function(
											response) {
											self.$eventBus.$emit('objDeleted');
									},
									error:function(errorresp){
										console.log(erroeresp)
									}
							});
						}
					}
					if(data.name=="setDefault"){
						self.$store.dispatch('setdefaultversion', self.objectid+'/'+self.objectversion)
					}
				}
			});
			this.$eventBus.$on('logout', function(){
				if(!self.isPublic){
					self.$eventBus.$emit('return');
				}
			});
			}
			, beforeRouteEnter (to, from, next) {
				axios.get("./static/json/config.json").then(response=> {
					if(!store.state.paths.shelf_url){
						store.commit('setpaths',response.data);
					}
			 		store.dispatch('fetchko', to.params).then(function(){
			 			next()
			 		})
				})
			}
			, beforeDestroy:function(){
					this.$eventBus.$off('objectSelected', this.objselectedlistener)
			}
			, mounted:function() {
			var self = this;
			this.$store.dispatch('fetchversionlist', this.objectid);
			this.$store.commit('setcurrenturl',this.$route.params.uri);
			window.scrollTo(0, 0)
		},
		computed : {
			user: function(){
				return this.$store.getters.getuser;
			},
			constSections:function(){
				return this.$store.getters.constSections
			},
		  uriarray:function(){
				console.log("decode:"+decodeURIComponent(this.$route.params.uri))
				return decodeURIComponent(this.$route.params.uri).split("/")
		 	},
			objectid:function(){
				return this.uriarray[0]+'/'+this.uriarray[1]
			},
			objectversion: function(){
				return this.uriarray[2]
			},
			objecturi:function() {
					return this.uriarray[0]+'/'+this.uriarray[1]+'/'+this.uriarray[2]
			},
		isMutable:function(){
			return true
		},
		objurl: function(){
			return this.$store.getters.getcurrenturl;
		},
		rawobj: function(){
			var obj = this.$store.getters.getobject
			if(obj.metadata){
				return obj.metadata
			} else {
				return obj
			}
		},
		validobj:function(){
			var obj = this.$store.getters.getobject
			return !obj.metadata
		},
		knowledgeobject: function(){
			console.log(this.rawobj)
			var obj={}
			if(this.rawobj){
				obj=JSON.parse(JSON.stringify(this.rawobj))
			}else {					//ld
			}
			return obj;
		},
		versionList:function(){
			return this.$store.getters.getverlist;
		},
		defaultversion: function(){
			var f = this.versionList.filter(function(e){return e.isdefault==true})
			if(f.length==1){
				return f[0].version
			}else {
				return ""
			}
		},
		defaulturi: function(){
				return this.$route.params.uri
		},
		isAdmin: function() {
			return this.$store.getters.isAdmin;
		},
		isDefault:function(){
			return true
		},
			isLoggedIn:function(){
				return this.$store.getters.isLoggedIn;
		},
			pubState: function(){
				return this.isPublic ? 'Public' : 'Private'
			},
			formattedUpdateDate : function() {
				if(!this.knowledgeobject.lastModified || this.knowledgeobject.lastModified=="" ){
					return ""
				}
				else
					{return moment(new Date(
							this.knowledgeobject.lastModified)).format('MMM DD, YYYY')
							}
				},
			formattedCreateDate : function() {
					if(!this.knowledgeobject.createdOn || this.knowledgeobject.createdOn=="" ){
						return ""
					}
					else
						{return moment(new Date(
								this.knowledgeobject.createdOn)).format('MMM DD, YYYY')
								}
					},
				downloadLink : function() {
					return this.$store.getters.getcurrenturl
				},
			htmldownloadlink: function(){
				return this.downloadLink+'?format=zip'
			},
			downloadFile : function() {
				return this.uriarray[0]+"-"+this.uriarray[1] +"-"+this.uriarray[2]+ '.zip'
			}
		},
		methods:{
			objselectedlistener:function(t){
			 this.$store.dispatch('fetchko', this.$route.params).then(function(){
				 // self.nextTick()
			 })
			 this.selectTab(this.constSections[0])
		 },
			viewswagger:function(){
				console.log("View Service Descriptor using Swagger UI")
				if(this.fieldname=='service') this.$eventBus.$emit('viewio','');
			},
			seteditmode:function(){
				this.isEditMode = true;
				this.$store.commit('updateMetadata',this.knowledgeobject)
			},
			clickcancel:function() {
				this.$eventBus.$emit('revertEdit')
				this.isEditMode = false;
			},
			discardChange:function(){
				console.log('discard changes')
				this.$eventBus.$emit('revertEdit')
			},
			saveChange:function(){
				this.isEditMode = false;
				this.$eventBus.$emit('saveMetadata')
			},
			cloneobj:function(){
				var req={};
				req.name='cloneobj';
				req.statement="Is this the correct object you'd like to clone?"
				req.content=this.objectid+"      "+this.objectversion
				req.btnText="Clone";
				this.$eventBus.$emit("confirmRequest",req)
			},
			gotodefault:function(){
				this.$eventBus.$emit('objectSelected',this.defaulturi)
			},
			setasDefault: function(){
				this.$store.commit('setdefaultversion', this.uriarray[2])
			},
			outside: function(){
				if(this.showPubDropdown){
					this.togglePubDropdown();
				}
			},
			copyURL:function(){
				var t=this.objurl;
				this.$store.dispatch('copytoclipboard', t);
			},
		addversion:function(){
			var self=this;
			var text=null;
			var ajaxJSON = {
				type : "POST",
				url : "knowledgeObject",
				contentType:"application/json",
				success : function(response) {
					if ((response != 'empty') && (response != null)) {
						var test = JSON.stringify(response);
						var obj = JSON.parse(test);
						self.retrievingVersion=false;
						self.$store.dispatch('fetchversionlist', self.$route.params.uri)
					}
					},
				error : function(response) {
				}
			};
			ajaxJSON.url="knowledgeObject/"+this.$route.params.uri;
			self.retrievingVersion=true;
			console.log(ajaxJSON);
			$.ajax(ajaxJSON);
		},
		togglePubDropdown: function () {
			this.showPubDropdown=!this.showPubDropdown;
			},
		trigDropdown: function(){
					this.showPubDropdown=true;
			},
		checkDropdown: function(){
					this.showPubDropdown=true;
			},
		leaveDropdown:function(){
					this.showPubDropdown=false;
			},
		setPublic: function(){
			this.toggleObject(true);
		},
		setPrivate: function(){
			this.toggleObject(false);
		},
		selectTab: function(section){
				this.activeTab=section.label;
				if(section.label=='VERSIONS'){

				}else{
					if(section.label!='LOG DATA'){
						this.$store.commit('setactivetab',section.label);
					}
				}
		},
			editObj:function(){
				this.$eventBus.$emit('editObj', {'object':this.knowledgeobject,'atab':this.activeTab});
			},
			toggleObject:function(pub){
				var uri=this.knowledgeobject.uri;
				var published='';
				var self=this;
				if(pub){
					published='published';
				}else{
					published='unpublished';
				}
				self.settingPubPri=true;
				self.showPubDropdown=false;
				$.ajax({
					beforeSend : function(xhrObj) {
						xhrObj.setRequestHeader("Content-Type", "application/json");
					},
					type : 'PUT',
					url : "knowledgeObject/" + uri + "/" + published,
					success : function(response) {
						self.isPublic=pub;
						knowledgeobject.published=pub;
						self.settingPubPri=false;
					},
					error : function(response, tStatus, xhr) {
						console.log(response);
						if(response.status=='500'){
							self.isPublic=pub;
							knowledgeobject.published=pub;
						}
						self.settingPubPri=false;
					}
				});
			},
			deleteObject : function() {
					this.confirmrequest.content=this.objectid+"      "+this.objectversion
					this.$eventBus.$emit("confirmRequest",this.confirmrequest);
			},
			setDefault : function() {
					this.$eventBus.$emit("confirmRequest",{name:"setDefault",statement:"Are you sure that you want to set this version as Default?",btnText:"Set as Default"});
			},
			snapshotrequest:function(){
				var req={};
				req.name='snapshot';
				req.statement="Are you ready to snapshot this draft to add a version?"
				req.content=this.objectid;
				req.btnText="Add a Version";
				this.$eventBus.$emit("confirmRequest",req)
			},
			copyurlrequest:function(){
				var req={};
				req.name='copyurl';
				req.statement="Is this the correct url?"
				req.content= location.protocol+'//'+location.hostname+(location.port ? ':'+location.port: '')+this.objurl.substr(1);
				req.btnText="Copy";
				this.$eventBus.$emit("confirmRequest",req)
			},
			returntolibrary: function(){
				this.$eventBus.$emit("return");
			},
			sendtoactivator : function(){
				this.$eventBus.$emit('objactivation','')
			}
		}
};
	</script>
<style>
.kgl-detail-titlerow {
	margin-left: -40px;
}
#type-status {
position: absolute;
	width: 10px;
	height: 42px;
	display: inline-block;
}
#type-status svg {
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
	position: relative;
  display: inline-block;
  left: 24px;
  margin: 0;
  text-align: left;
  padding: 0 20px 0 0;
	line-height: 1.15em;
}
.nav-tabs>li.active>a, .nav-tabs>li.active>a:focus, .nav-tabs>li.active>a:hover {
color:#0075bc;
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
ul#tab {
    list-style-type: none;
    margin: 22px 0px 0px 0px;
    padding: 0;
    width: 100%;
    float: left;
    min-height: 800px;
    box-sizing: border-box;
		overflow:visible;
}
ul#tab>li {
	display:none;
}
ul#tab>li.active {
	display:block;
	padding: 36px 36px;
	min-height:800px;
}
.date-title {
	font-size: 11px;
margin-bottom: 0px;
}
.date-data {
	font-size: 14px;
}
i#pubdropdowniconimg {
	transition: transform 0.5s linear;
}
i#pubdropdowniconimg.up {
-moz-transform: scaleY(-1);
-o-transform: scaleY(-1);
-webkit-transform: scaleY(-1);
transform: scaleY(-1);
}
i#pubdropdowniconimg.down {
-moz-transform: scaleY(1);
-o-transform: scaleY(1);
-webkit-transform: scaleY(1);
transform: scaleY(1);
}
.kg-btn {
	color:#0075bc;
	background-color:#fff;
	line-height:2em;
	padding-top:8px;
}
.dropdown-menu {
position: absolute;
top:auto;
right: 0;
left: auto;
z-index: 10000;
float: left;
min-width: 80px;
display: block;
padding: 0;
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
.dropdown-menu > li > a {
    padding: 0px 10px;
    line-height: 2.5em;
		text-decoration: none;
    cursor: pointer;
    transition: color 0.5s ease;
		background-color:transparent;
	}
.nav-tabs>li>a, .nav-tabs>li>a:focus, .nav-tabs>li>a:hover {
    cursor: pointer;
    background-color: #fff;
    border: 1px solid #fff;
    border-bottom-color: transparent;
}
ul.dropdown-menu#pubdropdown {
	width: 50px;
}
div.spinner {
 text-align:center;
}
.ver-dropdown {
		display:inline-block;
}
ul#tabs li {
	display: block;
	float: left;
	color: #b3b3b3;
	font-size: 14px;
	font-weight: 400;
	text-decoration: none;
	text-align: center;
	line-height: 2;
	margin-top: 9px;
	margin-bottom: 0px;
	margin-left: 48px;
	margin-right: 2px;
	cursor: pointer;
	vertical-align: bottom;
	padding: 0px 0px 0px 0px;
	transition: color 0.5s ease;
}
ul#tab>li {
	background-color:#fff;
}
ul#tab>li#versions, ul#tab>li#versions.active {
	background-color:#e5e5e5;
	padding:0px;
	height: 100%;
}
li#versions {
		overflow:visible;
		}
li#versions ul {
	list-style: none;
			overflow:visible;
}
li#versions>ul>li{
	margin:0px 0px 20px 0px;
				overflow:visible;
}
		.nav-tabs>li>a:hover {
		    border-color: #fff;
		}
		.nav-tabs {
		    border-bottom: 1px solid #fff;
		}
		.nav-tabs>li>a {
		    margin-right: 2px;
		    line-height: 1.42857143;
		    border: 1px solid transparent;
		    border-radius: 4px 4px 0 0;
		}
		.actionicon svg {
			margin-top:4px;
		}
</style>
