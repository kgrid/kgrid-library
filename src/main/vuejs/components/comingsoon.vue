<template id='comingsoon'>
<div class='content'>
	<applayout :nothelper='false'>
		<div slot='banner'>
			<p> About Banner </p>
		</div>
		<div slot='header'>
			<p> About Header </p>
		</div>
		<div slot='maincontent'>
		<div id="about_grid" class="infogrid" >
			<div class="infoblock">
				<div class="block_title">
					<div class="line">
						<hr></div>Coming Soon! <div class="line"><hr></div>
					</div>
				<div class="block_info">We are working on this feature. Please come back later.</div>
			</div>
		</div></div>
		</applayout>
</div>
</template>
<script>
import applayout from './applayout.vue';
export default {
    name: 'comingsoon',
	data : function() {
		return {
		}
	},
	created : function() {
	},
	mounted:function(){
	},
	computed : {
	},
	methods : {
	},
	components:{
		'applayout':applayout
		}
};
</script>
<style>

</style>