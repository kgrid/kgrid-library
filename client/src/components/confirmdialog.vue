<template id='confirm_overlay'>
	<olpane layerid=9 left='540'>
		<div slot='oltitle'><div style='width:50%; float:left;'><h3>{{statement}}</h3></div></div>
		<div slot='ol-form'>
			<div style='width:50%; left:0; position:absolute;'>
				<div class='loginField textwrap'>
				{{content}}
				</div>
				<div class='mar-t-10' style='text-align:center;'>
						<button class="kg-btn-secondary" v-on:click="cancel_clicked" id="cancelBtn">Cancel</button>
						<button class="kg-btn-primary" id="saveObjButton" v-on:click="ok_clicked">{{btnText}}</button>
				</div>
		</div>
	</div>
	</olpane>
</template>
<script>
import olpane from '../components/olpane';
export default {
  name: 'confirmdialog',
	props: ['name','statement','content','btnText'],
  data: function () {
    return {
	    question: 'Are you sure?',
			ret:{name:"",val:false}
    };
  },
  components: {
    olpane
  },
  methods: {
		ok_clicked: function(){
			console.log("OK");
			this.ret.name=this.name;
			this.ret.val=true;
			this.$eventBus.$emit("confirm", this.ret);
		},
		cancel_clicked: function(){
			console.log("Cancel");
			this.ret.al=false;
			this.ret.name=this.name;
			this.$eventBus.$emit("confirm", this.ret);
		}
  }
};
</script>
<style scoped>
.loginField {
  margin: 25px 0px 25px 0px;
  height: 120px;
}
.loginField label {
	position: relative;
	color: #666666;
	font-weight: 400;
	font-size: 16px;
 	line-height: 1.6em;
 	padding: 10px 14px;
 	margin:0px;
}
h3 {
  font-size: 32px;
  font-weight: 500;
	line-height: 1.5em;
}
</style>
