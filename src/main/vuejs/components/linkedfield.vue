	<template id="linkedfieldtile-template">
	   <a target="_blank" v-bind:href="link"><span>{{value}}</span></a>
	</template>
	<script>
	export default {
				name: "linkedfieldtile",
				props : [ "link", "value" ]
				};
	</script>
	<style scoped>
	a {
    display: block;
    border: none;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    height: 38px;
    padding: 10px 0 0 1px;
    border-radius: 0;
    margin: 0;
    font-size: 14px;
    color: #666;
    font-weight: 400;
}
a, a:hover, a:visited {
    text-decoration: none;
    cursor: pointer;
    transition: color 0.5s ease;
		background-color: transparent;
}
	</style>
