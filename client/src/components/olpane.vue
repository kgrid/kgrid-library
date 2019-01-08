<template id="ol-pane-template">
<transition name='modal'>
	<div class="modal-mask">
		<div class="ol_pane" ref='opane' :style='opanestyle' :class='paneclass'>
			<div class='sidebar_close kg-btn-close'   v-on:click='closeOverlay'>
				<h3><span>Close</span></h3>
				<div class='kgl-close'>
					<v-icon>close</v-icon>
				</div>
			</div>
			<div class="overlay-top"></div>
			<div class="overlay-board" ref='oboard' :style='oboardstyle'>
				<div class="overlay-title" v-if="hasTitleSlot">
					<slot name="oltitle"></slot>
				</div>
				<div class="entryform" ref="oform" :style='oformstyle'>
					<slot name="ol-form"></slot>
				</div>
			</div>
			<div class="overlay-alert" v-if="hasFooterSlot">
					<div class='formbutton'><slot name='buttons'></slot></div>
					<div class="alert-box processing" :class='{dispClass:stage=="processing"}'><slot name="ol-processing"></slot></div>
					<div class="alert-box success" :class='{dispClass:stage=="success"}'><slot name="ol-success"></slot></div>
					<div class="alert-box failure" :class='{dispClass:stage=="error"}'><slot name="ol-failure"></slot></div>
					<div class="alert-box warning" :class='{dispClass:stage=="warning"}'><slot name="ol-warning"></slot></div>
				</div>
			</div>
		</div>
	</transition>
</template>
<script>
export default {
  name: "olpane",
  props:['layerid', 'left', 'stage'],
	data:function(){
		return {
			opanestyle:{'height':''},
			oboardstyle:{'height':''},
			oformstyle:{'height':''},
			oalertstyle:{'height':''},
			otopstyle:{'height':''}
		}
	},
	created:function(){
			var self=this;
			window.addEventListener('resize', this.handleresize);
	}
	, beforeDestroy:function(){
			window.removeEventListener('resize', this.handleresize);
	}
	, mounted: function(){
		this.handleresize()
	}
	, computed:{
		paneclass:function(){
			if(!this.left){
				return 'full'
			}else{
				return 'half'
			}
		},
		hasFooterSlot() {
			return !!this.$slots.buttons
		},
		hasTitleSlot() {
			return !!this.$slots.oltitle
		}
	}
	,methods:{
		handleresize:function(){
			var ol_pane_height = window.innerHeight;
			// console.log('ol_pane_height: '+ol_pane_height)
			var boardHeight = ol_pane_height - 80;
			if(this.hasFooterSlot) boardHeight = boardHeight -100
			// console.log('boardHeight: '+boardHeight)
			var formHeight = boardHeight
			if(this.hasTitleSlot) formHeight = formHeight- 50;
			// console.log('formHeight: '+formHeight)
			var heightString = ol_pane_height + 'px';
			this.opanestyle.height=heightString;
			heightString = boardHeight +'px'
			this.oboardstyle.height=heightString;
			heightString = formHeight +'px'
			this.oformstyle.height=heightString;
		}
		, closeOverlay:function(){
			this.$eventBus.$emit('hideOverlay',this.layerid);
		}
	}
};
</script>
<style scoped>
.ol_pane {
		position: absolute;
    width: 1090px;
    margin: 0px auto;
    background-color: #fff;
    height: 100%;
    transition: all .8s ease;
}
.ol_pane.full {
	right: 0px;
}
.ol_pane.half {
	right: -540px;
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
.modal-enter, .modal-leave-active {
	  opacity: 0;
	}
.modal-enter .ol_pane,	.modal-leave-active .ol_pane {
	  right: -1080px;
	}
	.kgl-close {
	position: relative;
	top: 0px;
	left: 0px;
  border: none;
  width: 55px;
  height: 55px;
  line-height: 55px;
  padding: 0px 0px 0px 0px;
  background-color: transparent;
}
.sidebar_close {
    background-color:#fff;
    position:absolute;
    height: 100%;
    width:55px;
    text-align:center;
    border-right: 1px solid #e5e5e5;
		cursor:pointer;
}
.sidebar_close h3{
    position:absolute;
    line-height:60px;
    vertical-align:middle;
    top:40px;
    left:10px;
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
    width:28px;
    height:28px;
}
.overlay-top {
    height: 80px;
    width: 1030px;
    position: absolute;
    top: 0;
    left: 55px;
    background-color: transparent;
}
.overlay-title {
	margin:0px 30px;
	height: 50px;
}
.overlay-board {
    position: absolute;
    background-color: transparent;
    width: 1030px;
    top: 80px;
    left: 55px;
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
	left:0px;
	bottom:30px;
	padding: 12px;
    margin-bottom: 0px;
    border: 1px solid transparent;
    border-radius: 0px;
    width:600px;
		opacity:0;
		transition: all 0.8s ease;
}
.alert-box  div span{
	color: #fff;
}
.alert-box.dispClass {
	opacity:1;
}
.formbutton {
	position: absolute;
	bottom: 30px;
	right: 0px;
}
.success {
    background-color: #0075bc;
    border-color: #0075bc;
}
.processing{
	  border-color: #a5a5a5;
		background-color:#a5a5a5;
}
.failure {
    background-color: #bc2526;
    border-color: #bc2526;
}
.warning {
    color: #8a6d3b;
    background-color: #fcf8e3;
    border-color: #faebcc;
}
.entryform {
	height: 700px;
  margin:0px 30px 0px 30px;
	overflow:auto;
  }
</style>
