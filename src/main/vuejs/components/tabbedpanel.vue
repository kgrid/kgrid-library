	<template id= "tab-panel-template">
		<div class="tab-content view-obj">
			<ul>
				<li class="ot-viewlist" v-for="field in filteredFields">
					<fieldtile :field="field" :object="object" :isDisabled=isDisabled></fieldtile>
				</li>
			</ul>
		</div>
	</template>
	<script>
	import fieldtile from './fieldtile.vue';
	import { loadFieldsConfig, autoresize } from '../ot.js';
	export default {
		name:'tabpane', 
		props:['section','object','isDisabled'],
		data: function(){
			return {
				fields_json : {
					fields : []
				},
				raw:9
			}
		},
		components: {
			fieldtile:fieldtile
		},
		beforeCreate : function() {
			var self = this;
			loadFieldsConfig(function(data) {
				self.fields_json.fields = data.fields;
			});
		},
		updated : function() {
			//this.autoresize();
		},
		created : function() {
			//console.log(this.section);
		},
		mounted : function() {

		},
		computed : {
			filteredFields :function(){
				var section = this.section;
				var fields = this.fields_json.fields;
				return fields.filter(function(field){
					return (field.section==section)
				})
			}
		},
		methods: {
			autoresize:function(){
				console.log(this);
				var autosize = this.$el.querySelector(".autosize");
				console.log(autosize);
				console.log(autosize.scrollHeight);
				var sh = autosize.scrollHeight+15;
				autosize.style.height="0px";     //Reset height, so that it not only grows but also shrinks
				autosize.style.height = sh + 'px';    //Set new height
				console.log(this.section+"New Height = "+sh);
			}
		}
	};
	</script>
<style>
ul#tab {
    list-style-type: none;
    margin: 22px 0px 0px 0px;
    padding: 0;
    width: 100%;
    float: left;
    min-height: 800px;
    box-sizing: border-box;
    background-color: #ffffff;
}
input[type=text]:disabled {
	border: 1px solid #fff;
	background-color: #fff;
	color: #666666;
	font-weight: 400;
	font-size: 14px;
}
.addtext textarea {
	width: 900px;
	min-height:60px;
	resize:none;
	padding:8px 16px;
	border: 1px solid #e6e6e6;
	border-radius: 10px;
	color:#666666;
  	margin:2px 0;
  	font-size: 14px;
  	overflow-y:auto;
  	line-height:1.4em;
  	   	text-overflow: clip;
   	white-space: pre-wrap;
   	word-break: break-word;
   	background-color: #fff;
    font-weight: 400;
}

.addtext textarea:disabled{
	border: 1px solid #fff;
}

.addtext a {
    width: 100%;
    border: 1px solid #fff;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    height: 38px;
    padding: 0px 16px;
    border-radius: 10px;
    margin: 2px 0;
    font-size: 14px;
    color: #666666;
    font-weight: 400;
}
ul.list li {
    display: block;
    padding: 0px 16px;
}
a {
	text-decoration: none;
    background-color: transparent;
}

</style>
	