<template>
<div class='content'>
	<applayout :nothelper='true'>
		<div slot='banner'>
			<div v-if="isLoggedIn">
				<h1>Hello, {{firstname}}!</h1>
				<h1>Need to invite others? <router-link to='/soon'>Add Users.</router-link></h1>
			</div>
			<div v-else>
				<h1>Knowledge Grid Library is a digital repository for storing, curating, and managing computer-processable knowledge. <br></h1>
				<h1>Get Started, <router-link to='/soon'>Sign-Up</router-link> or <a v-on:click='login_click'> Log In </a>.</h1>
			</div>
			<div id='libname' v-if='libConnected'>
				<h5><span>{{libraryname}}</span></h5>
				<div id="bannericons" v-show='isLoggedIn'>
					<ul id="bannericonrow">
						<li>
							<div style="position: relative">
								<button class="roundbutton iconBtn" id="userlink"	@click="userlink_click"></button>
								<div class='ot-newuser' >
					       <div class='greenroundbutton kg-btn-bg-primary' > </div>
					       <div class='btnContent'><img src='../assets/images/Plus_Icon.png' width="8px"/></div>
								</div>
							</div>
						</li>
						<li>
							<button class="roundbutton open-overlay iconBtn" type="button" id="settinglink" disabled @click="settinglink_click"></button>
						</li>
					</ul>
					<div class="floatingInfo" id="homeIcons">
						<span></span>
					</div>
				</div>
			</div>
			<div id='libConBtn' v-if='!libConnected' @click='setlibrary'>
				<span>{{libraryname}}</span>
			</div>
		</div>
		<div slot='header'>
			<div v-show='isLoggedIn' class='ot-r-btn ot-newobj' v-on:click='addObject'>
				<div class='greenroundbutton kg-btn-bg-primary'></div>
				<div class='btnContent'><img src='../assets/images/Plus_Icon.png' width="14px"/></div>
			</div>
			<div class='row'>
				<div class='col-md-5 col-sm-5 col-xs-5 ot-search'>
					<img src='../assets/images/Search_Icon-01.png' width="20px"/>
					<input placeholder='Search by Keywords, Title, Owners or Object ID' v-model='newstring'  @keyup.enter='addFilterString'/>
				</div>
				<div class='col-md-1 col-sm-1 col-xs-1'></div>
				<div class='col-md-2 col-sm-2 col-xs-2'></div>
				<div class='col-md-3 col-sm-3 col-xs-3 ot-sort'></div>
			</div>
		</div>
		<div slot='maincontent'>
			<div id='filtercontrol'>
				<div class='row mar-top30'>
					<div class='col-md-2 filterBtnCol'>
						<div id='filterBtn'>
							<a v-on:click='toggleFilter'> Filters
								<span><img id='filterdowniconimg' class='down' src='../assets/images/Chevron.png' width='12px' /></span>
							</a>
						</div>
					</div>
					<div class='col-md-7 ot-count'>
						{{countString}}
					</div>
					<div class='col-md-3 lh-3 bg-white float-r'>
					<div class=''>
						<h6><span>Sort by:</span></h6>

<span  class='brd-r lh-1'><vselect :value.sync="kgselect" :options="optionlist" :searchable='false' :onChange='selectCallback'></vselect></span>

						<button id='sortordertoggle' class='bg-trans' v-on:click='toggleOrder()'>
							<span v-if='orderAsc'	class='down'><i class="fa fa-arrow-down" aria-hidden="true"></i></span>
							<span v-else class='up'><i class="fa fa-arrow-up" aria-hidden="true"></i></span>
						</button>
						</div>

					</div>
				</div>
				<div class='row' v-show='filterStrings.length|hasDateFilter'>
					<div class='col-md-2 filterBtnCol'>
						<div id='fillerdiv' class='bg-white'></div>
					</div>
					<div class='col-md-10 filterCol'>
						<ul class='filterlist'  v-show='filterStrings.length|hasDateFilter'>
							<li v-for='filterstring in filterStrings' class='todo' :key='filterstring.id'>
								<button class='destroy' @click='removeString(filterstring)'>
								</button>
								<label>{{ filterstring.title }}</label>
							</li>
							<li class='todo' v-show='hasDateFilter' >
								<button class='destroy' @click='removeDateFilter'></button>
								<label>{{dateTypeText}}: {{dateRange.startTime.time}} - {{dateRange.endTime.time}}</label>
							</li>
							<button id='clearAll' v-show=true @click='removeAllFilters'>Reset Filters</button>
						</ul>
					</div>
				</div>
				<transition name='expand'>
				<div id='filterpanel' v-if='showFilterControl' >
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
					<input v-model='check.showmyobj' disabled type="checkbox" class="custom-control-input">
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
					<input id="radio1" value='lastModified' v-model='dateRange.datetype' name="radio" type="radio" class="custom-control-input">
					<span class="custom-control-indicator"></span>
					<span class="custom-control-description">Last Updated</span>
					</label>

					</div>
					<div class='col-md-2'></div>
					<div class='col-md-4'>
					<label class="custom-control custom-radio">
					<input id="radio2" value='createdOn' v-model='dateRange.datetype' name="radio" type="radio" class="custom-control-input">
					<span class="custom-control-indicator"></span>
					<span class="custom-control-description">Created</span>
					</label>

					</div>
					<div class='col-md-1'></div>
					</div>
					<div class='row filter'><p>Search within the following date range:</p></div>
					<div class='row filter'>
					<div class='col-md-1'></div>
						<div class='col-md-5 datepick'>
							<span>Start</span>
							<p ><date-picker :date="dateRange.startTime" :option="option" class='leftalign' :limit="limit" v-on:change='setstartdate()' id='startdatepicker'></date-picker> </p>
						</div>
						<div class='col-md-5 datepick'>
						<span>End</span>	<p> <date-picker :date="dateRange.endTime" :option="option" class='rightalign' :limit="limit"  v-on:change='setenddate()' id='enddatepicker'></date-picker></p>
						</div>
						<div class='col-md-1'></div>
					</div>
			</div>
		</div>
</div>
</transition>
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
import myDatepicker from '../vendor/vue-datepicker-es6.vue'
import applayout from './applayout.vue';
import vselect from '../vendor/vue-select.vue';
import kotile from './kotile.vue';
import { overlayHeightResize, retrieveObject, retrieveObjectList, setenddate, otScroll, setBannerbkSize} from '../ot.js';
import eventBus from '../components/eventBus.js';
import { objModel, editObjModel, sections, userModel } from '../components/models.js'

export default {
    name: 'home',
	data : function() {
		return {
			libConnected:false,
			libraryname : '',
			sortKey : 'metadata.lastModified',
			order : 'desc',
			searchQuery : '',
			model : {
				koList : []
			},
			dlabel:"Select sort key",
			kgselect:'Last Updated',
			optionlist:[{'label':'Last Updated', 'value':'metadata.lastModified'},{'label':'Title','value':'metadata.title'},{'label':'Object ID','value':'uri'}],
			confirmrequest:{name:"removeallfilter",statement:"All filters will be cleared!"},
			check:{ keywords : true, owners : true, title : true, citations : false, contributors : false, objectID : false, pub : true, pri : false, showmyobj:false},
			defaultCheck:{ keywords : true, owners : true, title : true, citations : false, contributors : false, objectID : false, pub : true, pri : false, showmyobj:false},
 			filterStrings:[],
 			newstring:'',
 			dateRange:{datetype:'lastModified',startTime: {time: '09/01/2016'}, endTime: {time: new Date().format("shortDate")}},
 			defaultDateRange:{datetype:'lastModified', startTime:{time: '09/01/2016'}, endTime: {time:new Date().format("shortDate")}},
 			startdate:0,
			enddate:0,
			userModel:{user:{username:'',password:''}},
			isAdmin:true,
			showFilterControl:false,
			datetypetext:"Last Updated",
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
			console.log('Init Sort Key'+this.sortKey);
	  	if(sessionStorage.getItem("sortKey")==null){
	  		this.setSessionStorage();
	  	}
		$("#startdatepicker").val("03/01/16");
		$("#enddatepicker").val(new Date().format("shortDate"));
		this.startdate = new Date('March 1, 2016').getTime();
		this.enddate=new Date().getTime();
		$.extend(true,this.userModel,userModel);
		retrieveObjectList(this.$store.state.baseurl, function(response) {
			console.log("Object List Retrieval:");
			if(response instanceof Array){
				self.libraryname='Department of Learning Health Sciences Development Server';
				self.libConnected=true;
				self.model.koList = response;
				if(self.model.koList.length>0){
					$.extend(true,objModel.object,self.model.koList[0]);
				}
			}else {
				self.libraryname='No Library is found. Click here to connect.';
				self.libConnected=false;
			}
		}, function(response){
			console.log("Error in retrieving the list from the connected library.")
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
			$.extend(true, self.userModel.user,obj);
			self.isAdmin = (self.userModel.user.role=='ADMIN');
			self.check.pri=true;
			$('.ot-banner').addClass('loggedin');
			otScroll();
		});
		eventBus.$on('logout', function(){
			$.extend(true, self.userModel.user, {username:'',password:''});
			self.isAdmin=false;
			$('.ot-banner').removeClass('loggedin');
			otScroll();
		});
		eventBus.$on('objcreated', function(obj){
			var object = {};
			$.extend(true, object, obj);
			self.model.koList.push(object);
		});
		eventBus.$on('objDeleted',function(obj){
		      console.log(obj)
		      var dIndex = self.model.koList.map(function(e) {return e.uri}).indexOf(obj.uri);
		      self.model.koList.splice(dIndex,1);
		      if(self.model.koList.length>0){
					$.extend(true,objModel.object,self.model.koList[0]);
				}
		    });
				eventBus.$on('confirm', function (data) {
					console.log(data);
						if(data.name=="removeallfilter"){
						if(data.val==true){
							self.filterStrings.splice(0);
							self.removeDateFilter();
						}
					}
				});
	},
	mounted:function(){
		if(this.isLoggedIn){
			$('.ot-banner').addClass('loggedin');
		}else{
			$('.ot-banner').removeClass('loggedin');
		}
		otScroll();
		console.log('before'+this.sortKey)
		if(sessionStorage.getItem("sortKey")!=null){
		this.sortKey=sessionStorage.getItem("sortKey");
		console.log('after'+this.sortKey)
		this.order=sessionStorage.getItem("order");
		this.filterStrings=JSON.parse(sessionStorage.getItem("filters"));
		this.check=JSON.parse(sessionStorage.getItem("check"));
		this.dateRange=JSON.parse(sessionStorage.getItem("dateRange"));
		this.setstartdate();
		this.setenddate();
		}
	},
	updated: function() {
		this.setSessionStorage();
	  },
	computed : {
		dateTypeText: function(){
		  console.log("Date type " + this.dateRange.datetype);
		  var txt=''
		  if(_.isEqual(this.dateRange.datetype, this.defaultDateRange.datetype)){
			  txt='Last Updated'
		  }else {
			  txt= 'Created on'
		  }
		  return txt
	  },
		  hasDateFilter: function(){
		  return !(_.isEqual(this.dateRange, this.defaultDateRange))
	  },
		firstname: function(){
			return (this.$store.state.currentUser.first_name || "")
		},
		isLoggedIn: function () {
			return this.$store.getters.isLoggedIn;
		},
		countString : function() {
			var count = this.orderedList.length;
			if (count <= 1) {
				return 'Viewing ' +count + ' Object';
			} else if (count > 1000) {
				return 'Many many Objects';
			} else {
				return 'Viewing ' + count + ' Objects';
			}
		},
		listSize : function() {
			return this.filteredList.length;
		},
		orderAsc : function() {
			return (this.order == 'asc');
		},
		orderedList : function() {
			switch(this.sortKey) {
			case 'metadata.lastModified':
				return _.orderBy(this.filteredList, [i=>i.metadata.lastModified], this.order);
			case 'uri':
				return _.orderBy(this.filteredList, [i=>i.uri.toLowerCase()], this.order);
			case 'metadata.title':
				return _.orderBy(this.filteredList, [i=>i.metadata.title.toLowerCase()], this.order);
			default:
				return this.filteredList;
			}

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
										customFilter=customFilter&&(field.metadata[self.dateRange.datetype]>=self.startdate && field.metadata[self.dateRange.datetype]<=self.enddate );
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
		setSessionStorage: function(){
			console.log("setting sort key" + this.sortKey);
			sessionStorage.setItem("sortKey", this.sortKey);
			sessionStorage.setItem("order", this.order);
			sessionStorage.setItem("filters", JSON.stringify(this.filterStrings));
			sessionStorage.setItem("check", JSON.stringify(this.check));
			sessionStorage.setItem("dateRange", JSON.stringify(this.dateRange));
			},
		selectCallback: function(val){
		   console.log(val);
			 console.log("After callback" + this.sortKey+"  ==="+val);
			 this.sortKey=val.value;
			 switch(val) {
			 case 'Last Updated':
			 		this.sortKey='metadata.lastModified';
					break;
			 case 'Object ID':
			 		this.sortKey='uri';
					break;
			 case 'Title':
			 		this.sortKey='metadata.title';
					break;
					}
							sessionStorage.setItem("sortKey", this.sortKey);
		},
		onChange: function(){
			switch(this.sortKey) {
			case 'metadata.lastModified':
				this.order ='desc';
			break;
			case 'uri':
				this.order ='asc';
				break;
			case 'metadata.title':
				this.order ='asc';
				break
			}
	},
	login_click: function () {
		eventBus.$emit('openLogin'); // eslint-disable-line
	},
		userlink_click: function () {
	      eventBus.$emit('openUserManagement'); // eslint-disable-line
	    },
		settinglink_click: function () {
		      eventBus.$emit('openLibSetting'); // eslint-disable-line
		    },
				setlibrary: function () {
							eventBus.$emit('openLibCon'); // eslint-disable-line
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
							$('#fillerdiv').addClass('tall');
	    	}else
	    	{
	    	      $('img#filterdowniconimg').removeClass('up');  // eslint-disable-line
	    	      $('img#filterdowniconimg').addClass('down');  // eslint-disable-line
	    	      $('#filterBtn').removeClass('tall');
							$('#fillerdiv').removeClass('tall');
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
			eventBus.$emit("confirmRequest",this.confirmrequest);
	 },
	 	removeDateFilter : function(){
				 $.extend(true, this.dateRange, this.defaultDateRange);
				 this.setstartdate();
				this.setenddate();
			 },
			 removeString: function (s) {
				   this.filterStrings.splice(this.filterStrings.indexOf(s), 1);
				 },
				 setstartdate:  function(){
					 var sstamp=new Date(this.dateRange.startTime.time).getTime();
					 eventBus.$emit('startdate',sstamp);
					console.log('Start date:'+ sstamp);
				 },
				 setenddate:function(){
					 var estamp=new Date(this.dateRange.endTime.time).getTime();
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
		'date-picker': myDatepicker,
		vselect
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
    background-color: #fff;
    padding: 0 0 0 12px;
}
kgdropdown {
	border-right: 1px solid #666666;
}
.ot-search {
    display: inline-block;
    border: 1px solid #fff;
	border-radius: 10px;
    width: 46%;
    margin-left: 15px;
}


.ot-count, .ot-sort {
	line-height: 3em;
padding-right: 0px;
}

.ot-search input {
    line-height: 2.8em;
    font-size: 14px;
    padding: 0px 0px 0px 16px;
    width:85%;
}

input[type=text], input[type=password], input[type=textarea] {
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
#filterBtn, #fillerdiv {
    background-color: #fff;
    margin: 0px 0px 0px 0px;
    padding: 12px 0px 12px 0px;
    text-align: center;
	cursor: pointer;
	width: 100px;
	height: 45px;
}
#fillerdiv {
height: 55px;
}
#filterBtn.tall{
	height: 57px;
}

 #fillerdiv.tall{
	height: 77px;
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
img#filterdowniconimg
{
transition: transform 0.8s ease;
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
margin:10px 10px 15px 10px;
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
	background-size: 12px;
    background-image: url(../assets/images/Chevron.png);
    background-repeat: no-repeat;
    background-position: 90%;
    line-height: 1;
    -webkit-appearance: none;
    -moz-appearance: none;
    width: 100%;
    padding: 6px;
    height: 40px;
    border: none;
    border-radius: 0px;
		border-right: 1px solid #666666;
    margin: 2px 12px 2px 0px;
    font-size: 14px;
    color: #666666;
    font-weight: 400;
    font-family: 'Open Sans', sans-serif;
}

.ot-select {
	width:auto;
	padding: 0px 36px 0px 6px;
	height:auto;
	display: inline-block;
}
.ot-banner h1 {
    font-size: 24px;
    font-weight: 300;
    margin-top: 75px;
    text-align: left;
    margin-bottom: 20px;
    margin: 0 auto;
    line-height: 1.3em;
    padding-top: 30px;
    background: transparent;
}

.ot-newobj{
	width: 40px;
	height: 40px;
	position:absolute;
    bottom:0px;
    right:1px;
    margin:0 auto;
    z-index:500;
}
.ot-banner.loggedin h1 {
    padding-top: 30px;
}
.ot-banner h1 a {
	color: #e3e3e3;
	cursor: pointer;
    transition: color .5s ease;
}
.ot-banner h1 a:hover {
	color: #666666;
}
#libname, #libConBtn {
    text-align: right;
    font-size: 20px;
    /* margin-top: 30px; */
    color: #666666;
    font-weight: 300;
    bottom: 20px;
    position: absolute;
    right: 22px;
}
#libConBtn:hover {
	cursor:pointer;
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
    position: relative;
    width: 60px;;
    height: 40px;
    bottom: -10px;
    right: -20px;
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

.ot-newuser {
	position:relative;
bottom:8px;
right:-10px;
}
.ot-newuser .greenroundbutton {
	width:12px;
	height:12px;
        position:relative;
    bottom:0px;
    right:0px;
    padding:0;
    z-index:12;
    transition: none;
box-shadow:none;
}

.ot-newuser .btnContent {
	position: relative;
	height: 16px;
width: 16px;
	bottom: 10px;
	right: 2px;
	z-index: 16;
}

.ot-newuser .btnContent img {
    bottom: 12px;
    position: relative;
}
#newuser:hover {
	transform: none;
}

.datepick>span {
	position: absolute;
top: 4px;
left: 12px;
}
.datepick p {
	position: absolute;
	top: 0px;
	left: 30px;
}
#sortordertoggle>span img {
	margin-bottom:3px;
	}
#sortordertoggle>span.up img {
	transform: scaleY(-1);
}
#sortordertoggle>span.down img {
	transform: scaleY(1);
}
.filterBtnCol {
	width: 120px;
}

select >option {
	background-color:#fff;
	color:#666666;
}
select >option:hover {
	background-color:#e5e5e5;
	color:#666666;
}

.expand-enter-active, .expand-leave-active {
  transition: all .8s linear;
  height: 220px;
  overflow: hidden;
	opacity:1;
}

#sortordertoggle{
    margin-left: 6px;
}

.expand-enter, .expand-leave {
  height: 0;
	overflow:hidden;
  opacity: 0;
}
</style>
