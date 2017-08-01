<template id='confirm_overlay'>
	<olnpane layerid=0>
		<div slot='ol-title'><h3>Confirmation Dialog</h3></div>
		<div slot='ol-form'>
				<div class='loginField'>
						<span>{{statement}}</span>
						<span>{{question}}</span>
				</div>
				<div class='loginField'>
					<div class='ot-s-btn ot-login'  @click="ok_clicked">
					  <div class='greenbutton' > </div>
					  <div class='btnContent'>
						 		<button class='login'>OK</button></div>
						</div>
				</div>
				<div class='loginField'>
						<div class='ot-s-btn ot-login'>
							<div class='greenbutton' id='cancelBtn'> </div>
							<div class='btnContent'>
								<button class='login' @click="cancel_clicked">Cancel</button></div>
							</div>
						</div>
				</div>
		</div>
	</olnpane>
</template>
<script>
import olnpane from '../components/olnpane';
import eventBus from '../components/eventBus.js';
export default {
  name: 'confirmdialog',
	props: ['name','statement'],
  data: function () {
    return {
	    question: 'Are you sure?',
			ret:{name:"",val:false}
    };
  },
  components: {
    olnpane
  },
  methods: {
		ok_clicked: function(){
			console.log("OK");
			this.ret.name=this.name;
			this.ret.val=true;
			eventBus.$emit("confirm", this.ret);
		},
		cancel_clicked: function(){
			console.log("Cancel");
			this.ret.al=false;
			this.ret.name=this.name;
			eventBus.$emit("confirm", this.ret);
		}
  }
};
</script>
<style scoped>

.loginField label {
	position: relative;
	color: #666666;
	font-weight: 400;
	font-size: 16px;
line-height: 1.6em;
padding: 10px 14px;
margin:0px;
}

div.greenbutton#cancelBtn {
	background-color: #e5e5e5;
}
div.greeButton#cancelBtn:hover {
	background-color: #7e7e7e;
}
.fieldcontainer {
    display: block;
    position: relative;
    height: 95%;
    overflow: auto;
margin: 0 auto;
padding: 0px 20px;
}

.ot-s-btn .btnContent{
	  position: relative;
	  top:-86px;
	  left:0px;
	}
.ot-login {
	padding: 22px 0px;
}
button.login {
    width: 400px;
    position: relative;
    height: 50px;
    border-radius: 10px;
    border:none;
    background-color: transparent;
    color: #fff;
    margin-top: 38px;
}

.loginField {
    margin: 25px 0px 25px 0px;
    height: 120px;
}

h3 {
    font-size: 24px;
    font-weight: 300;
}
</style>
