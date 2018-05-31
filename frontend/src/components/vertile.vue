<template>
	<div class="container kgl-tile" :class="{'inview':isCurrentView}" v-bind:id="object.metadata.arkId.arkId" v-on:click="selected">
		<div class="row kgl-2">
			<div class="col-md-1 col-sm-1 col-xs-1 kgl-type ft-sz-12">
				<i v-if="object.metadata.published" class='fa fa-circle kg-fg-color'></i>
			</div>
			<div class="col-md-10 col-sm-10 col-xs-10 kgl-title" data-toggle="tooltip"
				data-placement="top" v-bind:title="ver.title">{{ver.title}}
			</div>
			<div class="col-md-1 col-sm-1 col-xs-1 kgl-menu ft-sz-12">
				<div class='dropdown' style='height:15px;overflow:visible' v-click-outside='outside'>
					<a data-target='#' v-on:mouseenter='trigDropdown' v-on:mouseleave='checkDropdown'>
						<div class='row float-r mar-r-15 pad-t-5 lh-1'>
							<i class='fa fa-ellipsis-v kg-fg-color float-r mar-t-5 fa-2x'></i>
						</div>
					</a>
				<ul  class='dropdown-menu ft-sz-12 kg-fg-color' id='pubdropdown' v-if='showPubDropdown' v-on:mouseleave='leaveDropdown'>
					<li><i class='fa fa-ellipsis-v kg-fg-color mar-t-5 pad-l-12 fa-2x'></i></li>
						<li v-if='isMutable && isAdmin'><a v-on:click=''><span>Snapshot</span></a></li>
						<li v-if='isMutable && isAdmin'><a v-on:click=''><span>Edit</span></a></li>
						<li ><a v-bind:href='downloadLink' :download='downloadFile' @click='downloadjson'><span>Download</span></a></li>
						<li ><a v-on:click='copyURL'><span>Copy URL</span></a></li>
						<li v-if='isLoggedIn'><a v-on:click=''><span>Clone</span></a></li>
						<li v-if='isAdmin && !isMutable  && !ver.isdefault'><span>Make Default</span></li>
						<li v-if='isAdmin && !ver.isdefault'><span>Delete</span></li>
						<li v-if='isAdmin && !isMutable && !ver.isdefault' class="brd-t"><span>Withdraw</span></li>
					</ul>
				</div>
			</div>
		</div>
		<div class="row kgl-2">
			<div class="col-md-1 col-sm-1 col-xs-1 kgl-empty"></div>
			<div v-if='isMutable' class='col-md-11 col-sm-11 col-xs-11 pad-0'>
				<div class='objsmalltag ft-sz-12'><span>Draft</span></div>
			</div>
			<div v-else class="col-md-11 col-sm-11 col-xs-11 pad-0">
				<div class="ft-sz-12 inlineblock"><span>{{ver.version}}</span></div>
				<div class='objsmalltag mar-l-10 ft-sz-12' v-if='ver.isdefault'><span>Default</span></div>
			</div>
		</div>
		<div class="row kgl-2">
			<div class="col-md-1 col-sm-1 col-xs-1 kgl-empty"></div>
			<div class="col-md-5 col-sm-5 col-xs-5 pad-0 ft-sz-12">
				<span v-if='isMutable'>Last Updated:</span>
				<span v-text="formattedlastModified"></span>
			</div>
		</div>
		<div class="row kgl-2">
			<div class="col-md-1 col-sm-1 col-xs-1 kgl-empty"></div>
			<div class="col-md-11 col-sm-11 col-xs-11 kgl-keywords" data-toggle="tooltip"
				data-placement="top" v-bind:title="ver.notes"><span  v-if='!isMutable'>What's New: </span>{{ver.notes}}</div>
		</div>
	</div>
</template>
	<script>
	import moment from 'moment'
	export default {
  	name:	"vertile",
		props : [ 'object', 'tileindex', 'ver'],
		data: function() {
			return {
				showPubDropdown:false,
			}
		},
		created: function(){
		},
		computed : {
			isLoggedIn:function(){
				return this.$store.getters.isLoggedIn;
			},
			isAdmin: function(){
				return this.$store.getters.isAdmin;
			},
			uriarray:function(){
				return decodeURIComponent(this.$route.params.uri).split("/")
				},
			objectid:function(){
				return this.object.metadata.arkId.arkId
			},
			objectversion: function(){
				return this.ver.version
			},
			isCurrentView:function(){
				if(this.uriarray[3]){
					return this.ver.version==this.uriarray[3]
				}else{
					if(this.objectversion==""){
						return true
					}else {
						return false
					}
				}
			},
			objuri:function(){
				return this.uriarray[0]+'/'+this.uriarray[1]+'/'+this.objectversion

			},
			formattedlastModified : function() {
				return moment(new Date(this.object.metadata.lastModified)).format('MMMM DD, YYYY')
					+ ' at '+moment(new Date(this.object.metadata.lastModified)).format('hh:mm:ssa')
			},
			objurl: function(){
				return window.location.protocol+'//'+window.location.host+'/'+this.downloadLink
			},
			downloadLink : function() {
				var linkstring='knowledgeObject/'
					+ this.objectid;
				if(this.objectversion!=''){
					linkstring=linkstring+'/'+this.objectversion
					linkstring=linkstring.replace(/[.]/g, '-')
				}else {
					linkstring=linkstring+'.json'
				}
				return linkstring
			},
			downloadFile : function() {
				return this.uriarray[1]+"-"+this.uriarray[2] + '.json'
			},
			isMutable:function(){
				return this.ver.version=="";
			},
		},
		methods : {
			outside: function(){
				if(this.showPubDropdown){
					this.togglePubDropdown();
				}
			},
			downloadjson:function(e){
							e.stopPropagation();

				if(this.showPubDropdown){
					this.togglePubDropdown();
				}
			},
			copyURL:function(e){
							e.stopPropagation();
				this.$store.dispatch('copytoclipboard', this.objurl);
				if(this.showPubDropdown){
					this.togglePubDropdown();
				}
			},
			togglePubDropdown: function () {
				this.showPubDropdown=!this.showPubDropdown;
			},
			setasdefault: function(e){
				e.stopPropagation();
				var i= this.tileindex;
				this.$store.commit('setdefaultversion', this.ver)
				if(this.showPubDropdown){
					this.togglePubDropdown();
				}
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
			selected: function(){
				console.log(this.objuri+"selected");
				this.$eventBus.$emit('objectSelected',this.objuri)
				return false;
			}
		}
	};
	</script>
	<style scoped>
	.kgl-type {
	  vertical-align: middle;
	  height: 25px;
	  width: 60px;
		text-align: center;
		}
	.kgl-empty {
    background-color: #fff;
    height: 0px;
    width: 60px;
		}
	.kgl-tile {
    text-align: left;
    width: 1024px;
		background-color: #fff;
		color: #696969;
    font-weight: 400;
    border-left: 3px solid #fff;
    padding: 0px ;
		transition: all 0.8s ease;
				overflow:visible;
		}
		.kgl-tile.inview {
			border: 1px solid #0075bc;
			border-left: 3px solid #0075bc;
		}
	.kgl-tile:hover {
		border-left: 3px solid #0075bc;
		cursor:pointer;
		}
	.kgl-tile>.row {
		margin: 10px 0px;
				overflow:visible;
		}
	.kgl-type>i{
		line-height:3.1em;
		}
	.kgl-title{
		font-size: 18px;
		font-weight: 400;
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
		padding: 5px 0px 0px 0px;
		background-color: transparent;
		color: #0075bc;
		}
		.kgl-title.draft {
		background-color: #0075bc;
		color: #fff;
		}
	.kgl-keywords {
		font-size: 12px;
		white-space: nowrap;
		overflow: hidden;
  	text-overflow: ellipsis;
    padding: 0px 15px 5px 0px;
		}
	ul.dropdown-menu#pubdropdown {
		position: absolute;
		z-index:9998;
		right: -146.5px;
		top:-5.5px;
		padding:6px 0px 0px 0px;
		width: 150px;
		}
ul.dropdown-menu#pubdropdown li>span{
	height:3em;
	line-height:3em;
	padding-left:12px;
	}
 ul.dropdown-menu#pubdropdown li a {
	height:3em;
	line-height:3em;
}
</style>
