<template>
	<olpane layerid=0 :stage='stage'>
		<div slot="oltitle">
			<div class='row' >
				<span class='body-1'>Knowledge Object</span><br>
					<span class='headline'> {{url}}</span>
					<!-- <v-chip v-if='objpackaged' outline color="#0075bc">packaged</v-chip>
					<v-chip v-if='objactivated' outline color="#0075bc">activated</v-chip> -->

					<span v-if='objpackaged' style="background-color:#0075bc;font-size:1em;color:#fff; padding:1px 4px; margin:0px 3px 5px 30px;">packaged</span>
					<span v-if='objactivated' style="background-color:#0075bc;font-size:1em;color:#fff; padding:1px 4px; margin:0px 3px 5px 10px;">activated</span>
			</div>
		</div>
		<div slot="ol-form">
			<div id="activator_pane" v-if='objpackaged && !objactivated'>
				<v-layout row wrap my-2>
					<span class='body-1'>The knowledge object has been packaged. Please enter or select the activator to send the KO.</span>
				</v-layout>
				<div class='row mar-top30'>
					<div class='col-md-3 pad-t-10'>
						<h2>Activator Site URL</h2>
					</div>
					<div class='col-md-7'>
						<input type='text' v-model='activatorurl' />
					</div>
					<div class='col-md-2'>
						<button class='kg-btn-entry' v-if='activatorurlindex==-1' @click='addactivatorentry'>Save URL</button>
						<button class='kg-btn-entry' v-if='!isDefaultActivator' @click='setdefaultactivator'>Set as Default</button>
					</div>
				</div>
				<div class='row mar-top30'>
					<div class='col-md-3'>
						<h4>Available activator Sites</h4>
					</div>
					<div class='col-md-7'>
						 <div class='row' v-for="(entry,index) in activatorurls" style="border-bottom:1px solid #eee;">
							 <input type="radio" :id="entry" :value="entry" @change='selectactivatorsite' v-model='activatorurlselect'>
							 <label :for="entry">{{entry}}</label>
							 <button v-if='index!=defaultactivatorindex' style="background-color:transparent;float:right;margin-top:10px;" @click='deleteactentry(index)'><v-icon>clear</v-icon></button>
						 </div>
				 	</div>
					<div class='col-md-2'>
					</div>
				</div>
			</div>
			<div id="demo_pane"  v-if='objactivated'>

				<div class='card'>
					<div class='row' style="padding:10px;">
 				 		<h2>KO Access at the activator: </h2>
			 		</div>
			 		<div class='row' >
						<h2 style=" padding: 30px 3px;position:relative;text-align:center;">  <a :href="targeturl" target="_blank" style="padding: 3px;position:relative;font-size:1.5em;text-align:center;"><span >{{targeturl}}</span></a></h2>
 			 		</div>
		 		</div>

				<div class='card'>
					<div class='row' style="padding:10px;">
					 <h2>KO Demo at KGRID-DEMOS <a href="https://swagger.io/tools/swagger-ui/" target="_blank">Swagger UI</a>  </h2>
					</div>
					<div class='row' >
						<h2 style=" padding: 30px 3px;position:relative;text-align:center;">  <a :href="kgriddemosurl" target="_blank" style="padding: 3px;position:relative;font-size:1.1em;text-align:center;"><span >{{kgriddemosurl}}</span></a></h2>
					</div>
				</div>

				<div class='card'>
					<div class='row' style="padding:10px;">
					 <h2>KO Demo at Online <a href="https://swagger.io/tools/swagger-editor/" target="_blank">Swagger Editor</a> </h2>
					</div>
					<div class='row' >
						<h2 style=" padding: 30px 3px;position:relative;text-align:center;">  <a :href="swaggereditorurl" target="_blank" style="padding: 3px;position:relative;font-size:1.1em;text-align:center;"><span >{{swaggereditorurl}}</span></a></h2>
					</div>
				</div>

			</div>
		</div>
		<div slot='buttons' >
			<v-btn
			 color="#0075bc"
			 right
			 bottom
			 relative
			 outline
			 @click='sendzip'
			 v-if='stage=="ready"&&!objactivated'
			 :disabled="btndisabled"
			 >
			 <span>Send KO to Activator</span>
		 </v-btn>

		</div>
		<div slot="ol-processing"><span>{{processMsg}}</span></div>
		<div slot="ol-success"><span>The Knowledge Object has been succesfully deployed. </span></div>
		<div slot="ol-failure"><span>{{errorMsg}}</span></div>
		<div slot="ol-warning"><span>Warning !!!</span></div>

	</olpane>
</template>
	<script>
	import olpane from '../components/olpane';
	export default {
		name:"objact",
		data:function(){
			return {
				stage:'',
				zipfile:'',
				objpackaged:false,
				objactivated:false,
				activatorurls:[],
				activatorurl:'',
				activatorurlselect:'',
				demourls:[],
				demourl:'',
				demourlselect:'',
				btndisabled:false
			}
		},
	created:function(){
		var self = this
		this.demourls=this.$store.getters.getdemourls
		this.activatorurls=this.$store.getters.getactivatorurls
		self.stage='processing'
		setTimeout(function(){
			self.$http.get(
				self.htmldownloadlink,
				{ responseType: 'blob' }
			).then(response=> {
				self.stage='ready'
				console.log("reading zip...")
				self.zipfile=new File([response.data], self.downloadFile, { type: 'application/zip' } )

				// const url = window.URL.createObjectURL(new Blob([response.data]));
				//  const link = document.createElement('a');
				//  link.href = url;
				//  link.setAttribute('download', 'file.zip'); //or any other extension
				//  document.body.appendChild(link);
				//  link.click();
				//


				console.log(self.zipfile.size)
				setTimeout(function(){self.objpackaged =true},500)
	    }).catch(e=>{
				self.stage='error'
	      console.log(e)
	    })
		}, 2500)
	},
	mounted:function(){
		this.demourl=this.demourls[this.defaultdemoindex]
		this.activatorurl=this.activatorurls[this.defaultactivatorindex]
		this.demourlselect=this.demourl
		this.activatorurlselect=this.activatorurl
		this.objactivated=false
		this.objpackaged = false
	},
	components: {
		olpane
	},
	computed:{
		processMsg:function(){
			if(this.objpackaged){
				return 'Deploying KO to the activator ...'
			}else {
				return 'Packaging the KO ... '
			}
		},
		errorMsg:function(){
			if(this.objpackaged){
				return 'Error in deploying the KO! Check the URL and try again.'
			}else {
				return 'Error in packaging the KO!'
			}
		},
		url:function(){
			return this.$store.getters.getcurrenturl
		},
		currentKOId: function(){
			return this.$store.getters.getCurrentKOId
		},
		htmldownloadlink: function(){
			return this.$store.getters.getshelfurl+this.currentKOId.naan+'/'+this.currentKOId.name+'?format=zip'
		},
		downloadFile : function() {
			return this.currentKOId.naan+'-'+this.currentKOId.name+'.zip'
		},
		defaultdemoindex: function(){
				return this.$store.getters.getdefaultdemourlindex
		},
		defaultactivatorindex: function(){
				return this.$store.getters.getdefaultactivatorurlindex
		},
		demourlindex: function(){
			return this.demourls.indexOf(this.demourl)
		},
		activatorurlindex: function(){
			return this.activatorurls.indexOf(this.activatorurl)
		},
		uri:function(){
			return this.$store.getters.getcurrenturi
		},
		isDefaultDemo: function(){
			if(this.demourlindex!=-1){
				return (this.defaultdemoindex==this.demourlindex)
			}else {
				return true
			}
		},
		isDefaultActivator: function(){
			if(this.activatorurlindex!=-1){
				return (this.defaultactivatorindex==this.activatorurlindex)
			}else {
				return true
			}
		},
		targeturl:function(){
			return this.activatorurl+"/"+this.url
		},
		kgriddemosurl:function(){
			return "https://kgrid-demos.github.io/swaggerui?url="+this.targeturl+'/service'
		},
		swaggereditorurl:function(){
			return "https://editor.swagger.io?url="+this.targeturl+'/service'
		}
	},
	methods: {
		selectactivatorsite:function(){
			this.activatorurl=this.activatorurlselect
		},
		sendzip:		function(){
					var formData = new FormData();
					var self = this;
					formData.append('ko', this.zipfile);
					this.stage='processing';
					setTimeout(function(){
					self.$http.put( self.targeturl,
						formData,
						{  headers: {}  }
					).then(function(resp){
						self.$http.get( self.activatorurl+'/endpoints').then(function(response){
							self.stage='success'
							setTimeout(function(){
								console.log(resp);
								self.objactivated =true
								self.stage='ready'
								} , 1500)
						}).catch(function(error){
							self.stage='error'
							console.log(err);
							setTimeout(function(){
								self.stage='ready'
							},1500)
						})
					})
					.catch(function(err){
						self.stage='error'
						console.log(err);
						setTimeout(function(){
							self.stage='ready'
						},1500)
					});}, 1000)
				},
				addactivatorentry:function(){
					this.activatorurls.push(this.activatorurl)
					this.activatorurlselect=this.activatorurl
					this.$store.commit('setactivatorurls', this.activatorurls)
				},
				deleteactentry:function(i){
					this.activatorurls.splice(i,1)
					this.$store.commit('setactivatorurls', this.activatorurls)
				},
				setdefaultactivator:function(){
					console.log(this.activatorurlindex)
					this.$store.commit("setdefaultactivatorurlindex", this.activatorurlindex)
				}
	}
};
	</script>
	<style scoped>
	#activator_pane {
		padding: 10px;
		border:none;
	}
	.row{
		margin-left:0px;
		margin-right:0px;
	}
	.alert-box  div span{
		color: #fff;
	}
	input[type=text] {
		font-size:16px;
		font-weight:600;
	}
	.overlay-title {
		border-bottom:1px solid #eee;
	}
	.card {
		border: 2px solid #eee;
		margin-top: 50px;
		margin-right:5px;
		box-shadow: 5px 5px 1px 1px #ddd;
	}
	</style>
