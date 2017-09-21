	<template id= "tab-panel-template">
		<div class="tab-content view-obj">
			<ul>
				<li class="kgl-viewlist" v-for="field in filteredFields">
					<fieldtile :field="field" :object="object" :isDisabled=isDisabled></fieldtile>
				</li>
			</ul>
		</div>
	</template>
	<script>
	import fieldtile from './fieldtile.vue';
	export default {
		name:'tabpane',
		props:['section','object','isDisabled'],
		data: function(){
			return {
			}
		},
		components: {
			fieldtile:fieldtile
		},
		computed : {
			fields_json : function(){
				return this.$store.getters.getfields
				},
			filteredFields :function(){
				var section = this.section;
				var fields = this.fields_json.fields;
				return fields.filter(function(field){
					return (field.section==section)
				})
			}
		},

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
	border: none;
	background-color: #fff;
	color: #666666;
	font-weight: 400;
	padding: 0px;
	height:40px;
}
.addtext .textview {
	resize:none;
	padding:10px 0px 18px 0px;
	border: none;
	border-radius: 0px;
	color:#666666;
  margin: 0;

  	overflow-y:auto;
  	line-height:1.6em;
  	   	text-overflow: clip;
   	white-space: pre-wrap;
   	word-break: break-word;
   	background-color: #fff;
    font-weight: 400;
}


.addtext a {
	display: block;
	border: none;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    height: 38px;
    padding: 10px 0px 0px 1px;
	border-radius: 0px;
    margin: 0px;
    font-size: 14px;
    color: #666666;
    font-weight: 400;
}

.addtext a span {
	border-bottom: 1px solid #0075bc;
	text-decoration: none;
}

.addtext a:visited span {
	text-decoration: none;
}

ul.list li {
    display: block;
    padding: 0px;
}

.kgl-viewlist {
	list-style:none;
	padding: 10px 16px 16px 16px;
}

.kgl-viewlist input, .kgl-viewlist div, .kgl-viewlist h4{
	background-color: #fff;
	padding: 0px;
	color: #555555;
	font-weight: 400;
	font-size: 14px;
	border-radius: 5px;
	margin: 0px;
}

</style>
