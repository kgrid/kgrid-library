'use strict';
var bus = new Vue();
var raw = 9;
var objModel = { viewObject : { metadata:{title:"View Object"}} };
var editObjModel = { editObject : { metadata:{title:"Edit object",keywords:"",contributors:"",published:"",citations:[],license:{licenseName:"",licenseLink:""}}, payload:{functionName:"",engineType:"",content:""},uri:"ark"} };
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

Vue.component("loginoverlay",{
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
//							objModel.object=this.object;
//							editObjModel.object=this.object;
							this.$emit("clicked");
						}
					}
				});
var vm_fields = Vue.component(
				"field-tile",
				{
					template : "#field-tile-template",
					props : [ 'field' ,'fieldmodel', 'object',"isDisabled"],
					created:function(){
//						console.log("Field:"+this.field.name+"Value:"+this.value);
					},
					data:function(){
						return {raw:raw}
					},
					computed : {
						fieldmodel1: function(){
							var propertyModel = "";
							switch (this.field.section) {
							case "metadata":
								propertyModel = "editObjModel.editObject"+this.field.section+"."+this.field.name;
								break;
							case "payload":
								propertyModel = "editObjModel.editObject"+this.field.section+"."+this.field.name;
								break;
							default:
								propertyModel = "editObjModel.editObject"+this.field.section+"."+this.field.name;
								break;
							}
							return propertyModel;
							
						},
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
//		console.log("Created TabPane for the object of "+this.object.uri);
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
		console.log("Object View created -> Edit Model Title: "+editObjModel.editObject.metadata.title+"  View Model Title: "+objModel.viewObject.metadata.title);

		$('ul#tabs li:first').addClass('active'); 
	    $('ul#tab li:first').addClass('active');
		otScroll();
	},
	mounted:function() {
		var self = this;
		console.log("Object View mounted -> Edit Model Title: "+editObjModel.editObject.metadata.title+"  View Model Title: "+objModel.viewObject.metadata.title);
		retrieveObject(this.$route.params.uri, "complete", function(
				response) {
			console.log(response);
			self.objModel.viewObject = response;
			editObjModel.editObject=response;
		}); 
		$('ul#tabs li:first').addClass('active'); 
	    $('ul#tab li:first').addClass('active');
	    $("html, body").animate({
	        scrollTop: 0
	    }, 200);
	},
	computed : {
		formattedUpdateDate : function() {
			return new Date(this.objModel.viewObject.metadata.lastModified)
					.format("mediumDate")
		},
		formattedCreateDate : function() {
			return new Date(this.objModel.viewObject.metadata.createdOn)
					.format("mediumDate")
		},
		downloadLink : function() {
			return '/ObjectTeller/knowledgeObject/'
					+ this.objModel.viewObject.uri + '/complete.json'
		}
	},
	updated : function() {
		$('ul#tabs li:first').addClass('active'); 
	    $('ul#tab li:first').addClass('active'); 
		$('.autosize').each(autoresize);
		console.log("Object View updated -> Edit Model Title: "+editObjModel.editObject.metadata.title+"  View Model Title: "+objModel.viewObject.metadata.title);
	},
	methods:{
		editObj:function(){
			showObjectEditor.show=true;
			showOverlay.show=true;
			console.log("Edit Clicked => Edit Model Title: "+editObjModel.editObject.metadata.title+"  View Model Title: "+objModel.viewObject.metadata.title);
	//		editObjModel.object=objModel.object;
	//		this.$emit('editObj');
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
				objModel.viewObject=self.model.koList[0];
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
			console.log("Object selected -> Edit Model Title: "+editObjModel.editObject.metadata.title+"  View Model Title: "+objModel.viewObject.metadata.title);

			console.log("selected Object:"+objModel.viewObject.uri);
			
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
			showObjectEditor:showObjectEditor,
			isDisabled:false,
			outputfile:false
			}
	},
	created:function(){
		console.log("ObjEditor created -> Edit Model Title: "+editObjModel.editObject.metadata.title+"  View Model Title: "+objModel.viewObject.metadata.title);
	},
	mounted:function(){
		this.outputfile=(this.editObjModel.editObject.outputMessage!=null);
		console.log("ObjEditor mounted -> Edit Model Title: "+editObjModel.editObject.metadata.title+"  View Model Title: "+objModel.viewObject.metadata.title);
	},
	updated : function() {
	//	this.outputfile=(this.editObjModel.object.outputMessage!=null);
		console.log("ObjEditor updated -> Edit Model Title: "+editObjModel.editObject.metadata.title+"  View Model Title: "+objModel.viewObject.metadata.title);
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
		        reader.onload = (e) => {
		        	$("#outputTextArea").val(e.target.result);
		        };
		        reader.readAsText(file);
		      },
		removeFile: function (e) {
				console.log("Remove button clicked.");
		        this.outputfile = false;
		        $("#outputTextArea").val("");
		      },
		saveObj:function(){
			var text = JSON.stringify(this.editObjModel.editObject);
			console.log(text);
			$("div.processing").fadeIn(300);
			$.ajax({
				beforeSend : function(xhrObj) {
					xhrObj.setRequestHeader("Content-Type", "application/json");
					xhrObj.setRequestHeader("Accept", "application/json");
				},
				type : "PUT",
				url : "../knowledgeObject/"+this.editObjModel.editObject.uri,
				data : text,
				dataType : "json",
				success : function(response) {
					console.log(response);
					if ((response != 'empty') && (response != null)) {
						var test = JSON.stringify(response);
						var obj = JSON.parse(test);
					}
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
			//editObjModel.editObject = objModel.object;
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
	history : false
});

const vm = new Vue({
	router : router,
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
//			objModel.object={};
		},
		editObj_click:function(){
			showObjectEditor.show=true;
			showOverlay.show=true;
			objURI=	objModel.viewObject.uri;
		}
	}}).$mount('#app');