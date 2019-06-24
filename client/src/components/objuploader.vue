<template>
	<olpane layerid=0 :stage='stage'>
		<div slot="oltitle"><h3>Add a Knowledge Object to the Library </h3></div>
		<div slot="ol-form" >
				<div style='margin-top:220px;'>
					<FilePond ref='fpond' name='ko' allowMultiple instantUpload=false allowRevert=false />
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
		<div slot="ol-success"><span>{{filecount}} Knowledge <span v-if='filecount>1'>Objects have</span> <span v-else>Object has</span> been succesfully added to the shelf. </span></div>
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
				requestField:'ko',
				filecount:0
			}
		},
	components: {
		olpane,
		FilePond: vueFilePond()
	},
	computed:{
		url:function(){
			return this.$store.getters.getbaseurl
		},

	},
	methods:{
		uploadFiles () {
			var self = this
			var files = this.$refs.fpond.getFiles()
			setOptions( {
				server: {
					url: self.url,
					process: (fieldName, file, metadata, load, error, progress, abort) => {
							// set data
							const formData = new FormData();
							formData.append(self.requestField, file, file.name);
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
					        // passing the file id to FilePond
					        load(file.name)
					    }).catch((thrown) => {
					        if (self.$http.isCancel(thrown)) {
					            console.log('Request canceled', thrown.message);
					        } else {
					            // handle error
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
			var findex = files.map(function(e, index){return index})
			console.log(findex)
			this.filecount = files.length
			this.uploadFile(findex)
			.then(
				()=>{
					console.log('Done with depositing '+files.length+ ' KOs.')
					self.stage='success'
					setTimeout(function(){
						self.$eventBus.$emit('objAdded',{})
					} , 1200)
				}, (err) => {
					self.stage='error'
					console.log(err)
				}
			)
		},
		uploadFile: function(array) {
			var self = this
			var index = 0
 			return new Promise(function(resolve,reject){
				function next() {
					console.log(index)
					if (index < array.length) {
						self.$refs.fpond.processFile(array[index++])
						.then(function(file){
							console.log(index+" done")
							next()
						}).catch(function(error){
							reject()
						});
					} else {
						resolve();
					}
				}
				next();
			})
		},
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
