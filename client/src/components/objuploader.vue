<template>
	<olpane layerid=0 :stage='stage'>
		<div slot="oltitle"><h3>Add a Knowledge Object to the Library </h3></div>
		<div slot="ol-form" >
				<div style='margin-top:250px;'>
					<FilePond ref='fpond' name='ko' allowMultiple instantUpload=false />
				</div>
			</div>
			<div slot='buttons'>
					<v-btn
					 color="#0075bc"
					 right
					 bottom
					 relative
					 outline
					 @click='uploadFiles'
					 >
					 <span>Deposit Object</span>
				 </v-btn>
			</div>
		<div slot="ol-processing"><span>Processing...</span></div>
		<div slot="ol-success"><span>The Knowledge Object has been succesfully added to the shelf.  Object ID: {{arkid}}</span></div>
		<div slot="ol-failure"><span>Error in depositing the KO! </span></div>
		<div slot="ol-warning"><span>Warning !!!</span></div>
	</olpane>
</template>
	<script>
	import olpane from '../components/olpane';
	import vueFilePond, { setOptions } from 'vue-filepond';
	import 'filepond/dist/filepond.min.css';
	export default {
		name:"objuploader",
		data:function(){
			return {
				stage:'',
				arkid:''
			}
		},
	components: {
		olpane,
		FilePond: vueFilePond()
	},
	computed:{
		url:function(){
			return this.$store.getters.getbaseurl
		}
	},
	methods:{
		uploadFiles () {
			var self=this
			setOptions( {
				server: {
					url: self.url,
					process: (fieldName, file, metadata, load, error, progress, abort) => {
					    // set data
					    const formData = new FormData();
					    formData.append(fieldName, file, file.name);
					    // related to aborting the request
					    const CancelToken = self.$http.CancelToken;
					    const source = CancelToken.source();
					    // the request itself
					    self.$http({
					        method: 'post',
					        url: self.url,
					        data: formData,
					        cancelToken: source.token,
					        onUploadProgress: (e) => {
					            progress(e.lengthComputable, e.loaded, e.total);
					        }
					    }).then(response => {
									self.stage='success'
									self.arkid = response.data.Added
									setTimeout(function(){
										console.log(response);
										self.$eventBus.$emit('objAdded',{})
									} , 1500)
					    }).catch((thrown) => {
					        if (self.$http.isCancel(thrown)) {
					            console.log('Request canceled', thrown.message);
					        } else {
					            // handle error
											self.stage='error'
											console.log(thrown);
					        }
					    });
					    // Setup abort interface
					    return {
					        abort: () => {
					            source.cancel('Operation canceled by the user.');
					        }
					    };
					}
				}
			} )
			self.stage='processing';
			this.$refs.fpond.processFile()
		}
	}
};
	</script>
	<style scoped>
	form{
		display: block;
		height: 400px;
		width: 400px;
		background: #eee;
		margin: auto;
		text-align: center;
		line-height: 400px;
		border-radius: 4px;
	}
	.alert-box  div span{
		color: #fff;
	}
	</style>
