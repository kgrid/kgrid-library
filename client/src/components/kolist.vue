<template>
	<v-container grid-list-xl>
		<v-layout row wrap justify-center>
			<v-flex v-for='(object,index) in list' v-bind:key='index' xs12 >
				<kotile :object='object' :listsize='listSize' :tileindex='index' ></kotile>
			</v-flex>
		</v-layout>
	</v-container>
</template>
<script>
import kotile from '../components/kotile.vue';
export default {
  name: 'KoList',
	props:['list'],
	data : function() {
		return {
	  }
	}
	, created : function() {
	}
	, computed : {
		listSize : function() {
			return this.list.length;
		},
	}
	, components:{
		kotile,
	}
};
</script>
