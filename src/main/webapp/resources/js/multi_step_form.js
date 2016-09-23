'use strict';

var curTitle = "";
var curURI = "";
var urlPrefix = "knowledgeObject";//
var viewObj = new Object();
var editObj = new Object();
var curMode = "new";
var citNumber = 0;

function initObj() {
	var objJson = $('#fedoraObj');
	if (objJson.length) {
		console.log("Set Obj:" + objJson.val());
		viewObj = objJson.val();
		editObj = JSON.stringify(objJson.val());
		curMode = "inEdit";
	} else {
		curMode = "new";
	}
}

function createViewField(inputName, inputID, inputLabel, maxLength, value,
		isMultiple, mode) {
	var beginTag = "<div class='addtext'>";
	var inLabel = "<h4>" + inputLabel + "</h4>";
	var entryArea = "<div class='entryArea' id='" + inputName
			+ "_entry'></div>";
	var charCounter = "<span>" + maxLength + "/" + maxLength + "</span>";
	var inField = '<input type="text" class="metaEdit ' + mode + '" name="'
			+ inputName + '" id="' + inputID + '-v" value="' + value
			+ '" maxlength=' + maxLength + '>';
	var endTag = "</div>";
	if (isMultiple) {
		return beginTag + inLabel + entryArea + inField + endTag;
	} else {
		return beginTag + inLabel + inField + endTag;
	}
}

function updateCount() {
	/* alert("key typed!"); */
	var cs = 0;
	if ($(this).val() != null) {
		cs = $(this).val().length;
	}

	var elementid = $(this).id;
	var count = $(this).parent().children("span").text();
	var maxl_s = count.substring(count.length - 3, count.length)
	var maxl = parseInt(maxl_s);
	var csl = maxl - cs;
	var s = csl.toString().concat("/" + maxl_s);
	var cs_color = "lightgreen";
	if (csl < 10) {
		cs_color = "red";
	}
	$(this).parent().children("span").text(s);
	$(this).parent().children("span").css("color", cs_color);
}

function overlayHeightResize(overlayID, window_height) {
	var overlayPane = $('#' + overlayID).find("> .ol_pane");
	var entryform = overlayPane.find(".entryform");
	var ol_pane_height = window_height;
	var calcHeight = (window_height - 157);
	entryform.css("height", calcHeight + "px");
	var ef_margin = (ol_pane_height - calcHeight) / 2;
	var addContent = entryform.find(".Add-content");
	addContent.css("height", (calcHeight - 70) + "px");
	$("ul#tab.inEdit li").css("height", (calcHeight - 120) + "px");
	$("ul#tab.inEdit li").css("min-height", (calcHeight - 120) + "px");
	if (calcHeight < 700) {
		$("[id$='EditWrapper']").css("bottom", "0px");
	}
	/*
	 * console.log("Overlay Height Resize - window height:
	 * "+window_height+"px"); console.log("Overlay Height Resize - overlay
	 * height: "+ol_pane_height+"px"); console.log("Overlay Height Resize -
	 * calculate height: "+calcHeight+"px"); console.log("Overlay Height Resize -
	 * ef Margin: "+ef_margin+"px");
	 */
	return ol_pane_height;
}

function deleteObject(uri) {

	var txt;
	if (uri == "") {
		uri = curURI;
	}
	if (uri != "") {
		var r = confirm("Do you really want to delete the object ? ");
		if (r == true) {
			$.ajax({
				type : 'DELETE',
				url : "deleteObject." + uri,
				success : function(response) {
					window.location.href = "home";
				}
			});
		}
	}
}

function downloadObject(uri) {

	window.location.href = urlPrefix + "/" + uri + "/complete";
}

function overlaySlide(overlayID, open, mode) {
	curMode = mode;
	document.body.classList.toggle('noscroll', open);
	var overlayPane = $('#' + overlayID).find("> .ol_pane");
	var window_width = $(window).width();
	var window_height = $(window).height();
	var overlayPane_width = overlayPane.width();
	var overlayPane_height = overlayHeightResize(overlayID, window_height);
	var overlayPane_left = window_width - overlayPane_width;
	/*
	 * if(overlayID=="login_overlay"){
	 * overlayPane_left=overlayPane_left+overlayPane_width/2; }
	 */
	if (overlayPane_left <= (window_width * 0.27)) {
		overlayPane_left = (window_width * 0.27);
	}
	if (overlayID == "addObject") {
		resetInputText();
		if (mode != "new") {
			$("#begin_page").hide();
			$("#entry_form1").show();
			if (open) {
				console.log("Init Obj:" + editObj);
				initInputTextFromObject(editObj,"overlay");
			}
		} else {
			curURI="";
			$("#begin_page").show();
			$("#end_page").hide();
			$("#entry_form1").hide();
			console.log("Overlay IN with URI:" + curURI);

		}
	}
	if (overlayID == "login_overlay") {
		resetLoginForm();
	}
	if (overlayID == "libraryuser") {
		resetUserInfoText();
	}
	if (overlayID == "citation") {
		resetCitationText();
	}
	if (overlayID == "license") {
		resetLicenseText();
	}
	if (open) {
		$('#' + overlayID).css("display", "block");
		$('#' + overlayID).fadeIn('fast', function() {
			overlayPane.animate({
				'left' : overlayPane_left + "px"
			}, 1000);
		});
	} else {

		overlayPane.animate({
			'left' : '100%'
		}, 1000, function() {
			$('#' + overlayID).delay(500).fadeOut('fast');
		});

		$('#' + overlayID).css("display", "none");
	}

}

function createViewTextarea(inputName, inputID, inputLabel, maxLength, value,
		isMultiple) {
	var beginTag = "<div class='addtext'>";
	var inLabel = "<h4>" + inputLabel + "</h4>";
	var entryArea = "<div class='entryArea' id='" + inputName
			+ "_entry'></div>";
	var inField = '<textarea name="' + inputName + '" id="' + inputID
			+ '_v" value="' + value + '" maxlength=' + maxLength
			+ '></textarea>';
	var charCounter = "<span>" + maxLength + "/" + maxLength + "</span>";
	var addBtn = '<button class="greenroundbutton"><img src="images/Plus_Icon.png" width="12px">';
	var endTag = "</div></div>";
	if (isMultiple) {
		return beginTag + inLabel + entryArea + inField + charCounter + endTag;
	} else {
		return beginTag + inLabel + inField + charCounter + endTag;
	}
}

function resetCitationText() {
	$('.addtext>input').each(updateCount);
	$("#citation_title").val("");
	$("#citation_link").val("");
	$("#citation_detail").attr("src", "");
	$("#entry_form").show();
	$("#citation_idx").val("");
	$("#addCitation").val("ADD");
}

function resetLoginForm() {
	$("#loginEntry").find(".error").removeClass("error");
	$("#loginEntry").find("[id$='-error']").hide();
	$("#username").val("");
	$("#password").val("");
}

function initCitationText(overlayID, cidx, ctitle, clink) {
	console.log("Citation Index:" + cidx + " Title:" + ctitle + " Link:"
			+ clink);
	$("#citation_title").val(ctitle);
	$("#citation_link").val(clink);
	$("#citation_detail").attr("src", "");
	$("#addCitation").val("UPDATE");
	$("#citation_idx").val(cidx);
	$('.addtext>input').each(updateCount);
}

function initLicenseText(overlayID, title, link) {
	$("#license_title").val(title);
	$("#license_link").val(link);
	$("#entry_form").show();
}

function resetLicenseText() {
	$("#license_title").val("");
	$("#license_link").val("");
	$("#license_detail").attr("src", "");
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
		url : "knowledgeObject/" + uri + "/" + section,
		success : function(response, tStatus, xhr) {
			console.log("GET request:\n" + "knowledgeObject/" + uri + "/"
					+ section);
			console.log("GET Text Status:\n" + tStatus);
			var test = JSON.stringify(response);
			console.log(test);
		},
		failure : function(response) {
			console.log("GET response:\n" + response);
		}
	});

}

function createInputField(inputName, inputID, inputLabel, maxLength,
		placeholderText, isMultiple, mode) {
	var beginTag = "<div>";
	var inLabel = "<h4>" + inputLabel + "</h4>";
	var entryArea = "<div class='entryArea' id='" + inputName
			+ "_entry'></div>";
	var inField = '<div class="addtext"><input spellcheck="false" type="text" name="'
			+ inputName
			+ '" class="'
			+ mode
			+ '" id="'
			+ inputID
			+ '" placeholder="'
			+ placeholderText
			+ '" maxlength='
			+ maxLength
			+ '>';
	var charCounter = "<span>" + maxLength + "/" + maxLength + "</span>";
	var addBtn = '<button class="greenroundbutton"><img src="/ObjectTeller/resources/images/Plus_Icon.png" width="12px">';
	var endTag = "</div></div>";
	if (isMultiple) {
		return beginTag + inLabel + entryArea + inField + charCounter + endTag;
	} else {
		return beginTag + inLabel + inField + charCounter + endTag;
	}
}

function createInputTextarea(inputName, inputID, inputLabel, maxLength,
		placeholderText, isMultiple, mode) {
	var beginTag = "<div>";
	var inLabel = "<h4>" + inputLabel + "</h4>";
	var entryArea = "<div class='entryArea' id='" + inputName
			+ "_entry'></div>";
	var inField = '<div class="addtext"><textarea spellcheck="false" name="'
			+ inputName + '" class="' + mode + '" id="' + inputID
			+ '" placeholder="' + placeholderText + '" maxlength=' + maxLength
			+ '></textarea>';
	var charCounter = "<span>" + maxLength + "/" + maxLength + "</span>";
	var addBtn = '<button class="greenroundbutton"><img src="images/Plus_Icon.png" width="12px">';
	var endTag = "</div></div>";
	if (isMultiple) {
		return beginTag + inLabel + entryArea + inField + charCounter + endTag;
	} else {
		return beginTag + inLabel + inField + charCounter + endTag;
	}
}

function resetInputText() {

	$(".current-tab").removeClass("current-tab");
	$("#progressbar li:first-child").addClass("current-tab");
	$('#progressbar li').children("img").css("display", "none");
	/*
	 * $('#addObj .fieldcontainer').css("display", "none");
	 * $('#first').css("display", "block");
	 */
	$('#payloadTextArea').val("");
	$('#functionName').val("");
	$('#inputTextArea').val("");
	$('#outputTextArea').val("");
	$("input[id$='_data']").val("");
	$("#description_data").val("");
	$("div[id$='_entry']").children().remove();
	$("#page_title").text("");
	$("#cDate").text("");
	$("#uDate").text("");
}

function initInputText(uri) {
	resetInputText();
	// Ajax retrieve
	retrieveObjectContent(uri, "metadata");
	retrieveObjectContent(uri, "payload");
	retrieveObjectContent(uri, "inputMessage");
	retrieveObjectContent(uri, "outputMessage");
	retrieveObjectContent(uri, "logData");
}

function initInputTextFromObject(obj, target) {
	var elementSuffix = "_v";
	if (target == "overlay") {
		elementSuffix = "";
		resetInputText();
	}

	displayMetaData(obj.metadata, elementSuffix);
	$("#functionName" + elementSuffix).val(obj.payload.functionName);
	$("#engineType" + elementSuffix).val(obj.payload.engineType);
	displayIOMessage("payload", obj.payload.content, elementSuffix);
	displayIOMessage("inputMessage", obj.inputMessage, elementSuffix);
	displayIOMessage("outputMessage", obj.outputMessage, elementSuffix);
}

function displayMetaData(obj, elementSuffix) {
	console.log("Metadata display:" + obj);
	console.log(" Reset Page Title to: " + obj.title+" Suffix:"+elementSuffix);
	if (elementSuffix == "") {
		
	}else{
		$("#citation_data_entry_v").empty();
	}
	if (obj.published) {
		$("#preTitle").show();
		$(".pri-pub2").addClass("current-tab");
		$(".pri-pub2 span").addClass("middleout");
		$(".pri-pub1").removeClass("current-tab");
		$(".pri-pub1 span").removeClass("middleout");
	} else {
		$("#preTitle").hide();
		$(".pri-pub1").addClass("current-tab");
		$(".pri-pub1 span").addClass("middleout");
		$(".pri-pub2").removeClass("current-tab");
		$(".pri-pub2 span").removeClass("middleout");
	}
	$("#page_title"+elementSuffix).text(obj.title);
	var title_length = obj.title.length;
	if (title_length > 74) {
		$("#page_title"+elementSuffix).css("font-size", "21px");
	} else {
		$("#page_title"+elementSuffix).css("font-size", "24px");
	}
	$("#title_data" + elementSuffix).val(obj.title);
	$("#description_data" + elementSuffix).val(obj.description);
	$("#keyword_data" + elementSuffix).val(obj.keywords);
	$("#owner_data" + elementSuffix).val(obj.owner);
	$("#contributor_data" + elementSuffix).val(obj.contributors);

	console.log("Number of Citation:" + obj.citations.length);
	for (var i = 0; i < obj.citations.length; i++) {
		console.log("CTitle:" + obj.citations[i].citation_title + " CLInk:"
				+ obj.citations[i].citation_at);
		addCitationEntry(obj.citations[i].citation_title,
				obj.citations[i].citation_at, elementSuffix);
	}
	$("#license_data" + elementSuffix).val(obj.license.licenseName);
	$("#license_data" + elementSuffix).attr("licenseLink",
			obj.license.licenseLink);

}

function displayIOMessage(section, obj, suffix) {
	var sect_id = section.replace("Message", "");
	// console.log(" Display "+section+": " +obj);
	if ((obj == "") && (suffix == "")) {
		$("#" + sect_id + "DropFile").show();
		$("#" + sect_id + "TextAreaDisplay").hide();
		$("#" + sect_id + "TextArea" + suffix).val("");
	} else {
		$("#" + sect_id + "DropFile").hide();
		$("#" + sect_id + "TextAreaDisplay").show();
		$("#" + sect_id + "TextArea" + suffix).val(obj);
	}
}

function parseJSON(data) {
	return window.JSON && window.JSON.parse ? window.JSON.parse(data)
			: (new Function("return " + data))();
}

function setURI(uri) {
	curURI = uri;
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
					if(sect_id=="newObj"){
						var obj=JSON.parse(contents);
						$("#new_title_data").val(obj.metadata.title);
					}
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
		var ajaxMethod = "GET";
		var ajaxUrl = urlPrefix + "/" + uri;
		if (section != "") {
			ajaxUrl = ajaxUrl + "/" + section;
		}
		console.log("URL: " + ajaxUrl);
		$.ajax({
			type : ajaxMethod,
			url : ajaxUrl,
			success : function(response, tStatus, xhr) {
				console.log("GET request:\n" + "knowledgeObject/" + uri + "/"
						+ section);
				// console.log("GET response:\n"+response);
				console.log("GET Text Status:\n" + tStatus);
				if (response != 'empty') {
					fObj = response;
					console.log(fObj);
					switch (section) {
					case "metadata":
						displayMetaData(fObj);
						break;
					case "payload":
						$("#functionName").val(fObj.functionName);
						$("#engineType").val(fObj.engineType);
						displayIOMessage("payload", fObj.content);
						break;
					case "inputMessage":
						displayIOMessage("inputMessage", fObj);
						break;
					case "outputMessage":
						displayIOMessage("outputMessage", fObj);
						break;
					case "logData":
						if (fObj != "") {
							$("#logdata_display").html(fObj);
						}
						break;
					default:
						break;
					}
				}
			},
			error : function(response) {
				document.getElementById("successResult").innerHTML = "ERROR";
			}
		});

	}
}

function toggleObject(uri, param) {
	$(this).find("span").addClass("middleout");
	$(".pri-pub .current-tab").find("span").removeClass("middleout");
	$.ajax({
		type : 'GET',
		url : "publishObject." + uri + "/" + param,
		success : function(response) {
			location.reload();
		}
	});
}

function initObject() {
	var metadata = new Object();
	metadata.title = document.getElementById("title_data_v").value;
	metadata.owner = document.getElementById("owner_data_v").value;
	metadata.description = document.getElementById("description_data_v").value;
	metadata.contributors = document.getElementById("contributor_data_v").value;
	metadata.keywords = document.getElementById("keyword_data_v").value;
	// metadata.license = document.getElementById("license_data").value;
	var ctitle;
	var clink;
	var citations = [];
	$("#citation_data_entry_v").find('div').each(function(index, element) {
		var citation = new Object();
		ctitle = $(element).find(".ctitle").val();
		clink = $(element).find(".clink").val();
		console.log(ctitle + " " + clink);
		citation.citation_title = ctitle;
		citation.citation_at = clink;
		citations.push(citation);
	});
	metadata.citations = citations;

	var lname = $("#license_data_v").val();
	var llink = $("#licenseLink_v").val();
	var license = new Object();
	license.licenseName = lname;
	license.licenseLink = llink;
	metadata.license = license;

	var payload = new Object();
	payload.functionName = document.getElementById("functionname_data_v").value;
	payload.engineType = document.getElementById("enginetype_data_v").value;
	payload.content = document.getElementById("payloadTextArea_v").value;

	var inputMessage = document.getElementById("inputTextArea_v").value;
	var outputMessage = document.getElementById("outputTextArea_v").value;
	return buildFedoraObject("update", metadata, payload, inputMessage,
			outputMessage);

}

function buildFedoraObject(mode, metadata, payload, inputMessage, outputMessage) {
	var obj = new Object();
	obj.metadata = metadata;
	if (mode != "new") {
		obj.payload = payload;
		obj.inputMessage = inputMessage;
		obj.outputMessage = outputMessage;
	}
	return obj;
}

function updateObject(section) {

	var fedoraObject = new Object();
	var metadata = new Object();
	var ajaxUrl;
	var ajaxSuccess;
	var ajaxMethod;
	/*
	 * console.log("constructing data elements for FedoraObject...");
	 */var uri = curURI;
	console.log("URI:" + uri);
	if (uri == "") {// new Object
		ajaxMethod = "POST";
		ajaxUrl = urlPrefix;
		ajaxSuccess = " Your Object Has Been Added To Your Library!<br> To make changes click on the edit button in each section.<br> Object ID: ";
		metadata.title = document.getElementById("new_title_data").value;
	} else {
		ajaxMethod = "PUT";
		ajaxUrl = urlPrefix + "/" + uri;
		if (section != "") {
			ajaxUrl = ajaxUrl + "/" + section;
		}
		ajaxSuccess = " Your Object Has Been Updated In Your Library!<br> To make changes click on the edit button in each section.<br> Object ID: ";
		metadata.title = document.getElementById("title_data").value;
		metadata.owner = document.getElementById("owner_data").value;
		metadata.description = document.getElementById("description_data").value;
		metadata.contributors = document.getElementById("contributor_data").value;
		metadata.keywords = document.getElementById("keyword_data").value;
		var ctitle;
		var clink;
		var citations = [];
		$("#citation_data_entry").find('div').each(function(index, element) {
			var citation = new Object();
			ctitle = $(element).find(".ctitle").val();
			clink = $(element).find(".clink").val();
			console.log(ctitle + " " + clink);
			citation.citation_title = ctitle;
			citation.citation_at = clink;
			citations.push(citation);
		});

		metadata.citations = citations;

		var lname = $("#license_data").val();
		var llink = $("#license_data").attr("licenseLink");
		var license = new Object();
		license.licenseName = lname;
		license.licenseLink = llink;
		metadata.license = license;
		var payload = new Object();
		payload.functionName = document.getElementById("functionName").value;
		payload.engineType = document.getElementById("engineType").value;
		payload.content = $("#payloadTextArea").val();

		var inputMessage = $("#inputTextArea").val();
		var outputMessage = $("#outputTextArea").val();

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
		text = $("#newObjTextArea").val();
		if(text==""){
			fedoraObject = buildFedoraObject("new", metadata, null, null, null);
			
		}else{
			fedoraObject=JSON.parse(text);
			fedoraObject.metadata.title = document.getElementById("new_title_data").value;
			initInputTextFromObject(fedoraObject,"overlay");
		}
		text = JSON.stringify(fedoraObject);
		break;
	default: // full object Add or Edit
		fedoraObject = buildFedoraObject("update", metadata, payload,
				inputMessage, outputMessage);
		text = JSON.stringify(fedoraObject);
		break;
	}
	console.log("Data to be sent: " + text);
	$("div.processing").fadeIn(300);
	editObj = fedoraObject;
	console.log(editObj);
	saveToServer(section, ajaxMethod, ajaxUrl, ajaxSuccess, text);
	curTitle = metadata.title;
}

function setActiveTab(section) {
	$('.current-tab').removeClass('current-tab');
	$('#progressbar #' + section).addClass('current-tab');
	var nthChild = $('#progressbar #' + section).index() + 1;
	console.log("Progress BAr index:" + nthChild);
	$(".fieldcontainer:nth-child(" + nthChild + ")").css({
		'display' : 'inline-block'
	});
}

function updateViewObject() {
	viewObj = editObj;
	console.log(viewObj);
	initInputTextFromObject(viewObj, "view");
}

function saveToServer(section, ajaxMethod, ajaxUrl, ajaxSuccess, text) {
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
					console.log(response);
					if ((response != 'empty') && (response != null)) {
						var test = JSON.stringify(response);
						var obj = JSON.parse(test);
						document.getElementById("successResult").innerHTML = ajaxSuccess
								+ obj.uri;
						curURI = obj.uri;
					}
					$("#page_title").text(editObj.title);
					$("#addObj_f").find("ul#tabs li").each(function() {
						console.log("UL li text:" + $(this).text());
						$(this).text($(this).text().replace("*", ""));
					});
					$("div.processing").fadeOut(200);
					$("div.success").fadeIn(300).delay(1500).fadeOut(400)
							.delay(1000);
					if (curMode == "new") {
						if (ajaxMethod == "POST") {
							$("#begin_page").hide();
							$("#end_page").show();
						} else {

						}
					} else {
						updateViewObject();
					}

				},
				error : function(response) {
					console.log(response);
					document.getElementById("successResult").innerHTML = "ERROR";
					// test code for
					$("div.processing").fadeOut(200);
					$("div.faiure").fadeIn(300).delay(1500).fadeOut(400);
					resetInputText();

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

function buildMetaDataTab(mode) {
	var inputLabels = [ "TITLE", "DESCRIPTION", "KEYWORDS", "OWNERS",
			"CONTRIBUTORS", "CITATIONS", "LICENSE" ];
	var inputNames = [ "title", "description_data", "keyword_data",
			"owner_data", "contributor_data", "citation_data", "license_data" ];
	var inputIDs = [ "title_data", "description_data", "keyword_data",
			"owner_data", "contributor_data", "citation_data", "license_data" ];
	var maxLengths = [ 140, 500, 140, 140, 140, 500, 140 ];
	var placeholderTexts = [
			"A title, which is a formal name.",
			"A text description of the object - akin to an abstract - maybe enetered for any object.",
			"Click here to add Keywords.", "Click here to add Owners.",
			"Click here to add Contributors.", "Click here to add Citations",
			"Click here to add License" ];
	var isMultiples = [ false, false, true, true, true, true, false ];
	var numberofFields = inputLabels.length;
	var inputField;
	for (var i = 0; i < (numberofFields); i++) {
		if (i != 1) {
			inputField = createInputField(inputNames[i], inputIDs[i],
					inputLabels[i], maxLengths[i], placeholderTexts[i],
					isMultiples[i], mode);

		} else {
			inputField = createInputTextarea(inputNames[i], inputIDs[i],
					inputLabels[i], maxLengths[i], placeholderTexts[i],
					isMultiples[i], mode);
		}
		switch (mode) {
		case "View":
			$(inputField).appendTo("#metadata_v_fields");
			break;
		case "inEdit":
			$(inputField).appendTo("#metadata_fields");
			break;
		}
	}
}

function addCitationEntry(cTitle, urllink, elementSuffix) {
	var idx = "citation" + citNumber;
	var idxName = "metadata.citations[" + citNumber + "]";
	console.log(idx);
	console.log("Suffix:"+elementSuffix+" Title:"+ cTitle);
	var springPath = "metadata.citations[" + citNumber + "]";

	var inField = '<div class="addtext"><input type="text" class="ctitle" id="'
			+ idx + '_title"' + '" name="' + idxName
			+ '.citation_title" path="' + springPath
			+ '.citation_title"  value="' + cTitle + '">';
	var inField2 = '<input type="hidden" class="clink" id="' + idx
			+ '_link" name="' + idxName + '.citation_at"  path="' + springPath
			+ '.citation_at" value="' + urllink + '">';
	var editBtn = '<button class="edit_btn" id="' + idx
			+ '_btn" type="button">EDIT</button>';
	var delBtn = '<button class="delete_btn" id="delete' + idx
	+ '_btn" type="button">DELETE</button>';
	var endTag = "</div>";
	var citationEntry = inField + inField2 +  endTag;
	if (elementSuffix == "") {
		citNumber++;
		citationEntry = inField + inField2+ editBtn + delBtn +endTag;
	}
	$(citationEntry).appendTo(".entryArea#citation_data_entry" + elementSuffix);
	$(".delete_btn").click(function(e) {
		var tgt = e.target;
		console.log(tgt);
		$(this).parent().remove();
		return false;
	});
	$(".edit_btn").click(function(e) {
		var idx = $(this).attr("id").replace("_btn", "_title");
		var ctitle = $("#" + idx).val();
		var linkid = idx.replace("title", "link");
		var clink = $("#" + linkid).val();
		console.log("Citation field in focus..." + idx);
		overlaySlide('citation', true);
		initCitationText('citation', idx, ctitle, clink);
	});
}

$(document)
		.ready(
				function() {
					$('[id^="file_"]').on('change', readMultipleFiles);

					// Overlay
					buildMetaDataTab("inEdit");
					if ($("#fObj").length) {
						var uri = $("#fObj").val();
						console.log("Current URI:" + uri);
						setURI(uri);
					}
					if (curURI != "") {
						editObj = initObject();
						viewObj = initObject();
					}
					var count = 0; // To Count Blank Fields
					$("[id$='ClearBtn']").click(
							function(event) {
								event.preventDefault();
								var btn_id = this.id;
								console.log(btn_id);
								var tArea_id = btn_id.replace("ClearBtn",
										"TextArea");
								var dropfile = btn_id.replace("ClearBtn",
										"DropFile");
								var fileinput = "file_"
										+ btn_id.replace("ClearBtn", "");
								console.log("Before Clear:" + fileinput
										+ $("#" + fileinput).val());
								$("#" + fileinput).val("");
								console.log("After Clear:"
										+ $("#" + fileinput).val());
								$("#" + dropfile).show();
								$("#" + tArea_id + "Display").hide();
								$("#" + tArea_id).text("");
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
					$('.addtext>input').change(
							function() {
								updateCount();
								$("#page_title").text($("#title_data").val());
								var tabTitle = $(this).parents("#addObj_f")
										.find("ul#tabs li.active").text();
								console.log("Actvie Tab Title:" + tabTitle);
								if (!tabTitle.endsWith("*")) {
									$(this).parents("#addObj_f").find(
											"ul#tabs li.active").text(
											tabTitle + "*");
								}
							});
					$('[id^="file_"]').change(
							function() {
								var tabTitle = $(this).parents("#addObj_f")
										.find("ul#tabs li.active").text();
								console.log($(this).val());
								console.log("Actvie Tab Title:" + tabTitle);
								if (!tabTitle.endsWith("*")) {
									$(this).parents("#addObj_f").find(
											"ul#tabs li.active").text(
											tabTitle + "*");
								}
							});
					$('#description_data').keyup(updateCount);
					$('#description_data').keydown(updateCount);
					$("[id$='_data']").each(updateCount);

					var next = 1;
					$("input[id$='_data']").click(function(e) {
						e.preventDefault();
					});
					$("input[type=text]").focus(function() {
						$(this).parent().find("span").addClass("inEdit");
						$("[id$='EditWrapper']").addClass("inEdit");
						$(this).addClass("inEdit");
						$(this).each(updateCount);
					});
					$("input[type=password]").focus(function() {
						$(this).parent().find("span").addClass("inEdit");
						$("[id$='EditWrapper']").addClass("inEdit");
						$(this).addClass("inEdit");
						$(this).each(updateCount);
					});
					$("input[type=text]").blur(function() {
						$(this).parent().find("span").removeClass("inEdit");
						$("[id$='EditWrapper']").removeClass("inEdit");
					});
					$("#description_data").focus(function() {
						$(this).parent().find("span").addClass("inEdit");
						$("[id$='EditWrapper']").addClass("inEdit");
						$(this).each(updateCount);
					});
					$("#description_data").blur(function() {
						$(this).parent().find("span").removeClass("inEdit");
						$("[id$='EditWrapper']").removeClass("inEdit");
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
					var citNumber = 0;
					$("#addCitation").click(function() {
						// Code to add citation to metadata form
						var validForm = citation_validator.form();
						console.log("validation result: ");
						if (validForm) {
							console.log("validation result: " + validForm);
							var cTitle = $("#citation_title").val();
							var urllink = $("#citation_link").val();
							var curCitation = $("#citation_idx").val();
							var buttonText = $(this).val();
							if (buttonText == "ADD") {
								addCitationEntry(cTitle, urllink,"");
							} else {
								$("#" + curCitation).val(cTitle);
								$("#" + curCitation).text(cTitle);
								$("#" + curCitation + "_link").val(urllink);
							}
							overlaySlide("citation", false);
						} else {
							console.log("validation result: " + validForm);

						}
					});
					$("#license_data").focus(
							function(e) {
								var idx = $(this).attr("id");
								var ltitle = $("#license_data").val();
								var llink = $("#license_data").attr(
										"licenseLink");

								console.log("license field in focus..."
										+ ltitle + "  " + llink);
								overlaySlide('license', true);
								initLicenseText('license', ltitle, llink);
							});
					$("#addLicense").click(function() {
						// Code to add citation to metadata form
						var lTitle = $("#license_title").val();
						var lLink = $("#license_link").val();
						$("#license_data").val(lTitle);
						$("#license_data").attr("licenseLink", lLink);
						overlaySlide("license", false);
					});

					$("[id$='ancelBtn']")
							.click(
									function(event) {
										console.log("View:" + viewObj);
										console.log("Edit:" + editObj);
										editObj = viewObj;
										initInputTextFromObject(viewObj,"overlay");
										$(this)
												.parents("#addObj_f")
												.find("ul#tabs li")
												.each(
														function() {
															console
																	.log("UL li text:"
																			+ $(
																					this)
																					.text());
															$(this)
																	.text(
																			$(
																					this)
																					.text()
																					.replace(
																							"*",
																							""));
														});

									});
					$("button").click(function(e) {
						e.preventDefault();
					});
					$("#preview_btn").click(function(e) {
						var urllink = $("#citation_link").val();
						console.log(urllink);
						$("#citation_detail").attr('src', urllink);

					});
					$("#license_preview_btn").click(function(e) {
						var urllink = $("#license_link").val();
						console.log(urllink);
						$("#license_detail").attr('src', urllink);
					});

					var citation_validator = $("#citation_f")
							.validate(
									{
										errorPlacement : function(error,
												element) { // Append error
															// within linked
															// label
											$(element).closest("form").find(
													"label[for='"
															+ element
																	.attr("id")
															+ "']")
													.after(error);
										},
										errorElement : "span",
										debug : true,
										success : function(form) {
											if (form.children(".error").length == 0) {
												$("#addCitation").removeAttr(
														'disabled');
												$("#addCitation").css(
														"background-color",
														"#39b45a");
											}
										},
										highlight : function(element,
												errorClass, validClass) {
											$(element).addClass(errorClass)
													.removeClass(validClass);
											$(element.form).find(
													"h4[title=" + element.id
															+ "]").addClass(
													errorClass);
											$(element.form).find(
													"input[id=" + element.id
															+ "]").addClass(
													errorClass);
										},
										unhighlight : function(element,
												errorClass, validClass) {
											$(element).removeClass(errorClass)
													.addClass(validClass);
											$(element.form).find(
													"h4[title=" + element.id
															+ "]").removeClass(
													errorClass);
											$(element.form).find(
													"input[id=" + element.id
															+ "]").removeClass(
													errorClass);
										},
										rules : {
											citation_title : {
												required : true
											},
											citation_link : {
												required : true
											//, url:true
											}
										},
										messages : {
											citation_title : {
												required : "Please enter a title for this citation."
											},
											citation_link : {
												required : "A valid URL link for your citation is required."
											//,url:"Please enter a valid URL link for your citation."
											}
										}
									});
				});