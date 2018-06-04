<template>
  <div class='dropdown bg-white' :style='dstyle.d' v-click-outside='outside'>
    <div class='row  float-r mar-r-12 lh-1' v-on:mouseenter='trigDropdown' v-on:mouseleave='trigDropdown'>
        <h6 v-if='label!=""'><span class='kg-fg-color mar-r-30'> {{label}}</span></h6>
        <p class=' ft-sz-14 kg-fg-color mar-r-12 wid-160' style='display:inline-block;'>{{selectvalue}}</p>
        <icon :class="{down:!showDropdown, up:showDropdown}" style='{padding-left:2px;display: inline;}'  color="#0075bc" name="caret-down"></icon>
    </div>
    <ul class='dropdown-menu' id='dropdown' :style='dstyle.ul' v-if='showDropdown' v-on:mouseleave='leaveDropdown'>
      <li :style='dstyle.li' v-for='opt in optionlist'>
        <a v-on:click='selected(opt)' :style='dstyle.a'>
          <span>{{opt.label}}</span>
        </a>
      </li>
    </ul>
  </div>
</template>
<script>
import 'vue-awesome/icons/caret-down'
export default {
  name:	"kgdropdown",
  props : [ 'label', 'selectvalue', 'optionlist' , 'dstyle'],
  data: function() {
    return {
      showDropdown:false
    }
  },
  created: function(){},
  computed: {},
  methods: {
    selected:function(opt){
      this.$emit('selected',opt)
    },
    outside: function(){
      if(this.showDropdown){
        this.toggledropdown();
      }
    },
    toggledropdown: function () {
      this.showDropdown=!this.showDropdown;
      },
    trigDropdown: function(){
          this.showDropdown=true;
      },
    leaveDropdown:function(){
          this.showDropdown=false;
      },
  }
}
</script>
<style scoped>
.dropdown-menu {
position: absolute;
top:auto;
right: 0;
left: auto;
z-index: 10000;
float: left;
min-width: 80px;
display: block;
padding: 0;
font-size: 14px;
text-align: left;
list-style: none;
background-color: #fff;
-webkit-background-clip: padding-box;
background-clip: padding-box;
border: 1px solid #ccc;
border: 1px solid rgba(0,0,0,.15);
border-radius: 4px;
-webkit-box-shadow: 0 6px 12px rgba(0,0,0,.175);
box-shadow: 0 6px 12px rgba(0,0,0,.175);
z-index:99999;
}
.dropdown ul {
 background-color: #fff;
 color: #333;
}
.dropdown-menu > li > a {
	  padding: 0px 10px;
	  text-decoration: none;
	  cursor: pointer;
	  transition: color 0.5s ease;
	  background-color:transparent;
 }
.dropdown-menu > li :hover {
    background-color:#e8e8e8;
  }
ul.dropdown-menu#dropdown {
	  margin-top:28px;
 	  width: 222px;
 }
i {
 	transition: transform 0.5s linear;
 }
i.up {
 -moz-transform: scaleY(-1);
 -o-transform: scaleY(-1);
 -webkit-transform: scaleY(-1);
 transform: scaleY(-1);
 }
i.down {
 -moz-transform: scaleY(1);
 -o-transform: scaleY(1);
 -webkit-transform: scaleY(1);
 transform: scaleY(1);
 }
</style>
