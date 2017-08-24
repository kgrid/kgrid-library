<template>
  <olpane layerid=0>
	<div slot="ol-title"><h3>Library Users</h3></div>
	  <div slot="ol-form">
		<div class="row addtext maxheight" >
		  <div class='col-md-6 maxheight'>
			<div class='kgl-sub'>USERS IN THIS LIBRARY</div>
			<div class='uListContainer'>
			  <div id='uList'>
		        <ul>
		          <li v-for='(user,index) in umodel.userList' v-bind:key='index'><usercard :user='user' :you='curUserModel.user'
		      						:tileindex='index' v-bind:class="{ active: selectedUserModel.user.username === user.username }" v-on:remove='orderedList.splice(index, 1)'></usercard></li>
		        </ul>

		      </div>
		      <div id='emptyuser' @click='adduser'>
          <div class='kg-roundbtn kgl-newuser middleout'>
            <a><span class='kg-fg-color'>Add New User</span></a>
            <div class='btnContent'><i class='fa fa-plus kg-fg-color'></i></div>
          </div></div>
		      </div>
		    </div>
	        <div class='col-md-6 maxheight userentry'>
			    <div class='kgl-sub'>BASIC INFORMATION</div>
			    <form @submit.prevent="validateuserform" data-vv-scope="userform" autocomplete='off'>
					<fieldset class='fieldcontainer maxheight' id='first'>
					<div class='loginField'>
						<label class="label">ROLE</label>
						<p class="control has-icon has-icon-right">
							<select class="userEdit" name="role" v-validate data-vv-delay="1000" data-vv-rules="required" v-model='userModel.user.role'>
								<option value="ADMIN">ADMIN</option>
								<option value="INFORMATICIAN">INFORMATICIAN</option>
								<option value="USER">USER</option>
								</select>

							<span v-show="errors.has('role', 'userform')" class="help is-danger">{{ errors.first('role', 'userform') }}</span>
						</p>
					</div>
					<div class='loginField'>
						<label class="label">FIRST NAME</label>
						<p class="control has-icon has-icon-right">
							<input spellcheck=false v-model='userModel.user.first_name' name="first name" v-validate data-vv-delay="1000" data-vv-rules="required" :class="{'input': true, 'is-danger': errors.has('first name', 'userform') }" type="text" placeholder="Your firstname">
						<span v-show="errors.has('first name', 'userform')" class="help is-danger">{{ errors.first('first name', 'userform') }}</span>
					</p>
				</div>
				<div class='loginField'>
				<label class="label">LAST NAME</label>
				<p class="control has-icon has-icon-right">
					<input spellcheck=false v-model='userModel.user.last_name' name="last name" v-validate data-vv-delay="1000" data-vv-rules="required" :class="{'input': true, 'is-danger': errors.has('last name', 'userform') }" type="text" placeholder="Your lastname">
					<span v-show="errors.has('last name', 'userform')" class="help is-danger">{{ errors.first('last name', 'userform') }}</span>
				</p>
			</div>
						<div class='loginField'>
							<label class="label">EMAIL</label>
							<p class="control has-icon has-icon-right">
								<input spellcheck=false v-model='userModel.user.username' autocomplete='off' name="email" v-validate data-vv-delay="1000" data-vv-rules="required|email" :class="{'input': true, 'is-danger': errors.has('email', 'userform') }" type="text" placeholder="Email">
								<span v-show="errors.has('email', 'userform')" class="help is-danger">{{ errors.first('email', 'userform') }}</span>
							</p>
						</div>
						<div class='loginField'>
							<label class="label">PASSWORD</label>
							<p class="control has-icon has-icon-right">
								<input spellcheck=false  v-model='userModel.user.password' autocomplete='off' name="password" v-validate data-vv-delay="800" data-vv-rules="required|min:4" :class="{'input': true, 'is-danger': errors.has('password', 'form-1') }" type="password" placeholder="Password">
								<span v-show="errors.has('password', 'userform')" class="help is-danger">{{ errors.first('password', 'userform') }}</span>
							</p>
						</div>
						<div class='loginField'>
              <labeL class='label'></label>
              <button class="edit" type='button' :disabled='!inEdit' v-on:click="undoEdit">UNDO</button>
							<button class='user' v-if='isNewUser' :disabled='!inEdit' id="addUserButton" type='submit'>ADD USER</button>
							<button class='user' v-if='!isNewUser' :disabled='!inEdit' id="updateUserButton" type='submit'>UPDATE</button>
						</div>
					</fieldset>
					</form>
		   </div>
		 </div>
			</div>
		</div>
		<div slot="ol-processing"><span>Processing...</span></div>
		<div slot="ol-success"><span>User succesfully <span v-if='!isNewUser'>updated</span><span v-else>added</span>!!! </span></div>
		<div slot="ol-failure"><span>Error during <span v-if='!isNewUser'>updating</span><span v-else>adding</span> the user !</span></div>
		<div slot="ol-warning"><span>Warning !!!</span></div>
	</olpane>
</template>
	<script>
	import olpane from '../components/olpane';
	import eventBus from '../components/eventBus.js';
	import {getCurrentUser, retrieveUserList, overlayHeightResize, retrieveObject, retrieveObjectList, setenddate, setstartdate, otScroll, setBannerbkSize} from '../ot.js';
	import usercard from '../components/usercard';
	import { userModel } from '../components/models.js'
	export default {
		name:"libraryusers",
		data:function(){
			return {
			    umodel:{userList:[
			              {username: 'gqmeng@umich.edu', passwd: '', id: 2, first_name: 'George', last_name: 'Meng', role: 'Informatician'},
			              {username: 'jrampton@umich.edu', passwd: '', id: 3, first_name: 'James', last_name: 'Rampton', role: 'Designer'}
			           ]},
				userModel:{user:{username: '', passwd: '',password:'', id: -1, first_name: '', last_name: '', role: ''}},
				selectedUserModel:{user:{username: '', passwd: '', id: -1, first_name: '', last_name: '', role: ''}},
				newuserModel:{user:{username: '', passwd: '', id: -1, first_name: '', last_name: '', role: 'USER'}},
				newtitle:"",
				curUserModel:{user:{}},
				isNewUser: false,
				inEdit: false,
			}
		},
		beforeCreate: function(){
			var self=this;
			retrieveUserList(function(response) {
				self.umodel.userList = response;
			},function(response){
				console.log(response);
			});
			getCurrentUser(self.$store.state.baseurl, function(response) {
				if(response!="")
					$.extend(true, self.curUserModel.user, response);
			},function(response) {
				console.log(response);
			});
		},
	created:function(){
			var self =this;
			eventBus.$on("userselected", function(user){
				$.extend(true, self.userModel.user, user);
				$.extend(true, self.selectedUserModel.user, user);
				self.isNewUser=false;
				self.inEdit=true;
				$('.userentry').css("opacity",1);
			});
			eventBus.$on("userAdded", function(user){
				var obj = {};
				$.extend(true, obj, user);
				self.umodel.userList.push(obj);
				$.extend(true, self.selectedUserModel.user, user);
        $('.userentry').css("opacity",1);

				self.isNewUser=false;
				self.inEdit=true;

			});
			eventBus.$on("userUpdated", function(user){
				var dIndex = self.umodel.userList.map(function(e) {return e.id}).indexOf(user.profile.id);
				console.log(dIndex);
				$.extend(true, self.umodel.userList[dIndex], user);
				self.inEdit=false;
        $('.userentry').css("opacity",1);

			});
			eventBus.$on('userdeleted',function(userid){
			      console.log(userid)
			      var dIndex = self.umodel.userList.map(function(e) {return e.id}).indexOf(userid);
			      self.umodel.userList.splice(dIndex,1);
				  $.extend(true, self.userModel.user, self.newuserModel.user);
          $('.userentry').css("opacity",1);

			    })
	},
  mounted:function(){
    		overlayHeightResize();
  },
	components: {
		olpane:olpane,
		usercard
		},
	computed:{
		existingUserNames: function() {
			var usernameList = new Array();
			for (var i = 0; i < this.umodel.userList.length; i ++ ){
				usernameList.push(this.umodel.userList[i].username);
			}
			return usernameList;
		},
		userdtoModel:function(){
			var obj = {username:'',password:'',role:'', profile:{first_name:'',id:-1,last_name:''}};
			obj.username=this.userModel.user.username;
			obj.password=this.userModel.user.password;
			obj.profile.first_name=this.userModel.user.first_name;
			obj.profile.last_name=this.userModel.user.last_name;
			obj.profile.id=this.userModel.user.id;
			obj.role=this.userModel.user.role;
			return obj;
		}

	},
	methods:{
		adduser:function(){
		  this.isNewUser=true;
		  $.extend(true, this.userModel.user, this.newuserModel.user);
		  $.extend(true, this.selectedUserModel.user, this.newuserModel.user);
		  this.inEdit=true;
	},
		undoEdit: function(){
		  $.extend(true, this.userModel.user, this.selectedUserModel.user);
	},
		  validateuserform(e) {
		          this.$validator.validateAll('userform');
		          if (!this.errors.any('userform')) {
			            this.addOrUpdateUser()
			        }
		      },
			  validateBeforeSubmit(e) {
			        this.$validator.validateAll();
			        if (!this.errors.any('userform')) {
			            this.addOrUpdateUser()
			        }
			      },
		    addOrUpdateUser(){
		    	var self=this;
		    	var userObject = self.userdtoModel;
		    	$( 'div.processing' ).fadeIn( 300 );
		    	if (self.isNewUser) {
		    		var text = JSON.stringify(userObject);
			    		$.ajax({
		    					beforeSend : function(xhrObj) {
		    						xhrObj.setRequestHeader("Content-Type","application/json");
		    						xhrObj.setRequestHeader("Accept","application/json");
		    					},
		    					type : 'POST',
		    					url : "user",
		    					data : text,
		    					dataType : "json",
		    					success : function(response) {
		    						if (response != 'empty') {
		    							$( 'div.processing' ).fadeOut( 200 );  // eslint-disable-line
		    				            $('div.success').fadeIn(300).delay(500).fadeOut(400, function(){  // eslint-disable-line
		    				           	$.extend(true, self.userModel.user, response);
		    				            eventBus.$emit('userAdded', self.userModel.user);  // eslint-disable-line
		    				            });
		    						}
		    					},

		    					error : function(response) {
		    						$( 'div.processing' ).fadeOut( 200 );  // eslint-disable-line
		    						$( 'div.failure' ).fadeIn( 300 ).delay( 500 ).fadeOut( 400 ); // eslint-disable-line
		    					}
		    				});

		    	}else {
		    		var text = JSON.stringify(userObject);
		    		var id = userObject.profile.id;
		    		$.ajax({
		    			beforeSend : function(xhrObj) {
		    						xhrObj.setRequestHeader("Content-Type",	"application/json");
		    						xhrObj.setRequestHeader("Accept","application/json");
		    					},
		    					type : 'PUT',
		    					url : "user/"+id,
		    					data : text,
		    					dataType : "json",

		    					success : function(
		    							response) {
		    						if (response != 'empty') {
		    							$( 'div.processing' ).fadeOut( 200 );  // eslint-disable-line
		    				            $('div.success').fadeIn(300).delay(500).fadeOut(400, function(){  // eslint-disable-line
		    				           	$.extend(true, self.userModel.user, response);
		    				            eventBus.$emit('userUpdated', self.userModel.user);  // eslint-disable-line
		    				            });
		    						}
		    					},

		    					error : function(response) {
		    						$( 'div.processing' ).fadeOut( 200 );  // eslint-disable-line
		    						$( 'div.failure' ).fadeIn( 300 ).delay( 500 ).fadeOut( 400 ); // eslint-disable-line
		    					}
		    				});

		    	}
		    }

	}
};
	</script>
	<style scoped>
	ul {
		list-style: none;
	}
	.uListContainer {
		height:100%;
		padding: 0px 22px 0px 0px;
	  border-right: 1px solid #e5e5e5;
	}
	#uList {
		margin: 10px 0px;
		padding: 0px 10px;

	max-height: 650px;
	overflow: auto;

	}
	#uList li {
		margin-top : 20px;
    border-left:2px solid #fff;
    transition: all 0.8s ease;
	}
  #uList li:hover {
      border-left:2px solid #0075bc;
  }

	.addtext {
	    position: relative;
	    width: 100%;
	    padding: 0;
	    margin: 5px 0px 15px 0px;
	    color: #666666;
	font-size: 11px;
	}
	.addtext span{
		position: relative;
		color: #0075bc;
	top: 5px;
	left: 850px;
	}
	.addtext span.nearmax {
		color: #ec2526;
	}
	.done_btn {
    height: 55px;
    width: 910px;
    /* right: 100px; */
    margin: 55px auto;
    background-color: #0075bc;
    color: #fff;
    border-radius: 10px;
    position: absolute;
}
	.kgl-sub {
		border-bottom: 1px solid #b3b3b3;
		width: 400px;
	    padding: 5px 0px;
		margin: 0px 10px;
	font-size:14px;
	}
	.kgl-sub+form{
		height: 100%;
		overflow-y: auto;
		margin: 10px 0px;
	}
	.loginField {
		position: relative;
		padding: 0px 0px;
	    margin: 10px 0px 35px 0px;
	    height: 70px;
	}
	.loginField select {
		 width: 400px;
	   height: 50px;
     border: 1px solid #e5e5e5;
     padding: 0 0 0 16px;
	}
	.fieldcontainer {
		padding:0px 10px;
	}
  .loginField input[type=text], .loginField input[type=password] {
      width: 400px;
      height: 50px;
      border-radius: 0px;
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
  .userentry {
    opacity: 1;

  }
	p.control {
	    position: absolute;
	    margin: 2px 0;
	}
	p.control span.is-danger {
		position: absolute;
		left: 16px;
		top: 58px;
	font-style: italic;
	font-size: 12px;
	color: #ec2526;


	}
	button.user {
	    width: 120px;
	    position: absolute;
      right: 50px;
	    height: 38px;
	    border-radius: 0px;
	    border: 1px solid #0075bc;
      background-color: #fff;
      color: #0075bc;
      transition: all 0.6s ease;

	}
  button.user:hover {
  background-color: #0075bc;
  color: #fff;
  }
	button.user:disabled {
		background-color: #e5e5e5;
	   border: 1px solid #e5e5e5;
     color: #fff;
	}
  button.edit {
    position: absolute;
    right: 220px;
    height: 38px;
  }
	button.edit:disabled {
		opacity: 0;
	}
	#emptyuser {
		position: relative;
	    text-align: left;
	    width: 400px;
		  height: 75px;
	    background-color: #fff;
	    margin: 10px 0px 10px 10px;
	    font-weight: 400;
      font-size: 14px;
	    border:none;
	    padding: 0px ;
      text-align: center;
      cursor: pointer;
	}

  .kgl-newuser{
  	width: 32px;
  	height: 32px;
  	position:absolute;
  	top: 25px;
    right:150px;
    margin:0 auto;
    z-index:500;
  }
  .kgl-newuser span {
  position: absolute;
  top:5px;
  left:-110px;
  opacity:1;
  border: none;
  }
  .kgl-newuser i {
  position: absolute;
  top: 8px;
  left:9.5px;
  }
	.maxheight {
		height: 100%;
	}
	.entryform>div {
		height: 100%;
	}
	</style>
