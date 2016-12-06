
<template id='navbar'>
		<div class='ot-nav'>
			<a class='navbar-brand ot-1 .router-link-active' href='index.html'>
				<img alt='Brand' src='../assets/logo.png'>
			</a>
			<nav class='navbar navbar-default navbar-fixed-top ot-1'>
				<div class='container-fluid ot-1'>
					<div class='navbar-header ot-1'>
						<button type='button' class='navbar-toggle collapsed ot-1'
							data-toggle='collapse'
							data-target='#bs-example-navbar-collapse-1' aria-expanded='false'>
							<span class='sr-only'>Toggle navigation</span> <span
								class='icon-bar'></span> <span class='icon-bar'></span> <span
								class='icon-bar'></span>
						</button>
					</div>
					<div class='collapse navbar-collapse ot-1'
						id='bs-example-navbar-collapse-1'>
						<ul class='nav navbar-nav'>
							<li><a href='static/about.html'>About</a></li>
							<li><a href='static/faq.html'>FAQ</a></li>
							<li><a href='static/contactus.html'>Contact Us</a></li>
						</ul>
						<div v-if='!isLoggedIn'>
						<ul class='nav navbar-nav navbar-right ot-1'>
							<li class='login-link' v-on:click='login_click'><a>Log In</a></li>
							<li class='signup-link'><a id='signupBtn'>Sign Up</a></li>
						</ul>
						</div>
						<div v-else>
							<ul class='nav navbar-nav navbar-right ot-1'>
								<li class='login-link1'>
									<div class='dropdown' id="userDropdown" style='height:55px;'>
										<a id='dLabel' data-target='#' data-toggle='dropdown' role='button'>Hello,
											{{userModel.user.first_name}}<span><img
											id='dropdowniconimg' class='down'
											src='../assets/Chevron_Icon.png' width='12px' /></span></a>
										<ul class='dropdown-menu' aria-labelledby='dLabel'>
											<li><a id='logoutBtn' v-on:click='userlogout'>Logout</a></li>
										</ul>
									</div>
								</li>
							</ul>
						</div>
					</div>
					<!-- /.navbar-collapse -->
				</div>
				<!-- /.container-fluid -->
			</nav>
		</div>

</template>

<script>
import eventBus from '../components/eventBus.js'
import {getCurrentUser, overlayHeightResize, retrieveObject, retrieveObjectList} from '../ot.js';
import { userModel } from '../components/models.js'

export default {
  name: 'navbar',
  data: function () {
    return {
      userModel: {user: {username: '', password: '', id: -1, first_name: '', last_name: '', role: ''}}
    };
  },
  created: function() {
  	var self = this;
  	 eventBus.$on('userloggedin',function(obj){
    	console.log(obj);
		$.extend(true, self.userModel.user, obj);
	});
  }, 
  mounted: function () {
    $('#userDropdown').on('show.bs.dropdown', function (e) { // eslint-disable-line
		console.log('dropdown shown..');
      var target = $(e.target).attr('id');  // eslint-disable-line
      $('img#dropdowniconimg').removeClass('down');  // eslint-disable-line
      $('img#dropdowniconimg').addClass('up');  // eslint-disable-line
    });
    $('#userDropdown').on('hide.bs.dropdown', function(e){ // eslint-disable-line
    console.log('doprdown hidden');
      var target = $(e.target).attr('id');  // eslint-disable-line
      $('img#dropdowniconimg').removeClass('up');  // eslint-disable-line
      $('img#dropdowniconimg').addClass('down');  // eslint-disable-line
    });
  },
  computed: {
    isLoggedIn: function () {
      var loggedin = false;
      loggedin = (this.userModel.user.username !== '');
      return loggedin;
    }
  },
  methods: {
    login_click: function () {
      eventBus.$emit('openOverlay'); // eslint-disable-line
    },
    userlogout: function () {
      var self = this;
      $.ajax({ // eslint-disable-line
        type: 'POST',
        url: '/ObjectTeller/logout',
        success: function (response) {
          $.extend(true, self.userModel.user, {username: '', password: ''}); // eslint-disable-line
        }
      });
    }
  }
};
</script>

<!-- Add 'scoped' attribute to limit CSS to this component only -->
<style scoped>
.ot-1 {
	background-color: #39b45a;
	color:#fff;
	z-index: 50;
}

.navbar-fixed-top.ot-1{
	background-image: none;
	background-color: #39b45a;
	height:60px;
	margin-bottom:15px;
}
.ot-1 .navbar-nav>li>a {
	color: #fff;
	font-size:14px;
	font-weight: 700;
	line-height: 1.8;
    letter-spacing: 0.05em;
        margin: 9px;
            padding: 8px;

}

.ot-nav {
	z-index:50;
}
.ot-1 .navbar-nav>li a:hover {
    color: #fff;
    background-color: transparent;
    background-image: none;
}

.ot-1 .navbar-collapse {
	padding: 0px 0px 0px 220px;
}

.ot-1 .navbar-nav>li a:focus {
    color: #fff;
}

.ot-1 .navbar-toggle .icon-bar {
    background-color: #fff;
}

.ot-1 .navbar-toggle:hover {
    background-color: #39b450;
}

.navbar-brand.ot-1{
	background-color:transparent;
	position:fixed;
	padding: 0px 30px;
	z-index:200;
	top:0;
	left:0;
}

.ot-1 .navbar-right {
	margin-right: 0px;
}

#signupBtn {
	border: 1px solid #e3e3e3;
    border-radius: 10px;
    color: #e3e3e3;
}

#dLabel{
    line-height: 3.4em;
    margin: 25px 30px 0px 30px;
    text-decoration: none;
    color: #fff;
    border: none;
    Padding: 18px;
    padding-top: 10px;
    Padding-left: 5px;
    Padding-bottom: 10px;
    Padding-right: 5px;
    cursor: pointer;
    font-size: 14px;
    font-style: normal;
    font-weight: bold;
		background-color: transparent;
}

#dLabel span {
	padding: 0px 5px;
}
.login-link1 .dropdown{
	height:55px;
}
.dropdown-menu>li> a {
	color:#fff;
}

img#dropdowniconimg.up {
-moz-transform: scaleY(1);
-o-transform: scaleY(1);
-webkit-transform: scaleY(1);
transform: scaleY(1);
}

img#dropdowniconimg.down {
-moz-transform: scaleY(-1);
-o-transform: scaleY(-1);
-webkit-transform: scaleY(-1);
transform: scaleY(-1);
}

.dropdown ul{
background-color:#39b45a;
}

.dropdown a{
color: #fff;
}

#logoutBtn {
    line-height: 3.4em;
    text-decoration: none;
    color: #fff;
    border: none;
    cursor: pointer;
    font-size: 14px;
    font-style: normal;
    font-weight: bold;
}
</style>
