<template>
	<olpane layerid=0 :stage='stage'>
		<div slot="ol-form">
			<div id="activator_pane" v-if='objpackaged && !objactivated'>
				<div class='row' >
					<h2>Knowledge Object <span style="background-color:#eee;padding: 3px;"> {{uri}}</span> has been packaged and ready to deploy.</h2><br>
				</div>
				<!-- <div class='row' >
					<p>* Enter the activator url or select from the list of available activators. If a new url is entered, you can save to the list and set it as default.
					</p>
				</div> -->
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
							 <button v-if='index!=defaultactivatorindex' style="background-color:transparent;float:right;margin-top:10px;" @click='deleteactentry(index)'><icon color="#0075bc" name="times"></icon></button>
						 </div>
				 	</div>
					<div class='col-md-2'>
					</div>
				</div>
			</div>
			<div id="demo_pane"  v-if='objactivated'>
				<div class='row' >
 				 <h2>Knowledge Object <span style="background-color:#eee;padding: 3px;"> {{uri}}</span> has been activated at <span style="background-color:#eee;padding: 3px;">{{activatorurl}}</span>.</h2><br>
 			 </div>
 			 <!-- <div class='row mar-t-10' >
 				 <p>* Enter the demo site url or select from the list of available demo sites. If a new url is entered, you can save to the list and set it as default.				 </p>
 			 </div>
			 <div class='row mar-t-10' >
 				 <p>* When ready, click on "Go to Demo Swagger UI", a new tab will open for you to try the knowledge object using Swagger UI.
 				 </p>
 			 </div> -->

			 <div class='row mar-top30'>
				 <div class='col-md-3 pad-t-10'>
					 <h2>Demo Site URL</h2>
				 </div>
				 <div class='col-md-7'>
					 <input type='text' v-model='demourl' />
				 </div>
				 <div class='col-md-2'>
					 <button class='kg-btn-entry' v-if='demourlindex==-1' @click='adddemoentry'>Save URL</button>
					 <button class='kg-btn-entry' v-if='!isDefaultDemo' @click='setdefaultdemo'>Set as Default</button>
				 </div>
			 </div>
			 <div class='row mar-top30'>
				 <div class='col-md-3'>
					 <h4>Available Demo Sites</h4>
				 </div>
				 <div class='col-md-7'>
 				 		<div class='row' v-for="(entry,index) in demourls">
				 			<input type="radio" :id="entry" :value="entry" @change='selectdemosite' v-model='demourlselect'>
				 			<label :for="entry">{{entry}}</label>
							<button v-if='index!=defaultdemoindex' style="background-color:transparent;float:right;margin-top:10px;" @click='deletedemoentry(index)'><icon color="#0075bc" name="times"></icon></button>
			 			</div>
			 	</div>
			 <div class='col-md-2'>

			 </div>
			 </div>
			</div>

		</div>
		<div slot='buttons' >
				<button v-if='stage=="ready"' class="kg-btn-primary" @click="btnclick" :disabled="btndisabled">
					<span v-if='objactivated'>Go to Demo Swagger UI</span>
					<span v-else > Send KO to Activator </span>
				</button>
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
				blob:'',
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
				self.url+'?format=zip',
				{ responseType: 'blob' }
			).then(response=> {
				self.stage='ready'
				console.log("reading zip...")
				self.zipfile=new File([response.data], self.downloadFile, { type: 'application/zip' } )
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
		downloadFile : function() {
			return this.$store.getters.getnaandashname + '.zip'
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
		}
	},
	methods: {
		selectdemosite:function(){
			this.demourl=this.demourlselect
		},
		selectactivatorsite:function(){
			this.activatorurl=this.activatorurlselect
		},
		btnclick:function(){
			if(this.objactivated){
				this.gotodemo()
			}else {
				this.sendzip()
			}
		},
		sendzip:		function(){
					var formData = new FormData();
					var self = this;
					formData.append('ko', this.zipfile);
					this.stage='processing';
					setTimeout(function(){
					self.$http.put( self.activatorurl+'/'+self.$store.getters.getnaanslashname+"/",
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
				gotodemo: function(){
					this.$eventBus.$emit('hideOverlay',"0");
					var target = this.demourl+'?url='+this.activatorurl+"/"+this.uri+'/service'
					setTimeout(function(){
						window.open(target);
					}, 500)
				},
				adddemoentry:function(){
					this.demourls.push(this.demourl)
					this.demourlselect=this.demourl
					this.$store.commit('setdemourls', this.demourls)
				},
				deletedemoentry:function(i){
					this.demourls.splice(i,1)
					this.$store.commit('setdemourls', this.demourls)
				},
				setdefaultdemo:function(){
					this.$store.commit("setdefaultdemourlindex", this.demourlindex)
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
	</style>
