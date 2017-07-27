<template id='navbar'>
		<div class='ot-nav'>
			<router-link class='navbar-brand ot-1' to='/'>
			</router-link>
			<nav class='navbar navbar-fixed-top ot-1 kg-bg-color kg-color'>
				<div class='wrapper'>
					<div class='datagrid'>
						<ul class='nav navbar-nav middleout'>
							<router-link tag='li' :class="{'active': $route.fullPath === '/about'}" to='/about'><a><span>About</span></a></router-link>
							<router-link tag='li' :class="{'active': $route.fullPath === '/faq'}" to='/faq'><a><span>FAQ</span></a></router-link>
							<router-link tag='li' :class="{'active': $route.fullPath === '/contactus'}" to='/contactus'><a><span>Contact Us</span></a></router-link>
						</ul>
					</div>
				</div>
				<div class='login-wrapper' >
					<ul class='nav navbar-nav navbar-right ot-1 middleout' v-if='!isLoggedIn'>
						<li class='login-link' v-on:click='login_click'><a><span>Log In</span></a></li>
						<li class='signup-link' v-show='false'><router-link id="signupBtn" to='/soon'>Sign Up</router-link></li>
					</ul>
					<ul class='nav navbar-nav navbar-right ot-1'  v-else  v-click-outside='outside'>
						<li class='login-link1'>
							<div class='dropdown' id="userDropdown" style='height:55px;'>
								<a id='dLabel' data-target='#' v-on:click='toggleDropdown'>
								<div class='row'>
								<div class='col-md-11'>
									<div class='row pad-t-2 pad-b-5 lh-1'>
										<span class='float-r'>Welcome, {{firstname}}</span></div>
									<div class='row pad-b-5 lh-1'>
									<span class='float-r ft-sz-12'>{{libraryname}}</span></div>
									</div>
									<div class='col-md-1 pad-0'>
									<span><img id='dropdowniconimg' class='down' src='../assets/images/Chevron.png' width='12px' /></span>
									</div>
									</div>
								</a>
								<ul class='dropdown-menu' v-if='showDropdown'>
									<li><a id='logoutBtn' v-on:click='userlogout'><span>Logout</span></a></li>
								</ul>
							</div>
						</li>
					</ul>
				</div>
			</nav>
		</div>
</template>

<script>
import eventBus from '../components/eventBus.js'

export default {
  name: 'navbar',
  data: function () {
    return {
 			showDropdown : false
    };
  },
  created: function() {
	  this.showDropdown=false;
  },
  computed: {
		firstname: function(){
			return (this.$store.state.currentUser.first_name || "")
		},
    isLoggedIn: function () {
      return this.$store.getters.isLoggedIn;
    },
		libraryname : function(){
			return this.$store.state.libraryname;
		}
  },
  methods: {
		outside: function(){
			if(this.showDropdown){
				this.toggleDropdown();
			}
		},
    login_click: function () {
      eventBus.$emit('openLogin'); // eslint-disable-line
    },
    toggleDropdown: function () {
    	this.showDropdown=!this.showDropdown;
    	if(this.showDropdown){
    	      $('img#dropdowniconimg').removeClass('down');  // eslint-disable-line
    	      $('img#dropdowniconimg').addClass('up');  // eslint-disable-line
    	}else
    	{
    	      $('img#dropdowniconimg').removeClass('up');  // eslint-disable-line
    	      $('img#dropdowniconimg').addClass('down');  // eslint-disable-line
    	}
    	},
    userlogout: function () {
      var self = this;
      this.showDropdown=false;
      $.ajax({ // eslint-disable-line
        type: 'POST',
        url: 'logout',
        success: function (response) {
          self.showDropdown = false;
					self.$store.commit('setuser',{username: '', password: ''});
        },
				error:function(response){
					console.log(response);
				}
      });
    }
  }
};
</script>

<!-- Add 'scoped' attribute to limit CSS to this component only -->
<style scoped>
.ot-1 .navbar-nav>li>a, #dLabel{
	font-size:14px;
	font-weight: 400;
	line-height: 2.6em;
  letter-spacing: 0.05em;
	background-color: transparent;
  background-image: none;
	position: relative;
  display: block;
  padding: 4px 0px 4px 0px;
  margin: 7px 0px;
}
.ot-nav {
	z-index:50;
}

.navbar-brand{
	background-color:#fff;
	position:fixed;
	padding: 0px 30px;
	z-index:400;
	top:0;
	width:200px;
	left:60px;
	height:75px;
	border:1px solid #e5e5e5;
}

.ot-1 .navbar-right {
	margin-right: 0px;
}

#signupBtn {
	border: 1px solid #e3e3e3;
    border-radius: 10px;
    color: #fff;
	width: 125px;
	text-align: center;
}


#dLabel span {
	padding: 0px 5px;
}
.login-link1 .dropdown{
	height:55px;
}

img#dropdowniconimg {
	transition: transform 0.5s linear;
}
img#dropdowniconimg.up {
-moz-transform: scaleY(-1);
-o-transform: scaleY(-1);
-webkit-transform: scaleY(-1);
transform: scaleY(-1);
}

img#dropdowniconimg.down {
-moz-transform: scaleY(1);
-o-transform: scaleY(1);
-webkit-transform: scaleY(1);
transform: scaleY(1);
}

.dropdown ul{
border-bottom-left-radius: 10px;
border-bottom-right-radius: 10px;
transition: all .5s ease;
padding: 0px;
left: initial;
right: -18px;
margin-top: -12px;
box-shadow: none;
border-radius: 0px;
display:block;
min-width:200px;
}

.dropdown ul li {
    height: 2.4em;
    line-height: 2.4em;
    text-align: left;
		display: table-cell;
}
.dropdown-menu > li > a:hover, .dropdown-menu > li > a:focus {
    background-color: #e8e8e8;
}
#logoutBtn {
    line-height: 2.4em;
    text-decoration: none;
    border: none;
    cursor: pointer;
    font-size: 14px;
    font-style: normal;
    font-weight: 400;
min-width: 200px;
}
</style>
