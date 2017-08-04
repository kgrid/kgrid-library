<template id="file_uploader">
<div class="uploadcontainer">
	<div v-if="!selectedfile">
	<div class="dropfile">
			<div class="upload-direction">
				<input type="file" :id="selectfile" @change="onFileChange" class="inputfile"
										data-multiple-caption="{count} files selected" multiple style="display: none;"/>
				<label :for="selectfile" id="filecount">
					<i class='fa fa-cloud-upload fa-2x kg-fg-color'></i>
					<p class="green">CHOOSE_FILE_FOR_{{section}}</p>
				</label>
			</div>
	</div></div>
	<div v-else>
		<button v-on:click="removeOutputFile" class='removeBtn'>Remove</button>
		<textarea :id='loader' class='contentDisp' @change="inputchange" spellcheck=false v-model="message.msg"></textarea>
	</div>
	</div>
</template>
	<script>
	import fieldtile from './fieldtile.vue';
	export default {
		name:"fileuploader",
	props:['section','src'],
	data: function(){
	   return { selectedfile:false,
	    		message:{msg:""},
	  }},
	  computed:{
		  selectfile:function(){
			  return this.section+"file";
		  },
			loader:function(){
				return this.section+'_loader';
			}
	  },
	  created:function(){
		  if(this.src!=null){
			  this.selectedfile=(this.src!="");
		  }else{
			  this.selectedfile=false;
		  }
		  this.message.msg=this.src;
	  },
		mounted:function(){

		},
	  watch:{
		  src:function(){
			  if(this.src!=null){
				  this.selectedfile=(this.src!="");
				  this.message.msg=this.src;
			  }else{
				  this.selectedfile=false;
				  this.message.msg='';
			  }
		  }
	  },
	methods:{
		inputchange:function(){
			this.$emit("filechange",this.section,this.message.msg);
		},
		loadContent: function(file) {
	    	var self=this;
	        var reader = new FileReader();
	        reader.onload = (function(f) {
				return function(e) {
					var contents = e.target.result;
					self.message.msg=contents;
					self.$emit("filechange",self.section,self.message.msg);
				};
			})(file);
	        reader.readAsText(file);
	      },
		 onFileChange: function(e) {
		      var files = e.target.files || e.dataTransfer.files;
		      this.selectedfile=true;
		      if (!files.length)
		        return;
		      this.loadContent(files[0]);
		      },
	    removeOutputFile: function (e) {
		      this.selectedfile = '';
		      this.message.msg="";
		      this.$emit("filechange",this.section,this.message.msg);
		    }
	}
};
	</script>
	<style>
.uploadcontainer{
	margin-top:-40px;
}
.dropfile {
    margin-top: 40px;
    margin-bottom: 10px;
    border: 2px dashed #e6e6e6;
    /* border-width: 1px; */
    width: 970px;
    height: 140px;
    border-radius: 0px;
    text-align: center;
	transition: opacity 0.8s ease;
    }
.upload-direction {
    padding: 10px 0;
}
label {
    font-weight: 400;
    margin: 10px 0px;
    cursor: pointer;
}
.removeBtn {
    background-color: #fff;
    color: #0075bc;
    font-size: 14px;
    font-weight: 300;
    padding: 10px 30px;
    border: none;
    outline: none;
    cursor: pointer;
    position: relative;
    float: right;
    display: block;
}
textArea.contentDisp {
    display: block;
    height: 400px;
    width: 970px;
    padding: 5px 16px;
    border: 1px solid #e6e6e6;
    border-radius: 0px;
    resize: none;
    color: #666666;
    font-size: 14px;
    font-weight: 400;
    margin-top: 40px;
	overflow-y: auto;
}
textArea.contentDisp#payload_loader{
	height:200px;
}
	</style>
