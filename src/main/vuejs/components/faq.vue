<template id='faq'>
<div class='content'>
	<applayout :nothelper='false'>
		<div slot='banner'>
			<p> FAQ Banner </p>
		</div>
		<div slot='header'>
			<p> FAQ Header </p>
		</div>
		<div slot='maincontent'>
			<div id="about_grid" class="infogrid">
				<div class="infoblock">
					<div class="block_title">
						<div class="line"><hr></div>What is ObjectTeller?<div class="line"><hr></div>
					</div>
					<div class="block_info">It&#39s an app for managing computable health knowledge.</div>
					</div>
				<div class="infoblock">
					<div class="block_title">
						<div class="line"><hr></div>Is ObjectTeller free?<div class="line"><hr></div>
					</div>
					<div class="block_info">Yes. Download it and put it to use!</div>
				</div>
				<div class="infoblock">
					<div class="block_title"><div class="line"><hr></div>Who built ObjectTeller?<div class="line"><hr></div>
					</div>
					<div class="block_info">A team of dedicated professionals at the University of Michigan.</div>
				</div>
				<div class="infoblock">
					<div class="block_title"><div class="line"><hr></div>Can I help further develop ObjectTeller?<div class="line"><hr></div>
					</div>
					<div class="block_info">Yes! Join our technical community by sending us an email to <a href="mailto:dlhs.knowledge.grid@umich.edu">dlhs.knowledge.grid@umich.edu</a>.</div>
				</div>
			</div>
		</div>
		</applayout>
</div>
</template>
<script>
import applayout from './applayout.vue';
export default {
    name: 'faq',
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