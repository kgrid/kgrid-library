<template id='libcon_overlay'>
	<olnpane layerid=0>
		<div slot='ol-title'><h3>Connect To A Library</h3></div>
		<div slot='ol-form'>
		   <form @submit.prevent="validateLibForm" data-vv-scope="libform" autocomplete='on'>
			<fieldset class='fieldcontainer' id='first'>
			<div class='loginField'>
               <label class="label">Library URL</label>
                <p class="control has-icon has-icon-right">
                    <input spellcheck=false v-model='baseurl' name="url" v-validate data-vv-delay="1000" data-vv-rules="required|url" :class="{'input': true, 'is-danger': errors.has('url', 'libform') }" type="text" placeholder="http://127.0.0.1:8080">
                    <i v-show="errors.has('url', 'libform')" class="fa fa-warning"></i>
                    <span v-show="errors.has('url', 'libform')" class="help is-danger">{{ errors.first('url', 'libform') }}</span>
                </p>
				</div>
				<div class='loginField'>
									<div class='ot-s-btn ot-login'>
								       <div class='greenbutton' > </div>
								       <div class='btnContent'><button class='login' type='submit'>CONNECT</button></div>
								</div>
							</div>
				</fieldset>
				</form>
		</div>
		<div slot='ol-processing'>Connecting ...</div>
		<div slot='ol-success'>Library Connected!!!</div>
		<div slot='ol-failure'>Unable to Connect. Please check the URL! </div>
		<div slot='ol-warning'>Warning !!!</div>
	</olnpane>
</template>
<script>
import olnpane from '../components/olnpane';
import eventBus from '../components/eventBus.js';
export default {
  name: 'libcon',
  data: function () {
    return {
    	baseurl:'',
      role: 'default',
      test: true
    };
  },
  components: {
    olnpane
  },
  methods: {
	  validateLibForm(e) {
          this.$validator.validateAll('libform');
          if (!this.errors.any('libform')) {
	            this.libconnect()
	        }
      },
	  validateBeforeSubmit(e) {
	        this.$validator.validateAll();
	        if (!this.errors.any()) {
	            this.libconnect()
	        }
	      },
    libconnect: function () {
      var self = this;  // eslint-disable-line
			console.log(this.baseurl.length);
			if(this.baseurl.substring(this.baseurl.length)!='/'){
				this.baseurl=this.baseurl+"/";
			}
			this.$store.commit('seturl',this.baseurl);
      if (this.test) {
        $( 'div.processing' ).fadeIn( 300 );  // eslint-disable-line
        $.ajax({  // eslint-disable-line
          type: 'GET',
					beforeSend: function (jqxhr) {
            jqxhr.setRequestHeader("Access-Control-Allow-Origin", self.baseurl);
        	},
          url: this.baseurl+'knowledgeObject',
          success: function (response) {
            $( 'div.processing' ).fadeOut( 200 );  // eslint-disable-line
						if(response instanceof Array){
            	$('div.success').fadeIn(300).delay(500).fadeOut(400, function(){  // eslint-disable-line
            		eventBus.$emit('libConnected', self.baseurl);  // eslint-disable-line
            	});
						}else {
							$( 'div.processing' ).fadeOut( 200 );  // eslint-disable-line
							$( 'div.failure' ).fadeIn( 300 ).delay( 500 ).fadeOut( 400 ); // eslint-disable-line
						}
          },
          error: function (response) {
				$( 'div.processing' ).fadeOut( 200 );  // eslint-disable-line
				$( 'div.failure' ).fadeIn( 300 ).delay( 500 ).fadeOut( 400 ); // eslint-disable-line
          }
        });
      } else {
      }
    }
  }
};
</script>
<style scoped>
p.control {
	position: absolute;
	margin: 12px 0;
}
.loginField input[type=text], .loginField input[type=password] {
    width: 400px;
    height: 50px;
}
.loginField label {
	position: relative;
	color: #666666;
	font-weight: 400;
	font-size: 16px;
line-height: 1.6em;
padding: 10px 14px;
margin:0px;
}
p.control span.is-danger {
	position: absolute;
	left: 16px;
	top: 62px;
font-style: italic;
font-size: 12px;
color: #ec2526;


}
.fieldcontainer {
    display: block;
    position: relative;
    height: 95%;
    overflow: auto;
margin: 0 auto;
padding: 0px 20px;
}

.ot-s-btn .btnContent{
	  position: relative;
	  top:-86px;
	  left:0px;
	}
.ot-login {
	padding: 22px 0px;
}
button.login {
    width: 400px;
    position: relative;
    height: 50px;
    border-radius: 10px;
    border:none;
    background-color: transparent;
    color: #fff;
    margin-top: 38px;
}
.entryform {
    height: 600px;
    padding: 16px 22px;
    margin-top: 0px;
}
.loginField {
    margin: 25px 0px 25px 0px;
    height: 120px;
}
fieldset h4 {
    display: inline-block;
    padding: 5px 16px;
    font-size: 14px;
    font-weight: 400;
    margin-right: 20px;
    color: #666666;
}
.addtext {
    position: relative;
    width: 100%;
    padding: 0;
    margin: 5px 0px 15px 0px;
    color: #666666;
}

h3 {
    font-size: 24px;
    font-weight: 300;
}
input[type=text], input[type=password], input[type=textarea], .addtext a {
    width: 350px;
    height: 53px;
    padding: 0px 16px;
    border: 1px solid #e6e6e6;
    border-radius: 10px;
    margin: 2px 0;
    font-size: 14px;
    color: #666666;
    font-weight: 400;
}
</style>
