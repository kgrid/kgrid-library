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
						<li class='signup-link' v-show='false'><a href="mailto:dlhs.knowledge.grid@umich.edu?Subject=Sign%20Up">Sign Up</a></li>
					</ul>
					<ul class='nav navbar-nav navbar-right kgl-1' v-else >
						<li >
							<div class='row pad-t-2 pad-b-5 lh-1 mar-r-12 mar-t-10'>
								<span class='float-r'>Welcome, {{firstname}}</span>
							</div>
							<kgdropdown label='' :selectvalue='libraryname' v-on:selected='setnavselect' :optionlist='optionlist' :dstyle='ddstyle'></kgdropdown>
						</li>
					</ul>
				</div>
			</nav>
		</div>
</template>
<script>
import kgdropdown from "./kgdropdown.vue"
export default {
  name: 'navbar',
  data: function () {
    return {
 			showDropdown : false,
			layoutop:0,
			skipAuth: true,
			optionlist:[
				 	// {'label':'Add User to Library', 'value':'user','order':'desc'},
					// {'label':'Logout', 'value':'logout','order':'asc'}
					],
			ddstyle:{	"d":"height:30px;padding:5px;background-color:#fff;",
								"ul":"margin-left:10px;",
								"li":"height:3.5em; line-height:3.5em;",
								"a":"line-height: 3.5em;"
							},
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
			if(this.skipAuth){
				return 'LocalLibrary(Dev)'
			}else{
				return this.$store.getters.getLibraryName;
			}
		},
		isAdmin: function(){
			if(this.skipAuth){
				return true
			}else{
				return this.$store.getters.isAdmin;
			}
		}
  },
	components: {
		kgdropdown
	},
  methods: {
		setnavselect:function(opt){
			switch(opt.value){
				case 'user':
					this.userlink_click()
					break;
				case 'logout':
					this.userlogout()
				break;
			}
		},
    login_click: function () {
      this.$eventBus.$emit('openLogin'); // eslint-disable-line
    },
		userlink_click: function () {
					this.$eventBus.$emit('openUserManagement'); // eslint-disable-line
				},
    userlogout: function () {
      var self = this;
      this.showDropdown=false;
			this.$store.dispatch('logout').then(function () {
				self.showDropdown = false;
				self.$store.commit('showprivate',false);
				self.$eventBus.$emit('logout');
			})
    }
  }
};
</script>
<style scoped>
	.signup-link {
		cursor: default;
		color: #333333;
	}
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
