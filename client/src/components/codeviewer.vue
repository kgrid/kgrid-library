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
		arkid:function(){
			if(this.files.length>0){
				var filename = this.files[0].name
				var index=filename.lastIndexOf('/')
				var l= filename.length
				var s = filename.slice(index+1,l)
				 return 'ark:/'+s.replace('.zip','').replace('-','/')
			 }else {
				 return ''
			 }
		},
		url:function(){
			return this.$store.getters.getcodeurl
		}

	},
	methods:{
		determineDragAndDropCapable(){
			var div = document.createElement('div');
			return ( ( 'draggable' in div )
							|| ( 'ondragstart' in div && 'ondrop' in div ) )
							&& 'FormData' in window
							&& 'FileReader' in window;
		},
		/*			Submits the files to the server		*/
		submitFiles(){
			var formData = new FormData();
			var file = this.files[0];
			var self = this;
			formData.append('ko', file);
			this.stage='processing';
			setTimeout(function(){
			self.$http.put( self.url+self.arkid,
				formData,
				{
					onUploadProgress: function( progressEvent ) {
						this.uploadPercentage = parseInt( Math.round( ( progressEvent.loaded * 100 ) / progressEvent.total ) );
					}.bind(this)
				}
			).then(function(resp){
				self.stage='success'
				setTimeout(function(){
					console.log(resp);

					self.$eventBus.$emit('objSaved',{})
				} , 1500)
			})
			.catch(function(err){
				self.stage='error'
				console.log(err);
			});}, 1000)
		},
		removeFile( key ){
			this.files.splice( key, 1 );
		},
	}
};
const DisableTryItOutPlugin = function() {
  return {
    statePlugins: {
      spec: {
        wrapSelectors: {
          allowTryItOutFor: () => () => false
        }
      }
    }
  }
}
	</script>

	<style >
	#ui_container, #rawyaml {
		padding: 10px;
		border:none;

	}
	#rawyaml {
		overflow: auto;
	}
.swagger-ui .info .title small pre  {
		background-color: transparent;
		padding:2px 4px;
		border:none;
	}
	.swagger-ui .info {
		margin: 30px 0 20px 0;
	}
	</style>
