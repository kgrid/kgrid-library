<template id="layout">
		<div class="kgl-content">
			<div class="whitebacking">
				<div class="wrapper">
					<slot name='backrow'></slot>
					<div class="kgl-banner" v-if="nothelper">
						<slot name="banner"></slot>
			</div></div></div>
			<div class="theadwrapper" :style='hwrapperstyle'>
				<div class="header" ref='headerbox' :class="{fixed:scrollPos>=navOffset, zset:scrollPos>60}" v-if="nothelper">
					<div class="headercontainer">
						<div class="headercol">
							<slot name="header"></slot>
			</div></div></div></div>
			<div class="e5backing">
				<div class="wrapper">
					<div class="datagrid">
						<slot name="maincontent"></slot>
					</div>
					<div class="m_logo_footer" v-if="!nothelper">
						<img src="../assets/images/blue-transparentbg.png" width="60px"	height="auto" />
					</div>
				</div>
			</div>
		</div>
	</template>
<script>
export default {
      name: 'applayout'
			, data: function() {
				return {
					navOffset:60
					, scrollPos:0
					, hwrapperstyle:{'height':''}
				}
			}
      , props: ['nothelper']
			, created:function(){
				window.addEventListener('scroll', this.handleScroll);
			}
			, beforeDestroy:function(){
				window.removeEventListener('scroll', this.handleScroll);
			}
			, mounted:function() {
				if(this.nothelper){
					var heightString = this.$refs.headerbox.clientHeight + 'px';
        	this.hwrapperstyle.height=heightString;
					this.navOffset = this.$refs.headerbox.offsetTop+60;
				}
			}
			, methods:{
				handleScroll:function(){
					this.scrollPos = window.scrollY;
				}
			}
  };
</script>
<style>
.kgl-content {
	position: absolute;
	margin-top: 60px;
	width:100%;
	min-width: 1584px;
}
.header {
	height:60px;
	position: relative;
	min-width: 1584px;
	width:100%;
	border: 1px solid lightgrey;
	box-shadow: 0 2px 2px rgba(0, 0, 0, 0.35);
	z-index: 150;
	margin: 0 auto;
	background-color:#fff;
}
.header.zset {
	z-index:350;
}
.fixed {
  top: 0px;
  position: fixed;
  width: 100%;
  margin: 0 auto;
  text-align: center;
  align-content: center;
	z-index:350;
}
.headercontainer {
	width:1584px;
	border:none;
	box-shadow: none;
	overflow:visible;
	height:auto;
	margin: 0 auto;
		z-index: 125;
}
.headercol{
	position: relative;
	text-align: left;
	height: 30px;
	border-collapse: collapse;
	display: table;
	max-width: 1028px;
	z-index: 125;
	margin: 8px auto;
	width:1024px;
}
.headercol ul {
	display: table-row;
}
.m_logo_footer {
	text-align: center;
	padding-top: 30px;
	padding-bottom: 30px;
}
</style>
