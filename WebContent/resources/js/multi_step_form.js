'use strict';

$(document)
		.ready(
				function() {
					document.getElementById('file_payload').addEventListener(
							'change', readMultipleFiles, false);
					document.getElementById('file_input').addEventListener(
							'change', readInputFiles, false);
					document.getElementById('file_output').addEventListener(
							'change', readOutputFiles, false);
					
					
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
					var isMultiples = [false,false,true,true,true,true,false];
					var numberofFields = inputLabels.length;
					var inputField;
					for(var i=0;i<numberofFields;i++){
						console.log(isMultiples[i]);
						if(i!=1){
							inputField = createInputField(inputNames[i], inputIDs[i], inputLabels[i], maxLengths[i],placeholderTexts[i],isMultiples[i]);

						}else{
							inputField = createInputTextarea(inputNames[i], inputIDs[i], inputLabels[i], maxLengths[i],placeholderTexts[i],isMultiples[i]);
						}
						$(inputField).appendTo("#metadata_fields");
					}
					
					
					
					/*
					 * $("#addObj_f").validate({ errorPlacement: function(error,
					 * element) { // Append error within linked label $( element )
					 * .closest( "form" ) .find( "label[for='" + element.attr(
					 * "id" ) + "']" ) .after( error ); }, errorElement: "span",
					 * debug:true, rules: { title_data: {required:true },
					 * description_data: "required", keyword_data: "required",
					 * owner_data: "required", contri_data:"required",
					 * payload_func:"required", payload_type:"required",
					 * payload_file:"required", input_file:"required",
					 * output_file:"required"
					 *  }, messages: { title_data: { required:"Please enter a
					 * title for your object."}, description_data: "Please enter
					 * a short description for your object.", keyword_data:
					 * "Please enter at least one keyword for your object.",
					 * owner_data:"Please enter at least one owner for your
					 * object.", contri_data:"Please enter at least one
					 * contributor for your object.", payload_func:"Please enter
					 * a function name for your payload.", payload_type:"Please
					 * select the engine type for your payload.",
					 * payload_file:"Please choose and upload a file as your
					 * payload.", input_file:"Please choose and upload a file as
					 * your input message.", output_file:"Please choose and
					 * upload a file as your output message." } });
					 */
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
						$('.current-tab>img').css("color", "#666666");
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
/*					$("#addObjButton").click(function() {
						$("#entry_form").css("display", "none");
						$("#end_page").css("display", "block");

					});*/
					$("#engineType").click(function(){
						
					    if($(this).hasClass("up")){
					    	$(this).removeClass("up");
					    } else {
					    	$(this).addClass("up");
					    }
					});
					$('.addtext>input').keyup(updateCount);
					$('.addtext>input').keydown(updateCount);
					$('#description_data').keyup(updateCount);
					$('#description_data').keydown(updateCount);

					function updateCount() {
						/* alert("key typed!"); */
						var cs = $(this).val().length;
						var elementid = this.id;
						var count = $(this).parent().children("span").text();
						var maxl_s = count.substring(count.length - 3,
								count.length)
						var maxl = parseInt(maxl_s);
						var csl = maxl - cs;
						var s = csl.toString().concat("/" + maxl_s);
						var cs_color = "lightgreen";
						if (csl < 10) {
							cs_color = "yellow";
							if (csl < 5) {
								cs_color = "orange";
								if (csl <= 0)
									cs_color = "red";
							}
						}
						$(this).parent().children("span").text(s);
						$(this).parent().children("span")
								.css("color", cs_color);
					};
					
					function createInputField(inputName, inputID, inputLabel, maxLength, placeholderText,isMultiple){
						var beginTag= "<div>";
						var inLabel = "<h4>"+inputLabel+"</h4>";
						var entryArea = "<div class='entryArea' id='"+inputName+"_entry'></div>";
						var inField = '<div class="addtext"><input type="text" name="'+inputName+'" id="'+inputID+'" placeholder="'+placeholderText+'" maxlength='+maxLength+'>';
                        var charCounter = "<span>"+maxLength+"/"+maxLength+"</span>";
                        var addBtn ='<button class="greenroundbutton"><img src="/ObjectTeller/resources/images/Plus_Icon.png" width="12px">';
                        var endTag="</div></div>";
                        if(isMultiple){
                        	return beginTag+inLabel+entryArea+inField+charCounter+endTag;
                        }else{
                        	return beginTag+inLabel+inField+charCounter+endTag;
                  	
                        }
					}
					
					function createInputTextarea(inputName, inputID, inputLabel, maxLength, placeholderText,isMultiple){
						var beginTag= "<div>";
						var inLabel = "<h4>"+inputLabel+"</h4>";
						var entryArea = "<div class='entryArea' id='"+inputName+"_entry'></div>";
						var inField = '<div class="addtext"><textarea name="'+inputName+'" id="'+inputID+'" placeholder="'+placeholderText+'" maxlength='+maxLength+'></textarea>';
                        var charCounter = "<span>"+maxLength+"/"+maxLength+"</span>";
                        var addBtn ='<button class="greenroundbutton"><img src="images/Plus_Icon.png" width="12px">';
                        var endTag="</div></div>";
                        if(isMultiple){
                        	return beginTag+inLabel+entryArea+inField+charCounter+endTag;
                        }else{
                        	return beginTag+inLabel+inField+charCounter+endTag;
                  	
                        }
					}

			        	
					var next = 1;
				$("input[id$='_data']").click(function(e){
					e.preventDefault();
				});
				$("input[id$='_title']").click(function(e){
					e.preventDefault();
				});
				$("input[id$='_link']").click(function(e){
					e.preventDefault();
				});
				$("#citation_data").focus(function(e){
					console.log("New citation field in focus...");
					overlaySlide('citation',true);
				});
				$("#license_data").focus(function(e){
					console.log("New license field in focus...");
					overlaySlide('license',true);
				});
				var citNumber = 0;
				$("#addCitation").click(function(){
					// Code to add citation to metadata form
					var cTitle=$("#citation_title").val();
					var urllink=$("#citation_link").val();
					addCitationEntry(cTitle,urllink);
					overlaySlide("citation", false);
				});
				$("#addLicense").click(function(){
					// Code to add citation to metadata form
					var lTitle=$("#license_title").val();
					$("#license_data").val(lTitle);
					overlaySlide("license", false);
				});
				function addCitationEntry(cTitle,urllink){
					var idx = "citation"+citNumber;
					citNumber++;
					console.log(idx);
					console.log(cTitle);
					var inField = '<div class="addtext"><input type="text" id="'+idx+'" disabled placeholder="'+cTitle+'">';
                    var delBtn ='<button class="redroundbutton" id="delete_btn"><img src="resources/images/Close_Icon.png" width="12px">';
                    var endTag="</div>";
                    var citationEntry = inField+delBtn+endTag;
                     $(citationEntry).appendTo(".entryArea#citation_data_entry");
                     $("#"+idx).attr("url",urllink);
                     $("#"+idx).attr("title",cTitle);
                     $("#delete_btn").click(function(e){
     					var tgt =e.target;
     					console.log(tgt);
     					$(this).parent().remove();
     				})
				}

				
				
				
				$("button").click(function(e){
					e.preventDefault();
				});
				$("#preview_btn").click(function(e){
					var urllink=$("#citation_link").val();
					console.log(urllink);
					$("#citation_detail").attr('src',urllink);
/*					var myWindow = window.open(urllink, "myWindow", "width=400,height=700");   // Opens a new window
					myWindow.focus();*/
/*					var resp =myWindow.confirm("Is the displayed link correct?");
					if(resp){
						console.log("citation added.");
						// Code to add citation
					}else{
						console.log("citation added.");
						// Go back to citation overlay
					}*/
					//myWindow.close();
					
				});
				$("#license_preview_btn").click(function(e){
					var urllink=$("#license_link").val();
					console.log(urllink);
					var myWindow = window.open(urllink, "myWindow", "width=400,height=700");   // Opens a new window
					myWindow.focus();
/*					var resp =myWindow.confirm("Is the displayed link correct?");
					if(resp){
						console.log("citation added.");
						// Code to add citation
					}else{
						console.log("citation added.");
						// Go back to citation overlay
					}*/
					//myWindow.close();
					
				});
					$(".add-more1")
							.click(
									function(e) {
										e.preventDefault();
										var addto = "#field" + next;
										var addRemove = "#field" + (next);
										next = next + 1;
										var newIn = '<input type="text" name="keyword_data'
												+ next
												+ '" id="keyword_data'
												+ next
												+ '" placeholder="A list of up to 7 keywords may be entered for any object." maxlength=140><span>0/140</span>';
										var newInput = $(newIn);
										var removeBtn = '<button id="remove'
												+ (next - 1)
												+ '" class="greenbutton remove-me" style="background-color:orange;" >-</button></div><divclass="addtext">';
										var removeButton = $(removeBtn);
										$(addto).after(newInput);
										$(addRemove).after(removeButton);
										$("#keyword_data" + next).attr(
												'data-source',
												$(addto).attr('data-source'));
										$("#count").val(next);

										$('.remove-me')
												.click(
														function(e) {
															e.preventDefault();
															var fieldNum = this.id
																	.charAt(this.id.length - 1);
															var fieldID = "#keyword_data"
																	+ fieldNum;
															$(this).remove();
															$(fieldID).remove();
														});
									});

				});