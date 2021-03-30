<template>
	<olpane layerid=0 :stage='stage'>
		<div slot="oltitle"><h3>Add Knowledge Objects to the Library </h3></div>
		<div slot="ol-form">
			<v-layout row wrap>
				<v-flex xs12>
					<v-btn-toggle v-model="toggle_one" mandatory>
           <v-btn flat>
             <span>ZIP files</span>
           </v-btn>
           <v-btn flat>
             <span>Manifest</span>
           </v-btn>
         </v-btn-toggle>
				 <div style="display:inline-block; " v-if='toggle_one==1'>
				 <input ref="fileInputImport" type="file" name="fileInputImport" @click="value=null" @change="selectFile">
				 <v-btn color="primary" name='manifestchooser' @click.stop="openFileExplorer()">Choose file</v-btn>
			 </div>
				</v-flex>

			</v-layout>
			<v-layout row wrap fill-height align-space-around mt-3>
				<v-flex xs12 sm12 md12 v-if='toggle_one==0'>
						<FilePond ref='fpond' name='ko' allowMultiple instantUpload=false allowRevert=false accepted-file-types="application/zip"/>
				</v-flex>
				<v-flex xs12 sm12 md12 v-else>
						<v-textarea
							box
							disabled
							name="manifestcontent"
							class="manifestcontent"
							label="Manifest"
							:value="prettyManifest"
							auto-grow
						></v-textarea>
				</v-flex>
			</v-layout>
		</div>
		<div slot='buttons'>
				<v-btn
				 color="#0075bc"
				 right
				 bottom
				 relative
				 outline
				 @click='uploadFiles'
				 v-if='toggle_one==0'
				 >
				 <span>Deposit Object</span>
			 </v-btn>
			 <v-btn
				color="#0075bc"
				right
				bottom
				relative
				outline
				@click='loadFiles'
				:disabled='prettyManifest==""'
				v-else
				>
				<span>Load Objects</span>
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
				filecount:0,
				toggle_one:0,
				importedData:""
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
		manifestJson () {
			var obj = {}
			try {
				obj = JSON.parse(this.importedData)
			} catch (e) {

			}
			return obj
		},
		prettyManifest () {
			if(Object.keys(this.manifestJson).length==0) {
				return ''
			} else {
				return JSON.stringify(this.manifestJson, null, 4)
			}
		}
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
                    self.stage='error'
                    console.log(thrown)
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
		loadFiles: function() {
			var self = this
			this.filecount = this.manifestJson.manifest.length
			self.stage='processing'
			self.$http({
					method: 'post',
					url: self.url+'manifest',
					data: self.manifestJson,
			}).then(()=>{
								console.log('Done with loading '+self.manifestJson.manifest.length+ ' KOs.')
								self.stage='success'
								setTimeout(function(){
									self.$eventBus.$emit('objAdded',{})
								} , 1200)
							}).catch((err) => {
								self.stage='error'
								console.log(err)
							}
						)
		},
		openFileExplorer() {
			this.$refs.fileInputImport.click();
		},
		selectFile(e) {
			const self = this;
			const file = e.target.files[0];
			let reader = new FileReader();
			reader.onload = e => {
				self.importedData = reader.result;
				self.dialogImport = true;
			};
			reader.readAsText(file);
			  this.$refs.fileInputImport.value = "";
		}
	}
};
	</script>
	<style>
	[type="file"] {
  	display: none;
	}
	.v-textarea textarea[name="manifestcontent"],
	.manifestcontent textarea
	 {
		font-size: 0.7em;
		line-height:1.5em;
	}
	button[name='manifestchooser'] {
		margin-top: 0px;
		margin-bottom:0px;
	}
	</style>
