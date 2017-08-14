<template id="ol-pane-template">
<transition name='modal'>
	<div class="modal-mask">
		<div class="ol_pane" >
			<div class='sidebar_close'   v-on:click='closeOverlay'>
				<h3><span class='kg-fg-color'>Close</span></h3>
				<div class='kgl-r-btn kgl-close'>
					<i class="fa fa-close kg-fg-color" aria-hidden="false"></i>
				</div>
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
					<div class='formbutton'><slot name='buttons'></slot></div>
					<div class="alert-box processing"><slot name="ol-processing"></slot></div>
					<div class="alert-box success"><slot name="ol-success"></slot></div>
					<div class="alert-box failure"><slot name="ol-failure"></slot></div>
					<div class="alert-box warning"><slot name="ol-warning"></slot></div>
				</div>
			</div>
		</div>
	</transition>
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
<style scoped>
.ol_pane{
	position: absolute;
    right:0px;
    width: 1090px;
    margin: 0px auto;
    background-color: #fff;
    height: 100%;
    transition: all 2s ease;
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
  transition: opacity 1s ease;
}

.modal-enter {
	  opacity: 0;
	}

	.modal-leave-active {
	  opacity: 0;
	}

	.modal-enter .ol_pane,
	.modal-leave-active .ol_pane {
	  right: -1080px;
	}


.sidebar_close {
    background-color:#fff;
		color: #b3b3b3;
    position:absolute;
    height: 100%;
    width:60px;
    text-align:center;
    border-right: 1px solid #e6e6e6;
		transition: color 0.5s ease;
		cursor:pointer;
}

.sidebar_close:hover {
	color: #666666;
}

.sidebar_close h3{
    position:absolute;
    line-height:60px;
    vertical-align:middle;
    top:40px;
    left:12px;
    font-size:14px;
    -webkit-transform: rotate(270deg);
    -moz-transform: rotate(270deg);
    -ms-transform: rotate(270deg);
    -o-transform: rotate(270deg);
    transform: rotate(270deg);
	transition: color 0.5s ease;
color: #b3b3b3;
}
#close_overlay {
    position: relative;
    top:10px;
    width:28px;
    height:28px;
}
.kgl-close {
	position: relative;
	top: 21px;
	left: 11px;
	cursor: pointer;
}
.kgl-close .greenroundbutton {
	width: 28px;
    height: 28px;
box-shadow: none;
}
.kgl-close .btnContent {
	position: relative;
	top: -24px;
left: 0px;
}

.sidebar_close:hover h3{
	color: #666666;
}
.sidebar_close:hover .greenroundbutton{
	  transform: scale(1.2);
	  margin: auto;
}

.overlay-top {
    height: 100px;
    width: 1030px;
    position: absolute;
    top: 0;
    left: 60px;
    background-color: transparent;

}
.overlay-title {
	margin:0px 30px;
	height: 100px;
}
.overlay-board {
    position: absolute;
    background-color: transparent;
    width: 1030px;
    top: 100px;
    left: 60px;
    padding: 0px 0px;
}

.overlay-alert {
    height: 100px;
    width: 970px;
    position: absolute;
    bottom:0px;
    left: 60px;
		margin: 0px 30px;
    background-color: transparent;
		border-top:1px solid #e3e3e3;
}
.alert-box {
	position:absolute;
	left:100px;
	bottom:0px;
	padding: 15px;
    margin-bottom: 20px;
    border: 1px solid transparent;
    border-radius: 0px;
    width:400px;
}

.formbutton {
	position: absolute;
	bottom: 30px;
	right: 0px;
}
.success {
    color: #fff;
    background-color: #00b5af;
    border-color: #00b5af;
    display: none;
}

.processing{
	  border-color: #0075bc;
		    background-color:#0075bc;
    display: none;

}
.failure {
    color: #fff;
    background-color: #a02816;
    border-color: #a02816;
    display: none;
}

.warning {
    color: #8a6d3b;
    background-color: #fcf8e3;
    border-color: #faebcc;
    display: none;
}
.entryform {
	height: 700px;
  margin:0px 0px 0px 30px;
    }

</style>
