<template id='login_overlay'>
	<olpane layerid=0 left=540>
		<div slot='oltitle'><h3>Sign In</h3></div>
		<div slot='ol-form'>
		   <form @submit.prevent="validateLoginForm" data-vv-scope="loginform" autocomplete='on'>
			<fieldset class='fieldcontainer' id='first'>
			<div class='loginField'>
               <label class="ft-sz-12 label text-cap">Username</label>
                <p class="control has-icon has-icon-right">
                    <input spellcheck=false v-model='userModel.user.username' name="email" ref='email'  type="text" placeholder="Enter your email">
                    <!-- <span v-show="errors.has('email', 'loginform')" class="help is-danger">{{ errors.first('email', 'loginform') }}</span> -->
                </p>
				</div>
				<div class='loginField'>
				 <label class="ft-sz-12 label text-cap">Password</label>
                 <p class="control has-icon has-icon-right">
                     <input spellcheck=false  v-model='userModel.user.password' name="password" type="password" placeholder="Enter your password">
                     <!-- <span v-show="errors.has('password', 'loginform')" class="help is-danger">{{ errors.first('password', 'loginform') }}</span> -->
                 </p>
				</div>
				<div class='loginField'>
					<label class="label"></label>
				  <div class='btnContent'>
					<transition name='fade' mode='out-in'>
						<button key='0' class='login' type='submit' v-if='status.ready'>Sign In</button>
						<div key='1' class='saving ol-processing login pad-t-15' v-if='status.processing'>Signing in <span>.</span><span>.</span><span>.</span></div>
						<div  key='2' class='ol-success login pad-t-15' v-if='status.success'>Sign in Successful<i class='fa fa-check'></i></div>
						<div  key='3' class='ol-error login  pad-t-15' v-if='status.error'>Unable to sign in. Please check credentials.</div>
						</transition>
					</div>
				</div>
				</fieldset>
				</form>
		</div>
	</olpane>
</template>
<script>
import olpane from '../components/olpane';
export default {
  name: 'login',
  data: function () {
    return {
      userModel: {user: {username: '', password: ''}},
      skipAuth: false,
			status:{'ready':true,'processing':false,'success':false,'error':false},
      errors:{}
    };
  },
  components: {
		olpane
  },
	mounted:function(){
		this.$refs.email.focus()
	},
  methods: {
	  // validateLoginForm(e) {
    //       this.$validator.validateAll('loginform');
    //       if (!this.errors.any('loginform')) {
	  //           this.userlogin()
	  //       }
    //   },
	  // validateBeforeSubmit(e) {
	  //       this.$validator.validateAll();
	  //       if (!this.errors.any()) {
	  //           this.userlogin()
	  //       }
	  //     },
		setButton: function(div, bool){
			this.status[div]=bool;
		},
    userlogin: function () {
      var self = this;  // eslint-disable-line
      if (!this.skipAuth) {
				this.status={'ready':false,'processing':true,'success':false,'error':false}
				this.$store.dispatch('login',this.userModel.user).then(function(response){
					self.status={'ready':false,'processing':false,'success':true,'error':false}
					self.status.success=self.$store.getters.isLoggedIn;
					self.status.error=!self.$store.getters.isLoggedIn;

					if(self.status.success){
						setTimeout(function(){  // eslint-disable-line
							self.$store.commit('showprivate',true);
							self.$eventBus.$emit('userloggedin',response);
							}, 1500);
					}else{
						setTimeout(function(){
								self.status={'ready':true,'processing':false,'success':false,'error':false}
								},2000)
					}
				})
      } else {
				this.status={'ready':false,'processing':true,'success':false,'error':false}

				setTimeout(function(){
						self.status={'ready':false,'processing':false,'success':true,'error':false}
						},2000)
				setTimeout(function(){  // eslint-disable-line
					self.$store.commit('setuser', {username: 'GUEST', first_name:'KGrid User', password: '0000'})
					self.$store.commit('showprivate',true);
					self.$eventBus.$emit('userloggedin',{user: {username: 'GUEST', password: '0000'}});
					}, 1500);
      }
    }
  }
};
</script>
<style scoped>
.ol-success {
    color: #fff;
    background-color: #0075bc;
    border-color: #0075bc;
}
.ol-processing{
	  border-color: #0075bc;
		    background-color:#0075bc;
				color:#fff;
}
.ol-error {
    color: #fff;
    background-color: #bc2526;
    border-color: #bc2526;
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
	left: 0px;
	top: 62px;
font-style: italic;
font-size: 12px;
color: #bc2526;
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
		border-style:none;
	}
 .btnContent{
 position: relative;
 top:20px;
 width:400px;
		height:54px;
	    border-radius: 0px;
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
h3 {
    font-size: 32px;
    font-weight: 500;
}
input[type=text], input[type=password], input[type=textarea] {
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
.saving span {
		/**
		 * Use the blink animation, which is defined above
		 */
		animation-name: blink;
		/**
		 * The animation should take 1.4 seconds
		 */
		animation-duration: 1.4s;
		/**
		 * It will repeat itself forever
		 */
		animation-iteration-count: infinite;
		/**
		 * This makes sure that the starting style (opacity: .2)
		 * of the animation is applied before the animation starts.
		 * Otherwise we would see a short flash or would have
		 * to set the default styling of the dots to the same
		 * as the animation. Same applies for the ending styles.
		 */
		animation-fill-mode: both;
}
.saving span:nth-child(2) {
		/**
		 * Starts the animation of the third dot
		 * with a delay of .2s, otherwise all dots
		 * would animate at the same time
		 */
		animation-delay: .2s;
}
.saving span:nth-child(3) {
		/**
		 * Starts the animation of the third dot
		 * with a delay of .4s, otherwise all dots
		 * would animate at the same time
		 */
		animation-delay: .4s;
}
.input.is-danger {
	border:1px solid #bc2526;
}
</style>
