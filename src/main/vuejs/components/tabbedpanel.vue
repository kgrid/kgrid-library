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
	import { loadFieldsConfig } from '../ot.js';
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

		},
		created : function() {
			console.log(self.section);
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
	};
	</script>