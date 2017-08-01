<template>
		<div class="container ot-tile" v-bind:id="object.uri" v-on:click="selected">
				<div class="row ot-2">
					<div class="col-md-1 col-sm-1 col-xs-1 ot-type">
						<i v-if="object.metadata.published" class='fa fa-circle kg-fg-color ft-sz-12 '></i>
						</div>
					<div class="col-md-11 col-sm-11 col-xs-11 ot-title" data-toggle="tooltip"
							data-placement="top" v-bind:title="object.metadata.title">{{object.metadata.title}}
					</div>

				</div>
				<div class="row ot-2">
					<div class="col-md-1 col-sm-1 col-xs-1 ot-empty"></div>
					<div class="col-md-11 col-sm-11 col-xs-11 ot-owner">{{object.metadata.owner}}</div>
				</div>
				<div class="row ot-2">
					<div class="col-md-1 col-sm-1 col-xs-1 ot-empty"></div>
					<div class="col-md-5 col-sm-5 col-xs-5 ot-keywords">Keyword: {{object.metadata.keywords}}</div>
					<div class='col-md-6 col-sm-6 col-xs-6 ot-iddate'>
					<div class='row'>
					<div class="col-md-7 col-sm-7 col-xs-7 ot-id">
						<span class="ot-left">Object ID: {{object.uri}}</span>
					</div>
					<div class="col-md-5 col-sm-5 col-xs-5 ot-udate">
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
				.ot-type {
				    vertical-align: middle;
				    height: 25px;
				    width: 60px;
						    text-align: center;
				}
				.ot-empty {
				    background-color: #fff;
			    height: 0px;
			    width: 60px;
				}

				.ot-tile {
				    text-align: left;
				    width: 1024px;
				    background-color: #fff;
				    margin: 10px 0px 0px 0px;
				    color: #696969;
				    font-weight: 400;
				    border-left: 3px solid #fff;
				    padding: 0px ;
						transition: all 0.8s ease;
				}
				.ot-tile:hover {
				   border-left: 3px solid #0075bc;
				}
		/*				.ot-tile:after {
					content: '';
					display: block;
					margin: auto;
					height: 2px;
					width: 0px;
					background: transparent;
					transition: width 0.8s ease, background-color .5s ease;
				}
		.ot-tile:hover:after{
					width:100%;
				background: #00274c;
				}    */
				.ot-tile>.row {
					margin: 10px 0px;
				}

				.ot-type>i{
					line-height:2.6em;

					}

.ot-title{
	font-size: 18px;
	font-weight: 400;
	white-space: nowrap;
	overflow: hidden;
  	text-overflow: ellipsis;
	padding: 5px 0px 0px 0px;
	width:950px;
}

.ot-owner{
	font-size: 14px;
	font-style: italic;
	white-space: nowrap;
	overflow: hidden;
  	text-overflow: ellipsis;
    padding: 0px 15px 0px 0px;
}

.ot-keywords, .ot-id, .ot-udate {
	font-size: 12px;
	white-space: nowrap;
	overflow: hidden;
  	text-overflow: ellipsis;
    padding: 0px 15px 5px 0px;
}
.ot-keywords {
	width: 450px;
}
.ot-iddate {
	padding-right: 0px;
}
.ot-id {
	text-align: right;
	padding-right: 0px;
	right:-10px;
}
.ot-id span {

}
.ot-udate {
	text-align: right;
	padding: 0px;
	width: 185px;
}
				</style>
