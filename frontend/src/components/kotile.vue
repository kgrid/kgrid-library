<template>
		<div class="container kgl-tile" v-bind:id="object.uri" v-on:click="selected">
				<div class="row kgl-2">
					<div class="col-md-1 col-sm-1 col-xs-1 kgl-type ft-sz-12">
						<icon style='{width:0.5em;height:1em;margin-top:10px;}' v-if="object.metadata.published" color="#0075bc" name="circle"></icon>
						</div>
					<div class="col-md-11 col-sm-11 col-xs-11 kgl-title kg-fg-color" data-toggle="tooltip"
							data-placement="top" v-bind:title="object.metadata.title">{{object.metadata.title}}
					</div>
				</div>
				<div class="row kgl-2">
					<div class="col-md-1 col-sm-1 col-xs-1 kgl-empty"></div>
					<div class="col-md-11 col-sm-11 col-xs-11 kgl-owner">{{object.metadata.owner}}</div>
				</div>
				<div class="row kgl-2">
					<div class="col-md-1 col-sm-1 col-xs-1 kgl-empty"></div>
					<div class="col-md-5 col-sm-5 col-xs-5 kgl-keywords">Keyword: {{object.metadata.keywords}}</div>
					<div class='col-md-6 col-sm-6 col-xs-6 kgl-iddate'>
					<div class='row'>
					<div class="col-md-8 col-sm-8 col-xs-8 kgl-id">
						<span class="kgl-left">Object ID: {{object.metadata.arkId.arkId}}</span>
						<span v-if='object.metadata.version!=""' class="pad-l-15"> Version: {{object.metadata.version}}</span>
						<span v-else class="pad-l-15"> No Version </span>
					</div>
					<div class="col-md-3 col-sm-3 col-xs-3 kgl-udate">
						<span> Date: <span v-text="formattedlastModified"></span></span>
					</div></div>
					</div>
				</div>

		</div>
	</template>
	<script>
	import moment from 'moment'
	import 'vue-awesome/icons/circle'
	export default {
		name:	"kotile",
		props : [ 'object', 'listsize' ,'tileindex'],
		created: function(){},
		computed : {
			formattedlastModified : function() {
				return moment(new Date(
					this.object.metadata.lastModified)).format('MMM DD, YYYY')
				},
			formattedCreatedOn : function() {
				return moment(new Date(
					this.object.metadata.createdOn)).format('MMM DD, YYYY')
			},
			objuri:function(){
				var s= this.object.metadata.arkId.fedoraPath;
				s = s.replace('-','/')
				if(this.object.metadata.version){
					s=s+'/'+this.object.metadata.version
				}
				return s
			},
		},
		methods : {
			selected: function(){
				this.$eventBus.$emit("objectSelected", this.objuri);
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
	}
	.kgl-tile:hover {
	  border-left: 3px solid #0075bc;
	 cursor:pointer;
	}
	.kgl-tile>.row {
		margin: 10px 0px;
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
		width:950px;
	}
	.kgl-owner{
		font-size: 14px;
		font-style: italic;
		white-space: nowrap;
		overflow: hidden;
		min-height:20px;
  	text-overflow: ellipsis;
    padding: 0px 15px 0px 0px;
	}
	.kgl-keywords, .kgl-id, .kgl-udate {
		font-size: 12px;
		white-space: nowrap;
		overflow: hidden;
  	text-overflow: ellipsis;
    padding: 0px 15px 5px 0px;
		}
	.kgl-keywords {
		width: 450px;
		}
	.kgl-iddate {
		padding-right: 0px;
		}
	.kgl-id {
		padding-right: 0px;
		right:-10px;
		text-align:right;
		}
	.kgl-udate {
		text-align: right;
		padding: 0px;
		}
	</style>
