<template id='contactus'>
<div class='content'>
	<applayout :nothelper='false'>
		<div slot='banner'>
			<p> FAQ Banner </p>
		</div>
		<div slot='header'>
			<p> FAQ Header </p>
		</div>
		<div slot='maincontent'>
		<div class="infogrid"><div class="infoblock"><div class="block_title"><div class="line"><hr></div>
		We are here
		<div class="line"><hr></div></div> <div id="divisionname" class="block_info"><span class="firstline">Department of Learning Health Sciences</span><br> Division of
		Learning and Knowledge Systems<br> University of Michigan
		Medical School<br> Ann Arbor, MI 48109<br></div> 
		<br><div id="contactus" class="block_info active"><span class="firstline middleout">Knowledge Grid Development Team</span><br class="middleout"> 
		<a href="mailto:dlhs.knowledge.grid@umich.edu">dlhs.knowledge.grid@umich.edu</a>
	</div></div></div>
		</div>
		</applayout>
</div>
</template>
<script>
import applayout from './applayout.vue';
export default {
    name: 'contactus',
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
.firstline {
	font-weight: 700;
}
</style>