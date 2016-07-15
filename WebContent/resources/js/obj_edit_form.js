'use strict';

$(document)
		.ready(
				function() {
					document.getElementById('file_payload').addEventListener(
							'change', readMultipleFiles, false);
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
					$('#description_data').keyup(updateCount);
					$('#description_data').keydown(updateCount);
					$("[id$='_data']").each(updateCount);

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
					
/*					function autoresize() {
						var eid= $(this).Attr("id");
						console.log(eid);
					    $(this).css("height","0px");     //Reset height, so that it not only grows but also shrinks
					    $(this).style.height = ($(this).scrollHeight+10) + 'px';    //Set new height
					}
					
					$('.autosize').each(autoresize());*/
//					$("#payloadTextArea-v").css("height","0px"); 

//					$("#payloadTextArea-v").css("height", sh + 'px');   

						var h = $("#inputTextArea-clone").height();
						console.log("Height:"+h);
						$("#inputTextArea-v").css("height", h + 'px');  
					
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


