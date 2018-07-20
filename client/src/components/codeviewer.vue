<template>
	<olpane layerid=0 :stage='stage'>
		<div slot="ol-form">
			<prism language='javascript' >{{rawfile}}</prism>
		</div>
			<div slot='buttons'></div>
	</olpane>
</template>
	<script>
	import olpane from '../components/olpane';
	import Prism from 'vue-prism-component'
	export default {
		name:"codeviewer",
		data:function(){
			return {
				dragAndDropCapable: false,
				files: [],
				uploadPercentage: 0,
				stage:'',
				rawfile:'',
				viewraw:false
			}
		},
	created:function(){
		var self = this
		this.$http.get(this.url).then(response=> {
			console.log("reading source code ...")
			console.log(response)
      self.rawfile = response.data
    }).catch(e=>{
      console.log(e)
    })
	},
	components: {
		olpane,
		Prism
	},
	computed:{
		url:function(){
			return this.$store.getters.getcodeurl
		}
	}
};
	</script>
	<style >
	#ui_container {
		padding: 10px;
		border:none;
	}
	</style>
