<template id='navbar'>
		<div class='kgl-nav'>
			<router-link class='navbar-brand kgl-1' to='/' v-if='layoutop==0'>
			<svg id="Layer_1" width="45" height="45" data-name="Layer 1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 40.32 27.4"><defs><svg:style>.cls-1{fill:#0075bc;}</svg:style></defs><title>Artboard 3</title><path class="cls-1" d="M8.54,11l-3,2a.45.45,0,0,0,0,.74l5.66,3.78a.57.57,0,0,1,0,1L9.42,19.78a.81.81,0,0,1-.91,0L.22,14.14a.49.49,0,0,1,0-.82L8.51,7.68a.81.81,0,0,1,.91,0l5.74,3.91a.81.81,0,0,1,.35.71L15.43,14a.59.59,0,0,1-.92.46L9.44,11A.81.81,0,0,0,8.54,11Z"/><path class="cls-1" d="M29,21.66,20.8,27.21a1.08,1.08,0,0,1-1.22,0l-8.16-5.55a.48.48,0,0,1,0-.79l1.89-1.29a.93.93,0,0,1,1,0l5.34,3.56a.81.81,0,0,0,.89,0l3-2a.45.45,0,0,0,0-.75l-3.45-2.31a.81.81,0,0,1-.36-.7l0-1.22a.68.68,0,0,1,.72-.66l.44,0a.44.44,0,0,1,.22.08L29,20.87A.48.48,0,0,1,29,21.66Z"/><path class="cls-1" d="M20.27,11.17l.3-7.23a.51.51,0,0,0-.8-.45L15.84,6.17a.81.81,0,0,0-.35.63l-.07,1.37a.63.63,0,0,1-1,.49L11.38,6.56a.45.45,0,0,1,0-.74L19.73.14a.81.81,0,0,1,.91,0l8.29,5.64a.49.49,0,0,1,0,.82l-7.59,5.17A.69.69,0,0,1,20.27,11.17Z"/><path class="cls-1" d="M40.07,14.2l-8.21,5.59a.81.81,0,0,1-.91,0l-3.58-2.44a.62.62,0,0,1,.38-1.13l3.15.15a.81.81,0,0,0,.48-.13l3.56-2.31a.45.45,0,0,0-.22-.82l-7.13-.43-1.81-.11a.63.63,0,0,1-.32-1.16L31,7.66a.77.77,0,0,1,.87,0l8.23,5.6A.56.56,0,0,1,40.07,14.2Z"/></svg>
			<div class='logotext'>Library</div>
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
						<li class='login-link' v-on:click='login_click'><a><span>Sign In</span></a></li>
						<li class='signup-link' v-show='false'><<a href="mailto:dlhs.knowledge.grid@umich.edu?Subject=Sign%20Up">Sign Up</a></li>
					</ul>
					<ul class='nav navbar-nav navbar-right kgl-1'  v-else  v-click-outside='outside'>
						<li class='login-link1'>
							<div class='dropdown' id="userDropdown" >
								<a data-target='#'>
									<div class='row pad-t-2 pad-b-5 lh-1'>
										<span class='float-r'>Welcome, {{firstname}}</span>
									</div>
									<div class='row pad-b-5 float-r lh-1'>
										<span class=' ft-sz-12 kg-fg-color' v-on:mouseenter='trigDropdown' v-on:mouseleave='checkDropdown'>{{libraryname}}</span>
										<i id='dropdowniconimg' class='fa fa-caret-down kg-fg-color down'></i>
									</div>

								</a>
								<ul class='dropdown-menu' v-if='showDropdown' v-on:mouseleave='leaveDropdown'>
									<li v-if='isAdmin'><a id='adduserBtn'  v-on:click='userlink_click'><span>Add User to Library</span></a></li>
									<li><a id='logoutBtn' v-on:click='userlogout'><span>Sign Out</span></a></li>
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
 			showDropdown : false,
			layoutop:0
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
			return this.$store.getters.getLibraryName;
		},
		isAdmin: function(){
			return this.$store.getters.isAdmin;
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
		trigDropdown: function(){
				this.showDropdown=true;
		},
		checkDropdown: function(){
				this.showDropdown=true;
		},
		leaveDropdown:function(){
				this.showDropdown=false;
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
div.logotext {
position:absolute;
	color: #0075bc;
	font-size: 22px;
	font-weight:700;
	display:inline;
	padding-left: 12px;
	line-height: 40px;
	vertical-align:middle;
}
.kgl-1 .navbar-nav>li a {
	font-size:14px;
	font-weight: 400;
	line-height: 2.7em;
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
.kgl-1 {
	z-index: 250;
}

.kgl-1 .navbar-nav>li{
    margin: 0px 15px;
}
.navbar-brand{
	background-color:#fff;
	position:fixed;
	margin: 1px 0px;
	padding: 6px 30px;
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
	margin-top: 10px;
	box-shadow: none;
	border-radius: 0px;
	display:block;
	min-width:200px;
}

#userDropdown.dropdown ul li {
    height: 3.5em;
    line-height: 2.8em;
    text-align: left;
		display: list-item;
		margin:0;
		transition: all 0.5s ease;
		background-color:#fff;
}
#userDropdown.dropdown ul li > a {
    padding: 10px 20px;
		text-decoration: none;
    cursor: pointer;
		background-color:transparent;
		margin:0;
	}

#userDropdown.dropdown ul li:hover {
	background-color:#e5e5e5;
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
		padding:10px 35px;
		border:1px solid #0075bc;
		background-color:#fff;
		color: #0075bc;
		list-style:none;
		margin: 10px 0px;
		transition: background-color 0.5s ease, color 0.5s ease;
	}
.login-link span{
	background-color:transparent;
}
.login-link:hover {
		background-color:#0075bc;
		color: #fff;

}
.login-link:hover span{
	color:#fff;
}
</style>
