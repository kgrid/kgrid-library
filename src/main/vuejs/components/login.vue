<template id='login_overlay'>
	<olnpane layerid=0>
		<div slot='ol-title'><h3>Log in</h3></div>
		<div slot='ol-form'>
		   <form @submit.prevent="validateLoginForm" data-vv-scope="loginform" autocomplete='on'>
			<fieldset class='fieldcontainer' id='first'>
			<div class='loginField'>
               <label class="label">Username</label>
                <p class="control has-icon has-icon-right">
                    <input spellcheck=false v-model='userModel.user.username' name="email" v-validate data-vv-delay="1000" data-vv-rules="required|email" :class="{'input': true, 'is-danger': errors.has('email', 'loginform') }" type="text" placeholder="Enter your email">
                    <span v-show="errors.has('email', 'loginform')" class="help is-danger">{{ errors.first('email', 'loginform') }}</span>
                </p>
				</div>
				<div class='loginField'>
				 <label class="label">Password</label>
                 <p class="control has-icon has-icon-right">
                     <input spellcheck=false  v-model='userModel.user.password' name="password" v-validate data-vv-delay="800" data-vv-rules="required|min:4" :class="{'input': true, 'is-danger': errors.has('password', 'form-1') }" type="password" placeholder="Enter your password">
                     <span v-show="errors.has('password', 'loginform')" class="help is-danger">{{ errors.first('password', 'loginform') }}</span>
                 </p>
				</div>
				<div class='loginField'>
					<label class="label"></label>
				  <div class='btnContent'>
					<transition name='fade' mode='out-in'>
						<button key='0' class='login' type='submit' v-if='status.ready'>LOG IN</button>
						<div key='1' class='spinner ol-processing  login pad-t-15' v-if='status.processing'>Logging in ......</div>
						<div  key='2' class='ol-success login pad-t-15' v-if='status.success'>Login Successful<i class='fa fa-check'></i></div>
						<div  key='3' class='ol-error login  pad-t-15' v-if='status.error'>Unable to log in. Please check credentials.</div>
						</transition>
					</div>
				</div>
				</fieldset>
				</form>
		</div>
	</olnpane>
</template>
<script>
import olnpane from '../components/olnpane';
import eventBus from '../components/eventBus.js';
export default {
  name: 'login',
  data: function () {
    return {
      userModel: {user: {username: '', password: ''}},
      test: true,
			status:{'ready':true,'processing':false,'success':false,'error':false}
    };
  },
  components: {
    olnpane
  },
  methods: {
	  validateLoginForm(e) {
          this.$validator.validateAll('loginform');
          if (!this.errors.any('loginform')) {
	            this.userlogin()
	        }
      },
	  validateBeforeSubmit(e) {
	        this.$validator.validateAll();
	        if (!this.errors.any()) {
	            this.userlogin()
	        }
	      },
		setButton: function(div, bool){
			this.status[div]=bool;
		},
    userlogin: function () {
      var self = this;  // eslint-disable-line
      if (this.test) {
				this.status={'ready':false,'processing':true,'success':false,'error':false}
				setTimeout(function(){
        $.ajax({  // eslint-disable-line
          type: 'POST',
          url: 'login',
          data: self.userModel.user,
          dataType: 'json',
          success: function (response) {
						self.status={'ready':false,'processing':false,'success':true,'error':false}
						self.$store.commit('setuser',response);
 						setTimeout(function(){  // eslint-disable-line
							eventBus.$emit('userloggedin',response);
            }, 1500);
          },
          error: function (response) {
						self.status={'ready':false,'processing':false,'success':false,'error':true};
						setTimeout(function(){
								self.status={'ready':true,'processing':false,'success':false,'error':false}
						},3000)
          }
        });}, 1500);
      } else {
      }
    }
  }
};
</script>
<style scoped>
.ol-success {
    color: #fff;
    background-color: #00b5af;
    border-color: #00b5af;

}

.ol-processing{
	  border-color: #0075bc;
		    background-color:#0075bc;
				color:#fff;


}
.ol-error {
    color: #fff;
    background-color: #a02816;
    border-color: #a02816;

}

p.control {
	position: absolute;
	margin: 2px 0;
}
.loginField input[type=text], .loginField input[type=password] {
    width: 400px;
    height: 50px;
}
.loginField label {
	position: relative;
	color: #666666;
	font-weight: 400;
	font-size: 12px;
line-height: 1.6em;
padding: 10px 0px;
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
.fade-enter-active,
.fade-leave-active {
  transition: opacity 1s
}

.fade-enter,
.fade-leave-to
/* .fade-leave-active in <2.1.8 */

{
  opacity: 0
}

.fieldcontainer {
    display: block;
    position: relative;
    height: 95%;
    overflow: auto;
margin: 0 auto;
padding: 0px 0px;
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
    border: 1px solid #666666;
    border-radius: 0px;
    margin: 2px 0;
    font-size: 14px;
    color: #666666;
    font-weight: 400;
}
</style>
