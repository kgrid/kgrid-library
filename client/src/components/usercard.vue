<template>
	<div class="container kgl-usercard" v-bind:id="user.username" v-on:click="userselected">
		<div v-if='user.id!=-1'>
      <div class='kgl-name'>{{user.first_name}} {{user.last_name}}<span>{{user.role}}</span></div>
      <div class='kgl-username'>{{user.username}}</div>
      <button v-show='!isCurrentUser' class='cardBtn' @click='deleteuser'><i class='fa fa-trash kg-fg-color fa-2x'></i></button>
    </div>
    <div v-else><img src='../assets/images/'</div>
    <div class='kgl-bk'></div>
	</div>
</template>
<script>
	export default {
  		name:	"usercard",
		props : [ 'user', 'you'],
		data: function() {
			return {}
		},
		mounted: function(){},
		computed : {
			isCurrentUser: function(){
				return (this.user.id==this.you.id)
			},
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
										     self.$eventBus.$emit('userdeleted',userid);
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
							this.$eventBus.$emit("userselected", this.user);
							return false;
			}
		}
	};
				</script>
				<style>
				.kgl-usercard {
					position: relative;
			    text-align: left;
			    width: 400px;
					height: 75px;
				  background-color: #fff;
				  margin: 10px 0px 10px 0px;
				  color: #696969;
				  font-weight: 400;
				  border: 1px solid #e5e5e5;
				  padding: 0px ;
					transition: all 0.8s ease;
				}
				.kgl-usercard.active {
					border: 1px solid #0075bc;
				}
        .kgl-bk {
        width: 400px;
        height: 73px;
        }
				.kgl-usercard:after {
					content: '';
					display: block;
					margin: auto;
					height: 2px;
					width: 0px;
					background: transparent;
					transition: width 0.8s ease, background-color .5s ease;
				}
				.kgl-usercard:hover{
					border-left: 1px solid #0075bc
				}
        .kgl-initial {
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
.kgl-name {
position: absolute;
top: 15px;
left:15px;
padding-right: 0px;
font-size:14px;
font-weight: 400;
}
.kgl-username {
  position: absolute;
  top: 40px;
  left:15px;
font-size: 14px;
font-weight: 400;
}
.kgl-name span {
 border-left: 1px solid #666666;
 padding-left: 10px;
 margin-left: 10px;
}
.cardBtn{
position: absolute;
top: 25px;
right: 25px;
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
