'use strict';
var objModel = { object : {} };
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

var overlaylayout= Vue.component("overlaylayout",{
	template:"#overlaylayout",
	props:['ol_id', 'width'],
	data:function(){
		return {show:true}
	},
	methods:{
		overlaySlideOut: function(){
			this.hide();
			
		}
	}

})

var loginOverlay = Vue.component("loginoverlay",{
	template:'#login_overlay',
	methods:{
		openOverlay:function(){
			overlaySlide();
		}
	}
})
var vmcomp = Vue
		.component(
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
						selectObject: function(){
							console.log(this.object.uri+"selected");
							this.$emit("selectObj");
						}
					}
				});
var vm_fields = Vue
		.component(
				"field-tile",
				{
					template : "#field-tile-template",
					props : [ 'field', 'value' ],
					computed : {
						value : function() {
							var propertyValue = "";
							switch (this.field.section) {
							case "metadata":
								propertyValue = objModel.object[this.field.section][this.field.name];
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
	props:['section'],
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
	},
	computed : {
		filteredFields :function(){
			var section = this.section;
			var fields = this.fields_json.fields;
			return fields.filter(function(field){
				console.log("Section: "+section);
				return (field.section==section)
			})
		}
}
});
const
objDetail = Vue.component('ko-detail', {
	template : '#obj-detail',
	data : function() {
		return {
			objModel : objModel,
			sections:sections
		}
	},
	created : function() {
		var self = this;
		console.log('Object URI:' + this.$route.params.uri);
		retrieveObject(this.$route.params.uri, "complete", function(
				response) {
			console.log(response);
			objModel.object = response;
		}); 
		$('ul#tabs li:first').addClass('active'); 
	    $('ul#tab li:first').addClass('active'); 
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
	}
});
const
Home = Vue.component("ko-main", {
	template : '#ko-list',
	data : function() {
		return {
			sortKey : "metadata.lastModified",
			order : "asc",
			filterString : "",
			searchQuery : "",
			model : {
				koList : []
			}
		}
	},
	created : function() {
		var self = this;
		retrieveObjectList(function(response) {
			self.model.koList = response;
		});
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
/* 					console.log(this.filteredList.length);
*/					return _.orderBy(this.filteredList, this.sortKey,
					this.order)
		},
		filteredList :function(){
			var list = this.model.koList;
			var filterString = this.filterString;
			return list.filter(function(field){
/* 						console.log("Filter String: "+filterString);
*/						return (field.metadata.title.includes(filterString) ||
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
		objectSelected: function(index){
			console.log("selected index:"+index)
			objModel.object=this.model.koList[index];
		}
	},
	components:{'appLayout':applayout}
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
	router : router/* ,
	component : Home */
}).$mount('#app');