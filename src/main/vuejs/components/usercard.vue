<template>
		<div class="container ot-usercard" v-bind:id="user.username" v-on:click="userselected">
		<div v-if='user.id!=-1'>
    <div class='ot-initial'>{{userInitial}}</div>
        <div class='ot-name'>{{user.first_name}} {{user.last_name}}<span>{{user.role}}</span></div>
        <div class='ot-username'>{{user.username}}</div>
        <button v-show='!isCurrentUser' class='cardBtn' @click='deleteuser'>Delete</button>
        </div>
        <div v-else><img src='../assets/images/'</div>
        <div class='ot-bk'></div>
        			</div>
	</template>
	<script>
	import eventBus from '../components/eventBus.js';
	export default {
  		name:	"usercard",
		props : [ 'user', 'you'],
		data: function() {
		return {
			isCurrentUser: false
		}
	},
		mounted: function(){
			this.isCurrentUser = (this.user.id==this.you.id);
		},
		computed : {
			userInitial: function(){
				return this.user.first_name.substring(0,1) + this.user.last_name.substring(0,1)
            }
		},
		methods : {
						deleteuser : function(event) {
							var self=this;
							var userid = this.user.id;
							var txt;
							if (userid != "") {
								var r = confirm("Do you really want to delete the user ? ");
								if (r == true) {
									$.ajax({
										type : 'DELETE',
										url : "user/" + userid,

										success : function(response) {
										     eventBus.$emit('userdeleted',userid);
										},

										error : function(response) {
										
											var test = JSON.stringify(response);
											var obj = JSON.parse(test);
											alert(obj.responseText);
										}

									});
								    
                }
							}
						},
						userselected: function(){
							console.log("User #"+this.user.id+" selected");
							eventBus.$emit("userselected", this.user);
							sessionStorage.setItem("otUser", JSON.stringify(this.user));
							return false;
						}
					}
				};
				</script>
				<style>
				.ot-usercard {
					position: relative;
				    text-align: left;
				    width: 385px;
					height: 75px;
				    background-color: #fff;
				    margin: 10px 0px 10px 0px;
				    color: #696969;
				    font-weight: 400;
				    border: 1px solid #e6e6e6;
				    padding: 0px ;
				}
				
				.ot-usercard.active {
					border: 1px solid #39b45a;
				}
				.ot-usercard#emptyuser{
					border: 1px dashed #39b45a;
				}
        .ot-bk {
        width: 385px;
        height: 73px;
        }
				.ot-usercard:after {
					content: '';
					display: block;
					margin: auto;
					height: 2px;
					width: 0px;
					background: transparent;
					transition: width 0.8s ease, background-color .5s ease;
				}
				.ot-usercard:hover:after{
					width:100%;
				    background: #39b45a;
				}
        .ot-initial {
          position: absolute;
          top: 17px;
          left: 17px;
          width: 40px;
          height: 40px;
          font-size: 16px;
          font-weight: 400;
          line-height: 41px;
          text-align: center;
          border-radius: 100%;
          border: 1px solid #666666;
        }
.ot-name {
position: absolute;
top: 13px;
left:75px;
padding-right: 0px;
font-size:14px;
font-weight: 400;
}

.ot-username {
  position: absolute;
  top: 35px;
  left:75px;
font-size: 14px;
font-weight: 400;
}

.ot-name span {
 border-left: 1px solid #666666;
 padding-left: 10px;
 margin-left: 10px;
}



.cardBtn{
position: absolute;
top: 5px;
right: 10px;
font-size: 11px;
border: none;
cursor: pointer;
background-color: #fff;
color: #b3b3b3;
transition: color 0.5s ease;
}
.cardBtn:hover{
color: #666666;
}
				</style>
