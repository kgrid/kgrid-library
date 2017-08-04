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

					  <div class='btnContent'>
						 		<button class='login'  @click="ok_clicked">OK</button></div>
						</div>
				</div>
				<div class='loginField'>
						<div class='ot-s-btn ot-login'>

							<div class='btnContent'>
								<button class='login cancel' @click="cancel_clicked">Cancel</button></div>
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
.btnContent{
position: relative;
top:20px;
width:400px;
	 height:54px;
		 border-radius: 0px;
 }
.ot-login {
 padding: 22px 0px;
}
div.login {
	 width: 100%;
	 position: absolute;
	 height: 100%;
	 text-align:center;
	 transition: opacity 0.2s ease;
}
button.login {
 border:1px solid #0057bc;
 width: 100%;
 position: absolute;
 height: 100%;
 text-align:center;
 background-color:#fff;
 color:#0057bc;
 transition: all 0.8s ease;
}
button.login:hover {
 background-color:#0057bc;
 color:#fff;

}
button.login.cancel {
	border:none;
	background-color:#e5e5e5;
	color:#555555;
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
.loginField {
    margin: 25px 0px 25px 0px;
    height: 120px;
}

h3 {
    font-size: 24px;
    font-weight: 300;
}
</style>
