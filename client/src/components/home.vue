<template>
<div class='content'>
	<applayout :nothelper='true'>
		<div slot='banner'>
			<div class='bannercontent' v-if="isLoggedIn">
				<h1 style='color:#0075bc;font-weight:700;'>Knowledge Grid Library</h1>
				<h1>A Repository of Computable Health Knowledge where you can Store, Curate and Manage Resources and Services</h1>
			</div>
			<div class='bannercontent' v-else>
				<h1 style='color:#0075bc;font-weight:700;'>Knowledge Grid Library</h1>
				<h1>A Repository of Computable Health Knowledge where you can Store, Curate and Manage Resources and Services</h1>
			</div>
		</div>
		<div slot='header'>
			<div v-show='isLoggedIn' class='kg-roundbtn kgl-newobj fade-in' v-on:click='addObject'>
				<a><span class='kg-fg-color'>Deposit Knowledge Object</span></a>
				<div class='btnContent ft-sz-10 '>
					<icon color="#0075bc" name="plus"></icon>
				</div>
			</div>
			<div class='row'>
				<div class='col-md-10 col-sm-10 col-xs-10 kgl-search'>
					<icon color="#0075bc" name="search"></icon>
					<input id='searchinput' aria-label='search' :placeholder='searchPlaceHolder' v-model='newstring'  @keyup.enter='addFilterString'/>
				</div>
				<div class='col-md-2 col-sm-2 col-xs-2'></div>
			</div>
		</div>
		<div slot='maincontent'>
			<div id='filtercontrol'>
			<div class='row  mar-top20' v-show='newfilterstrings.length|hasDateFilter'>
				<div class='col-md-12 col-sm-12 col-xs-12 filterCol'>
					<ul class='filterlist'  v-show='newfilterstrings.length|hasDateFilter'>
						<li v-for='filterstring in newfilterstrings' class='todo' :key='filterstring.id'>
							<button class='destroy' @click='removeString(filterstring)'>
										<icon color="#0075bc" name="regular/times-circle"></icon>
							</button>
							<label>{{ filterstring.title }}</label>
						</li>
						<li class='todo' v-show='hasDateFilter' >
							<button class='destroy' @click='removeDateFilter'>
									<icon color="#0075bc" name="regular/times-circle"></icon>
									</button>
							<label>{{dateTypeText}}: {{dateRange.startTime.time}} - {{dateRange.endTime.time}}</label>
						</li>
						<button id='clearAll' v-show=true @click='removeAllFilters'><span>Clear All Filters</span></button>
					</ul>
				</div>
			</div>
				<div class='row  mar-top20'>
					<div class='col-md-2  col-sm-2 col-xs-2 filterBtnCol'>
						<div id='filterBtn' :class='{tall:showFilterControl}'>
							<a v-on:click='toggleFilter'>
								<div class='kg-fg-color inlineblock mar-r-12' >Filters </div>
								<icon style='{padding:10px 25px;display: inline;}' color="#0075bc" name="sliders-h"></icon>
							</a>
						</div>
					</div>
					<div class='col-md-2  col-sm-6 col-xs-6 kgl-count'>
						{{countString}}
					</div>
					<div class='col-md-4  col-sm-4 col-xs-4 ht-45-lh float-r'>
						<kgdropdown label='Sort by:' :selectvalue='newkgselect.label' v-on:selected='setkgselect' :optionlist='optionlist' :dstyle='ddstyle'></kgdropdown>
					</div>
				</div>
				<transition name='expand'>
				<div id='filterpanel' v-if='showFilterControl' >
					<div class='row'>
						<div class='col-md-6  col-sm-6 col-xs-6'>
							<div class='row filterlabel pad-l-20'><span class='ft-sz-12 text-cap'>SEARCH ONLY WITHIN THE FOLLOWING</span></div>
								<div class='row filter'>
									<div class='col-md-6  col-sm-6 col-xs-6'>
 										<label class="custom-control custom-checkbox">
 											<input :value='check.keywords' type="checkbox" @click='setfiltercheck("keywords", $event)' class="custom-control-input">
 											<span class="custom-control-indicator ft-sz-12 ">
												<icon v-show='check.keywords'  color="#0075bc" name="check"></icon>
											</span>
											<span class="custom-control-description">Keywords</span>
										</label>
									</div>
						<div class='col-md-6  col-sm-6 col-xs-6'>
							<label class="custom-control custom-checkbox">
								<input :value='check.objectID' type="checkbox" @change='setfiltercheck("objectID", $event)' class="custom-control-input">
								<span class="custom-control-indicator ft-sz-12">
									<icon v-show='check.objectID'  color="#0075bc" name="check"></icon>
								</span>
								<span class="custom-control-description">Object ID</span>
							</label>
						</div>
					</div>
					<div class='row filter'>
					<div class='col-md-6 col-sm-6 col-xs-6'>
					<label class="custom-control custom-checkbox">
					<input :value='check.owners' type="checkbox" @change='setfiltercheck("owners", $event)' class="custom-control-input">
					<span class="custom-control-indicator ft-sz-12">
							<icon v-show='check.owners'  color="#0075bc" name="check"></icon>
					</span>
					<span class="custom-control-description">Owners</span>
				</label>
					</div>
					<div class='col-md-6 col-sm-6 col-xs-6'>
					<label class="custom-control custom-checkbox">
					<input :value='check.contributors' type="checkbox" @change='setfiltercheck("contributors", $event)' class="custom-control-input">
					<span class="custom-control-indicator ft-sz-12">
							<icon v-show='check.contributors' color="#0075bc" name="check"></icon>
					</span>
					<span class="custom-control-description">Contributors</span>
				</label>
								</div>
					</div>
					<div class='row filter'>
					<div class='col-md-6 col-sm-6 col-xs-6'>
					<label class="custom-control custom-checkbox">
					<input :value='check.title' type="checkbox" @change='setfiltercheck("title", $event)' class="custom-control-input">
					<span class="custom-control-indicator ft-sz-12">
							<icon v-show='check.title' color="#0075bc" name="check"></icon>
					</span>
					<span class="custom-control-description">Title</span>
				</label>
					</div>
						</div>
					<div class='row filterlabel pad-l-20'>
							<span class='ft-sz-12 text-cap'>Type of Knowledge Object</span></div>
						<div class='row filter'>
					<div class='col-md-6 col-sm-6 col-xs-6'>
					<label class="custom-control custom-checkbox">
					<input :value='check.pub' type="checkbox" @change='setfiltercheck("pub", $event)' class="custom-control-input">
					<span class="custom-control-indicator ft-sz-12">
					<i v-show='check.pub' class='fa fa-check kg-fg-color'></i>
						<icon v-show='check.pub'  color="#0075bc" name="check"></icon>
					</span>
					<span class="custom-control-description">Public</span>
				</label>
					 </div>
					<div class='col-md-6 col-sm-6 col-xs-6'>
					<label v-if='isLoggedIn' class="custom-control custom-checkbox">
					<input :value='check.pri' type="checkbox" @change='setfiltercheck("pri", $event)' class="custom-control-input">
					<span class="custom-control-indicator ft-sz-12">
						<i v-show='check.pri' class='fa fa-check kg-fg-color'></i>
							<icon v-show='check.pri'  color="#0075bc" name="check"></icon>
					</span>
					<span class="custom-control-description">Private</span>
				</label>
								</div>
								<div class='col-md-1 col-sm-1 col-xs-1'></div>
								</div>
				</div>
				<div class='col-md-6 col-sm-6 col-xs-6'>
					<div class='row filterlabel  pad-l-20'><span class='ft-sz-12 text-cap'>Select the type of date</span></div>
					<div class='row filter '>
						<div class='col-md-4 col-sm-4 col-xs-4'>
						<label class="custom-control custom-radio">
						<input id="radio0" value='none' v-model='dateRange.datetype' @change='setDrange' name="radio" type="radio" class="custom-control-input">
						<span class="custom-control-indicator ft-sz-16">
										<icon v-show='dateRange.datetype=="none"' color="#0075bc" name="circle"></icon>
						</span>
						<span class="custom-control-description">None</span>
						</label>
						</div>
					<div class='col-md-4 col-sm-4 col-xs-4'>
					<label class="custom-control custom-radio">
					<input id="radio1" value='lastModified' v-model='dateRange.datetype' @change='setDrange' name="radio" type="radio" class="custom-control-input">
					<span class="custom-control-indicator ft-sz-16">
							<icon v-show='dateRange.datetype=="lastModified"'  color="#0075bc" name="circle"></icon>
					</span>
					<span class="custom-control-description">Last Updated</span>
					</label>
					</div>
					<div class='col-md-4 col-sm-4 col-xs-4'>
					<label class="custom-control custom-radio">
					<input id="radio2" value='createdOn' v-model='dateRange.datetype'  @change='setDrange' name="radio" type="radio" class="custom-control-input">
					<span class="custom-control-indicator ft-sz-16">
								<icon v-show='dateRange.datetype=="createdOn"'  color="#0075bc" name="circle"></icon>
					</span>
					<span class="custom-control-description">Created</span>
					</label>
					</div>
					</div>
									<div class='row filter'></div>
					<div class='row filterlabel  pad-l-20' v-show='dateRange.datetype!="none"'><span class='ft-sz-12 text-cap'>Search within the following date range</span></div>
					<div class='row filter'  v-show='dateRange.datetype!="none"' >
					<div class='col-md-6 col-sm-6 col-xs-6 datepick'  >
							<span class='kg-fg-color pad-r-10'>Start</span>
							<p class=' pad-l-30' ><date-picker ref='startpicker' :date="dateRange.startTime" :option="option" class='leftalign' :limit="limit" v-click-outside='outsidestart' v-on:change='setdaterange()' id='startdatepicker'></date-picker> </p>
						</div>
						<div class='col-md-5 col-sm-5 col-xs-5 datepick'>
						<span class='kg-fg-color '>End</span>	<p  class=' pad-l-20' >
						<date-picker ref='endpicker' :date="dateRange.endTime" :option="option" class='rightalign' :limit="limit"  v-on:change='setdaterange()' id='enddatepicker' v-click-outside='outsideend'></date-picker></p>
						</div>
						<div class='col-md-1 col-sm-1 col-xs-1'></div>
					</div>
			</div>
		</div>
</div>
</transition>
</div>
<ul id='kolisting'>
<li v-for='(object,index) in orderedList' v-bind:key='index'><kotile :object='object'
								:listsize='listSize' :tileindex='index' v-on:remove='orderedList.splice(index, 1)'></kotile></li>
					</ul>
				</div>
			</applayout>
		</div>
</template>
<script>
import moment from 'moment'
import myDatepicker from '../vendor/vue-datepicker-es6.vue'
import applayout from './applayout.vue';
import kotile from './kotile.vue';
import store from '../store'
import axios from 'axios';
import kgdropdown from "./kgdropdown.vue"
import 'vue-awesome/icons/plus'
import 'vue-awesome/icons/sliders-h'
import 'vue-awesome/icons/search'
import 'vue-awesome/icons/check'
import 'vue-awesome/icons/circle'
import 'vue-awesome/icons/regular/times-circle'
export default {
  name: 'home',
	data : function() {
		return {
			searchQuery : '',
			rawlist:[],
			optionlist:[{'label':'Last Updated - Newest', 'value':'metadata.lastModified','order':'desc'},
									{'label':'Last Updated - Oldest', 'value':'metadata.lastModified','order':'asc'},
									{'label':'Title - Z to A','value':'metadata.title','order':'desc'},
									{'label':'Title - A to Z','value':'metadata.title','order':'asc'},
									{'label':'Object ID - Z to A','value':'uri','order':'desc'},
									{'label':'Object ID - A to Z','value':'uri','order':'asc'}
									],
			ddstyle:{	"d":"height:45px;padding:13px;background-color:#fff;",
								"ul":"",
								"li":"",
								"a":"line-height: 2.5em;"
							},
			confirmrequest:{name:"removeallfilter",statement:"All filters will be cleared!"},
			filterStrings:[],
 			newstring:'',
 			dateRange:{datetype:'none',startTime: {time: moment('2016-09-01T00:00:00-05:00').format("MM/DD/YYYY")}, endTime: {time: moment().format("MM/DD/YYYY")}},
 			defaultDateRange:{datetype:'none', startTime:{time: moment('2016-09-01T00:00:00-05:00').format("MM/DD/YYYY")}, endTime: {time: moment().format("MM/DD/YYYY")}},
			kgselect:{label: "Title - Z to A", value: "metadata.title", order: "desc"},
			showFilterControl:false,
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
		            'border': '1px solid #0075bc',
		            'box-shadow': 'none',
		            'border-radius': '2px',
		            'color': '#0075bc',
		            'width': '120px'
		          },
		          color: {
		            checkedDay: '#e5e5e5',
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
		    }
	,created : function() {
		this.rawlist=this.$store.getters.getobjectlist;
	}
	, mounted:function(){
		var self=this
		this.kgselect = JSON.parse(JSON.stringify(this.newkgselect));
		this.dateRange= JSON.parse(JSON.stringify(this.newdaterange));
		this.filterStrings.splice(0);
	 	this.newfilterstrings.forEach(function(e){
			 self.filterStrings.push(e)
		 })
	},
	beforeRouteEnter (to, from, next) {
		axios.get("./static/json/config.json").then(response=> {
			store.commit('setpaths',response.data);
			// console.log("before Route Enter:"+store.getters.getshelfurl)
			store.dispatch('fetchkolist').then(function(){
			 	next()
			})
		  store.dispatch('checkenv');
			// store.dispatch('getcurrentuser')
		}).catch(e=>{
			console.log(e)
		})
  },
	computed : {
		koversionlist:function(){
			var objlist={}
			this.rawlist.forEach(function(e){
				var key = Object.keys(e)[0]
				var obj = e[key]
					objlist[key]=Object.keys(obj)
			})
			return objlist
		},
		kolist:function(){
			var self=this
			var list=[]
			this.rawlist.forEach(function(e){
				var obj={arkId:'', title:'', keyword:'',createdOn:0,lastModified:0,version:'',owners:''}
				var key = Object.keys(e)[0]
				var vlist=self.koversionlist[key]
				var o = e[key][vlist[0]]
				// console.log("o from rawlist"+ JSON.stringify(o))
				var rawobj ={}
				if(o.metadata){
					rawobj = o.metadata
				} else {
					rawobj =o
				}
					// console.log(JSON.stringify(rawobj))

					obj.arkId = rawobj.arkId
					obj.title = rawobj.title
					obj.keywords = rawobj.keywords
					obj.createdOn = rawobj.createdOn
					obj.lastModified = rawobj.lastModified
					obj.version = rawobj.version
					obj.owners=rawobj.owners
				list.push(obj)

			})
			return list
		},
		startdate:function(){
			return moment(this.dateRange.startTime.time,"MM-DD-YYYY").startOf('day').valueOf()
		},
		enddate:function(){
			return moment(this.dateRange.endTime.time,"MM-DD-YYYY").endOf('day').valueOf()
		},
		newkey: function(){
			return this.$store.getters.getSortKey;
		},
		neworder: function(){
			return this.$store.getters.getSortOrder;
		},
		newkgselect: function(){
			return this.$store.getters.getkgselect;
		},
		check: function(){
			return this.$store.getters.getcheck;
		},
		newdaterange: function(){
			return this.$store.getters.getdaterange;
		},
		newfilterstrings: function(){
			return this.$store.getters.getfilters;
		},
		user: function(){
			return this.$store.getters.getuser;
		},
		url:function(){
      return this.$store.getters.getshelfurl
    },
		searchPlaceHolder:function(){
			var s = 'Search by ';
			var queryfields =[];
			if(this.check.keywords){ queryfields.push('Keywords') }
			if(this.check.owners){ queryfields.push('Owners') }
			if(this.check.title){ queryfields.push('Title') }
			if(this.check.objectID){ queryfields.push('Object ID')}
			if(this.check.contributors){queryfields.push('Contributors')}
			for(var i=0; i<queryfields.length;i++){
				if(i==0){
				 	s= s+queryfields[i];
				} else if(i==queryfields.length-1) {
					s=  s+' or '+queryfields[i];
				} else {
					s=  s+', '+queryfields[i];
				}
			}
			return s
		},
		dateTypeText: function(){
			  var txt=''
		  switch(this.newdaterange.datetype){
				case 'lastModified':
					 txt='Last Updated'
					 break
				case 'createdOn':
					txt= 'Created on'
					break
				}
				return txt
	  },
		libraryname : function(){
			return this.$store.state.libraryname;
		},
	  hasDateFilter: function(){
		  return !(_.isEqual(this.dateRange, this.defaultDateRange))
	  },
		firstname: function(){
			return (this.$store.state.currentUser.first_name || "")
		},
		isLoggedIn: function () {
			return this.$store.getters.isLoggedIn
		},
		isAdmin: function() {
			return this.$store.getters.isAdmin;
		},
		countString : function() {
			var count = this.orderedList.length;
			if (count <= 1) {
				return 'Viewing ' +count + ' Object';
			} else if (count > 1200) {
				return 'Many many Objects';
			} else {
				return 'Viewing ' + count + ' Objects';
			}
		},
		listSize : function() {
			return this.filteredList.length;
		},
		orderedList : function() {
			var l = []
			this.filteredList.forEach(function(e){
				l.push(e)
			})
			switch(this.newkey) {
			case 'metadata.lastModified':
				return _.orderBy(l, [i=>i.lastModified], this.neworder);
			case 'uri':
				return _.orderBy(l, [i=>i.arkId.toLowerCase()], this.neworder);
			case 'metadata.title':
				return _.orderBy(l, [i=>i.title.toLowerCase()], this.neworder);
			default:
				return l;
			}
		},
		filteredList : function() {
			var self = this;
			var list = this.kolist;
			if (!this.isLoggedIn) {
				  list = list.filter(function(ko) {
				  	return (ko.published);
					});
			}
			if (!this.isAdmin) {
			  list = list.filter(function(ko) {
				  return (ko.version!="");
				});
			}
			return list.filter(function(field){
									var customFilter = true;
										var filterString = {
											id : 0,
											title : ''
										};
										if(self.newdaterange.datetype!='none') {
											customFilter=customFilter&&(field[self.newdaterange.datetype]>=self.startdate && field[self.newdaterange.datetype]<=self.enddate );
										}
										if(!self.check.pub){
											customFilter=customFilter&&(!field.published);
										}
										if(!self.check.pri){
											customFilter=customFilter&&(field.published);
										}
										if (self.newfilterstrings.length > 0) {
											for (var i = 0; i < self.newfilterstrings.length; i++) {
												var filterResult = false;
												filterString = self.newfilterstrings[i];
												if (filterString.title === '') {
													filterResult = true;
												} else {
													var fString = new RegExp(filterString.title,'i');
													if (self.check.title&&field.title) {
														filterResult = (filterResult || ((field.title
																.search(fString))!=-1));
													}
													if (self.check.keywords&&field.keywords) {
														filterResult = (filterResult || ((field.keywords
																.search(fString))!=-1));													}
													if (self.check.owners&&field.owners) {
														filterResult = (filterResult || ((field.owners
																.search(fString))!=-1));
													}
													if (self.check.contributors&&field.contributors) {
														filterResult = (filterResult || ((field.contributors
																.search(fString))!=-1));
													}
													if (self.check.objectID&&field.arkId) {
														filterResult = (filterResult || ((field.arkId
																.search(fString))!=-1));
													}
													if(self.check.citations&&field.citations){
													  if(field.citations!=null){
														  if(field.citations.length>0){
															  for(var i=0;i<field.citations.length;i++){
																  filterResult = (filterResult || ((field.citations[i].citation_title
																		  .search(fString))!=-1) || ((field.citations[i].citation_at.search(fString))!=-1));
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
		setfiltercheck:function(key, event){
			var obj={}
			obj[key]=(event.target.value=='false')
			this.$store.commit('setcheck',obj)
		},
		setDrange:function(){
			if(this.dateRange.datetype=='none'){
				 this.dateRange=JSON.parse(JSON.stringify(this.defaultDateRange));
			}
			this.$store.commit('setdaterange',this.dateRange);
		},
		setkgselect: function(value){
			this.$store.commit('setkgselect',value);
		},
		outsideend: function(e){
			if(this.$refs.endpicker.showInfo.check)
				this.$refs.endpicker.showCheck();
			this.$refs.endpicker.dismiss(e);
		},
		outsidestart: function(e){
			if(this.$refs.startpicker.showInfo.check){
				this.$refs.startpicker.showCheck();
				this.$refs.startpicker.dismiss(e);
			}
		},
		toggleFilter: function(){
			this.showFilterControl=!this.showFilterControl;
		},
		addObject:function(){
			this.$eventBus.$emit('addobj','');
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
				 this.$store.commit('setfilterstrings', this.filterStrings);
		},
	 	removeAllFilters: function(){
			if(this.filterStrings.length!=0){
				this.filterStrings.splice(0);
			}
			this.removeDateFilter();
			this.$store.commit('setfilterstrings', this.filterStrings);
	  },
	 	removeDateFilter : function(){
				 this.dateRange=JSON.parse(JSON.stringify(this.defaultDateRange));
				 this.setdaterange();
		},
		removeString: function (s) {
		  this.filterStrings.splice(this.filterStrings.indexOf(s), 1);
			this.$store.commit('setfilterstrings', this.filterStrings);
		},
		setdaterange:function(){
			this.$store.commit('setdaterange',this.dateRange);
		}
	},
	components:{
		applayout,
		kotile,
		'date-picker': myDatepicker,
		kgdropdown
		}
};
</script>
<style>
ul#kolisting li {
	margin-top:20px;
}
.kgl-search {
    display: inline-block;
    border: 1px solid #fff;
	border-radius: 10px;
    width: 46%;
    margin-left: 15px;
}
.kgl-count, .kgl-sort {
	line-height: 3em;
padding-right: 0px;
}
.kgl-search input {
    line-height: 2.8em;
    font-size: 14px;
    padding: 0px 0px 0px 16px;
    width:95%;
}
input[id$="datepicker"] {
    width: 150px;
}
#filterBtn {
  background-color: #fff;
  margin: 0px 0px 0px 0px;
  padding: 12px 0px 12px 0px;
  text-align: center;
	cursor: pointer;
	width: 130px;
	height: 45px;
}
#filterBtn.tall{
	height: 65px;
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
	margin: 0px -10px 0px 10px;
}
i#filterdowniconimg
{
	margin-left:25px;
}
#filterpanel {
	background-color:#fff;
	margin: 0px 0px 0px 0px;
	padding: 25px;
}
.filterlist li, .filterlist button#clearAll{
	display:inline-block;
	background-color:#fff;
	margin:0px 20px 0px 0px;
	padding:12px 15px 12px 8px;
	height: 44px;
}
button#clearAll span {
	font-size:14px;
  border-bottom: 2px solid transparent;
	transition: border 0.5s ease;
}
.filterlist button#clearAll:hover span{
	border-bottom: 2px solid #0075bc;
}
.filterlist li:hover *{
	color: #0075bc;
}
.destroy {
	width:26px;
	height: 26px;
	border:1px solid #fff;
	border-radius: 100%;
	margin: -5px 5px 0px 0px;
	padding: 0px 0px 0px 0px;
	background-color: transparent;
}
.filterlist li label {
	display: inline-block;
	margin: 0px;
	text-align: center;
	vertical-align: top;
}
.filterlist button#clearAll {
	background-color: #e5e5e5;
	font-size: 12px;
	vertical-align: top;
	color: #0075bc;
}
#filterBtn a:hover, #filterBtn a:visited {
  color: #555555;
}
label {
  font-weight: 400;
  margin:10px 0px;
}
input[type=radio] {
  display: inline-block;
}
.bannercontent {
    margin-top: 75px;
    text-align: left;
    margin-bottom: 20px;
    margin: 0 auto;
    line-height: 1.5em;
    padding-top: 65px;
    background: transparent;
}
.bannercontent h1 {
	line-height:1.2em;
}
.kgl-newobj{
	width: 24px;
	height: 24px;
	position:absolute;
	top: 10px;
  right:0px;
  margin:0 auto;
  z-index:500;
}
.kgl-newobj span {
	position: absolute;
	top:2px;
	left:-190px;
	width:174px;
	opacity:1;
	border-bottom: 2px solid transparent;
	transition: border 0.5s ease;
}
.kgl-newobj:hover span{
	border-bottom: 2px solid #0075bc;
	cursor:pointer;
}
.kgl-newobj i {
	position: absolute;
	top: 6px;
	left:7.5px;
}
.row.filter {
	height: 36px;
}
.row.filterlabel {
	height: 28px;
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
	padding-left: 28px;
	top: 11px;
	width: 200px;
	color: #0275bc;
}
.custom-control-indicator {
    position: absolute;
    top: 10px;
    left: 5px;
    display: block;
    width: 22px;
    height: 22px;
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
	border: 1px solid #0075bc;
}
.custom-checkbox .custom-control-input:disabled~.custom-control-indicator {
  background-color: #e5e5e5;
	border: 1px solid #555555;
}
.custom-checkbox .custom-control-indicator i {
	position:absolute;
	left:4px;
	top:3px;
}
.custom-radio .custom-control-indicator i{
	position:absolute;
	left:3px;
	top:2px;
}
.btnContent{
	padding: 3px 0px 0px 4px;
}
.custom-radio .custom-control-indicator {
  border-radius: 100%;
	border: 1px solid #0075bc;
}
.datepick>span {
	position: absolute;
	top: 4px;
	left: 20px;
}
.datepick p {
	position: absolute;
	top: 0px;
	left: 30px;
}
.filterBtnCol {
	width: 170px;
}
.expand-enter-active, .expand-leave-active {
  transition: all .3s linear;
  height: 270px;
  overflow: hidden;
	opacity:1;
}
.expand-enter, .expand-leave {
  height: 0;
	overflow:hidden;
  opacity: 0;
}
 span svg {
	 margin-left:2px;
	 margin-top:2px;
 }
</style>
