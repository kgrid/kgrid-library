<template>
	<v-container grid-list-xs class='kg-tile' v-bind:id="object.uri"  pa-10 v-on:click="selected(object.identifier)">
    <v-layout row wrap pa-10>
      <v-flex xs12 sm12 md12 py-1 >
        <span class='body-2'>{{object.identifier}}</span>
				<v-chip class='caption'>{{object.version}}</v-chip>
			</v-flex>
			<v-flex  xs12 sm12 md12 py-0>
        <div class='headline'>{{object.title}}</div>
      </v-flex>
			<v-flex  xs12 sm12 md12 py-1 class="text-xs-right">
				<v-chip v-for='key in keywords' outline class='body-2' @click.stop='clickonkeyword(key)'>{{key}}</v-chip>
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
				filterStrings:[]
			}
		},
		created: function(){},
		computed : {
			keywords:function(){
				if(Array.isArray(this.object.keywords)){
					return this.object.keywords
				}else {
					return this.object.keywords.split(",")
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
			},
			clickonkeyword: function(s){
				this.$store.commit('addFilterString', s);
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
	</style>
