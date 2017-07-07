<template id='confirm_overlay'>
	<olnpane layerid=0>
		<div slot='ol-title'><h3>Confirmation Dialog</h3></div>
		<div slot='ol-form'>
				<div class='loginField'>
						<span>{{statement}}</span>
						<span>{{question}}</span>
									    			</div>
								<div class='loginField'>
								<div class='ot-s-btn ot-login'>
								       <div class='greenbutton' > </div>
								       <div class='btnContent'>
											 		<button class='login' @click="ok_clicked">OK</button></div>
								</div>
								<div class='ot-s-btn ot-login'>
											 <div class='greenbutton' > </div>
											 <div class='btnContent'>
													<button class='login' @click="cancel_clicked">Cancel</button></div>
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
p.control {
	position: absolute;
	margin: 12px 0;
}
.loginField input[type=text], .loginField input[type=password] {
    width: 400px;
    height: 50px;
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
p.control span.is-danger {
	position: absolute;
	left: 16px;
	top: 62px;
font-style: italic;
font-size: 12px;
color: #ec2526;


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
.entryform {
    height: 600px;
    padding: 16px 22px;
    margin-top: 0px;
}
.loginField {
    margin: 25px 0px 25px 0px;
    height: 120px;
}
fieldset h4 {
    display: inline-block;
    padding: 5px 16px;
    font-size: 14px;
    font-weight: 400;
    margin-right: 20px;
    color: #666666;
}
.addtext {
    position: relative;
    width: 100%;
    padding: 0;
    margin: 5px 0px 15px 0px;
    color: #666666;
}

h3 {
    font-size: 24px;
    font-weight: 300;
}
input[type=text], input[type=password], input[type=textarea], .addtext a {
    width: 350px;
    height: 53px;
    padding: 0px 16px;
    border: 1px solid #e6e6e6;
    border-radius: 10px;
    margin: 2px 0;
    font-size: 14px;
    color: #666666;
    font-weight: 400;
}
</style>
