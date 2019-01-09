<template>
	<olpane layerid=0 :stage='stage'>
		<div slot="oltitle"><h3>Add a Knowledge Object to the Library </h3></div>
		<div slot="ol-form" >
				<div class="panel-body">
					<div id="file-drag-drop">
						<form ref="fileform" v-show='files.length==0'>
							<label for='selectfile'><span class="drop-files">Select or Drop the KO ZIP file here!</span></label>
							<input type="file" id="selectfile" @change="onFileChange" class="inputfile"
										data-multiple-caption="{count} files selected" multiple style="display: none;"/>
						</form>
						<div v-for="(file, key) in files" class="file-listing" v-show='files.length>0'>
							{{ file.name }}
							<div class="inline float-r">
								<a class="remove" v-on:click="removeFile( key )"><i class='fa fa-times'></i></a>
							</div>
							<v-progress-linear v-model="uploadPercentage"></v-progress-linear>
						</div>
					</div>
				</div>
			</div>
			<div slot='buttons'>
					<v-btn
					 color="#0075bc"
					 right
					 bottom
					 relative
					 outline
					 @click='submitFiles'
					 >
					 <span>Deposit Object</span>
				 </v-btn>
			</div>
		<div slot="ol-processing"><span>Processing...</span></div>
		<div slot="ol-success"><span>The knowledge Object succesfully added to the shelf.  Object ID:{{arkid}}</span></div>
		<div slot="ol-failure"><span>Error in depositing the KO! </span></div>
		<div slot="ol-warning"><span>Warning !!!</span></div>
	</olpane>
</template>
	<script>
	import olpane from '../components/olpane';
	export default {
		name:"objcreator",
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
		olpane
		},
	computed:{
		arkid:function(){
			if(this.files.length>0){
				var filename = this.files[0].name
				var index=filename.lastIndexOf('/')
				var l= filename.length
				var s = filename.slice(index+1,l)
        return s.replace('.zip','').replace(/-/g,'/')
			 }else {
				 return ''
			 }
		},
		url:function(){
			return this.$store.getters.getshelfurl
		}
	},
	mounted:function(){
		/*			Determine if drag and drop functionality is capable in the browser		*/
		this.dragAndDropCapable = this.determineDragAndDropCapable();
		/*			If drag and drop capable, then we continue to bind events to our elements.		*/
		if( this.dragAndDropCapable ){
			/*
				Listen to all of the drag events and bind an event listener to each
				for the fileform.
			*/
			['drag', 'dragstart', 'dragend', 'dragover', 'dragenter', 'dragleave', 'drop'].forEach( function( evt ) {
				/*
					For each event add an event listener that prevents the default action
					(opening the file in the browser) and stop the propagation of the event (so
					no other elements open the file in the browser)
				*/
				this.$refs.fileform.addEventListener(evt, function(e){
					e.preventDefault();
					e.stopPropagation();
				}.bind(this), false);
			}.bind(this));
			/*
				Add an event listener for drop to the form
			*/
			this.$refs.fileform.addEventListener('drop', function(e){
				/*
					Capture the files from the drop event and add them to our local files
					array.
				*/
				for( let i = 0; i < e.dataTransfer.files.length; i++ ){
					this.files.push( e.dataTransfer.files[i] );
				}
			}.bind(this));
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
						self.uploadPercentage = parseInt( Math.round( ( progressEvent.loaded * 100 ) / progressEvent.total ) );
					}.bind(this)
				}
			).then(function(resp){
				self.stage='success'
				setTimeout(function(){
					console.log(resp);
					self.$eventBus.$emit('objAdded',{})
				} , 1500)
			})
			.catch(function(err){
				self.stage='error'
				console.log(err);
			});}, 1000)
		},
		onFileChange: function(e) {
			var files = e.target.files || e.dataTransfer.files;
				this.selectedfile=true;
				if (!files.length)
					return;
				this.files=[]
				this.files.push( files[0] );

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
		background: #eee;
		margin: auto;
		text-align: center;
		line-height: 400px;
		border-radius: 4px;
	}
	div.file-listing{
		width: 400px;
		margin: auto;
		padding: 10px;
		min-height:400px;
	}
	.alert-box  div span{
		color: #fff;
	}
	.panel-body {
		padding: 15px;
		border:1px solid #0075bc;
	}
	</style>
