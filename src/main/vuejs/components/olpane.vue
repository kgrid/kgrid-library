<template id="ol-pane-template">
	<div class="modal-mask">
	<div class="ol_pane">
		<div class="sidebar_close">
			<h3>CLOSE</h3>
			<button class="greenroundbutton" id="close_overlay"	v-on:click="closeOverlay">
				<img src="../assets/Close_Icon.png" >
			</button>
		</div>
		<div class="overlay-top">
		</div>
		<div class="overlay-board">
				<div class="overlay-title">
					<slot name="ol-title"></slot>
				</div>
				<div class="entryform" id="entry_form">
					<slot name="ol-form"></slot>
				</div>
		</div>
		<div class="overlay-alert">
					<div class="alert-box processing"><slot name="ol-processing"></slot></div>
					<div class="alert-box success"><slot name="ol-success"></slot></div>
					<div class="alert-box failure"><slot name="ol-failure"></slot></div>
					<div class="alert-box warning"><slot name="ol-warning"></slot></div>
		</div>
	</div>
	</div>
</template>
<script>
import eventBus from '../components/eventBus.js';
export default {
  	name: "olpane",
  	props:['layerid', 'left'],
	created:function(){
		var self=this;
   		eventBus.$on('openOverlay', function () {
      		document.body.classList.toggle('noscroll', true);
      		self.$el.style.right = '0px';
    	});
		eventBus.$on("open",function(x){
//			console.log(x);
			$(".modal-mask").css('opacity',1);
			$(".ol_pane").animate({
			'left' : x
		}, 1000);
		});
	},
	methods:{
		closeOverlay:function(){
			eventBus.$emit('hideOverlay',this.layerid);
		},
		openOverlay:function(){
			this.find(".ol_pane").animate({
				'left' : '30%'
			}, 1000);
		}
	}
};
</script>
<style>
.ol_pane{
	position: absolute;
    right:0px;
    width: 1024px;
     margin: 0px auto;
    background-color: #fff;
    height: 100%;
    transition: all .8s ease;
}

.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, .5);
  display: table;
  transition: opacity .5s ease;
}
.sidebar_close {
    background-color:#fff;
    position:absolute;
    height: 100%;
    width:60px;
    text-align:center;
    border-right: 1px solid #e6e6e6;
}

.sidebar_close h3{
    position:absolute;
    line-height:60px;
    vertical-align:middle;
    top:60px;
    left:10px;
    color:#666666;
    font-size:14px;
    -webkit-transform: rotate(270deg);
    -moz-transform: rotate(270deg);
    -ms-transform: rotate(270deg);
    -o-transform: rotate(270deg);
    transform: rotate(270deg);
}
#close_overlay {
    position: relative;
    top:10px;
    width:35px;
    height:35px;
}

.overlay-top {
    height: 120px;
    width: 960px;
    position: absolute;
    top: 0;
    left: 60px;
    background-color: #fff;

}
.overlay-title {
	padding:0px 22px;
}
.overlay-board {
    position: absolute;
    background: white;
    width: 960px;
    top: 120px;
    left: 60px;
    padding: 0px 0px;
    border-left: 1px solid #e6e6e6;
}

.overlay-alert {
    height: 60px;
    width: 960px;
    position: absolute;
    bottom:0px;
    left: 60px;
    background-color: #fff;

}
.alert-box {
	position:absolute;
	left:100px;
	bottom:0px;
	padding: 15px;
    margin-bottom: 20px;
    border: 1px solid transparent;
    border-radius: 10px;
    width:400px;
}

.success {
    color: #3c763d;
    background-color: #dff0d8;
    border-color: #d6e9c6;
    display: none;
}

.processing{
	   border-color: #d6e9c6;
    display: none;

}
.failure {
    color: #a94442;
    background-color: #f2dede;
    border-color: #ebccd1;
    display: none;
}

.warning {
    color: #8a6d3b;
    background-color: #fcf8e3;
    border-color: #faebcc;
    display: none;
}
.entryform {
	height: 600px;
    padding: 16px 22px;
    margin-top:0px;
    }
.noscroll { overflow: hidden; }
.greenroundbutton {
    width: 41px;
    height: 41px;
    border-radius: 100%;
    color: white;
    background-color: #39b54a;
    border: none;
    font-size: 20px;
    padding: 0;
}
</style>
