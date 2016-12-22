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
		</div>
		<div slot='header'>
				<div v-show='isLoggedIn'>
					<button class='greenroundbutton ot-newobj'  v-on:click='addObject'>
						<img src='../assets/images/Plus_Icon.png' width="14px"/>
					</button>
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
		<div id='filterpanel' v-if='showFilterControl'>
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
			order : 'asc',
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
		        time: ''
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
		            'padding': '6px',
		            'line-height': '22px',
		            'font-size': '16px',
		            'border': '2px solid #fff',
		            'box-shadow': '0 1px 3px 0 rgba(0, 0, 0, 0.2)',
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
		          from: '2016-03-01',
		          to: ''
		        }]
		      }
		    },
	created : function() {
		var self = this;
	  	getCurrentUser(function(response) {
				if(response!="")
					$.extend(true, self.userModel.user, response);
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
	    	}else
	    	{
	    	      $('img#filterdowniconimg').removeClass('up');  // eslint-disable-line
	    	      $('img#filterdowniconimg').addClass('down');  // eslint-disable-line
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
					 var sstamp=new Date(this.startTime.time).getTime();
					 eventBus.$emit('startdate',sstamp);
					console.log('Start date:'+ sstamp);
				 },
				 setenddate:function(){
					 var estamp=new Date(this.endtime.time).getTime();
						eventBus.$emit("enddate",estamp);
						console.log("End date:"+ estamp);
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
	height: 200px;
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
    right: 48px;
}
</style>