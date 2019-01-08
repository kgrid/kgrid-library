<template>
  <v-container grid-list-md text-xs-center>
    <v-layout row wrap justify-center>
  <div class="infogrid" >
  <div class="infoblock">
    <div class="block_title title font-weight-light">
      <div class="line"><hr></div>Who We Are<div class="line"><hr></div>
    </div>
    <div class="subheading font-weight-thin block_info">Knowledge Grid has been developed at the University of Michigan Medical School for one purpose,<br>
      to speed up the rate at which new health knowledge is routinely used to help people be well.<br>Here in the Department of Learning Health Sciences we are dedicated to the scientific pursuit of<br>infrastructures - comprised of people, processes, and technologies - for routine, sustainable learning<br>about how to continuously improve human health. Knowledge Grid has been created within our<br>Division of Learning and Knowledge Systems to address the challenge of managing massive amounts<br>of rapidly changing computable health knowledge. It is for storing, curating, preserving, and protecting<br>health knowledge, and for making it accessible on demand for all people to use at any time.<br>Knowledge Grid enables its users to be effective movers of health knoweldge into practice.</div></div>
  <div class="infoblock">
    <div class="block_title title font-weight-light">
      <div class="line"><hr></div>Open Source<div class="line"><hr></div>
      </div>
      <div class="subheading font-weight-thin block_info">Knowledge Grid is an open source software system for managing computable health knowledge. As such,<br>its code can be inspected and extended, and its uses can multiply. Knowledge Grid has been designed to<br> promote open sharing of the computable health knowledge in its libraries too. And Knowledge Grid maintains<br>detailed records and audit trails that its users are encouraged to open to the world of linked data.<br>At the University of Michigan Medical School, we understand that openness begets trust and that is why<br>with Knowledge Grid, we are committed to supporting openness in all things.</div>
    </div>
  <div class="infoblock" v-if='isAdmin'>
      <div class="block_title title font-weight-light">
        <div class="line"><hr></div>Build Information<div class="line"><hr></div>
      </div>
      <div class="subheading font-weight-thin block_info">
        Version:   {{version}}<br>Build TIme:   {{buildtime}}<br>
      </div>
    </div>
  </div>
</v-layout>
</v-container>
</template>
<script>
export default {
  name: 'about',
  data : function() {
    return {}
  },
  created : function() {
  },
  mounted:function(){
  },
  computed : {
    isLoggedIn: function () {
      return this.$store.getters.isLoggedIn;
    },
    libraryname : function(){
      return this.$store.getters.getLibraryName;
    },
    version : function(){
      return this.$store.getters.getVersion;
    },
    buildtime : function(){
      return this.$store.getters.getBuildTime
    },
    // gitid : function(){
    //   return this.$store.getters.getGitID;
    // },
    isAdmin: function(){
      return this.$store.getters.isAdmin;
    }
  }
};
</script>
