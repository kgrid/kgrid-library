<template id='ol-narrow-pane-template'>
<transition name='modal'>
<div class='modal-mask'>
	<div class='ol_narrow_pane'>
		<div class='sidebar_close kg-btn-close' v-on:click='closeOverlay'>
			<h3><span>Close</span></h3>
			<div class='kgl-close' >
				<i class="fa fa-close" aria-hidden="false"></i>
			</div>
		</div>
		<div class='overlay-top'>
		</div>
		<div class='overlay-board'>
				<div class='overlay-title'>
					<slot name='ol-title'></slot>
				</div>
				<div class='entryform' id='entry_form'>
					<slot name='ol-form'></slot>
				</div>
		</div>
		<div class='overlay-alert'>
					<div class='alert-box processing'><slot name='ol-processing'></slot></div>
					<div class='alert-box success'><slot name='ol-success'></slot></div>
					<div class='alert-box failure'><slot name='ol-failure'></slot></div>
					<div class='alert-box warning'><slot name='ol-warning'></slot></div>
		</div>
	</div>

	</div>
	</transition>
</template>
<script>
import eventBus from '../components/eventBus.js';
export default {
  name: 'olnpane',
  props: ['layerid', 'left'],
  created: function () {
    var self = this  // eslint-disable-line
  },
  methods: {
    closeOverlay: function () {
      this.$el.style.right = '-540px';
      eventBus.$emit('hideOverlay',this.layerid); // eslint-disable-line
    }
  }
};
</script>
<style scoped>
.ol_narrow_pane{
		position: absolute;
    right: 0px;
    width: 550px;
    margin: 0px auto;
    background-color: #fff;
    height: 100%;
    transition: all 0.8s ease;
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

	.modal-enter .ol_narrow_pane,
	.modal-leave-active .ol_narrow_pane {
	  right: -540px;
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
    height: 120px;
    width: 494px;
    position: absolute;
    top: 0;
    left: 60px;
    background-color: #fff;

}
.overlay-title {
	padding:0px;
}
.overlay-board {
    position: absolute;
    background: white;
    width: 494px;
    top: 120px;
    left: 60px;
    padding: 0px 40px;
}

.overlay-alert {
    height: 60px;
    width: 494px;
    position: absolute;
    bottom:0px;
    left: 60px;
    background-color: #fff;

}
.alert-box {
	position:absolute;
	left:47px;
	bottom:0px;
	padding: 15px;
    margin-bottom: 20px;
    border: 1px solid transparent;
    border-radius: 10px;
    width:400px;
}

.success {
    color: #0075bc;
    background-color: trnasparent;
    border:none;
    display: none;
}

.processing{
		color: #0075bc;
		 background-color: #fff;
		 border:none;
    display: none;

}
.failure {
    color: #bc2526;
    background-color: #fff;
    border:none;
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
    padding: 16px 0px;
    margin-top:0px;
    }
.noscroll { overflow: hidden; }

</style>
