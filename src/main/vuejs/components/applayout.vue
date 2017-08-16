<template id="layout">
		<div class="kgl-content">
			<div class="whitebacking">
				<div class="wrapper">
					<div class="kgl-banner" v-if="nothelper">
						<slot name="banner"></slot>
			</div></div></div>
			<div class="header" v-if="nothelper">
				<div class="headercontainer">
					<div class="headercol">
						<slot name="header"></slot>
			</div></div></div>
			<div class="e5backing">
				<div class="wrapper">
					<div class="datagrid">
						<slot name="maincontent"></slot>
					</div>
					<div class="m_logo_footer" v-if="nothelper==false">
						<img src="../assets/images/blue-transparentbg.png" width="60px"	height="auto" />
					</div>
				</div>
			</div>
		</div>
	</template>
<script>
import { backToTop } from '../ot.js';
export default {
      name: 'applayout',
      props: ['nothelper']
  };
</script>
<style>

.m_logo_footer {
	text-align: center;
	padding-top: 30px;
	padding-bottom: 30px;
}
</style>
