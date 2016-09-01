'use strict';

var curTitle = "";
var curURI = "";
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

function resetInputText() {
	$(".current-tab").removeClass("current-tab");
	$("#progressbar li:first-child").addClass("current-tab");
	$('#progressbar li').children("img").css("display", "none");
	$('#addObj .fieldcontainer').css("display", "none");
	$('#first').css("display", "block");
	$("#begin_page").show();
	$("#end_page").hide();
	$("#entry_form1").hide();
	$('#payloadTextArea').val("");
	$('#functionName').val("");
	$('#inputTextArea').val("");
	$('#outputTextArea').val("");
	$("input[id$='_data']").val("");
	$("#description_data").val("");
	$("div[id$='_entry']").children().remove();
}
function parseJSON(data) {
	return window.JSON && window.JSON.parse ? window.JSON.parse(data)
			: (new Function("return " + data))();
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

function retrieveObjectContent(fedoraObj) {
	if (fedoraObj != "") {

	}
}

function updateObject(section) {
	var urlPrefix = "knowledgeObject";
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

		var payloadDescriptor = new Object();
		payloadDescriptor.functionName = document
				.getElementById("functionName").value;
		payloadDescriptor.engineType = document.getElementById("engineType").value;
		console.log("Payload element done.");
	}
	var text;
	switch (section) {
	case "metadata":
		fedoraObject.metadata = metadata;
		text = JSON.stringify(metadata);
		break;
	case "payload":
		fedoraObject.payloadDescriptor = payloadDescriptor;
		fedoraObject.payload = document.getElementById("payloadTextArea").value;
		text = JSON.stringify(payload);
		break;
	case "input":
		fedoraObject.inputMessage = document.getElementById("inputTextArea").value;
		text = JSON.stringify(inputMessage);
		break;
	case "output":
		fedoraObject.outputMessage = document.getElementById("outputTextArea").value;
		text = JSON.stringify(outputMessage);
		break;
	case "new":
		fedoraObject.metadata = metadata;
		text = JSON.stringify(fedoraObject);
		break;
	default: // full object Add or Edit
		fedoraObject.metadata = metadata;
		fedoraObject.payloadDescriptor = payloadDescriptor;
		fedoraObject.payload = document.getElementById("payloadTextArea").value;
		fedoraObject.inputMessage = document.getElementById("inputTextArea").value;
		fedoraObject.outputMessage = document.getElementById("outputTextArea").value;
		text = JSON.stringify(fedoraObject);
		break;
	}


	console.log("Data to be sent: "+text);
	$("#begin_page").hide();
	$("#end_page").show();
	curTitle = metadata.title;

	$
			.ajax({
				beforeSend : function(xhrObj) {
					xhrObj.setRequestHeader("Content-Type", "application/json");
					xhrObj.setRequestHeader("Accept", "application/json");
				},
				type : ajaxMethod,
				url : ajaxUrl,
				data : text,
				dataType : "json",

				success : function(response) {

					if (response != 'empty') {
						console.log(response);
						var test = JSON.stringify(response);
						var obj = JSON.parse(test);
						document.getElementById("successResult").innerHTML = ajaxSuccess
								+ obj.uri;
						//$("#entry_form1").show();
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
	$("#page_title").text(curTitle);
	$("#title_data").val(curTitle);
	// enableEdit();
}

function addNewObject() {
	var fedoraObject = new Object();
	var metadata = new Object();
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
	fedoraObject.metadata = metadata;
	fedoraObject.payload = document.getElementById("payloadTextArea").value;
	fedoraObject.inputMessage = document.getElementById("inputTextArea").value;
	fedoraObject.outputMessage = document.getElementById("outputTextArea").value;

	var payloadDescriptor = new Object();
	payloadDescriptor.functionName = document.getElementById("functionName").value;
	payloadDescriptor.engineType = document.getElementById("engineType").value;
	fedoraObject.payloadDescriptor = payloadDescriptor;

	var text = JSON.stringify(fedoraObject);

	$("#entry_form1").hide();
	$("#end_page").show();
	$
			.ajax({
				beforeSend : function(xhrObj) {
					xhrObj.setRequestHeader("Content-Type", "application/json");
					xhrObj.setRequestHeader("Accept", "application/json");
				},
				type : 'POST',
				url : "createNewObject",
				data : text,
				dataType : "json",

				success : function(response) {

					if (response != 'empty') {
						var test = JSON.stringify(response);
						var obj = JSON.parse(test);
						document.getElementById("successResult").innerHTML = " Your New Object Has Been Added To Your Library!<br> To make changes click on the edit button in each section.<br> Object ID: "
								+ obj.uri;
					}
				},

				error : function(response) {
					document.getElementById("successResult").value = "ERROR";
				}
			});

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
					for (var i = 0; i < (numberofFields - 1); i++) {
						console.log(isMultiples[i]);
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
					/*
					 * $("#addObjButton").click(function() {
					 * $("#entry_form").css("display", "none");
					 * $("#end_page").css("display", "block");
					 * 
					 * });
					 */
					$("#engineType").click(function() {

						if ($(this).hasClass("up")) {
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
						var inField = '<div class="addtext"><input type="text" name="'
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
						var inField = '<div class="addtext"><textarea name="'
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
					$("#addCitation").click(function() {
						// Code to add citation to metadata form
						var cTitle = $("#citation_title").val();
						var urllink = $("#citation_link").val();
						addCitationEntry(cTitle, urllink);
						overlaySlide("citation", false);
					});
					$("#addLicense").click(function() {
						// Code to add citation to metadata form
						var lTitle = $("#license_title").val();
						$("#license_data").val(lTitle);
						overlaySlide("license", false);
					});
					function addCitationEntry(cTitle, urllink) {
						var idx = "citation" + citNumber;
						citNumber++;
						console.log(idx);
						console.log(cTitle);
						var inField = '<div class="addtext"><input type="text" id="'
								+ idx
								+ '" disabled placeholder="'
								+ cTitle
								+ '">';
						var delBtn = '<button class="redroundbutton" id="delete_btn"><img src="resources/images/Close_Icon.png" width="12px">';
						var endTag = "</div>";
						var citationEntry = inField + delBtn + endTag;
						$(citationEntry).appendTo(
								".entryArea#citation_data_entry");
						$("#" + idx).attr("url", urllink);
						$("#" + idx).attr("title", cTitle);
						$("#delete_btn").click(function(e) {
							var tgt = e.target;
							console.log(tgt);
							$(this).parent().remove();
						})
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
				});