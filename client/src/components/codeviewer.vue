<template>
	<olpane layerid=0 :stage='stage'>
		<div slot="oltitle" >
			<div class='row' >
				<h2>Knowledge Object
					<span style="padding: 3px;"> {{uri}}</span></h2>
			</div>
		</div>
		<div slot="ol-form" style="height:100%;">
			<h2>{{rscfilename}}</h2>
			<prism language='javascript'  style="max-height:90%;">{{rawfile}}</prism>
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
			return this.$store.getters.getkoresourceurl
		},
		urlarray: function(){
			return this.url.split('/')
		},
		rscfilename:function(){
			return this.urlarray[this.urlarray.length-1]
		},
		uri:function(){
			return this.$store.getters.getcurrenturi
		},
	}
};
	</script>
	<style >
	#ui_container {
		padding: 10px;
		border:none;
	}
	</style>
