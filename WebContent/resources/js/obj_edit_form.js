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
/*					for(var i=0;i<numberofFields;i++){
						console.log(isMultiples[i]);
						if(i!=1){
							inputField = createViewField(inputNames[i], inputIDs[i], inputLabels[i], maxLengths[i],values[i],isMultiples[i]);

						}else{
							inputField = createViewTextarea(inputNames[i], inputIDs[i], inputLabels[i], maxLengths[i],values[i],isMultiples[i]);
						}
						$(inputField).appendTo("#metadata_view");
					}
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
					
/*					function autoresize(element) {
						var eid= $(this).attr("id");
						console.log("autoresize"+eid);
					    $(this).css("height","0px");     //Reset height, so that it not only grows but also shrinks
					    $(this).style.height = ($(this).scrollHeight+10) + 'px';    //Set new height
					}
					
					$('.autosize').each(autoresize());*/
//					$("#payloadTextArea-v").css("height","0px"); 

//					$("#payloadTextArea-v").css("height", sh + 'px');   

/*						var h = $("#inputTextArea-clone").height();
						console.log("Height:"+h);
						$("#inputTextArea-v").css("height", h + 'px');  */
					
/*					var next = 1;
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
				
				
*/
						$(".delete_btn").click(function(e){
							var tgt =e.target;
	     					console.log(tgt);
	     					$(this).parent().remove();
	     					return false;
	     				});
						
						$("#citation_data").focus(function(e){
							console.log("New citation field in focus...");
							resetCitationText();
							overlaySlide('citation',true);
						});
						
					
						$(".entryArea input[id^='citation']")
							.focus(function(e){
								var idx = $(this).attr("id");
								var ctitle = $("#"+idx).val();
								var clink = $("#"+idx+"_link").val();
							console.log("Citation field in focus..."+idx);
							initCitationText('citation',idx,ctitle,clink);
							overlaySlide('citation',true);
						});
						$("#license_data").focus(function(e){
							console.log("New license field in focus...");
							overlaySlide('license',true);
						});

						$("#addCitation").click(function(){
							// Code to add citation to metadata form
							var cTitle=$("#citation_title").val();
							var urllink=$("#citation_link").val();
							var curCitation = $("#citation_idx").val();
							var buttonText = $(this).val();
							console.log(buttonText+" CC: "+curCitation+ " "+cTitle);
							if (buttonText == "ADD") {
								console.log("Adding CC: "+curCitation+ " "+cTitle);

								addCitationEntry(cTitle,urllink);
							}else{
								console.log("Updating  CC: "+curCitation+ " "+cTitle);

								$("#"+curCitation).val(cTitle);
								$("#"+curCitation).text(cTitle);
								
								$("#"+curCitation+"_link").val(urllink);
							}
							overlaySlide("citation", false);
						});
						$("#addLicense").click(function(){
							// Code to add citation to metadata form
							var lTitle=$("#license_title").val();
							$("#license_data").val(lTitle);
							overlaySlide("license", false);
						});
						$("#preview_btn").click(function(e){
							var urllink=$("#citation_link").val();
							console.log(urllink);
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
			
						
						
				});
