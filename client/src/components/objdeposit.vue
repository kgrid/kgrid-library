<template>
	<olpane layerid=0 :stage='stage'>
		<div slot="oltitle"><h3>Deposit a Knowledge Object to the Library </h3></div>
		<div slot="ol-form" >
			<div class="upload">
	      <ul v-if="files.length">
	        <li v-for="(file, index) in files" :key="file.id">
	          <span>{{file.name}}</span> -
	          <span>{{file.size | formatSize}}</span> -
	          <span v-if="file.error">{{file.error}}</span>
	          <span v-else-if="file.success">success</span>
	          <span v-else-if="file.active">active</span>
	          <span v-else-if="file.active">active</span>
	          <span v-else></span>
	        </li>
	      </ul>
	      <ul v-else>
	        <td colspan="7">
	          <div class="text-center p-5">
	            <h4>Drop files anywhere to upload<br/>or</h4>
	            <label for="file" class="btn btn-lg btn-primary">Select Files</label>
	          </div>
	        </td>
	      </ul>

	      <div v-show="$refs.upload && $refs.upload.dropActive" class="drop-active">
	    		<h3>Drop files to upload</h3>
	      </div>

	    </div>
			</div>
			<div slot='buttons'>

				      <div class="example-btn">
				        <file-upload
				          class="btn btn-primary"
				          :multiple="false"
				          :drop="true"
				          :drop-directory="true"
				          v-model="files"
				          ref="upload">
				          <i class="fa fa-plus"></i>
				          Select files
				        </file-upload>
				        <button type="button" class="btn btn-success" v-if="!$refs.upload || !$refs.upload.active" @click="submitFiles" @click.prevent="$refs.upload.active = true">
				          <i class="fa fa-arrow-up" aria-hidden="true"></i>
				          Start Upload
				        </button>
				        <button type="button" class="btn btn-danger"  v-else @click.prevent="$refs.upload.active = false">
				          <i class="fa fa-stop" aria-hidden="true"></i>
				          Stop Upload
				        </button>
				      </div>
									</div>
		<div slot="ol-processing"><span>Processing...</span></div>
		<div slot="ol-success"><span>The knowledge Object succesfully added to the shelf.  Object ID:{{arkid}}</span></div>
		<div slot="ol-failure"><span>Error in depositing the KO! </span></div>
		<div slot="ol-warning"><span>Warning !!!</span></div>
	</olpane>
</template>
	<script>
	import olpane from '../components/olpane';
	import FileUpload from 'vue-upload-component'
	export default {
		name:"objdeposit",
		data:function(){
			return {
				dragAndDropCapable: false,
				files: [],
				uploadPercentage: 0,
				stage:''
			}
		},
	created:function(){},
	components: {
		olpane,
		FileUpload
		},
	computed:{
		arkid:function(){
			if(this.files.length>0){
				var filename = this.files[0].name
				var index=filename.lastIndexOf('/')
				var l= filename.length
				var s = filename.slice(index+1,l)
				 return s.replace('.zip','').replace('-','/')
			 }else {
				 return ''
			 }
		},
		url:function(){
			return this.$store.getters.getshelfurl
		},
		puturl: function(){
			return this.url+this.arkid
		}
	},
	mounted:function(){

	},
	methods:{

		/*			Submits the files to the server		*/
		submitFiles(){
			var formData = new FormData();
			var file = this.files[0].file;
			var self = this;
			formData.append('ko', file, this.files[0].name);
			this.stage='processing';
			setTimeout(function(){
			self.$http.put( self.url+self.arkid,
				formData,
				{

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
	</script>
	<style scoped>
	form{
		display: block;
		height: 400px;
		width: 400px;
		background: #ccc;
		margin: auto;
		margin-top: 40px;
		text-align: center;
		line-height: 400px;
		border-radius: 4px;
	}
	div.file-listing{
		width: 400px;
		margin: auto;
		padding: 10px;
		border-bottom: 1px solid #ddd;
	}
	.alert-box  div span{
		color: #fff;
	}
	progress{
		width: 400px;
		margin: auto;
		display: block;
		margin-top: 20px;
		margin-bottom: 20px;
	}
	.panel-body {
		height:630px;
		padding: 15px;
		border:1px solid #0075bc;
	}
	label.btn {
  margin-bottom: 0;
  margin-right: 1rem;
}
 .drop-active {
  top: 0;
  bottom: 0;
  right: 0;
  left: 0;
  position: fixed;
  z-index: 9999;
  opacity: .6;
  text-align: center;
  background: #000;
}
 .drop-active h3 {
  margin: -.5em 0 0;
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  -webkit-transform: translateY(-50%);
  -ms-transform: translateY(-50%);
  transform: translateY(-50%);
  font-size: 40px;
  color: #fff;
  padding: 0;
}
	</style>
