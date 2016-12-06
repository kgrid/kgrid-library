'use strict';
var eventBus = new Vue({});
var raw = 9;
var objModel = { object : { metadata:{title:"",keywords:"",contributors:"",published:"",citations:[],license:{licenseName:"",licenseLink:""}}, payload:{functionName:"",engineType:"",content:""},inputMessage:"", outputMessage:"", uri:"",published:false,lastModified:0,createdOn:0} };
var editObjModel = { object : { metadata:{title:"Edit object",keywords:"",contributors:"",published:"",citations:[],license:{licenseName:"",licenseLink:""}}, payload:{functionName:"",engineType:"",content:""},inputMessage:"", outputMessage:"", uri:"ark"} };
//var userModel= {user:{username:"",password:"",id:-1,first_name:"",last_name:"",role:""}};
var userModel= {user:{username:"",password:""}};

var sections = [{name:"metadata",id:"#metadata",label:"METADATA"},
                {name:"payload",id:"#payload",label:"PAYLOAD"},
                {name:"inputMessage",id:"#inputMessage", label:"INPUT"},
                {name:"outputMessage",id:"#outputMessage", label:"OUTPUT"},
                {name:"logData",id:"#logData", label:"LOG DATA"}];
var applayout = Vue.component("applayout", {
		template:"#layout",
		props:['nothelper'],
		methods:{
			backToTop: backToTop
		}
})
var olpane= Vue.component("olpane",{
	template:"#ol-pane-template",
	props:['layerid', 'left'],
	created:function(){
		var self=this;
		eventBus.$on("open",function(x){
//			console.log(x);
			$(".modal-mask").css('opacity',1);
			$(".ol_pane").animate({
			'left' : x
		}, 1000);
		});
	},
	methods:{
		closeOverlay:function(){
			eventBus.$emit('slideout',this.layerid);
		},
		openOverlay:function(){
			this.find(".ol_pane").animate({
				'left' : '30%'
			}, 1000);
		}
	}
});

var olnarrowpane= Vue.component("olnarrowpane",{
	template:"#ol-narrow-pane-template",
	props:['layerid', 'left'],
	created:function(){
		var self=this;
		eventBus.$on("open",function(x){
//			console.log(x);
			$(".modal-mask").css('opacity',1);
			$(".ol_pane").animate({
			'left' : x
		}, 1000);
		});
	},
	methods:{
		closeOverlay:function(){
			eventBus.$emit('slideout',this.layerid);
		},
		openOverlay:function(){
			this.find(".ol_pane").animate({
				'left' : '30%'
			}, 1000);
		}
	}
});

var login= Vue.component("loginoverlay",{
	template:'#login_overlay',
	data:function(){
		return {
			userModel:{user:{username:"",password:""}},
			role:"default",
		}
	},
	methods:{
		userlogin:function(){
			var self = this;
			var text = JSON.stringify(this.userModel.user);
			console.log("Data to send: "+text);
			if(true){
				$( "div.processing" ).fadeIn( 300 );
				$.ajax({
						type : 'POST',
						url : "/ObjectTeller/login",
						data : self.userModel.user,
						success : function(response,tStatus,xhr) {
    					      $( "div.processing" ).fadeOut( 200 );
						      $("div.success").fadeIn(300).delay(500).fadeOut(400, function(){
					    	  eventBus.$emit("userloggedin",self.userModel.user);
								});
						} ,
						error : function(response) {
							$( "div.processing" ).fadeOut( 200 );
							$( "div.failure" ).fadeIn( 300 ).delay( 500 ).fadeOut( 400 );
						}
					});
			}else{
				}
			}, 
		}
});

var vmcomp = Vue.component(
				"ko-tile",
				{
					template : "#ko-tile-template",
					props : [ 'object', 'listsize' ,'tileindex'],
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
//													console.log("Deletion successful!");
													self.$emit('remove');
												}
											});
								}
							}
						},
						selected: function(){
//							console.log(this.object.uri+"selected");
							eventBus.$emit("objectSelected", this.object);
							sessionStorage.setItem("otObj", JSON.stringify(this.object));
							return false;
						}
					}
				});
var vm_fields = Vue.component(
				"field-tile",
				{
					template : "#field-tile-template",
					props : [ 'field' ,'fieldmodel', 'object',"isDisabled"],
					created:function(){
					},
					data:function(){
						return {raw:raw}
					},
					computed : {
						value : function() {
							var propertyValue = "";
							switch (this.field.section) {
							case "metadata":
								propertyValue = this.object[this.field.section][this.field.name];
								break;
							case "payload":
								propertyValue = this.object[this.field.section][this.field.name];
								break;
							default:
								propertyValue = this.object[this.field.name];
								break;
							}
							return propertyValue;
						},
						isInput : function() {
							return (this.field.type == "text");
						},
						isTextArea : function() {
							return (this.field.type == "textarea");
						},
						isLicense : function() {
							return this.field.name == "license";
						},
						isCitation : function() {
							return this.field.name == "citations";
						}
					},
					methods:{
					onInput: function (event) {
					      this.$emit('input', event.target.value)
					    }
					}
				});
var vm_linkedfields = Vue.component("linkedfield-tile", {
	template : "#linkedfield-tile-template",
	props : [ "link", "value" ]
});
const tabPane = Vue.component('tab-pane', {
	template: "#tab-panel-template",
	props:['section','object','isDisabled'],
	data: function(){
		return {
			fields_json : {
				fields : []
			},
			raw:9
		}
	},
	beforeCreate : function() {
		var self = this;
		loadFieldsConfig(function(data) {
			self.fields_json.fields = data.fields;
		});
	},
	updated : function() {

	},
	created : function() {

	},
	computed : {
		filteredFields :function(){
			var section = this.section;
			var fields = this.fields_json.fields;
			return fields.filter(function(field){
				return (field.section==section)
			})
		}
}
});
const objDetail = Vue.component('ko-detail', {
	template : '#obj-detail',
	data : function() {
		return {
			objModel : objModel,
			sections : sections,
			isDisabled: 1,
			isPublic:false
		}
	},
	created : function() {
		var self = this;
		eventBus.$on("objSaved",function(obj){
			$.extend(true, objModel.object, obj);
		});
		eventBus.$on('objectSelected',function(obj){
			$.extend(true, objModel.object, obj);	
		});
		var sessionObj = sessionStorage.getItem("otObj");
		if(sessionObj){
			 $.extend(true, self.objModel.object, JSON.parse(sessionObj));
		}

	},
	mounted:function() {
		var self = this;
		retrieveObject(this.$route.params.uri, "complete", function(
				response) {
			self.objModel.object = response;
			self.isPublic = self.objModel.object.metadata.published;

		}); 
		$('ul#tabs li:first').addClass('active'); 
	    $('ul#tab li:first').addClass('active');
	    $('ul#tabs li.active').addClass('middleout');
	   // tabNav("");
	    otScroll();
	    $("html, body").animate({
	        scrollTop: 0
	    }, 200);
	    
		$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
			  var target = $(e.target).attr("href") // activated tab
			  $('ul#tabs li.middleout').removeClass('middleout');
			  $('ul#tabs li.active').addClass('middleout');
			  $(".autosize").each(autoresize);
		});	
	},
	computed : {
		formattedUpdateDate : function() {
			if(!this.objModel.object.metadata.lastModified || this.objModel.object.metadata.lastModified=="" ){
				return ""
				}
			else
				{return new Date(this.objModel.object.metadata.lastModified)
				.format("mediumDate")}
		},
		formattedCreateDate : function() {
			return new Date(this.objModel.object.metadata.createdOn)
					.format("mediumDate")
		},
		downloadLink : function() {
			return '/ObjectTeller/knowledgeObject/'
					+ this.objModel.object.uri + '/complete.json'
		}
	},
	updated : function() {
		$('ul#tabs li:first').addClass('active'); 
	    $('ul#tab li:first').addClass('active'); 
		$(".autosize").each(autoresize);
	},
	methods:{
		editObj:function(){
			$.extend(true, editObjModel.object, this.objModel.object);
			if(editObjModel.object.outputMessage==null){
				editObjModel.object.outputMessage="";
			}
			if(editObjModel.object.inputMessage==null){
				editObjModel.object.inputMessage="";
			}
			eventBus.$emit('editObj', this.objModel.obj);
		},
		publish:function(){
			this.toggleObject(true);
		},
		unpublish:function(){
			this.toggleObject(false);
		},
		toggleObject:function(pub){
			var uri=this.objModel.object.uri;
			var published='';
			var self=this;
			if(pub){
				published='published';
			}else{
				published='unpublished';
			}
			$.ajax({
				beforeSend : function(xhrObj) {
					xhrObj.setRequestHeader("Content-Type", "application/json");
				},
				type : 'PUT',
				url : "/ObjectTeller/knowledgeObject/" + uri + "/" + published,
				success : function(response) {
					self.isPublic=pub;
					objModel.object.metadata.published=pub;
				},
				error : function(response, tStatus, xhr) {
					console.log(response);
				}
			});
		},
		deleteObject : function() {
			var self=this;
			var uri = this.objModel.object.uri;
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
									window.location.href = "/ObjectTeller/vuehome/home.html";
								}
							});
				}
			}
		},
		returntolibrary: function(){
			eventBus.$emit("return");
		},
	}
});
const Home = Vue.component("ko-main", {
	template : '#ko-list',
	data : function() {
		return {
			sortKey : "metadata.lastModified",
			order : "asc",
			isLoggedIn:false,
			searchQuery : "",
			model : {
				koList : []
			},
			check:{keywords:true,owners:true,title:true,citations:false,contributors:false,objectID:false, pub:true,pri:false},
 			showmyobj:false,
 			filterStrings:[],
 			newstring:'',
 			datetype:'lastModified',
 			startdate:0,
			enddate:0,
			userModel:{user:{username:"",password:""}},
			isAdmin:true
 		}
	},
	created : function() {
		var self = this;
		this.startdate = new Date("March 1, 2016").getTime();
		this.enddate=new Date().getTime();
		//$.extend(true,this.userModel,userModel);
		this.isLoggedIn = (this.userModel.user.username!="");
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
		eventBus.$on("startdate",function(date){
			self.startdate=date;
		});
		eventBus.$on("enddate",function(date){
			self.enddate=date;
		});
		eventBus.$on("userloggedin",function(obj){
			self.isLoggedIn=true;
			$.extend(true, self.userModel.user,obj);
			self.isAdmin = (self.userModel.user.role=="ADMIN");
			self.check.pri=true;
		});
		eventBus.$on("logout", function(){
			$.extend(true, self.userModel.user, {username:"",password:""});
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
				return count + " Object";
			} else if (count > 200) {
				return "Many many Objects";
			} else {
				return count + " Objects";
			}
		},
		listSize : function() {
			return this.filteredList.length;
		},
		orderAsc : function() {
			return (this.order == "asc")
		},
		orderedList : function() {
					return _.orderBy(this.filteredList, this.sortKey,
					this.order)
		},
		filteredList :function(){
			var self = this;
			var list = this.model.koList;
			if(!this.isLoggedIn){
				list=list.filter(function(field){
					return (field.metadata.published)
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
													var fString = new RegExp(filterString.title,"i");
													
													if (self.check.title) {
														filterResult = (filterResult || ((field.metadata.title
																.search(fString))!=-1));
													}
//													console.log(fString.toString()+" "+filterResult);
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
			})
		}
	},
	methods : {
		toggleOrder : function() {
			if (this.order == "asc") {
				this.order = "desc";
			} else {
				this.order = "asc";
			}
		},
		addObject:function(){
			eventBus.$emit("addobj","");
		},
		 addFilterString: function () {
			   var value = this.newstring && this.newstring.trim()
			   if (!value) {
			     return
			   }
			   var uid=this.filterStrings.length;
			   this.filterStrings.push({
			     id: uid++,
			     title: value,
			   })
			   this.newstring = '';
			 },
		 
			 removeString: function (s) {
				   this.filterStrings.splice(this.filterStrings.indexOf(s), 1)
				 },
				 setstartdate:  function(){
					 var startdate=$("#startdatepicker").val();
					 var sstamp=new Date(startdate).getTime();
					 eventBus.$emit("startdate",sstamp);
					console.log("Start date:"+ sstamp);
				 }
		
	},
	components:{'appLayout':applayout}
});

var objcreator = Vue.component("objcreator",{
	template:"#objcreator_overlay",
	data:function(){
		return {
		newobjModel:{object:{metadata:{title:""},uri:"arkid"}},
		newtitle:"",
		jsonobj:""
		}
	},
	created:function(){
		
	},
	computed:{
		newid:function(){
			if(!this.newobjModel.object.uri){
				return "";
			}else{
			return this.newobjModel.object.uri;
			}
		}
	},
	methods:{
		createObj:function(){
			var self=this;
			var text = JSON.stringify(self.newobjModel.object);
//			console.log("data to sent:"+text);
			$.ajax({
				beforeSend : function(xhrObj) {
					xhrObj.setRequestHeader("Content-Type", "application/json");
					xhrObj.setRequestHeader("Accept", "application/json");
				},
				type : "POST",
				url : "/ObjectTeller/knowledgeObject",
				data : text,
				dataType : "json",
				success : function(response) {
					console.log(response);
					if ((response != 'empty') && (response != null)) {
						var test = JSON.stringify(response);
						var obj = JSON.parse(test);
	//					console.log(test);
						$.extend(true, self.newobjModel.object, obj);
					}
					$("div.processing").fadeOut(200);
					$("div.success").fadeIn(300).delay(2000).fadeOut(400, function(){
							eventBus.$emit("objcreated",obj);
					});
				},
				error : function(response) {
	//				console.log(response);
					$("div.processing").fadeOut(200);
					$("div.warning").text(response.status+"   "+response.statusText);
					$("div.warning").show();
					
					//test code, to be removed 
					eventBus.$emit("objcreated",{"metadata":{"title":"error"}});
				}
			});	
		},
		 updatedisplay : function(sec, msg){
	//	    	console.log("Section:"+sec+" Msg:"+msg);
		    	var fullobj={};
		    	if(msg!=""){
		    	try{
		    		fullobj=JSON.parse(msg);
		    		$.extend(true, this.newobjModel.object, fullobj);
				}catch(e){
					$("div.warning").text(e.message);
					$("div.warning").show();
				}
		    	}else{
		    		$.extend(true, this.newobjModel.object, {"metadata":{"title":""}});
		    	}
		    },
		    
	}
});

var objeditor=Vue.component("objeditor",{
	template:'#objEditor_overlay',
	data:function(){
		return {
			editObjModel:editObjModel,
			sections:sections,
			isDisabled:false,
			citationIndex:0,
			showSecOverlay:{show:false},
			srcFieldModel:{object:{title:"",link:""},originalObject:{title:"",link:""}},
			inEdit:"License",
		}
	},
	created:function(){
		var self=this;
		$(".ol_pane").css("width","1100px");
		eventBus.$on('slideout',function(id){
			if(id==1) self.showSecOverlay.show=false;
			self.resetSrcField();
		});
	},
	updated:function(){
	},
	computed:{

	},
	mounted:function(){
		$('ul#edittabs li:first').addClass('active'); 
	    $('ul#edittab li:first').addClass('active'); 
		$("ul#edittabs li.active").addClass("middleout");
		editTabNav();
	},
	updated : function() {


	},
	component:{
		  fileuploader:fileuploader
	  },
	methods: {
		 updatedisplay : function(sec, msg){
//			    	console.log("Section:"+sec+" Msg:"+msg);
			    	switch(sec) {
			    		case "payload":
			    			this.editObjModel.object.payload.content=msg;
			    			break;
			    		case "inputMessage":
			    			this.editObjModel.object.inputMessage=msg;
			    			break;
			    		case "outputMessage":
			    			this.editObjModel.object.outputMessage=msg;
			    			break;
			    		
			    	}
			    },
		saveObj:function(){
			var text = JSON.stringify(this.editObjModel.object);
			var self =this;
//			console.log(text);
			$("div.processing").fadeIn(300);
			$.ajax({
				beforeSend : function(xhrObj) {
					xhrObj.setRequestHeader("Content-Type", "application/json");
					xhrObj.setRequestHeader("Accept", "application/json");
				},
				type : "PUT",
				url : "../knowledgeObject/"+this.editObjModel.object.uri,
				data : text,
				dataType : "json",
				success : function(response) {
//					console.log(response);
					if ((response != 'empty') && (response != null)) {
						var test = JSON.stringify(response);
						var obj = JSON.parse(test);
					}
					eventBus.$emit("objSaved",self.editObjModel.object);
					$("#addObj_f").find("ul#tabs li").each(function() {
//						console.log("UL li text:" + $(this).text());
						$(this).text($(this).text().replace("*", ""));
					});
					$("div.processing").fadeOut(200);
					$("div.success").fadeIn(300).delay(2000).fadeOut(400, function(){
							
						});
					
				},
				error : function(response) {
//					console.log(response);
					$("div.processing").fadeOut(200);
					$("div.failure").text(response.status+"   "+ response.statusText);
					$("div.faiure").fadeIn(300).delay(1500).fadeOut(400);
					
				}
			});
		},
		undoEdit: function(){
			editObjModel.object = this.$parent.getObject();
			if(editObjModel.object.outputMessage==null){
				editObjModel.object.outputMessage="";
			}
			if(editObjModel.object.inputMessage==null){
				editObjModel.object.inputMessage="";
			}
		},
		setSrcField:function(obj){
			$.extend(true, this.srcFieldModel.object, obj);
			$.extend(true, this.srcFieldModel.originalObject,obj);
		},
		getOriginalField:function(){
			return $.extend(true, {}, this.srcFieldModel.originalObject);
		},
		resetSrcField:function(){
			this.srcFieldModel.object={title:"",link:""};
		},
		addLicense: function(){
			var srcObj = {title:"",link:""};
			this.setSrcField(srcObj);
			this.showSecOverlay.show=true;
			this.inEdit="License";
		},
		selectLicense:function(obj){
			var srcObj = {title:"",link:""};
			srcObj.title=obj.licenseName;
			srcObj.link=obj.licenseLink;
			this.setSrcField(srcObj);
			this.showSecOverlay.show=true;
			this.inEdit="License";
			},
		deleteLicense:function(){
			editObjModel.object.metadata.license={};
		},
		addCitation:function(){
			if(!editObjModel.object.metadata.citations){
				editObjModel.object.metadata.citations=[];
			}
			this.citationIndex=editObjModel.object.metadata.citations.length;
			editObjModel.object.metadata.citations.push({citation_title:"",citation_at:""});
			this.srcFieldModel.object={title:"",link:""};
			this.showSecOverlay.show=true;
			this.inEdit="Citation";

		},
		selectCitation:function(obj){
			var srcObj = {title:"",link:""};
			srcObj.title=obj.citation_title;
			srcObj.link=obj.citation_at;
			this.setSrcField(srcObj);
			this.showSecOverlay.show=true;
			this.inEdit="Citation";
			this.citationIndex=editObjModel.object.metadata.citations.indexOf(obj);
//			console.log("selected index:"+this.citationIndex);
		},
		deleteCitation:function(obj){
			editObjModel.object.metadata.citations.splice(editObjModel.object.metadata.citations.indexOf(obj), 1);
		},
		update:function(obj){
			
			this.showSecOverlay.show=false;
			this.setSrcField(obj);
	//		console.log(JSON.stringify(obj));
			switch(this.inEdit){
			case "License":
				editObjModel.object.metadata.license.licenseName=obj.title;
				editObjModel.object.metadata.license.licenseLink=obj.link;
				break;
			case "Citation":
				if(editObjModel.object.metadata.citations.length==0){
//					editObjModel.object.metadata.citations=[];
					editObjModel.object.metadata.citations.push({citation_title:obj.title,citation_at:obj.link});
				}else{			
					editObjModel.object.metadata.citations[this.citationIndex].citation_title=obj.title;
					editObjModel.object.metadata.citations[this.citationIndex].citation_at=obj.link;
				}
				break;
			}
		}
	}
	
});


var linkedfieldEditor = Vue.component("linkedfieldeditor",{
	template:'#linkedfield_editor',
	props:['inedit', 'srcfield'],
	data: function(){
		return {linkedField:{}}
	},
	mounted:function(){
		this.linkedField=this.srcfield;
	},
	watch:{
		srcfield:function(){
			this.linkedField=this.srcfield;
		}
	},
	methods:{
		undoEdit: function(){
			this.linkedField= this.$parent.getOriginalField();
		},
		doneEdit:function(){
			this.$emit('slideout',this.linkedField);
		},
		preview: function(){
			var myWindow = window.open(this.linkedField.link, "myWindow", "width=400,height=700");   // Opens a new window
			myWindow.focus();
		}
	}
});

var fileuploader = Vue.component("fileuploader",{
	template:"#file_uploader",
	props:['section','src'],
	data: function(){
	   return { selectedfile:false,
	    		message:{msg:""},
	  }},
	  computed:{
		  selectfile:function(){
			  return this.section+"file";
		  }
	  },
	  created:function(){
		  if(this.src!=null){
			  this.selectedfile=(this.src!="");
		  }else{
			  this.selectedfile=false;
		  }
		  this.message.msg=this.src;
	  },
	  watch:{
		  src:function(){
			  if(this.src!=null){
				  this.selectedfile=(this.src!="");
				  this.message.msg=this.src;
			  }else{
				  this.selectedfile=false;
				  this.message.msg='';
			  }
		  }
	  },
	methods:{
		inputchange:function(){
			this.$emit("filechange",this.section,this.message.msg);
		},
		loadContent: function(file) {
	    	var self=this;
	        var reader = new FileReader();
	        reader.onload = (function(f) {
				return function(e) {
					var contents = e.target.result;
					self.message.msg=contents;
					self.$emit("filechange",self.section,self.message.msg);
				};
			})(file);
	        reader.readAsText(file);
	      },
		 onFileChange: function(e) {
		      var files = e.target.files || e.dataTransfer.files;
		      this.selectedfile=true;
		      if (!files.length)
		        return;
		      this.loadContent(files[0]);
		      },
	    removeOutputFile: function (e) {
		      this.selectedfile = '';
		      this.message.msg="";
		      this.$emit("filechange",this.section,this.message.msg);
		    }
	}
});
const routes = [ 
    { path : '/', component : Home	}, 
    { path : '/object/:uri', name : 'object', component : objDetail, data: function(){
 //   	console.log("current URI"+ this.$route.params.uri);
    }	} 
    ];
const router = new VueRouter({
	routes : routes,
	hashbang : false,
	history : true
});
var navbar = Vue.component("navbar",{
	template:"#navbar",
	data:function(){
		return {
			userModel:userModel
		}
	},
	updated:function(){
		$('#userDropdown').on('show.bs.dropdown', function(e){
			  var target = $(e.target).attr("id"); // activated tab
			  $("img#dropdowniconimg").removeClass('down');
			  $("img#dropdowniconimg").addClass('up');	
		});
		$('#userDropdown').on('hide.bs.dropdown', function(e){
			  var target = $(e.target).attr("id"); // activated tab
			  $("img#dropdowniconimg").removeClass('up');
			  $("img#dropdowniconimg").addClass('down');		  
		});
	},
	computed:{
		isLoggedIn:function(){
			return (this.userModel.user.username!="")
		}
	},
	methods:{
		login_click:function(){
			eventBus.$emit("open","500px");
		},
		userlogout:function(){
			var self=this;
			$.ajax({
				type : 'POST',
				url : "/ObjectTeller/logout" ,
				success : function(response) {
					 $.extend(true, self.userModel.user,{username:"",password:"",id:-1,first_name:"",last_name:"",role:""});
					 eventBus.$emit("logout");
				}
			});
		},
	},
});
var vm = new Vue({
	router : router,
	data : {
		currentOLView:'objeditor',
		koModel:objModel,
		showOverlay:{show:false},
		showSecOverlay:{show:false},
		userModel:userModel
	},
	components:{
		login : login,
		objeditor : objeditor,
		objcreator :objcreator
	},
	computed:{
		isLoggedIn:function(){
			var loggedin =false;
			loggedin = (this.userModel.user.username!="");
			return loggedin;
		}
	},
	updated:function(){
	},
	beforeCreate:function(){
		var self=this;
		getCurrentUser(function(response) {
			if(response!="")
				$.extend(true, self.userModel.user, response);
		})
	},
	created: function(){
		var self=this;
		eventBus.$on('editObj', function(obj){
			self.currentOLView='objeditor';
			self.showOverlay.show=true;
			document.body.classList.toggle('noscroll', true);
		});
		eventBus.$on('objSaved', function(obj){
			self.updateObject(obj);
			self.showOverlay.show=false;
			document.body.classList.toggle('noscroll', false);
		});
		eventBus.$on('slideout', function(layerid){
			switch(layerid){
				case '0':
					self.showOverlay.show=false;
					document.body.classList.toggle('noscroll', false);
					break;
				case '1': 
					self.showSecOverlay.show=false;
					break;
			}
		});
		eventBus.$on("objcreated",function(obj){
			$.extend(true, self.koModel.object, obj);
			$.extend(true, editObjModel.object, obj);
			self.currentOLView="objeditor";
			if(editObjModel.object.metadata.license==null){
				editObjModel.object.metadata.license={licenseName:"",licenseLink:""};
			}
		});
		eventBus.$on("addobj", function(s){
			self.currentOLView="objcreator";
			self.showOverlay.show=true;
			document.body.classList.toggle('noscroll', true);
		});
		 eventBus.$on("userloggedin",function(obj){
			 self.showOverlay.show=false;
			 document.body.classList.toggle('noscroll', false);		
			 getCurrentUser(function(response) {
				if(response!="")
						$.extend(true, self.userModel.user, response);
			})
		 });
		 eventBus.$on("return", function(){
			router.push({ path: '/' }); 
		 });
			eventBus.$on("open", function(x){
				self.showOverlay.show=true;
				self.currentOLView='login';
				document.body.classList.toggle('noscroll', true);
			});
	},
	mounted:function(){
		overlayHeightResize();
	},
	methods:{
		createObj_click:function(){
			this.showOverlay.show=true;
			objURI="";
		},
		getObject:function(){
			return $.extend(true, {}, this.koModel.object);
		},
		updateObject:function(obj){
			$.extend(true, objModel.object, obj);
		},
	}}).$mount('#app');