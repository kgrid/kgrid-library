<template id="linkedfield_editor">
	<olpane layerid=1>
		<div slot="ol-title">
			<h3>Add/Edit {{inedit}}</h3>
			<div classs="row">
				<div class="col-md-9"></div>
			</div>
		</div>
		<div slot="ol-form">
			<fieldset class="fieldcontainer" id="linkedfields">
				<div class="licenseField">
					<h4 title="license_title">{{inedit}} TITLE</h4>
					<div class="addtext">
						<input class="textbox inEdit" v-model="linkedField.title" type="text" spellcheck="false" placeholder="Enter Title"	maxlength="140"/>
					</div>
					<label class="errorLabel" for="username"> </label>
				</div>
				<div class="licenseField">
					<h4 title="citation_link">HYPERLINK</h4>
					<button class="inline edit" v-on:click="preview">PREVIEW</button>
					<div class="addtext">
						<input class="textbox inEdit" v-model="linkedField.link" type="text" spellcheck="false"	placeholder="Please provide the URL. "/>
					</div>
					<label class="errorLabel" for="license_link"> </label>
				</div>

			</fieldset>
		</div>
		<div slot='buttons'>
							<button class="kg-btn-secondary" v-on:click="undoEdit">CANCEL</button>
							<button class="kg-btn-primary" v-on:click="doneEdit">DONE</button>
		</div>
	</olpane>
</template>
<script>
import olpane from '../components/olpane';
export default {
	name:"linkedfieldeditor",
	props:['inedit', 'srcfield'],
	data: function(){
		return {linkedField:{}}
	},
	components: {
		olpane
	},
	mounted:function(){
		this.linkedField=this.srcfield;
	},
	watch:{
		srcfield:function(){
			this.linkedField=this.srcfield;
		}
	},
	methods:{
		undoEdit: function(){
			this.linkedField= this.$parent.getOriginalField();
		},
		doneEdit:function(){
			this.$emit('slideout',this.linkedField);
		},
		preview: function(){
			var myWindow = window.open(this.linkedField.link, "myWindow", "width=400,height=700");   // Opens a new window
			myWindow.focus();
		}
	}
};
</script>
