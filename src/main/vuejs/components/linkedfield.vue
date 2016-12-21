	<template id="linkedfieldtile-template"> 
	   <a target="_blank" v-bind:href="link"><span>{{value}}</span></a> 
	</template>
	<script>
	export default {
				name: "linkedfieldtile",
				props : [ "link", "value" ]
				};
	</script>