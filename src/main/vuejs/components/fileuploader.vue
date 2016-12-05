<template id="file_uploader"> 
	<div v-if="!selectedfile">
	<div class="dropfile">
			<div class="upload-direction">
				<input type="file" :id="selectfile" @change="onFileChange" class="inputfile" 
										data-multiple-caption="{count} files selected" multiple style="display: none;"/>
				<label :for="selectfile" id="filecount"> 
					<img src="images/Upload_Icon.png">
					<p class="green">CHOOSE_FILE_FOR_{{section}}</p>
				</label>
			</div>
	</div></div>
	<div v-else>
		<button v-on:click="removeOutputFile">Remove</button>
		<textarea @change="inputchange" spellcheck=false v-model="message.msg"></textarea>
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