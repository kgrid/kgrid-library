<template>
  <v-container fluid>
    <v-layout row wrap mt-5>
      <v-flex xs12 sm8 md6 offset-md3 offset-sm2 mt-5>
        <span class='headline font-weight-light'>Welcome to Knowledge Grid ...</span>
      </v-flex>
    </v-layout>
    <v-layout row wrap mt-5 v-if='!hasError'>
      <v-flex xs12 sm8 md6 offset-md3 offset-sm2 mt-5>
        <span class='headline font-weight-light'>Connecting to the server ...</span>
      </v-flex>
      <v-flex xs12 sm8 md6 offset-md3 offset-sm2>
        <v-progress-linear :indeterminate="true"></v-progress-linear>
      </v-flex>
    </v-layout>
    <v-layout row wrap mt-5 v-else>
      <v-flex xs12 sm8 md6 offset-md3 offset-sm2 mt-5>
        <span class='headline font-weight-light'>Error in connecting to the server. Please select a different server.</span>
      </v-flex>
    </v-layout>
  </v-container>
</template>
<script>
  export default {
    name:'Home',
    data : function() {
      return {}
    },
    mounted : function() {
      var self = this
      if(!this.hasError){
        setTimeout(function(){
          self.$router.push('/list')
        }, 1500)
      }
    },
    computed: {
      hasError: function() {
        return this.$store.getters.getErrorStatus
      }
    }
  }
</script>
