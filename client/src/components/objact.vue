<template>
	<olpane layerid=0 :stage='stage'>
		<div slot="oltitle">
			<div class='row' >
				<span class='body-1'>Knowledge Object</span><br>
					<span class='headline'> {{url}}</span>
					<span v-if='objpackaged' style="background-color:#0075bc;font-size:1em;color:#fff; padding:1px 4px; margin:0px 3px 5px 30px;">packaged</span>
					<span v-if='objactivated' style="background-color:#0075bc;font-size:1em;color:#fff; padding:1px 4px; margin:0px 3px 5px 10px;">activated</span>
			</div>
		</div>
		<div slot="ol-form">
			<div id="activator_pane" v-if='objpackaged && !objactivated'>
				<v-layout row wrap my-2>
					<span class='body-1'>The knowledge object has been packaged. Please enter or select the activator to send the KO.</span>
				</v-layout>

				<v-layout row wrap my-5 >
					<v-flex xs3>
						<span class='title'>Activator Site URL</span>
					</v-flex>
					<v-flex xs7>
						<v-text-field
							label="Activator Site URL"
							box
							solo
							v-model='activatorurl'
						></v-text-field>
					</v-flex>
				<v-flex xs2>
					<div class='col-md-2'>
						<v-btn
						 color="#0075bc"
						 outline
						 @click='addactivatorentry'
						 v-if='activatorurlindex==-1'
						 >
						 <span>Save URL</span>
					 </v-btn>
					 <v-btn
						color="#0075bc"
						outline
						@click='setdefaultactivator'
						v-if='!isDefaultActivator'
						>
						<span>Set as Default</span>
					</v-btn>
					</div>
				</v-flex>
			</v-layout>

			<v-layout row wrap my-5>
				<v-flex xs3>
						<span class='title'>Available Activator Sites</span>
				</v-flex>
				<v-flex xs9>
					<v-radio-group v-model='activatorurlselect' :mandatory="false" >
						<v-layout row wrap v-for="(entry,index) in activatorurls" my-1 py-2 align-center fill-height>
							<v-flex xs10>
								<v-radio :label="entry" :value="entry"></v-radio>
							</v-flex>
							<v-flex xs2>
								<v-btn
								 fab
								 small
								 color="#0075bc"
								 outline
								 @click='deleteactentry(index)'
								 :disabled='(index==defaultactivatorindex)||(index==activatorurlindex)'
								>
								 <v-icon>clear</v-icon>
								</v-btn>
							</v-flex>
							<v-divider></v-divider>
						</v-layout>
					</v-radio-group>
				</v-flex>
				</v-layout>
			</div>
			<div id="demo_pane"  v-if='objactivated'>

				<div class='card'>
					<div class='row' style="padding:10px;">
 				 		<h2>KO Access at the activator: </h2>
			 		</div>
			 		<div class='row' >
						<h2 style=" padding: 10px 3px;position:relative;text-align:center;">  <a :href="targeturl" target="_blank" style="padding: 3px;position:relative;font-size:1.2em;text-align:center;"><span >{{targeturl}}</span></a></h2>
 			 		</div>
		 		</div>

				<div class='card'>
					<div class='row' style="padding:10px;">
					 <h2>KO Demo at KGRID-DEMOS <a href="https://swagger.io/tools/swagger-ui/" target="_blank">Swagger UI</a>  </h2>
					</div>
					<div class='row' >
						<h2 style=" padding: 10px 3px;position:relative;text-align:center;">  <a :href="kgriddemosurl" target="_blank" style="padding: 3px;position:relative;font-size:1.2em;text-align:center;"><span >{{kgriddemosurl}}</span></a></h2>
					</div>
				</div>

				<div class='card'>
					<div class='row' style="padding:10px;">
					 <h2>KO Demo at Online <a href="https://swagger.io/tools/swagger-editor/" target="_blank">Swagger Editor</a> </h2>
					</div>
					<div class='row' >
						<h2 style=" padding: 10px 3px;position:relative;text-align:center;">  <a :href="swaggereditorurl" target="_blank" style="padding: 3px;position:relative;font-size:1.2em;text-align:center;"><span >{{swaggereditorurl}}</span></a></h2>
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
	watch: {
		activatorurlselect () {
			this.selectactivatorsite()
		}
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
						console.log(error);
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
	<style>
	#activator_pane {
		padding: 10px;
		border:none;
	}

	.alert-box  div span{
		color: #fff;
	}
	.card {
		border: 2px solid #eee;
		margin-top: 30px;
		margin-right:5px;
		box-shadow: 5px 5px 1px 1px #ddd;
	}
	.entryform .v-input  {
		font-size: 1.8em;
	}
	.entryform .v-input--selection-controls .v-input__control {
		flex-grow: 1;
	}
	.entryform .v-input--selection-controls {
		margin-top: 0;
	}
	</style>
