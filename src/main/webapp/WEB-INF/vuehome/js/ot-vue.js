'use strict';
var bus = new Vue();
var objModel = { object : {} };
var editObjModel = { object : { metadata:{title:""}, uri:"ark"} };
var sections = [{name:"metadata"},
                {name:"payload"},
                {name:"inputMessage"},
                {name:"outputMessage"},
                {name:"logData"}];
var applayout = Vue.component("applayout", {
		template:"#layout",
		props:['nothelper'],
		methods:{
			backToTop: backToTop
		}
})
var showLogin = {show:false};
var showOverlay ={show:false};
var showObjectEditor ={show:false};
var overlaylayout= Vue.component("overlaylayout",{
	template:"#overlaylayout",
	props:['ol_id', 'width'],
	data:function(){
		return {showOverlay:showOverlay}
	},
	methods:{
		closeOverlay:function(){
			showOverlay.show=false;
			this.$emit('slideout');
		}
	}
})

var loginOverlay = Vue.component("loginoverlay",{
	template:'#login_overlay',
	data:function(){
		return {showLogin:showLogin}
	},
	methods:{
		olSlideout:function(){
			showLogin.show=false;
		},
		olSlidein:function(){
			showLogin.show=true;
		}
	}
})


var vmcomp = Vue.component(
				"ko-tile",
				{
					template : "#ko-tile-template",
					props : [ 'object', 'listsize' ],
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
						deleteObject : function() {
							var uri = this.object.uri;
							var txt;
							if (uri != "") {
								var r = confirm("Do you really want to delete the object ? ");
								if (r == true) {
									$
											.ajax({
												type : 'DELETE',
												url : "/ObjectTeller/knowledgeObject/"
														+ uri,
												success : function(
														response) {
													console.log("Deletion successful!");
												}
											});
								}
							}
						},
						clicked: function(){
							console.log(this.object.uri+"selected");
							objModel.object=this.object;
							editObjModel.object=this.object;
							this.$emit("clicked");
						}
					}
				});
var vm_fields = Vue.component(
				"field-tile",
				{
					template : "#field-tile-template",
					props : [ 'field' , 'object'],
					created:function(){
						console.log("Field:"+this.field.name+"Value:"+this.value);
					},
					computed : {
						value : function() {
							var propertyValue = "";
							switch (this.field.section) {
							case "metadata":
								propertyValue = editObjModel.object[this.field.section][this.field.name];
								break;
							case "payload":
								propertyValue = objModel.object[this.field.section][this.field.name];
								break;
							default:
								propertyValue = objModel.object[this.field.name];
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
					}
				});
var vm_linkedfields = Vue.component("linkedfield-tile", {
	template : "#linkedfield-tile-template",
	props : [ "link", "value" ]
});
const tabPane = Vue.component('tab-pane', {
	template: "#tab-panel-template",
	props:['section','object'],
	data: function(){
		return {
			fields_json : {
				fields : []
			}
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
		console.log("Created TabPane for the object of "+this.object.uri);
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
			sections : sections
		}
	},
	created : function() {
		var self = this;
		$('ul#tabs li:first').addClass('active'); 
	    $('ul#tab li:first').addClass('active');
	    //this.object= this.objModel.object;
		//editObjModel.object=objModel.object;
		otScroll();
	},
	mounted:function() {
		var self = this;
		console.log('Object URI:' + this.$route.params.uri);
		retrieveObject(this.$route.params.uri, "complete", function(
				response) {
			console.log(response);
			objModel.object = response;
			editObjModel.object=objModel.object;
		}); 
		$('ul#tabs li:first').addClass('active'); 
	    $('ul#tab li:first').addClass('active');
	    $("html, body").animate({
	        scrollTop: 0
	    }, 200);
	},
	computed : {
		formattedUpdateDate : function() {
			return new Date(objModel.object.metadata.lastModified)
					.format("mediumDate")
		},
		formattedCreateDate : function() {
			return new Date(objModel.object.metadata.createdOn)
					.format("mediumDate")
		},
		downloadLink : function() {
			return '/ObjectTeller/knowledgeObject/'
					+ objModel.object.uri + '/complete.json'
		}
	},
	updated : function() {
		$('ul#tabs li:first').addClass('active'); 
	    $('ul#tab li:first').addClass('active'); 
		$('.autosize').each(autoresize);
	},
	methods:{
		editObj:function(){
			showObjectEditor.show=true;
			showOverlay.show=true;
			editObjModel.object=objModel.object;
			this.$emit('editObj');
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
			editObjModel:editObjModel,
			objModel:objModel
		}
	},
	created : function() {
		var self = this;
		retrieveObjectList(function(response) {
			self.model.koList = response;
			if(self.model.koList.length>0){
				objModel.object=self.model.koList[0];
				editObjModel.object=objModel.object;
			}
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
			return this.model.koList.length;
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
		objectSelected: function(){
			console.log("selected Object:"+objModel.object.uri);
			
		}
	},
	components:{'appLayout':applayout}
});
var objeditor=Vue.component("objeditor",{
	template:'#objEditor_overlay',
	data:function(){
		return {
			editObjModel:editObjModel,
			sections:sections,
			showObjectEditor:showObjectEditor
			}
	},
	created:function(){
		this.editObjModel.object = objModel.object;
	},
	mounted:function(){
//		editObjModel.object = objModel.object;
		console.log("objEditor Mounted... with Obj. "+this.editObjModel.object.uri);
	},
	updated : function() {
		$('ul#etabs li:first').addClass('active'); 
	    $('ul#etab li:first').addClass('active'); 
		$('.autosize').each(autoresize);
	},
	methods: {
		saveObj:function(response){
			
		},
		undoEdit: function(){
			editObjModel.object = objModel.object;
		},
		createObj: function(){
			
		},
		olSlideout:function(){
			showObjectEditor.show=false;
		},
		olSlidein:function(){
			showObjectEditor.show=true;
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

const app = new Vue({
	router : router,
	data:{
		objModel:objModel,
		editObjModel:editObjModel
	},
	components:{
		objeditor:objeditor
	},
	methods:{
		login_click:function(){
			console.log(showLogin.show);
			console.log(showOverlay.show);
			showLogin.show=true;
			showOverlay.show=true;
		},
		createObj_click:function(){
			showObjectEditor.show=true;
			showOverlay.show=true;
			objURI="";
			objModel.object={};
			editObjModel.object=objModel.object;
		},
		editObj_click:function(){
			showObjectEditor.show=true;
			showOverlay.show=true;
			objURI=	objModel.object.uri;
			editObjModel.object=objModel.object;
		}
	}/* ,
	component : Home */
}).$mount('#app');