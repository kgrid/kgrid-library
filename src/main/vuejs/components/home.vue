<template>
<div class='content'>
	<applayout :nothelper='true'>
		<div slot='banner'>
			<div v-if="isLoggedIn">
				<h1>Hello, {{userModel.user.first_name}}!<br>Need to invite others? <router-link to='/soon'>Add Users.</router-link></h1>
			</div>
			<div v-else>
				<h1>Object Teller is a repository for storing, curating, managing,<br> and
				making accessible health knowledge for <br>learning health
				systems. <br>Get Started, <router-link to='/soon'>Sign-Up.</router-link></h1>
			</div>
			<div id='libname'>{{libraryname}}</div>
			<div id="bannericons" v-show='isLoggedIn'>
			<ul id="bannericonrow">
				<li><div style="position: relative">
						<button class="roundbutton iconBtn" id="userlink"
							@click="userlink_click">
						</button>
						<button class="greenroundbutton iconBtn" id="newuser">
						</button>
					</div></li>
				<li>
					<button class="roundbutton open-overlay iconBtn" type="button"
						id="settinglink" disabled @click="settinglink_click">
						
					</button>
				</li>
				
			</ul>
			<div class="floatingInfo" id="homeIcons">
				<span></span>
			</div>
		</div>
			
		</div>
		<div slot='header'>
				<div v-show='isLoggedIn' class='ot-r-btn ot-newobj'  v-on:click='addObject''>
			       <div class='greenroundbutton' > </div>
			       <div class='btnContent'><img src='../assets/images/Plus_Icon.png' width="14px"/></div>
			</div>
				
				
				
					
				<div class='row'>
						<div class='col-md-6 ot-search'>
						<img src='../assets/images/Search_Icon-01.png' width="20px"/><input
								placeholder='Search by Keywords, Title, Owners or Object ID' v-model='newstring'  @keyup.enter='addFilterString'/>
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
				<a v-on:click='toggleFilter'> Filters <span><img
						id='filterdowniconimg' class='down'
						src='../assets/images/dropdown_chevron.png' width='12px' /></span></a></div>
			</div>
			<div class='col-md-10'>
				<section class='main' v-show='filterStrings.length'>
					<ul class='filterlist'>
						<li v-for='filterstring in filterStrings' class='todo' :key='filterstring.id'>
							
								<button class='destroy' @click='removeString(filterstring)'>
								</button>
								<label>{{ filterstring.title }}</label>
							
						</li>
						<button id='clearAll' @click='removeAllFilters'>Clear Filters</button>
					</ul>
		
				</section>
			</div>
		</div>
		<div id='filterpanel' v-if='showFilterControl'>
			<div class='row'>
				<div class='col-md-6'>
					<div class='row filter'><p>Search only within the following:</p></div>
					<div class='row filter'>
						<div class='col-md-4'>
 							<label class="custom-control custom-checkbox">
 								<input v-model='check.keywords' type="checkbox" class="custom-control-input">
 								<span class="custom-control-indicator"></span>
								<span class="custom-control-description">Keywords</span>
							</label>
						</div>
						<div class='col-md-8'>
							<label class="custom-control custom-checkbox">
								<input v-model='check.objectID' type="checkbox" class="custom-control-input">
								<span class="custom-control-indicator"></span>
								<span class="custom-control-description">Object ID</span>
							</label>					
						</div>
					</div>
					<div class='row filter'>
					<div class='col-md-4'>
					<label class="custom-control custom-checkbox">
					<input v-model='check.owners' type="checkbox" class="custom-control-input">
					<span class="custom-control-indicator"></span>
					<span class="custom-control-description">Owners</span>
				</label>	
					</div>
					<div class='col-md-8'>
					<label class="custom-control custom-checkbox">
					<input v-model='check.citations' disabled type="checkbox" class="custom-control-input">
					<span class="custom-control-indicator"></span>
					<span class="custom-control-description">Citations</span>
				</label>								
						</div>
					</div>				
					<div class='row filter'>
					<div class='col-md-4'>
					<label class="custom-control custom-checkbox">
					<input v-model='check.title' type="checkbox" class="custom-control-input">
					<span class="custom-control-indicator"></span>
					<span class="custom-control-description">Title</span>
				</label>	
					
					</div>
					<div class='col-md-8'>
					<label class="custom-control custom-checkbox">
					<input v-model='check.contributors' type="checkbox" class="custom-control-input">
					<span class="custom-control-indicator"></span>
					<span class="custom-control-description">Contributors</span>
				</label>	
							
								</div>
						</div>
											<div class='row filter'>
					
					<div class='col-md-12'>
					<label class="custom-control custom-checkbox">
					<input v-model='showmyobj' disabled type="checkbox" class="custom-control-input">
					<span class="custom-control-indicator"></span>
					<span class="custom-control-description">View only My Objects</span>
				</label>	
							
								</div>		
								<div class='col-md-5'>				
					</div>
					</div>
				</div>
				<div class='col-md-6'>
					<div class='row'>
							<p>Show Knowledge Object:</p></div>
						<div class='row filter datetype'>
							
							<div class='col-md-1'></div>
					<div class='col-md-4'>
					<label class="custom-control custom-checkbox">
					<input v-model='check.pub' id='pub' type="checkbox" class="custom-control-input">
					<span class="custom-control-indicator"></span>
					<span class="custom-control-description">Public</span>
				</label>	
					 </div>
							<div class='col-md-2'></div>
					<div class='col-md-4'>
					<label v-if='isLoggedIn' class="custom-control custom-checkbox">
					<input v-model='check.pri' id='pri' type="checkbox" class="custom-control-input">
					<span class="custom-control-indicator"></span>
					<span class="custom-control-description">Private</span>
				</label>	
								</div>
								<div class='col-md-1'></div>
								</div>
					<div class='row filter'><p>Select the type of date:</p></div>
					<div class='row filter datetype'>
					<div class='col-md-1'></div>
					<div class='col-md-4'>
					<label class="custom-control custom-radio">
					<input id="radio1" value='lastModified' v-model='datetype' name="radio" type="radio" class="custom-control-input">
					<span class="custom-control-indicator"></span>
					<span class="custom-control-description">Last Updated</span>
					</label>
						
					</div>
					<div class='col-md-2'></div>
					<div class='col-md-4'>
					<label class="custom-control custom-radio">
					<input id="radio1" value='createdOn' v-model='datetype' name="radio" type="radio" class="custom-control-input">
					<span class="custom-control-indicator"></span>
					<span class="custom-control-description">Created</span>
					</label>
						
					</div>
					<div class='col-md-1'></div>
					</div>
					<div class='row filter'><p>Search within the following date range:</p></div>
					<div class='row filter'>
						<div class='col-md-6'>
							<p>Start <date-picker :date="startTime" :option="option" class='leftalign' :limit="limit" v-on:change='setstartdate()' id='startdatepicker'></date-picker> </p>
						</div>
						<div class='col-md-6'>
							<p>End  <date-picker :date="endtime" :option="option" class='rightalign' :limit="limit"  v-on:change='setenddate()' id='enddatepicker'></p>
						</div>
					</div>
			</div>
		</div>
</div>
</div>				
<ul>
<li v-for='(object,index) in orderedList' v-bind:key='index'><kotile :object='object'
								:listsize='listSize' :tileindex='index' v-on:remove='orderedList.splice(index, 1)'></kotile></li>
					</ul>
				</div>
			</applayout>
		</div>
</template>
<script>
import myDatepicker from '../vendor/vue-datepicker.vue'
import applayout from './applayout.vue';
import kotile from './kotile.vue';
import {getCurrentUser, overlayHeightResize, retrieveObject, retrieveObjectList, setenddate, setstartdate, otScroll, setBannerbkSize} from '../ot.js';
import eventBus from '../components/eventBus.js';
import { objModel, editObjModel, sections, userModel } from '../components/models.js'

export default {
    name: 'home',
	data : function() {
		return {
			libraryname : 'Department of Learning Health Systems Development Server',
			sortKey : 'metadata.lastModified',
			order : 'desc',
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
			isAdmin:true,
			showFilterControl:false,
			startTime: {
		        time: '01/01/2016'
		      },
		      endtime: {
		        time: 'Today'
		      },
		      option: {
		          type: 'day',
		          SundayFirst: true,
		          week: ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa'],
		          month: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
		          format: 'MM/DD/YYYY',
		          placeholder: 'MM/DD/YYYY',
		          inputStyle: {
		            'display': 'inline-block',
		            'height':'30px',
		            'padding': '3px 0px 3px 10px',
		            'margin':'0px 0px 0px 5px',
		            'line-height': '20px',
		            'font-size': '14px',
		            'border': '1px solid #39b45a',
		            'box-shadow': 'none',
		            'border-radius': '2px',
		            'color': '#5F5F5F',
		            'width': '134px'
		          },
		          color: {
		            checkedDay: '#e6e6e6',
		            header: '#fff',
		            headerText: '#666666'
		          },
		          buttons: {
		            ok: 'Ok',
		            cancel: 'Cancel'
		          },
		          overlayOpacity: 0.5, // 0.5 as default
		          dismissible: true // as true as default
		        },
		        timeoption: {
		          type: 'min',
		          week: ['Su','Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa'],
		          month: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
		          format: 'YYYY-MM-DD HH:mm'
		        },
		        multiOption: {
		          type: 'multi-day',
		          week: ['Su','Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa'],
		          month: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
		          format: 'YYYY-MM-DD HH:mm'
		        },
		        limit: [{
		          type: 'weekday',
		          available: [0, 1, 2, 3, 4, 5, 6]
		        },
		        {
		          type: 'fromto',
		          from: '2016-01-01',
		          to: ''
		        }]
		      }
		    },
	created : function() {
		var self = this;
	  	getCurrentUser(function(response) {
				if(response!="")
					$.extend(true, self.userModel.user, response);
				$('.ot-banner').addClass('loggedin');
			},function(response) {
				console.log(response);
			});
		$("#startdatepicker").val("03/01/16");
		$("#enddatepicker").val(new Date().format("shortDate"));
		this.startdate = new Date('March 1, 2016').getTime();
		this.enddate=new Date().getTime();
		console.log('Home created ==> '+ userModel.user.username);
		$.extend(true,this.userModel,userModel);
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
			$('.ot-banner').addClass('loggedin');
		});
		eventBus.$on('logout', function(){
			$.extend(true, self.userModel.user, {username:'',password:''});
			self.isLoggedIn=false;
			self.isAdmin=false;
			$('.ot-banner').removeClass('loggedin');
		});	
	},
	mounted:function(){
		if(this.isLoggedIn){
			$('.ot-banner').addClass('loggedin');
		}else{
			$('.ot-banner').removeClass('loggedin');
		}
		otScroll();
	},
	computed : {
		isLoggedIn:function(){
			var loggedin =false;
			console.log('Computing isLoggedIn ==> '+ userModel.user.username);
			loggedin = (userModel.user.username!="");
			this.check.pri=loggedin;
			return loggedin;
		},
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
		userlink_click: function () {
	      eventBus.$emit('openUserManagement'); // eslint-disable-line
	    },
		settinglink_click: function () {
		      eventBus.$emit('openLibSetting'); // eslint-disable-line
		    },
		toggleOrder : function() {
			if (this.order == 'asc') {
				this.order = 'desc';
			} else {
				this.order = 'asc';
			}
		},
		toggleFilter: function(){
			this.showFilterControl=!this.showFilterControl;
	    	if(this.showFilterControl){
	    	      $('img#filterdowniconimg').removeClass('down');  // eslint-disable-line
	    	      $('img#filterdowniconimg').addClass('up');  // eslint-disable-line
	    	      $('#filterBtn').addClass('tall');
	    	      
	    	}else
	    	{
	    	      $('img#filterdowniconimg').removeClass('up');  // eslint-disable-line
	    	      $('img#filterdowniconimg').addClass('down');  // eslint-disable-line
	    	      $('#filterBtn').removeClass('tall');
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
	 	removeAllFilters: function(){
				 this.filterStrings.splice(0);
			 },
			 removeString: function (s) {
				   this.filterStrings.splice(this.filterStrings.indexOf(s), 1);
				 },
				 setstartdate:  function(){
					 var sstamp=new Date(this.startTime.time).getTime();
					 eventBus.$emit('startdate',sstamp);
					console.log('Start date:'+ sstamp);
				 },
				 setenddate:function(){
					 var estamp=new Date(this.endtime.time).getTime();
						eventBus.$emit("enddate",estamp);
						console.log("End date:"+ estamp);
					 },
				 beforeEnter: function () {
						 el.style.opacity = 0
						 el.style.height = 0
					    },
					    enter: function (el, done) {
					      var delay = el.dataset.index * 150
					      setTimeout(function () {
					        Velocity(
					          el,
					          { opacity: 1, height: '1.6em' },
					          { complete: done }
					        )
					      }, delay)
					    },
					    leave: function (el, done) {
					      var delay = el.dataset.index * 150
					      setTimeout(function () {
					    	  Velocity(
					    	  el,
					          { opacity: 0, height: 0 },
					          { complete: done }
					        )
					      }, delay)
					    }

				 
	},
	components:{
		'applayout':applayout,
		'kotile':kotile,
		'date-picker': myDatepicker
		}
};
</script>
<style>
#bannerbk{
	position:fixed;
	width:1024px;
	top:85px;
	text-align: center;
}
.ot-glybtn {
    top: 0px;
    line-height: 2.2em;
    font-size: large;
    color: #39b54a;
    background-color: #fff;
    padding: 0 0 0 12px;
}
.ot-search {
    display: inline-block;
    border: 1px solid #fff;
	border-radius: 10px;
    width: 46%;
    margin-left: 15px;
}


.ot-search input {
    line-height: 2.8em;
    font-size: 14px;
    padding: 0px 0px 0px 16px;
    width:85%;
}

input[type=text], input[type=password], input[type=textarea], .addtext a {
    width: 900px;
    height: 38px;
    padding: 0px 16px;
    border: 1px solid #e6e6e6;
    border-radius: 10px;
    margin: 2px 0;
    font-size: 14px;
    color: #666666;
    font-weight: 400;
}
input[id$="datepicker"] {
    width: 150px;
}
#filterBtn {
    background-color: #fff;
    margin: 25px 0px 0px 0px;
    padding: 12px 0px 12px 0px;
    text-align: center;
	cursor: pointer;
	width: 100px;
	height: 45px;
}

#filterBtn.tall{
	height: 57px;
}
#filterBtn a {
    background-color: #fff;
    margin: 25px 0px 0px 0px;
    padding: 12px 10px 12px 0px;
    text-align: center;
	cursor: pointer;
	width: 80px;
	height: 45px;
	color: #666666;
}
#filterBtn a span {
	margin: 0px -15px 0px 12px;
}
img#filterdowniconimg.down {
    -moz-transform: scaleY(1);
    -o-transform: scaleY(1);
    -webkit-transform: scaleY(1);
    transform: scaleY(1);
}
img#filterdowniconimg.up {
    -moz-transform: scaleY(-1);
    -o-transform: scaleY(-1);
    -webkit-transform: scaleY(-1);
    transform: scaleY(-1);
}
#filterpanel {
	background-color:#fff;
	margin: 0px 0px 12px 0px;
	padding: 12px;
		
		
}
.filterlist li, .filterlist button#clearAll{
display:inline-block;
background-color:#fff;
margin:25px 10px 0px 10px;
padding:12px;
height: 44px;
}
.destroy {
	width:20px;
	height: 20px;
	border:none;
	background-image:url('../assets/images/Close_Circle_Green_Default.png');
background-size: 100% 100%;
background-repeat: no-repeat;
background-position: center center;
	margin: 0px 5px 0px 0px;
	color:#39b45a;
background-color: transparent;
}

.filterlist li label {
	display: inline-block;
	margin: 0px;
	text-align: center;
	vertical-align: top;
}
.filterlist button#clearAll {
background-color: #e6e6e6;
font-size: 12px;
vertical-align: top;
}
select {
    background: transparent;
	background-size: 18px;
    background-image: url(../assets/images/dropdown_chevron.png);
    background-repeat: no-repeat;
    background-position: 90%;
    line-height: 1;
    -webkit-appearance: none;
    -moz-appearance: none;
    width: 100%;
    padding: 6px;
    height: 40px;
    border: 1px solid #e6e6e6;
    border-radius: 10px;
    margin: 2px 0;
    font-size: 14px;
    color: #666666;
    font-weight: 400;
    font-family: 'Open Sans', sans-serif;
}
.ot-banner {
    background-color: transparent;
    position: relative;
    width: 1024px;
    overflow: visible;
    z-index: 10;
    height: 400px;
    margin: 0 auto;
padding: 0px 48px 0px 48px;
}
.ot-banner.loggedin {
	height: 220px;
}
.ot-banner h1 {
    font-size: 32px;
    font-weight: 300;
    margin-top: 75px;
    text-align: left;
    margin-bottom: 20px;
    color: dimgrey;
    margin: 0 auto;
    line-height: 1.3em;
    padding-top: 100px;
    background: transparent;
}

.ot-newobj{
	width: 40px;
	height: 40px;
	position:absolute;
    bottom:-30px;
    right:-30px;
    margin:0 auto;
    z-index:500;
}
.ot-banner.loggedin h1 {
    padding-top: 52px;
}
.ot-banner h1 a {
	color: #e3e3e3;
	cursor: pointer;
    transition: color .5s ease;
}
.ot-banner h1 a:hover {
	color: #666666;
}
#libname {
    text-align: right;
    font-size: 24px;
    /* margin-top: 30px; */
    color: #666666;
    font-weight: 300;
    bottom: 20px;
    position: absolute;
    right: 128px;
}
.row.filter {
	height: 30px;
	
}
.custom-control {
	
    position: relative;
    display: inline;
    padding-left: 1.5rem;
	margin-bottom: 1.5rem;
    cursor: pointer;
}
.custom-control-input {
    position: absolute;
    z-index: -1;
    opacity: 0;
}
.custom-control-description {
	position: absolute;
	padding-left: 15px;
	top: 8px;
	width: 180px;
}
.custom-control-indicator {
    position: absolute;
    top: 10px;
    left: 5px;
    display: block;
    width: 15px;
    height: 15px;
    pointer-events: none;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
    background-color: #fff;
    background-repeat: no-repeat;
    background-position: center center;
    -webkit-background-size: 80% 80%;
    background-size: 80% 80%;
}
.custom-checkbox .custom-control-indicator {
    border-radius: 0px;
	border: 1px solid #39b45a;
}
.custom-checkbox .custom-control-input:checked~.custom-control-indicator {
    background-image: url(../assets/images/checkmark.png);
	background-size: 50% 50%;
}
.custom-checkbox .custom-control-input:disabled~.custom-control-indicator {
    background-color: #e6e6e6;
	border: 1px solid #b3b3b3;
}
.custom-radio .custom-control-indicator {
    border-radius: 100%;
	border: 1px solid #39b45a;
}
.custom-radio .custom-control-input:checked~.custom-control-indicator {
    background-image: url(../assets/images/LittleGreenDot.png);
	background-size: 75% 75%;
}

#bannericons {
	display: inline-block;
    position: absolute;
    width: 20%;
    height: 40px;
    bottom: 16px;
    right: 24px;
    overflow: visible;
}
#bannericonrow {
    list-style: none;
    position: relative;
    display: table-row;
    float: right;
}
#bannericonrow li {
    display: table-cell;
}
#bannericonrow li button#userlink, #bannericonrow li button#settinglink {
	width: 30px;
height: 30px;
background-size: 65% 65%;
background-repeat: no-repeat;
background-position: center center;
background-color: #fff;
border: 1px solid #b3b3b3;
}

#bannericonrow li button#userlink:hover, #bannericonrow li button#settinglink:hover {
	background-color: #fff;
	border: 1px solid #666666;
}
button#userlink {
	   background-image: url(../assets/images/Person_Icon_Dark-01.png);
		opacity: 0.5;
transition: opacity 0.5s ease;
}
button#userlink:hover {
	  opacity: 1;

}
button#settinglink {
	   background-image: url(../assets/images/Gear_Icon-01.png);
		opacity: 0;
	
}


#newuser{
	width:12px;
	height:12px;
        position:absolute;
    bottom:-5px;
    right:-5px;
    padding:0;
    z-index:12;
    transition: none;
background-size: 65% 65%;
background-repeat: no-repeat;
background-position: center center;
background-image: url(../assets/images/Plus_Icon.png);
}
#newuser:hover {
	transform: none;
}
</style>