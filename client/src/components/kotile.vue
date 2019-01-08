<template>
		<v-container grid-list-xs class='kg-tile' v-bind:id="object.uri"  pa-10 v-on:click="selected(object.identifier)">
		    <v-layout row wrap pa-10>
		      <v-flex xs6 sm6 md6 py-1>
		        <span class='body-1 kg-koentry' v-on:click="selected(object.identifier)">{{object.identifier}}</span>
		      </v-flex>
					<v-flex  xs12 sm12 md12 py-1>
		        <div class='title'>{{object.title}}</div>
		      </v-flex>
					<v-flex  xs12 sm12 md12 py-0>
						<span class='text-uppercase caption font-italic'>Keywords: </span><span v-for='key in keywords' class='body-2 kg-keyentry'>{{key}}</span>
					</v-flex>
					<v-flex  xs12 sm12 md12 py-0 class='text-xs-right'>
						<span class='text-uppercase caption font-italic'>Implementations: </span>
						<span v-for='imp in implementation' class='body-2 kg-impentry' v-on:click.stop="selected(imp)">{{imp}}</span>
					</v-flex>
		    </v-layout>
		</v-container>
	</template>
	<script>
	export default {
		name:	"kotile",
		props : [ 'object', 'listsize' ,'tileindex'],
		data : function(){
			return {
			}
		},
		created: function(){},
		computed : {
			keywords:function(){
				return this.object.keywords.split(" ")
			},
			implementation:function(){
				if(Array.isArray(this.object.hasImplementation)){
					return this.object.hasImplementation
				} else {
					var list = []
					list.push(this.object.hasImplementation)
					return list
				}
			},
		},
		methods : {
			selected: function(s){
				if(this.$DEBUG) console.log(s);
				s=s.replace('ark:/','');
				s=s.replace('-','/');
				this.$store.commit('setCurrentKO',s);
        this.$eventBus.$emit("objectSelected", s);
				return false;
			}
		}
	};
	</script>
	<style scoped>
	.kg-tile {
		background-color: #fff;
		border-left: 3px solid #fff;
		transition: all 0.8s ease;
	}
	.kg-tile:hover {
		border-left: 3px solid #0075bc;
	}
	.kg-keyentry {
		padding: 0 4px;
	}
	.kg-impentry {
		padding: 0px 4px;
		color: #0075bc;
		cursor:pointer;
		margin: 0 4px;
	}
	.kg-koentry {
		padding: 0 4px;
		color: #0075bc;
		cursor:pointer;
	}
	</style>
