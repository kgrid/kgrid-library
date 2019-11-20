<template>
	<v-container grid-list-xs class='kg-tile' v-bind:id="object.uri"  pa-10 v-on:click="selected(object[versionIndex])">
    <v-layout row wrap pa-10>
      <v-flex xs12 sm12 md12 py-1 >
        <span class='body-2'>{{object[versionIndex].identifier}}</span>
				<v-chip v-if='versionList.length>1' v-for='(ver,index) in versionList' class='caption' @click.stop='setVersionIndex(index)'><span v-bind:class="{'subheading':isCurrent(ver)}">{{ver}}</span></v-chip>
				<v-chip v-else v-for='(ver,index) in versionList' outline class='caption' ><span v-bind:class="{'subheading':isCurrent(ver)}">{{ver}}</span></v-chip>
			</v-flex>
			<v-flex  xs12 sm12 md12 py-0>
        <div class='headline'>{{object[versionIndex].title}}</div>
      </v-flex>
			<v-flex  xs12 sm12 md12 py-1 class="text-xs-right">
				<v-chip v-for='key in keywords' class='caption' @click.stop='clickonkeyword(key)'>{{key}}</v-chip>
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
				versionIndex: 0
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
			isCurrent: function(ver){
				return ver==this.versionList[this.versionIndex]
			},
			setVersionIndex: function(i) {
				this.versionIndex = i
			},
			selected: function(s){
				if(this.$DEBUG) console.log(s);
				this.$store.commit('setCurrentKO',this.object);
				this.$store.commit('setCurrentObject',s)
        this.$eventBus.$emit("objectSelected", this.object[this.versionIndex].identifier.replace('ark:/','')+'/'+this.versionList[this.versionIndex]);
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
