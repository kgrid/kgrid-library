<template>
	<v-container grid-list-xs class='kg-tile' v-bind:id="object.uri"  pa-10 v-on:click="selected(object[versionIndex].identifier)">
    <v-layout row wrap pa-10>
      <v-flex xs12 sm12 md12 py-1 >
        <span class='body-2'>{{object[versionIndex].identifier}}</span>
				<v-chip v-for='(ver,index) in versionList' class='caption' @click.stop='setVersionIndex(index)'><span v-bind:class="{'subheading':isDefault(ver)}">{{ver}}</span></v-chip>
			</v-flex>
			<v-flex  xs12 sm12 md12 py-0>
        <div class='headline'>{{object[versionIndex].title}}</div>
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
				versionIndex: 0,
				defaultVersion: 0
			}
		},
		created: function(){},
		computed : {
			keywords:function(){
				if(Array.isArray(this.object[this.versionIndex].keywords)){
					return this.object[this.versionIndex].keywords
				}else {
					return this.object[this.versionIndex].keywords.split(",")
				}
			},
			versionList: function(){
				var vl = []
				this.object.forEach(function(e){
					vl.push(e.version)
				})
				return vl
			},
		},
		methods : {
			isDefault: function(ver){
				return ver==this.versionList[this.defaultVersion]
			},
			setVersionIndex: function(i) {
				this.versionIndex = i
			},
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
	.default {
		font-weight: 500;
	}
	</style>
