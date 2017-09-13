<template id='confirm_overlay'>
	<olnpane layerid=9>
		<div slot='ol-title'><h3>{{statement}}</h3></div>
		<div slot='ol-form'>
				<div class='loginField'>
						<span>{{question}}</span>
				</div>
				<div class='float-r'>
						<button class="kg-btn-secondary" v-on:click="cancel_clicked" id="cancelBtn">NO</button>
						<button class="kg-btn-primary" id="saveObjButton" v-on:click="ok_clicked">YES</button>
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
.kgl-login {
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
 border:1px solid #0075bc;
 width: 100%;
 position: absolute;
 height: 100%;
 text-align:center;
 background-color:#fff;
 color:#0075bc;
 transition: all 0.8s ease;
}
button.login:hover {
 background-color:#0075bc;
 color:#fff;

}
button.login.cancel {
	border:none;
}
.fieldcontainer {
    display: block;
    position: relative;
    height: 95%;
    overflow: auto;
margin: 0 auto;
padding: 0px 20px;
}

.kgl-login {
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
