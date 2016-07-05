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
						$("#entryform").css("display", "none");
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

					}
					;
					var next = 1;
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