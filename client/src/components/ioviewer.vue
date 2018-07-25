<template>
	<olpane layerid=0 :stage='stage'>
		<div slot="ol-form">
			<div id="ui_container" v-show='!viewraw'></div>
			<prism language='yaml' v-show='viewraw'>{{rawfile}}</prism>
		</div>
		<div slot='buttons'>
				<button class="kg-btn-primary" @click="viewraw=!viewraw">
					<span v-if='viewraw'>View in Swagger UI</span>
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
				viewraw:false
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
	#ui_container, #rawyaml {
		padding: 10px;
		border:none;
	}
	#rawyaml {
		overflow: auto;
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
