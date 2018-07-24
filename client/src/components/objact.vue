<template>
	<olpane layerid=0 :stage='stage'>
		<div slot="ol-form">
			<div id="ui_container"></div>

		</div>
		<div slot='buttons'>
				<button class="kg-btn-primary" >
					<span v-if='objactivated' @click='gotodemo'>Go to Demo Swagger UI</span>
					<span v-else @click='sendzip'> Send KO to Activator </span>
				</button>
		</div>
	</olpane>
</template>
	<script>
	import olpane from '../components/olpane';

	export default {
		name:"objact",
		data:function(){
			return {
				stage:'',
				blob:'',
				zipfile:'',
				objactivated:false
			}
		},
	created:function(){
		var self = this
		this.$http.get(
			this.url+'?format=zip',
			{ responseType: 'blob' }
	).then(response=> {
			console.log("reading zip...")
			console.log(response)
      self.blob = new Blob([response.data], { type: 'application/zip' } )
			self.zipfile=new File([response.data], self.downloadFile, { type: 'application/zip' } )
			console.log(self.zipfile.size)
    }).catch(e=>{
      console.log(e)
    })
	},
	mounted:function(){
		this.objactivated=false
	},
	components: {
		olpane
	},
	computed:{
		url:function(){
			return this.$store.getters.getcurrenturl
		},
		uriarray:function(){
			console.log("decode:"+decodeURIComponent(this.$route.params.uri))
			return decodeURIComponent(this.$route.params.uri).split("/")
		},
		downloadFile : function() {
			return this.uriarray[0]+"-"+this.uriarray[1] + '.zip'
		},
		shelfurl:function(){
			return 'http://localhost:8080/'
		}
	},
	methods: {
		sendzip:		function(){
					var formData = new FormData();
					var self = this;
					formData.append('ko', this.zipfile);
		//			this.stage='processing';
					setTimeout(function(){
					self.$http.put( self.shelfurl+self.uriarray[0]+"/"+self.uriarray[1]+"/",
						formData,
						{
        headers: {
            // 'Content-Type': 'multipart/form-data'
        }
    }
					).then(function(resp){

						setTimeout(function(){
							console.log(resp);
										self.objactivated =true
							// self.$eventBus.$emit('objAdded',{})

						} , 1500)
					})
					.catch(function(err){
						self.stage='error'
						console.log(err);
					});}, 1000)
				},
				gotodemo: function(){
					window.open('https://kgrid-demos.github.io/swaggerui?url=http://kgrid-activator.herokuapp.com/99999/newko2/v0.0.1/service');
				}
	}
};
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
