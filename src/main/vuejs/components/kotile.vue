<template>
		<div class="container kgl-tile" v-bind:id="object.uri" v-on:click="selected">
				<div class="row kgl-2">
					<div class="col-md-1 col-sm-1 col-xs-1 kgl-type ft-sz-12">
						<i v-if="object.metadata.published" class='fa fa-circle kg-fg-color  '></i>
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
					<div class="col-md-7 col-sm-7 col-xs-7 kgl-id">
						<span class="kgl-left">Object ID: {{object.uri}}</span>
					</div>
					<div class="col-md-5 col-sm-5 col-xs-5 kgl-udate">
						<span class="kgl-right">Last Updated: <span v-text="formattedlastModified"></span></span>
					</div></div>
					</div>
				</div>
			</div>
	</template>
	<script>
	import moment from 'moment'
	import eventBus from '../components/eventBus.js';
	export default {
  		name:	"kotile",
		props : [ 'object', 'listsize' ,'tileindex'],
		created: function(){

		},
		computed : {
						formattedlastModified : function() {
							return moment(new Date(
									this.object.metadata.lastModified)).format('MMM DD, YYYY')
						},
						formattedCreatedOn : function() {
							return moment(new Date(
									this.object.metadata.createdOn)).format('MMM DD, YYYY')

						},
						objLink : function() {
							return 'home.html#object/'+encodeURIComponent(this.object.uri)
						},
					},
		methods : {
						deleteObject : function(event) {
							var self=this;
							var uri = this.object.uri;
							var txt;
							if (uri != "") {
								var r = confirm("Do you really want to delete the object ? ");
								if (r == true) {
									$.ajax({
												type : 'DELETE',
												url : "knowledgeObject/"
														+ uri,
												success : function(
														response) {
													console.log("Deletion successful!");
													self.$emit('remove');
												}
											});
								}
							}
						},
						selected: function(){
							console.log(this.object.uri+"selected");
							eventBus.$emit("objectSelected", this.object);
							sessionStorage.setItem("otObj", JSON.stringify(this.object));
							return false;
						}
					}
				};
				</script>
				<style>
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
				    padding: 0px;
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
	text-align: right;
	padding-right: 0px;
	right:-10px;
}
.kgl-id span {

}
.kgl-udate {
	text-align: right;
	padding: 0px;
	width: 185px;
}
				</style>
