'use strict';
var eventBus = new Vue({});
var raw = 9;
var objModel = { object : { metadata:{title:"View Object"}} };
var editObjModel = { object : { metadata:{title:"Edit object",keywords:"",contributors:"",published:"",citations:[],license:{licenseName:"",licenseLink:""}}, payload:{functionName:"",engineType:"",content:""},uri:"ark"} };
var licenseModel = {license:{}};
var citationModel = {citation:{}};

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
	methods:{
		closeOverlay:function(){
			eventBus.$emit('slideout',this.layerid);
		}
	}
});

var login= Vue.component("loginoverlay",{
	template:'#login_overlay',
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
							return {
								name : 'object',
								params : {
									uri : this.object.uri
								}
							};
						}
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
/*						fieldmodel1: function(){
							var propertyModel = "";
							switch (this.field.section) {
							case "metadata":
								propertyModel = "editObjModel.object"+this.field.section+"."+this.field.name;
								break;
							case "payload":
								propertyModel = "editObjModel.object"+this.field.section+"."+this.field.name;
								break;
							default:
								propertyModel = "editObjModel.object"+this.field.section+"."+this.field.name;
								break;
							}
							return propertyModel;
							
						},*/
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
		$('.autosize').each(autoresize);
	},
	created : function() {
		$('.autosize').each(autoresize);
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
			isDisabled: 1
		}
	},
	created : function() {
		var self = this;
		eventBus.$on("objSaved",function(obj){
			console.log("obj Saved");
			$.extend(true, objModel.object, obj);
		});
	},
	updated: function(){
		var textareas =  $('.autosize');
		textareas.each(autoresize());
	},
	mounted:function() {
		var self = this;
		retrieveObject(this.$route.params.uri, "complete", function(
				response) {
			self.objModel.object = response;
		}); 
		$('ul#tabs li:first').addClass('active'); 
	    $('ul#tab li:first').addClass('active');
	    otScroll();
	    $("html, body").animate({
	        scrollTop: 0
	    }, 200);
	   
	},
	computed : {
		formattedUpdateDate : function() {
			return new Date(this.objModel.object.metadata.lastModified)
					.format("mediumDate")
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
		$('.autosize').each(autoresize);
	},
	methods:{
		editObj:function(){
			$.extend(true, editObjModel.object, this.objModel.object);
			eventBus.$emit('editObj', this.objModel.obj);
		},
		deleteobj:function(){
			eventBus.$emit('deleteObj', this.$route.params.uri);
		}
	}
});
const Home = Vue.component("ko-main", {
	template : '#ko-list',
	data : function() {
		return {
			sortKey : "metadata.lastModified",
			order : "asc",
			filterString : "",
			searchQuery : "",
			model : {
				koList : []
			},
		}
	},
	created : function() {
		var self = this;
		retrieveObjectList(function(response) {
			self.model.koList = response;
			if(self.model.koList.length>0){
				objModel.object=self.model.koList[0];
			}
		});
		eventBus.$on('objectSelected',function(obj){
			objModel.object=obj;
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
			var list = this.model.koList;
			var filterString = this.filterString;
			return list.filter(function(field){
						return (field.metadata.title.includes(filterString) ||
						field.metadata.keywords.includes(filterString) ||
						field.metadata.owner.includes(filterString) )
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
		
	},
	components:{'appLayout':applayout}
});
var objeditor=Vue.component("objeditor",{
	template:'#objEditor_overlay',
	data:function(){
		return {
			editObjModel:editObjModel,
			sections:sections,
			isDisabled:false,
			citationIndex:0,
			licenseModel:licenseModel,
			newCitation:{},
			showSecOverlay:{show:false},
			srcObjectModel:{object:{title:"",link:""}},
			inEdit:"License",
		}
	},
	created:function(){
		var self=this;
		eventBus.$on('slideout',function(id){
			if(id==1) self.showSecOverlay.show=false;
		});
	},
	updated:function(){
	},
	computed:{
		outputfile:function(){
			var outputEmpty=false;
			if(this.editObjModel.object.outputMessage){
				console.log("output empty:"+outputEmpty);
				outputEmpty = (this.editObjModel.object.outputMessage=="");
			}
			console.log(outputEmpty);
			return outputEmpty;
		}
	},
	mounted:function(){
		editTabNav();
	},
	updated : function() {
		$('ul#etabs li:first').addClass('active'); 
	    $('ul#etab li:first').addClass('active'); 
		$('.autosize').each(autoresize);
	},
	methods: {
		 onFileChange: function(e) {
		      var files = e.target.files || e.dataTransfer.files;
		      this.outputfile=true;
		      if (!files.length)
		        return;
		      this.loadContent(files[0]);
		    },
		loadContent: function(file) {
		        var reader = new FileReader();
		        reader.onload = (function(f) {
					return function(e) {
						var contents = e.target.result;
						$("#outputTextArea").val(contents);
					};
				})(f);
		        reader.readAsText(file);
		      },
		removeOutputFile: function () {
				console.log("Remove button clicked.");
				$("#outputTextArea").val("");;
		      },
		saveObj:function(){
			var text = JSON.stringify(this.editObjModel.object);
			var self =this;
			console.log(text);
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
					console.log(response);
					if ((response != 'empty') && (response != null)) {
						var test = JSON.stringify(response);
						var obj = JSON.parse(test);
					}
					eventBus.$emit("objSaved",self.editObjModel.object);
					$("#addObj_f").find("ul#tabs li").each(function() {
						console.log("UL li text:" + $(this).text());
						$(this).text($(this).text().replace("*", ""));
					});
					$("div.processing").fadeOut(200);
					$("div.success").fadeIn(300).delay(2000).fadeOut(400, function(){
							overlaySlide("addObject", false, "edit");
						});
				},
				error : function(response) {
					console.log(response);
					$("div.processing").fadeOut(200);
					$("div.failure").text(response.status+"   "+ response.statusText);
					$("div.faiure").fadeIn(300).delay(1500).fadeOut(400);
					
				}
			});
		},
		undoEdit: function(){
			editObjModel.object = vuem.getObject();
		},
		addLicense: function(){
			this.srcObjectModel.object={title:"",link:""};
			this.showSecOverlay.show=true;
			this.inEdit="License";
			//eventBus.$emit('editLicense', {});
		},
		selectLicense:function(obj){
			this.srcObjectModel.object.title=obj.licenseName;
			this.srcObjectModel.object.link=obj.licenseLink;
			this.showSecOverlay.show=true;
			this.inEdit="License";
			},
		deleteLicense:function(){
			editObjModel.object.metadata.license={};
		},
		addCitation:function(obj){
			this.citationIndex=editObjModel.object.metadata.citations.length;
			editObjModel.object.metadata.citations.push(obj);
			this.newCitation = {};
			this.srcObjectModel.object={title:"",link:""};
			this.showSecOverlay.show=true;
			this.inEdit="Citation";
			console.log("selected index:"+this.citationIndex);
		},
		selectCitation:function(obj){
			this.srcObjectModel.object.title=obj.citation_title;
			this.srcObjectModel.object.link=obj.citation_at;
			this.showSecOverlay.show=true;
			this.inEdit="Citation";
			this.citationIndex=editObjModel.object.metadata.citations.indexOf(obj);
			console.log("selected index:"+this.citationIndex);
		},
		updateCitation:function(obj){
			$.extend(true, this.citationModel.citation, obj);
		},
		deleteCitation:function(obj){
			editObjModel.object.metadata.citations.splice(editObjModel.object.metadata.citations.indexOf(obj), 1);
		},
		
		update:function(obj){
			this.showSecOverlay.show=false;
			console.log(JSON.stringify(obj));
			switch(this.inEdit){
			case "License":
				editObjModel.object.metadata.license.licenseName=obj.title;
				editObjModel.object.metadata.license.licenseLink=obj.link;
				this.srcObjectModel.object.title=obj.title;
				this.srcObjectModel.object.link=obj.link;
				break;
			case "Citation":
				editObjModel.object.metadata.citations[this.citationIndex].citation_title=obj.title;
				editObjModel.object.metadata.citations[this.citationIndex].citation_at=obj.link;
			}
		}
	}
	
});


var linkedfieldEditor = Vue.component("linkedfieldeditor",{
	template:'#linkedfield_editor',
	props:['inEdit'],
	data: function(){
		return {linkedField:{},
			beforeEdit:{}}
	},
	created:function(){
		this.linkedField=this.$parent.srcObjectModel.object;
		
	},
	updated:function(){
		$.extend(true, this.beforeEdit, this.linkedField);
	},
	methods:{
		undoEdit: function(){
			$.extend(true, this.linkedField, this.$parent.srcObjectModel.object);
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

const About = {
	template : '<div><applayout :nothelper="true"><div slot="banner">BANNER</div><div slot="header">HEADER</div><div slot="maincontent">About CONTENT</div></applayout><div>'
};
const Faq = {
	template : '<div><applayout :nothelper="false"><div slot="banner">FAQ BANNER</div><div slot="header">FAQ HEADER</div><div slot="maincontent">FAQ CONTENT</div></applayout><div>'
};
const Contactus = {
	template : '<div><applayout :nothelper="false"><div slot="banner">Contactu Banner</div><div slot="header">Contact HEADER</div><div slot="maincontent">Contact Us CONTENT</div></applayout><div>'
};
const routes = [ 
    { path : '/', component : Home	}, 
    { path : '/about',	component : About }, 
    { path : '/faq',	component : Faq	}, 
    { path : '/contactus',	component : Contactus}, 
    { path : '/object/:uri', name : 'object', component : objDetail	} 
    ];
const router = new VueRouter({
	routes : routes,
	hashbang : false,
	history : true
});

var vuem = new Vue({
	router : router,
	data : {
		currentOLView:'objeditor',
		koModel:objModel,
		showOverlay:{show:false},
		showSecOverlay:{show:false},
	},
	components:{
		info:{template:"<div>Information</div>"},
		login : login,
		objeditor : objeditor,

	},
	created: function(){
		var self=this;
		eventBus.$on('deleteobj',function(uri){
			
		});
		eventBus.$on('editObj', function(obj){
			self.currentOLView='objeditor';
			self.showOverlay.show=true;
			document.body.classList.toggle('noscroll', true);
		});
		eventBus.$on('objSaved', function(obj){
			self.updateObject(obj);
			self.showOverlay.show=false;
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
	},
	mounted:function(){
		overlayHeightResize();
	},
	methods:{
		login_click:function(){
			this.currentOLView='login';
			this.showOverlay.show=true;
		},
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