<template>
	<olpane layerid=0 :stage='stage'>
		<div slot="oltitle">
			<div class='row' >
				<span class='body-2'> {{uri}}</span><br>
				<span class='title'>{{rscfilename}}</span>
			</div>
		</div>
		<div slot="ol-form" style="height:98%;">
			<Prism :language='filetype' style="max-height:90%;">{{rawfile}}</Prism>
		</div>
	</olpane>
</template>
	<script>
	import olpane from '../components/olpane';
	import Prism from 'vue-prism-component'
	export default {
		name:"ioviewer",
		data:function(){
			return {
				stage:'',
				rawfile:''
			}
		},
	created:function(){
		var self = this
		this.$http.get(this.url).then(response=> {
			console.log("reading service descriptor...")
			console.log(response)
      self.rawfile = response.data
    }).catch(e=>{
      console.log(e)
    })
	},
	mounted:function(){
	},
	components: {
		olpane,
		Prism
	},
	computed:{
		url:function(){
			return this.$store.getters.getkoiourl
		},
		urlarray: function(){
			return this.url.split('/')
		},
		rscfilename:function(){
			return this.urlarray[this.urlarray.length-1]
		},
		uri:function(){
			return this.$store.getters.getcurrenturl
		},
		filetype: function(){
			var file = this.rscfilename.split('.')
			var ext = file[file.length-1]
			switch (ext) {
				case 'yaml':
					return 'yaml'
				case 'js':
					return 'javascript'
				default:
					return 'yaml'
			}
		}
	},
	methods:{
	}
};

	</script>
	<style >
	#ui_container {
		padding: 10px;
		border:none;
		margin-top:-60px;
	}
	.ol_pane pre {
		overflow-y: auto;
	}
	code {
		box-shadow: none;
	}
</style>
