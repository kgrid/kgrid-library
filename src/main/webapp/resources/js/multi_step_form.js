'use strict';

var curTitle = "";
var curURI = "";
var urlPrefix = "knowledgeObject";
var fObj=new Object();
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

function overlayHeightResize(overlayID, window_height){
	var overlayPane =$('#'+overlayID).find("> .ol_pane");
	var entryform = overlayPane.find(".entryform");
	var ol_pane_height =window_height;
	var calcHeight = (window_height-120);
	entryform.css("height",calcHeight+"px");
	var ef_margin = (ol_pane_height-calcHeight)/2;
	var addContent = entryform.find(".Add-content");
	addContent.css("height",(calcHeight-70)+"px");
	return ol_pane_height;
}

function overlaySlide(overlayID, open){
    document.body.classList.toggle('noscroll', open);
	var overlayPane =$('#'+overlayID).find("> .ol_pane");
	var window_width= $(window).width();
	var window_height = $(window).height();
	var overlayPane_width=overlayPane.width();
	var overlayPane_height=	overlayHeightResize(overlayID, window_height);

	var overlayPane_left = window_width-overlayPane_width;
	if(overlayPane_left<=(window_width*0.27)){
		overlayPane_left=(window_width*0.27);
	}
	if(open){
		$('#'+overlayID).css("display","block");
        $('#'+overlayID).fadeIn('fast',function(){
            overlayPane.animate({'left':overlayPane_left+"px"},1000);
        });
    }else{
		$('#'+overlayID).css("display","none");
    	overlayPane.animate({'left':'100%'},1000,function(){
            $('#'+overlayID).fadeOut('fast');
        });
    }

	if(overlayID=="addObject"){
		console.log("Overlay IN with URI:"+curURI);
		if(curURI!=""){
			$("#begin_page").hide();
			$("#entry_form1").show();
		}else{
			$("#begin_page").show();
			$("#entry_form1").hide();
			
		}
		resetInputText(curURI);
	}
	if(overlayID=="libraryuser"){
		resetUserInfoText();
	}
	if(overlayID=="citation"){
		resetCitationText();
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

function resetCitationText() {
	$("#citation_title").val("");
	$("#citation_link").val("");
	$("#citation_detail").attr("src", "");
	$("#entry_form").show();
}

function resetUserInfoText() {
	$("input[id$='_data']").val("");
	$("#pwd_data").prop('disabled', false);
	$("#addUserButton").text("ADD USER");
	document.getElementById("addUserButton").style.display = "block";
	document.getElementById("cancelButton").style.display = "block";
}

function getSection(uri, section) {
	$.ajax({
		type : 'GET',
		url : "knowledgeObject/" + uri +"/"+section,
		success : function(response, tStatus, xhr) {
			console.log("GET request:\n"+"knowledgeObject/" + uri +"/"+section);
			//console.log("GET response:\n"+response);
			console.log("GET Text Status:\n"+tStatus);
			//console.log("GET jqXHR:\n"+xhr);
			var test = JSON.stringify(response);
			console.log(test);
		},
		failure : function(response){
			console.log("GET response:\n"+response);
			//var test = JSON.stringify(response);
			//alert(test);
		}
	});

}
function resetInputText(uri) {

	$(".current-tab").removeClass("current-tab");
	$("#progressbar li:first-child").addClass("current-tab");
	$('#progressbar li').children("img").css("display", "none");
	$('#addObj .fieldcontainer').css("display", "none");
	$('#first').css("display", "block");
	$('#payloadTextArea').val("");
	$('#functionName').val("");
	$('#inputTextArea').val("");
	$('#outputTextArea').val("");
	$("input[id$='_data']").val("");
	$("#description_data").val("");
	$("div[id$='_entry']").children().remove();
	retrieveObjectContent(uri,"metadata");
	retrieveObjectContent(uri,"payload");
	retrieveObjectContent(uri,"inputMessage");
	retrieveObjectContent(uri,"outputMessage");
//	retrieveObjectContent(uri,"logData");

}

function parseJSON(data) {
	return window.JSON && window.JSON.parse ? window.JSON.parse(data)
			: (new Function("return " + data))();
}

function setURI(uri){
	curURI=uri;
	console.log(curURI);
}

function readMultipleFiles(evt) {
	var field_id = this.id;
	var sect_id = field_id.replace("file_", "");
	var files = evt.target.files;
	if (files) {
		for (var i = 0, f; f = files[i]; i++) {
			var r = new FileReader();
			r.onload = (function(f) {
				return function(e) {
					var contents = e.target.result;
					$("#" + sect_id + "DropFile").hide();
					$("#" + sect_id + "TextAreaDisplay").show();
					$("#" + sect_id + "TextArea").val(contents);
				};
			})(f);
			r.readAsText(f);
		}
	} else {
		alert("Failed to load files");
	}
}

function retrieveObjectContent(uri, section) {
	if (uri != "") {
		var ajaxMethod="GET";
		var ajaxUrl = urlPrefix+"/"+uri;
		if(section!=""){
			ajaxUrl = ajaxUrl+"/"+section;
		}
		console.log("URL: "+ ajaxUrl);
		$.ajax({
			type : ajaxMethod,
			url : ajaxUrl,
			success : function(response, tStatus, xhr) {
					console.log("GET request:\n"+"knowledgeObject/" + uri +"/"+section);
					//console.log("GET response:\n"+response);
					console.log("GET Text Status:\n"+tStatus);
				if (response != 'empty') {
					fObj = response;
					console.log(fObj);
					switch(section){
					
					case "metadata":
						console.log(" Reset Page Title to: " +fObj.title);
						$("#page_title").text(fObj.title);
						$("#title_data").val(fObj.title);
						$("#description_data").val(fObj.description);
						$("#keyword_data").val(fObj.keywords);
						$("#owner_data").val(fObj.owner);
						$("#contributor_data").val(fObj.contributors);
					break;
					case "payload":
						$("#functionName").val(fObj.functionName);
						$("#engineType").val(fObj.engineType);
						if(fObj.content!=""){
							$("#payloadTextAreaDisplay").show();
							$("#payloadDropFile").hide();
							$('#payloadTextArea').val(fObj.content);
						}else{
							$("#payloadTextAreaDisplay").hide();
							$("#payloadDropFile").show();
						}
						break;
					case "inputMessage":
						console.log(" Display Input: " +fObj);
						if(fObj!=""){
							$("#inputTextAreaDisplay").show();
							$("#inputDropFile").hide();
							$('#inputTextArea').val(fObj);
						}else{
							$("#inputTextAreaDisplay").hide();
							$("#inputDropFile").show();
						}
						
						break;
					case "outputMessage":
						if(fObj!=""){
							$("#outputTextAreaDisplay").show();
							$("#outputDropFile").hide();
							$('#outputTextArea').val(fObj);
						}else{
							$("#outputTextAreaDisplay").hide();
							$("#outputDropFile").show();
						}
						break;
					case "logData":
						
						break;
					default:
						break;
				}}
			},
			error : function(response) {
				document.getElementById("successResult").innerHTML = "ERROR";
				
			}
		});

	}
}

function updateObject(section) {

	var fedoraObject = new Object();
	var metadata = new Object();
	var ajaxUrl;
	var ajaxSuccess;
	var ajaxMethod;
	console.log("constructing data elements for FedoraObject...");
	var uri =curURI;
	console.log("URI:"+uri);
	if (uri == "") {// new Object
		ajaxMethod="POST";
		ajaxUrl = urlPrefix;
		ajaxSuccess = " Your Object Has Been Added To Your Library!<br> To make changes click on the edit button in each section.<br> Object ID: ";
		metadata.title = document.getElementById("new_title_data").value;
	} else {
		ajaxMethod="PUT";
		ajaxUrl = urlPrefix +"/"+ uri;
		if(section!=""){
			ajaxUrl=ajaxUrl+ "/" +section;
		}	
		ajaxSuccess = " Your Object Has Been Updated In Your Library!<br> To make changes click on the edit button in each section.<br> Object ID: ";
		metadata.title = document.getElementById("title_data").value;
		metadata.owner = document.getElementById("owner_data").value;
		metadata.description = document.getElementById("description_data").value;
		metadata.contributors = document.getElementById("contri_data").value;
		metadata.keywords = document.getElementById("keyword_data").value;
		// metadata.license = document.getElementById("license_data").value;
		var ctitle;
		var clink;
		var citations = [];
		$("#citation_data_entry").find('input').each(function(index, element) {
			var citation = new Object();
			ctitle = $(element).attr("title");
			clink = $(element).attr("url");
			console.log(ctitle + " " + clink);
			citation.citation_title = ctitle;
			citation.citation_at = clink;
			citations.push(citation);
		});
		metadata.citations = citations;
		console.log("Metadata element done.");
		
		var payload = new Object();
		payload.functionName = document
				.getElementById("functionName").value;
		payload.engineType = document.getElementById("engineType").value;
		payload.content = document.getElementById("payloadTextArea").value;

		
		console.log("Payload element done.");
		
		var inputMessage= document.getElementById("inputTextArea").value;
//		console.log("INPUT: "+inputMessage);
		var outputMessage= document.getElementById("outputTextArea").value;
//		console.log("OUTPUT: "+outputMessage);
		console.log("I/O element done.");

	}
	var text;
	switch (section) {
	case "metadata":
		text = JSON.stringify(metadata);
		break;
	case "payload":
		text = JSON.stringify(payload);
		break;
	case "inputMessage":
		text = inputMessage;
		break;
	case "outputMessage":
		text = outputMessage;
		break;
	case "new":
		fedoraObject.metadata = metadata;
		text = JSON.stringify(fedoraObject);
		break;
	default: // full object Add or Edit
		fedoraObject.metadata = metadata;
		fedoraObject.payload = payload;
		fedoraObject.inputMessage = inputMessage;
		fedoraObject.outputMessage = outputMessage;
		text = JSON.stringify(fedoraObject);
		break;
	}

	console.log("Data to be sent: "+text);
	saveToServer(ajaxMethod,ajaxUrl,ajaxSuccess,text);

	$("#begin_page").hide();
	$("#end_page").show();
	curTitle = metadata.title;
}

function setActiveTab(section){
	$('.current-tab').removeClass('current-tab');
	$('#progressbar #'+section).addClass('current-tab');
	var nthChild = $('#progressbar #'+section).index()+1;
	console.log("Progress BAr index:"+nthChild);
	$(".fieldcontainer:nth-child(" + nthChild + ")").css({
		'display' : 'inline-block'
	});
}
	
function saveToServer(ajaxMethod,ajaxUrl,ajaxSuccess,text)	{
	$.ajax({
				beforeSend : function(xhrObj) {
					xhrObj.setRequestHeader("Content-Type", "application/json");
					xhrObj.setRequestHeader("Accept", "application/json");
				},
				type : ajaxMethod,
				url : ajaxUrl,
				data : text,
				dataType : "json",
				success : function(response) {
					console.log(response);
					if ((response != 'empty')&&(response!=null)) {
						var test = JSON.stringify(response);
						var obj = JSON.parse(test);
						document.getElementById("successResult").innerHTML = ajaxSuccess + obj.uri;
						curURI=obj.uri;
					}
				},
				error : function(response) {
					document.getElementById("successResult").innerHTML = "ERROR";
					// test code for
					$("#begin_page").show();
				}
			});
}

function addObjContent() {
	$("#entry_form1").show();
	$("#end_page").hide();
	$("#page_title").text(curTitle);
	$("#title_data").val(curTitle);
	$("[id$='_data']").each(updateCount);
	// enableEdit();
}

$(document)
		.ready(
				function() {
					$('[id^="file_"]').on('change', readMultipleFiles);
					var inputLabels = [ "TITLE", "DESCRIPTION", "KEYWORDS",
							"OWNERS", "CONTRIBUTORS", "CITATIONS", "LICENSE" ];
					var inputNames = [ "title", "description_data",
							"keyword_data", "owner_data", "contri_data",
							"citation_data", "license_data" ];
					var inputIDs = [ "title_data", "description_data",
							"keyword_data", "owner_data", "contri_data",
							"citation_data", "license_data" ];
					var maxLengths = [ 140, 500, 140, 140, 140, 500, 140 ];
					var placeholderTexts = [
							"A title, which is a formal name.",
							"A text description of the object - akin to an abstract - maybe enetered for any object.",
							"Click here to add Keywords.",
							"Click here to add Owners.",
							"Click here to add Contributors.",
							"Click here to add Citations",
							"Click here to add License" ];
					var isMultiples = [ false, false, true, true, true, true,
							false ];
					var numberofFields = inputLabels.length;
					var inputField;
					for (var i = 0; i < (numberofFields); i++) {
						//console.log(isMultiples[i]);
						if (i != 1) {
							inputField = createInputField(inputNames[i],
									inputIDs[i], inputLabels[i], maxLengths[i],
									placeholderTexts[i], isMultiples[i]);

						} else {
							inputField = createInputTextarea(inputNames[i],
									inputIDs[i], inputLabels[i], maxLengths[i],
									placeholderTexts[i], isMultiples[i]);
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
					 * output_file:"required" }, messages: { title_data: {
					 * required:"Please enter a title for your object."},
					 * description_data: "Please enter a short description for
					 * your object.", keyword_data: "Please enter at least one
					 * keyword for your object.", owner_data:"Please enter at
					 * least one owner for your object.", contri_data:"Please
					 * enter at least one contributor for your object.",
					 * payload_func:"Please enter a function name for your
					 * payload.", payload_type:"Please select the engine type
					 * for your payload.", payload_file:"Please choose and
					 * upload a file as your payload.", input_file:"Please
					 * choose and upload a file as your input message.",
					 * output_file:"Please choose and upload a file as your
					 * output message." } });
					 */
					var count = 0; // To Count Blank Fields
					/*------------ Validation Function-----------------*/
					$("[id$='ClearBtn']").click(function(event) {
						event.preventDefault();
						var btn_id = this.id;
						console.log(btn_id);
						var tArea_id = btn_id.replace("ClearBtn", "TextArea");
						var dropfile = btn_id.replace("ClearBtn", "DropFile");
						$("#" + dropfile).show();
						$("#" + tArea_id + "Display").hide();
						$("#" + tArea_id).text("");

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

					$("#engineType").click(function() {
						if ($(this).hasClass("up")) {
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
							cs_color = "red";
						}
						$(this).parent().children("span").text(s);
						$(this).parent().children("span")
								.css("color", cs_color);
					}
					;

					function createInputField(inputName, inputID, inputLabel,
							maxLength, placeholderText, isMultiple) {
						var beginTag = "<div>";
						var inLabel = "<h4>" + inputLabel + "</h4>";
						var entryArea = "<div class='entryArea' id='"
								+ inputName + "_entry'></div>";
						var inField = '<div class="addtext"><input spellcheck="false" type="text" name="'
								+ inputName
								+ '" id="'
								+ inputID
								+ '" placeholder="'
								+ placeholderText
								+ '" maxlength=' + maxLength + '>';
						var charCounter = "<span>" + maxLength + "/"
								+ maxLength + "</span>";
						var addBtn = '<button class="greenroundbutton"><img src="/ObjectTeller/resources/images/Plus_Icon.png" width="12px">';
						var endTag = "</div></div>";
						if (isMultiple) {
							return beginTag + inLabel + entryArea + inField
									+ charCounter + endTag;
						} else {
							return beginTag + inLabel + inField + charCounter
									+ endTag;
						}
					}

					function createInputTextarea(inputName, inputID,
							inputLabel, maxLength, placeholderText, isMultiple) {
						var beginTag = "<div>";
						var inLabel = "<h4>" + inputLabel + "</h4>";
						var entryArea = "<div class='entryArea' id='"
								+ inputName + "_entry'></div>";
						var inField = '<div class="addtext"><textarea spellcheck="false" name="'
								+ inputName + '" id="' + inputID
								+ '" placeholder="' + placeholderText
								+ '" maxlength=' + maxLength + '></textarea>';
						var charCounter = "<span>" + maxLength + "/"
								+ maxLength + "</span>";
						var addBtn = '<button class="greenroundbutton"><img src="images/Plus_Icon.png" width="12px">';
						var endTag = "</div></div>";
						if (isMultiple) {
							return beginTag + inLabel + entryArea + inField
									+ charCounter + endTag;
						} else {
							return beginTag + inLabel + inField + charCounter
									+ endTag;
						}
					}

					var next = 1;
					$("input[id$='_data']").click(function(e) {
						e.preventDefault();
					});
					$("input[id$='_title']").click(function(e) {
						e.preventDefault();
					});
					$("input[id$='_link']").click(function(e) {
						e.preventDefault();
					});
					$("#citation_data").focus(function(e) {
						console.log("New citation field in focus...");
						overlaySlide('citation', true);
					});
					$("#license_data").focus(function(e) {
						console.log("New license field in focus...");
						overlaySlide('license', true);
					});
					var citNumber = 0;
					$("#addCitation").click(function(){
						// Code to add citation to metadata form
						var validForm=citation_validator.form();
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
					$("#addLicense").click(function() {
						// Code to add citation to metadata form
						var lTitle = $("#license_title").val();
						$("#license_data").val(lTitle);
						overlaySlide("license", false);
					});
					function addCitationEntry(cTitle,urllink){
						var idx = "citation"+citNumber;
						var idxName = "metadata.citations["+citNumber+"]";
						console.log(idx);
						console.log(cTitle);
						var springPath = "metadata.citations["+citNumber+"]";
						citNumber++;
						var inField = '<div class="addtext"><input type="text" id="'+idx+'_title"'+'" name="'+idxName+'.citation_title" path="'+springPath+'.citation_title"  value="'+cTitle+'">';
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
						$("#"+idx+"_title").focus(function(e){
							var idx = $(this).attr("id");
							var ctitle = $("#"+idx).val();
							var linkid=idx.replace("title","link");
							var clink = $("#"+linkid).val();
							
						console.log("Citation field in focus..."+idx);
						overlaySlide('citation',true);
						initCitationText('citation',idx,ctitle,clink);
					});
					}
					function initCitationText(overlayID, cidx, ctitle, clink){
						console.log("Citation Index:"+cidx+" Title:"+ctitle+" Link:"+clink);
						$( "#citation_title" ).val( ctitle );
						$( "#citation_link" ).val( clink );
						$("#citation_detail").attr("src","");
						$("#addCitation").val("UPDATE");
						$("#citation_idx").val(cidx);
						$('.addtext>input').each(updateCount);
					}
					$("button").click(function(e) {
						e.preventDefault();
					});
					$("#preview_btn").click(function(e) {
						var urllink = $("#citation_link").val();
						console.log(urllink);
						$("#citation_detail").attr('src', urllink);

					});
					$("#license_preview_btn").click(
							function(e) {
								var urllink = $("#license_link").val();
								console.log(urllink);
								var myWindow = window.open(urllink, "myWindow",
										"width=400,height=700"); // Opens a
																	// new
																	// window
								myWindow.focus();
							});
					
					var citation_validator = $("#citation_f").validate({ 
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
					   	messages: { 
					   		citation_title: { 
					   			required:"Please enter a title for this citation."}, 
					   		citation_link: {
					   			required:"A valid URL link for your citation is required.",
					   			url:"Please enter a valid URL link for your citation."}
					   		}
						});
					
				});