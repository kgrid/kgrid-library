<template>
	<olpane layerid=0 :stage='stage'>
		<div slot="oltitle"  v-if='viewraw'>
			<div class='row'  >
				<h2>Knowledge Object
					<span style="padding: 3px;"> {{uri}}</span></h2>
			</div>
		</div>
		<div slot="ol-form" style="height:100%;">
				<h2  v-show='viewraw'>{{rscfilename}}</h2>
			<div id="ui_container" v-show='!viewraw'></div>
			<prism language='yaml' v-show='viewraw' style="max-height:90%;">{{rawfile}}</prism>
		</div>
		<div slot='buttons'>
				<button class="kg-btn-primary" @click="golivedemo">
					<span v-if='viewraw'>Live Demo</span>
					<span v-else> View Raw YAML file </span>
				</button>
		</div>
	</olpane>
</template>
	<script>
	import olpane from '../components/olpane';
	import SwaggerUI from 'swagger-ui'
	import Prism from 'vue-prism-component'
	export default {
		name:"ioviewer",
		data:function(){
			return {
				stage:'',
				rawfile:'',
				viewraw:true
			}
		},
	created:function(){
		var self = this
		this.$http.get(this.url).then(response=> {
			console.log("redaing service descriptor...")
			console.log(response)
      self.rawfile = response.data
    }).catch(e=>{
      console.log(e)
    })
	},
	mounted:function(){
		SwaggerUI({
			dom_id:'#ui_container',
			url:this.url,
			plugins: [
	 			DisableTryItOutPlugin
 			]
		})
	},
	components: {
		olpane,
		SwaggerUI,
		Prism
	},
	computed:{
		url:function(){
			return this.$store.getters.getkoserviceurl
		},
		urlarray: function(){
			return this.url.split('/')
		},
		rscfilename:function(){
			return this.urlarray[this.urlarray.length-1]
		},
		uri:function(){
			return this.$store.getters.getcurrenturi
		},
	},
	methods:{
		switchview:function(){
			this.viewraw=!this.viewraw
		},
		golivedemo:function(){
			this.$eventBus.$emit('objactivation','')
		}
	}
};
const DisableTryItOutPlugin = function() {
  return {
    statePlugins: {
      spec: {
        wrapSelectors: {
          allowTryItOutFor: () => () => false
        }
      }
    }
  }
}
	</script>
	<style >
	#ui_container {
		padding: 10px;
		border:none;
		margin-top:-60px;
	}
.ol_pane pre {
		overflow-y: auto;
	}

.swagger-ui .info .title small pre  {
		background-color: transparent;
		padding:2px 4px;
		border:none;
	}
	.swagger-ui .info {
		margin: 30px 0 20px 0;
	}
	</style>
