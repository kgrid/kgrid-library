<template id='login_overlay'>
	<olnpane layerid=0>
		<div slot='ol-title'><h3>Log in</h3></div>
		<div slot='ol-form'>
			<fieldset class='fieldcontainer' id='first'>
								<div class='loginField'>
									<h4 title='username'>USERNAME</h4>
									<div class='addtext'>
										<input class='textbox login' type='text' v-model='userModel.user.username'  spellcheck='false'
											placeholder='Enter your email' autofocus
											maxlength='140'>
<!-- 											<button class='edit_btn' >Need to create an account?</button>
 -->									</div>
									<label class='errorLabel' for='username'> </label>
								</div>
								<div class='loginField'>
									<h4 title='password'>PASSWORD</h4>
									<div class='addtext'>
										<input class='textbox login' v-model='userModel.user.password'
											type='password' spellcheck='false'
											placeholder='Enter your password'  @keyup.enter='userlogin'
											maxlength='140'>
											<!-- <button class='edit_btn'>Forgot Password?</button> -->
									</div>
									<label class='errorLabel' for='password'> </label>
								</div>
								<div class='loginField'>
									<button class='login' @click='userlogin'>LOG IN</button>
								</div>
				</fieldset>
		</div>
		<div slot='ol-processing'>Log in ...</div>
		<div slot='ol-success'>Login Successful !!!</div>
		<div slot='ol-failure'>Unable to login. Please check your username and password! </div>
		<div slot='ol-warning'>Warning !!!</div>
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
      role: 'default',
      test: true
    };
  },
  components: {
    olnpane
  },
  methods: {
    userlogin: function () {
      var self = this;  // eslint-disable-line
      if (this.test) {
        $( 'div.processing' ).fadeIn( 300 );  // eslint-disable-line
        $.ajax({  // eslint-disable-line
          type: 'POST',
          url: '/ObjectTeller/login',
          data: self.userModel.user,
          dataType: 'json',
          success: function (response) {
            $( 'div.processing' ).fadeOut( 200 );  // eslint-disable-line
            $('div.success').fadeIn(300).delay(500).fadeOut(400, function(){  // eslint-disable-line
           	$.extend(true, self.userModel.user, response);
            eventBus.$emit('userloggedin', self.userModel.user);  // eslint-disable-line
            });
          },
          error: function (response) {
							$( 'div.processing' ).fadeOut( 200 );  // eslint-disable-line
							$( 'div.failure' ).fadeIn( 300 ).delay( 500 ).fadeOut( 400 ); // eslint-disable-line
          }
        });
      } else {
      }
    }
  }
};
</script>
<style>
.fieldcontainer {
    display: block;
    position: relative;
    height: 95%;
    overflow: auto;
}
button.login {
    width: 350px;
    position: relative;
    height: 53px;
    border-radius: 10px;
    border:1px solid #39b45a;
    background-color: #39b45a;
    color: #fff;
    margin-top: 38px;
}
.entryform {
    height: 600px;
    padding: 16px 22px;
    margin-top: 0px;
}
.loginField {
    margin: 15px 0px 15px 0px;
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
button.login:hover {
	border:1px solid #e6e6e6;
    background-color: #fff;
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
