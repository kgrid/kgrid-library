<template> 
		<div class="container ot-tile" v-bind:id="object.uri" v-on:click="selected">
				<div class="row ot-2">
					<div class="col-md-1 ot-type">
						<img v-if="object.metadata.published" src="../assets/images/LittleGreenDot.png" width="8px"	height="auto" />
					</div>
					<div class="col-md-10 ot-title" data-toggle="tooltip"
							data-placement="top" v-bind:title="object.metadata.title">{{object.metadata.title}}
					</div>
					<div class="col-md-1 ot-empty-1"></div>
				</div>
				<div class="row ot-2">
					<div class="col-md-1 ot-empty"></div>
					<div class="col-md-11 ot-owner">{{object.metadata.owner}}</div>
				</div>
				<div class="row ot-2">
					<div class="col-md-1 ot-empty"></div>
					<div class="col-md-5 ot-keywords">Keyword:{{object.metadata.keywords}}</div>
					<div class='col-md-6 ot-iddate'>
					<div class='row'>
					<div class="col-md-6 ot-id">
						<span class="ot-left">Object ID: {{object.uri}}</span>
					</div>
					<div class="col-md-6 ot-udate">
						<span class="ot-right">Last Updated: <span v-text="formattedlastModified"></span></span>
					</div></div>
					</div>
				</div>
			</div>
	</template>
	<script>
	import eventBus from '../components/eventBus.js';
	export default {
  		name:	"kotile",
		props : [ 'object', 'listsize' ,'tileindex'],
		created: function(){

		},
		computed : {
						formattedlastModified : function() {
							return new Date(
									this.object.metadata.lastModified)
									.format("mediumDate")
						},
						formattedCreatedOn : function() {
							return new Date(
									this.object.metadata.createdOn)
									.format("mediumDate")
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
												url : "/ObjectTeller/knowledgeObject/"
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
				.ot-type {
				    vertical-align: middle;
				    height: 40px;
				    width: 48px;
				}
				.ot-empty {
				    background-color: #fff;
			    height: 0px;
			    width: 48px;
				}
				.ot-id {
					text-align: right;
			    	padding-right: 0;
				}
				.ot-tile {
				    text-align: left;
				    width: 1024px;
				    background-color: #fff;
				    margin-top: 5px;
				    margin-bottom: 5px;
				    color: #696969;
				    font-weight: 400;
				    border-bottom: 2px solid #fff;
				    padding: 8px;
				    transition: border-bottom .8s ease-in-out;
				}
				</style>