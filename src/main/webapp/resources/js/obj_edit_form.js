'use strict';

$(document)
		.ready(
				function() {
					document.getElementById('file_payload').addEventListener(
							'change', readMultipleFiles, false);
					var citNumber = $(".entryArea .addtext").length;
					console.log("number of citations:"+citNumber);
					
					var inputLabels = ["TITLE","DESCRIPTION","KEYWORDS","OWNERS","CONTRIBUTORS","CITATIONS","LICENSE"];
					var inputNames =  ["title","description_data","keyword_data","owner_data","contri_data","citation_data","license_data"];
					var inputIDs =  ["title_data","description_data","keyword_data","owner_data","contri_data","citation_data","license_data"];
					var maxLengths = [140,500,140,140,140,500,140];
					var placeholderTexts=["A title, which is a formal name.",
					                      "A text description of the object - akin to an abstract - maybe enetered for any object.",
					                      "Click here to add Keywords.",
					                      "Click here to add Owners.",
					                      "Click here to add Contributors.",
					                      "Click here to add Citations",
					                      "Click here to add License"];
					var values = ["${fedoraObject.metadata.title}","${fedoraObject.metadata.description}","${fedoraObject.metadata.keywords}",
					              "${fedoraObject.metadata.owner}","${fedoraObject.metadata.contributors}","${fedoraObject.metadata.citations}","${fedoraObject.metadata.license}"];
					
					var isMultiples = [false,false,true,true,true,true,false];
					var numberofFields = inputLabels.length;
					var inputField;
					
					
					var count = 0; // To Count Blank Fields
					/*------------ Validation Function-----------------*/
					$("#clearPayloadButton").click(function(event) {
						event.preventDefault();
						$("#payloadDropFile").show();
						$("#payloadTextAreaDisplay").hide();
						$('#payloadTextArea').text("");
					});
					$("#clearInputButton").click(function(event) {
						event.preventDefault();
						$("#inputDropFile").show();
						$("#inputTextAreaDisplay").hide();
						$('#inputTextArea').text("");
					});
					$("#clearOutputButton").click(function(event) {
						event.preventDefault();
						$("#outputDropFile").show();
						$("#outputTextAreaDisplay").hide();
						$('#outputTextArea').text("");
					});
					$(".next_btn").click(function() { // Function Runs On NEXT
						// Button Click
						/* $(this).parent().children("input").validate(); */

						$(this).parent().next().fadeIn('slow');
						$(this).parent().css({
							'display' : 'none'
						});
						// Adding Class Active To Show Steps Forward;
						$('.current-tab>img').css("display", "inline-block");
						$('.current-tab').next().addClass('current-tab');
						$('.current-tab').prev().removeClass('current-tab');
						$('.current-tab').prev().addClass('checked');
					});
					$(".pre_btn")
							.click(
									function() { // Function Runs On PREVIOUS
										// Button Click
										$(this).parent().prev().fadeIn('slow');
										$(this).parent().css({
											'display' : 'none'
										});
										// Removing Class Active To Show Steps
										// Backward;
										$('.current-tab').prev()
												.children("img").css("display",
														"none");
										$('.current-tab').prev().removeClass(
												'checked');
										$('.current-tab').prev().addClass(
												'current-tab');
										$('.current-tab:last').removeClass(
												'current-tab');
									});
					// Validating All Input And Textarea Fields
					$(".submit_btn").click(
							function(e) {
								if ($('input').val() == ""
										|| $('textarea').val() == "") {
									alert("*All Fields are mandatory*");
									return false;
								} else {
									return true;
								}
							});
					$("#addObjButton").click(function() {
						$("#entry_form").css("display", "none");
						$("#end_page").css("display", "block");

					});
					$("#engineType").click(function(){
						
					    if($(this).hasClass("up")){
					    	$(this).removeClass("up");
					    } else {
					    	$(this).addClass("up");
					    }
					});
					$('.addtext>input').keyup(updateCount);
					$('.addtext>input').keydown(updateCount);
					$('.addtext>input').each(updateCount);
					$('.addtext>input').change(updateCount);
					$('#description_data').keyup(updateCount);
					$('#description_data').keydown(updateCount);
					$("[id$='_data']").each(updateCount);
					
					function resetCitationText(){
						$('.addtext>input').each(updateCount);
						$( "#citation_title" ).val( "" );
						$( "#citation_link" ).val( "" );
						$("#citation_detail").attr("src","");
						$("#citation_idx").val("");
						$("#addCitation").val("ADD");
					}
					
					function initCitationText(overlayID, cidx, ctitle, clink){
						console.log("Citation Index:"+cidx+" Title:"+ctitle);
						$( "#citation_title" ).val( ctitle );
						$( "#citation_link" ).val( clink );
						$("#citation_detail").attr("src","");
						$("#addCitation").val("UPDATE");
						$("#citation_idx").val(cidx);
						$('.addtext>input').each(updateCount);
					}
					function updateCount() {
						var cs = $(this).val().length;
						var elementid = this.id;
						var count = $(this).parent().children("span").text();
						var maxl_s = count.substring(count.length - 3,
								count.length)
						var maxl = parseInt(maxl_s);
						var csl = maxl - cs;
						var s = csl.toString().concat("/" + maxl_s);
						var cs_color = "#39b45a";
						if (csl < 10) {
							cs_color = "red";
						}
						$(this).parent().children("span").text(s);
						$(this).parent().children("span")
								.css("color", cs_color);

					};
					
						$(".delete_btn").click(function(e){
							var tgt =e.target;
	     					$(this).parent().remove();
	     					return false;
	     				});
						
						$("#citation_data").focus(function(e){
							resetCitationText();
							overlaySlide('citation',true);
						});
						
					
						$(".entryArea input[id^='citation']")
							.focus(function(e){
								var idx = $(this).attr("id");
								var ctitle = $("#"+idx).val();
								var clink = $("#"+idx+"_link").val();
								console.log(idx+" " +ctitle+" "+clink);
							overlaySlide('citation',true);
							initCitationText('citation',idx,ctitle,clink);
						});

						$("#license_data").focus(function(e){
							overlaySlide('license',true);
						});

						$("#addCitation").click(function(){
							// Code to add citation to metadata form
							var validForm=validator.form();
							console.log("validation result: " );
							if(validForm){
								console.log("validation result: " +validForm);
							var cTitle=$("#citation_title").val();
							var urllink=$("#citation_link").val();
							var curCitation = $("#citation_idx").val();
							var buttonText = $(this).val();
							if (buttonText == "ADD") {
								addCitationEntry(cTitle,urllink);
							}else{
								$("#"+curCitation).val(cTitle);
								$("#"+curCitation).text(cTitle);
								$("#"+curCitation+"_link").val(urllink);
							}
							overlaySlide("citation", false);
							}else{
								console.log("validation result: " +validForm);

							}
						});
						$("#addLicense").click(function(){
							// Code to add citation to metadata form
							var lTitle=$("#license_title").val();
							$("#license_data").val(lTitle);
							overlaySlide("license", false);
						});
						$("#preview_btn").click(function(e){
							var urllink=$("#citation_link").val();
							$("#citation_detail").attr('src',urllink);
							return false;
						});
						
						function addCitationEntry(cTitle,urllink){
							var idx = "citation"+citNumber;
							var idxName = "metadata.citations["+citNumber+"]";
							console.log(idx);
							console.log(cTitle);
							var springPath = "metadata.citations["+citNumber+"]";
							citNumber++;
							var inField = '<div class="addtext"><input type="text" id="'+idx+'" name="'+idxName+'.citation_title" path="'+springPath+'.citation_title"  value="'+cTitle+'">';
							var inField2 ='<input type="hidden" id="'+idx+'_link" name="'+idxName+'.citation_at"  path="'+springPath+'.citation_at" value="'+urllink+'">';
		                    var delBtn ='<button class="redroundbutton delete_btn" type="button"><img src="resources/images/Close_Icon.png" width="12px"></button>';
		                    var endTag="</div>";
		                    var citationEntry = inField+inField2+delBtn+endTag;
		                     $(citationEntry).appendTo(".entryArea#citation_data_entry");
		                     $(".delete_btn").click(function(e){
		                 		var tgt =e.target;
		     					console.log(tgt);
		     					$(this).parent().remove();
		     					return false;
		     				})
							$("#"+idx).focus(function(e){
								var idx = $(this).attr("id");
								var ctitle = $("#"+idx).val();
								var clink = $("#"+idx+"_link").val();
								
							console.log("Citation field in focus..."+idx);
							overlaySlide('citation',true);
							initCitationText('citation',idx,ctitle,clink);
						});
						}
						
						function createViewField(inputName, inputID, inputLabel, maxLength, value,isMultiple){
							var beginTag= "<div class='addtext'>";
							var inLabel = "<h4>"+inputLabel+"</h4>";
							var entryArea = "<div class='entryArea' id='"+inputName+"_entry'></div>";
						     var charCounter = "<span>"+maxLength+"/"+maxLength+"</span>";
							var inField = '<input type="text" class="metaEdit" name="'+inputName+'" id="'+inputID+'-v" value="'+value+'" maxlength='+maxLength+'>';
	                        var endTag="</div>";
	                        if(isMultiple){
	                        	return beginTag+inLabel+entryArea+inField+endTag;
	                        }else{
	                        	return beginTag+inLabel+inField+endTag;
	                  	
	                        }
						}
						
						function createViewTextarea(inputName, inputID, inputLabel, maxLength, value,isMultiple){
							var beginTag= "<div class='addtext'>";
							var inLabel = "<h4>"+inputLabel+"</h4>";
							var entryArea = "<div class='entryArea' id='"+inputName+"_entry'></div>";
							var inField = '<textarea name="'+inputName+'" id="'+inputID+'_v" value="'+value+'" maxlength='+maxLength+'></textarea>';
	                        var charCounter = "<span>"+maxLength+"/"+maxLength+"</span>";
	                        var addBtn ='<button class="greenroundbutton"><img src="images/Plus_Icon.png" width="12px">';
	                        var endTag="</div></div>";
	                        if(isMultiple){
	                        	return beginTag+inLabel+entryArea+inField+charCounter+endTag;
	                        }else{
	                        	return beginTag+inLabel+inField+charCounter+endTag;
	                  	
	                        }
						}
			
						
						var validator = $("#citation_f").validate({ 
							 errorPlacement: function(error, element) { // Append error within linked label
								 $( element ).closest( "form" ) .find( "label[for='" + element.attr("id" ) + "']" ) .after( error );
								 }, 
							errorElement: "span",
							debug:true,
							success:function(form) {
								if(form.children(".error").length==0){
							    $("#addCitation").removeAttr('disabled');
							    $("#addCitation").css("background-color","#39b45a");
								}
							  },
							//onkeyup:true,
							//onfocusout: true,
							highlight: function(element, errorClass, validClass) {
							    $(element).addClass(errorClass).removeClass(validClass);
							    $(element.form).find("h4[title=" + element.id + "]")
							      .addClass(errorClass);
							    $(element.form).find("input[id=" + element.id + "]")
							      .addClass(errorClass);
							  },
							  unhighlight: function(element, errorClass, validClass) {
							    $(element).removeClass(errorClass).addClass(validClass);
							    $(element.form).find("h4[title=" + element.id + "]")
							      .removeClass(errorClass);
							    $(element.form).find("input[id=" + element.id + "]")
							      .removeClass(errorClass);
							  },
							rules: { 
								citation_title: {
									required:true },
								citation_link:  {
									required:true,
									url:true
								}
							},
/*							submitHandler: function(form) {
							    $("#addCitation").removeAttr('disabled');
							    $("#addCitation").css("background-color","#39b45a");
							  },
							invalidHandler: function(event, validator) {
								$("#addCitation").attr('disabled','disabled');
								 $("#addCitation").css("background-color","#e6e6e6");
							  },*/
						   	messages: { 
						   		citation_title: { 
						   			required:"Please enter a title for this citation."}, 
						   		citation_link: {
						   			required:"A valid URL link for your citation is required.",
						   			url:"Please enter a valid URL link for your citation."}
						   		}
							});
/*						 
						$("#addCitation").attr('disabled','disabled');
						$("#addCitation").css("background-color", "#e6e6e6");
						validator.form();*/
				});
