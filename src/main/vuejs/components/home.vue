<template>
 <div class='content'>
			<div id='bannerbk'>
				<img src='../assets/bannerBackground.png'>
			</div>
			<applayout :nothelper='true'>
				<div slot='banner'>
					<h1>
						ObjectTeller supports Health Knowledge Services<br> <small>by
					providing a repository toolkit for storing, curating, managing and
					making computable health knowledge accessible for learning health
					systems. </small>
					</h1>
				</div>
				<div slot='header'>
				<div v-show='isLoggedIn'>
					<button class='greenroundbutton ot-newobj'  v-on:click='addObject'>
						<img src='../assets/Plus_Icon.png'/>
					</button>
				</div>
				<div class='row'>
						<div class='col-md-6 ot-search'>
							<span class='ot-glybtn glyphicon glyphicon-search'></span> <input
								placeholder='Search' v-model='newstring'  @keyup.enter='addFilterString'/>
						</div>
						<div class='col-md-1'></div>
						<div class='col-md-2 ot-count'>{{countString}}</div>
						<div class='col-md-2'>
							<select class='ot-select' v-model='sortKey'>
								<option value='metadata.title'>Title</option>
								<option value='uri'>Object ID</option>
								<option value='metadata.lastModified'>Last Updated</option>
							</select>
						</div>
						<div class='col-md-1'>
							<button v-on:click='toggleOrder()'>
								<span v-if='orderAsc'
									class='ot-glybtn glyphicon glyphicon-sort-by-attributes'></span>
								<span v-else
									class='ot-glybtn glyphicon glyphicon-sort-by-attributes-alt'></span>
							</button>
						</div>
					</div>
					</div>
				<div slot='maincontent'>

		<div id='filtercontrol'>
		<div class='row'>
			<div class='col-md-2'>
				<div id='filterBtn'>
				<a href='#filterpanel' data-toggle='collapse'> Filter </a></div>
			</div>
			<div class='col-md-10'>
				<section class='main' v-show='filterStrings.length'>
					<ul class='filterlist'>
						<li v-for='filterstring in filterStrings' class='todo' :key='filterstring.id'>
							<div class='view'>
								<button class='destroy' @click='removeString(filterstring)'>
								<span class='ot-small-glybtn glyphicon glyphicon-remove'></span>
								</button>
								<label>{{ filterstring.title }}</label>
							</div>
						</li>
					</ul>
				</section>
			</div>
		</div>
		<div id='filterpanel' class='collapse'>
			<div class='row'>
				<div class='col-md-6'>
					<div class='row'><p>Search only within the following:</p></div>
					<div class='row'>
						<div class='col-md-5'>
							<input type='checkbox' v-model='check.keywords' /> <label
								for='keywordcheck'>Keywords</label> 
						</div>
						<div class='col-md-7'>
							<input type='checkbox' v-model='check.objectID' /> <label
								for='keywordcheck'>Object ID</label> 
						</div>
						
					</div>
					<div class='row'>
					<div class='col-md-5'>
					<input type='checkbox' v-model='check.owners' /> 
					<label for='keywordcheck'>Owners</label></div>
					<div class='col-md-7'>
							<input type='checkbox' disabled v-model='check.citations' /> <label
								for='keywordcheck'>Citations</label> 
						</div>
					</div>				
					<div class='row'>
					<div class='col-md-5'>
						
					<input type='checkbox' v-model='check.title' /> <label
								for='keywordcheck'>Title</label>
					
					</div>
					<div class='col-md-7'>
							<input type='checkbox'
								v-model='check.contributors' /> <label for='keywordcheck'>Contributors</label>
								</div>
						</div>
											<div class='row'>
					
					<div class='col-md-7'>
							<input type='checkbox' disabled v-model='showmyobj' /> <label
								for='keywordcheck'>View only My Knowledge Objects</label>
								</div>		
								<div class='col-md-5'>				
					</div>
					</div>
				</div>
				<div class='col-md-6'>
					<div class='row'>
							<p>Show Knowledge Object:</p></div>
						<div class='row'>
							
							<div class='col-md-1'></div>
					<div class='col-md-4'><input type='checkbox' id='pub' v-model='check.pub' /> <label
								for='pub'>Public</label> </div>
							<div class='col-md-21'></div>
					<div class='col-md-4'>
								<input type='checkbox' v-if='isLoggedIn'  id = 'pri' v-model='check.pri' /> <label
								v-if='isLoggedIn' for='pri'>Private</label></div>
								<div class='col-md-1'></div>
								</div>
					<div class='row'><p>Select the type of date:</p></div>
					<div class='row datetype'>
					<div class='col-md-1'></div>
					<div class='col-md-4'>
						<label class='radio-inline'><input type='radio' value='lastModified' v-model='datetype'>Last Updated</label>
					</div>
					<div class='col-md-2'></div>
					<div class='col-md-4'>
						<label class='radio-inline'><input type='radio' value='createdOn' v-model='datetype'>Created</label>
					</div>
					<div class='col-md-1'></div>
					</div>
					<div class='row'><p>Search within the following date range:</p></div>
					<div class='row'>
						<div class='col-md-6'>
							<p>Start <input type='text' onchange='setstartdate()' id='startdatepicker'></p>
						</div>
						<div class='col-md-6'>
							<p>End <input type='text' onchange='setenddate()' id='enddatepicker'></p>
						</div>
					</div>
			</div>
		</div>
</div>
</div>				
					<ul>
						<li v-for='(object,index) in orderedList' v-bind:key='index'><ko-tile :object='object'
								:listsize='listSize' :tileindex='index' v-on:remove='orderedList.splice(index, 1)'></ko-tile></li>
					</ul>
				</div>
			</applayout>
		</div>
</template>
<script>
import applayout from './layout/applayout.vue';
export default {
    nme: 'home',
	data : function() {
		return {
			sortKey : 'metadata.lastModified',
			order : 'asc',
			isLoggedIn:false,
			searchQuery : '',
			  model : {
				koList : []
			},
			check:{ keywords : true, owners : true, title : true, citations : false, contributors : false, objectID : false, pub : true, pri : false},
 			showmyobj:false,
 			filterStrings:[],
 			newstring:'',
 			datetype:'lastModified',
 			startdate:0,
			enddate:0,
			userModel:{user:{username:'',password:''}},
			isAdmin:true
 		};
	},
	created : function() {
		var self = this;
		this.startdate = new Date('March 1, 2016').getTime();
		this.enddate=new Date().getTime();
		//$.extend(true,this.userModel,userModel);
		this.isLoggedIn = (this.userModel.user.username!='');
		this.check.pri=this.isLoggedIn;
		retrieveObjectList(function(response) {
			self.model.koList = response;
			if(self.model.koList.length>0){
				$.extend(true,objModel.object,self.model.koList[0]);
			}
		});
		eventBus.$on('objectSelected',function(obj){
			objModel.object=obj;
		});
		eventBus.$on('startdate',function(date){
			self.startdate=date;
		});
		eventBus.$on('enddate',function(date){
			self.enddate=date;
		});
		eventBus.$on('userloggedin',function(obj){
			self.isLoggedIn=true;
			$.extend(true, self.userModel.user,obj);
			self.isAdmin = (self.userModel.user.role=='ADMIN');
			self.check.pri=true;
		});
		eventBus.$on('logout', function(){
			$.extend(true, self.userModel.user, {username:'',password:''});
			self.isLoggedIn=false;
			self.isAdmin=false;
		});	
	},
	mounted:function(){
		otScroll();
	},
	computed : {
		countString : function() {
			var count = this.orderedList.length;
			if (count <= 1) {
				return count + ' Object';
			} else if (count > 200) {
				return 'Many many Objects';
			} else {
				return count + ' Objects';
			}
		},
		listSize : function() {
			return this.filteredList.length;
		},
		orderAsc : function() {
			return (this.order == 'asc');
		},
		orderedList : function() {
					return _.orderBy(this.filteredList, this.sortKey,
					this.order);
		},
		filteredList : function() {
			  var self = this;
			  var list = this.model.koList;
			  if (!this.isLoggedIn) {
  list = list.filter(function(field) {
					  return (field.metadata.published);
				});
			}
			return list.filter(function(field){
										var customFilter = true;
										var filterString = {
											id : 0,
											title : ''
										};
										customFilter=customFilter&&(field.metadata[self.datetype]>=self.startdate && field.metadata[self.datetype]<=self.enddate );
										if(!self.check.pub){
											customFilter=customFilter&&(!field.metadata.published);
										}
										if(!self.check.pri){
											customFilter=customFilter&&(field.metadata.published);
										}
										if (self.filterStrings.length <= 0) {
											
										} else {
											for (var i = 0; i < self.filterStrings.length; i++) {
												var filterResult = false;
												filterString = self.filterStrings[i];
												if (filterString.title === '') {
													filterResult = true;
												} else {
													var fString = new RegExp(filterString.title,'i');
													
													if (self.check.title) {
														filterResult = (filterResult || ((field.metadata.title
																.search(fString))!=-1));
													}
//													console.log(fString.toString()+' '+filterResult);
													if (self.check.keywords) {
														filterResult = (filterResult || ((field.metadata.keywords
																.search(fString))!=-1));													}
													if (self.check.owners) {
														filterResult = (filterResult || ((field.metadata.owner
																.search(fString))!=-1));
													}
													if (self.check.contributors) {
														filterResult = (filterResult || ((field.metadata.contributors
																.search(fString))!=-1));
													}
													if (self.check.objectID) {
														filterResult = (filterResult || ((field.uri
																.search(fString))!=-1));
													}
													if(self.check.citations){
													  if(field.metadata.citations!=null){
														  if(field.metadata.citations.length>0){
															  for(var i=0;i<field.metadata.citations.length;i++){
																  filterResult = (filterResult || ((field.metadata.citations[i].citation_title
																		  .search(fString))!=-1) || ((field.metadata.citations[i].citation_at.search(fString))!=-1));
																  } } }
													   }
													 
												}
													customFilter = customFilter	&& filterResult;
											}

										}
										return customFilter;
			});
		}
	},
	methods : {
		toggleOrder : function() {
			if (this.order == 'asc') {
				this.order = 'desc';
			} else {
				this.order = 'asc';
			}
		},
		addObject:function(){
			eventBus.$emit('addobj','');
		},
		 addFilterString: function () {
			   var value = this.newstring && this.newstring.trim();
			   if (!value) {
			     return;
			   }
			   var uid=this.filterStrings.length;
			   this.filterStrings.push({
			     id: uid++,
			     title: value,
			   });
			   this.newstring = '';
			 },
		 
			 removeString: function (s) {
				   this.filterStrings.splice(this.filterStrings.indexOf(s), 1);
				 },
				 setstartdate:  function(){
					 var startdate=$('#startdatepicker').val();
					 var sstamp=new Date(startdate).getTime();
					 eventBus.$emit('startdate',sstamp);
					console.log('Start date:'+ sstamp);
				 }
		
	},
	components:{'applayout':applayout}
};
</script>
