<template id='navbar'>
		<div class='kgl-nav'>
			<router-link class='navbar-brand kgl-1' to='/'>
						<img src='../assets/images/kg-library-logo.png' height='30px' alt='KGrid Library'/>
			</router-link>
			<nav class='navbar navbar-fixed-top kgl-1 kg-bg-color kg-color'>
				<div class='wrapper'>
					<div class='datagrid'>
						<ul class='nav navbar-nav'>
							<router-link tag='li' :class="{'active': $route.fullPath === '/about'}" to='/about'><a><span>About</span></a></router-link>
							<router-link tag='li' :class="{'active': $route.fullPath === '/faq'}" to='/faq'><a><span>FAQ</span></a></router-link>
							<router-link tag='li' :class="{'active': $route.fullPath === '/contactus'}" to='/contactus'><a><span>Contact Us</span></a></router-link>
						</ul>
					</div>
				</div>
				<div class='login-wrapper' >
					<ul class='nav navbar-right kgl-1 ' v-if='!isLoggedIn'>
						<li class='login-link' v-on:click='login_click'><a><span>Log In</span></a></li>
						<li class='signup-link' v-show='false'><<a href="mailto:dlhs.knowledge.grid@umich.edu?Subject=Sign%20Up">Sign Up</a></li>
					</ul>
					<ul class='nav navbar-nav navbar-right kgl-1'  v-else  v-click-outside='outside'>
						<li class='login-link1'>
							<div class='dropdown' id="userDropdown" style='height:55px;'>
								<a id='dLabel' data-target='#' v-on:click='toggleDropdown'>

									<div class='row pad-t-2 pad-b-5 lh-1'>
										<span class='float-r'>Welcome, {{firstname}}</span>
									</div>
									<div class='row pad-b-5 float-r lh-1'>
										<span class=' ft-sz-12 kg-fg-color'>{{libraryname}}</span>
										<i id='dropdowniconimg' class='fa fa-caret-down kg-fg-color down'></i>
									</div>

								</a>
								<ul class='dropdown-menu' v-if='showDropdown'>
									<li><a id='adduserBtn' v-on:click='userlink_click'><span>Add User to Library</span></a></li>
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
    	      $('#dropdowniconimg').removeClass('down');  // eslint-disable-line
    	      $('#dropdowniconimg').addClass('up');  // eslint-disable-line
    	}else
    	{
    	      $('#dropdowniconimg').removeClass('up');  // eslint-disable-line
    	      $('#dropdowniconimg').addClass('down');  // eslint-disable-line
    	}
    	},
			userlink_click: function () {
					eventBus.$emit('openUserManagement'); // eslint-disable-line
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
					eventBus.$emit('logout');
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
.kgl-1 .navbar-nav>li>a, #dLabel{
	font-size:14px;
	font-weight: 400;
	line-height: 2.8em;
  letter-spacing: 0.05em;
	background-color: transparent;
  background-image: none;
	position: relative;
  display: block;
  padding: 4px 0px 4px 0px;
  margin: 7px 0px;
}
.kgl-nav {
	z-index:50;
}

.navbar-brand{
	background-color:#fff;
	position:fixed;
	margin: 1px 0px;
	padding: 14px 0px;
	z-index:400;
	top:0;
	width:200px;
	left:40px;
	height:56px;
	border:none;
}



.kgl-1 .navbar-right {
	margin-right: 0px;
}

#signupBtn {
	border: 1px solid #e3e3e3;
    border-radius: 10px;
    color: #fff;
	width: 125px;
	text-align: center;
}


.login-link1 .dropdown{
	height:55px;
}

i#dropdowniconimg {
	transition: transform 0.5s linear;
}
i#dropdowniconimg.up {
-moz-transform: scaleY(-1);
-o-transform: scaleY(-1);
-webkit-transform: scaleY(-1);
transform: scaleY(-1);
}

i#dropdowniconimg.down {
-moz-transform: scaleY(1);
-o-transform: scaleY(1);
-webkit-transform: scaleY(1);
transform: scaleY(1);
}

#userDropdown.dropdown ul{
	border-bottom-left-radius: 10px;
	border-bottom-right-radius: 10px;
	transition: all .5s ease;
	padding: 0px;
	left: initial;
	right: -18px;
	margin-top: -10px;
	box-shadow: none;
	border-radius: 0px;
	display:block;
	min-width:200px;
}

#userDropdown.dropdown ul li {
    height: 2.5em;
    line-height: 2.8em;
    text-align: left;
		display: list-item;
}
.dropdown-menu > li > a:hover, .dropdown-menu > li > a:focus {
    background-color: #e8e8e8;
}
#adduserBtn, #logoutBtn {
    line-height: 2.5em;
    text-decoration: none;
    border: none;
    cursor: pointer;
    font-size: 14px;
    font-style: normal;
    font-weight: 400;
min-width: 200px;
}


.login-link {
		cursor:pointer;
		padding:5px 25px;
		border:1px solid #0075bc;
		background-color:#fff;
		color: #0075bc;
		transition:all 0.8s ease;
		list-style:none;
		margin: 15px 0px;
	}
.login-link span{
	background-color:#fff;
	color: #0075bc;
	transition:all 0.8s ease;
}
.login-link:hover,  .login-link:hover span{
		background-color:#0075bc;
		color: #fff;
}
</style>
