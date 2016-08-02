<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
<script src="<c:url value="/resources/js/multi_step_form.js"/>"></script>
<title>Add Object</title>
<script>
	function readMultipleFiles(evt) {
		//Retrieve all the files from the FileList object
		var files = evt.target.files;

		if (files) {
			for (var i = 0, f; f = files[i]; i++) {
				var r = new FileReader();
				r.onload = (function(f) {
					return function(e) {
						var contents = e.target.result;
						$("#payloadDropFile").hide();
						$("#payloadTextAreaDisplay").show();
						$('#payloadTextArea').val(contents);
					};
				})(f);
				r.readAsText(f);
			}
		} else {
			alert("Failed to load files");
		}
	}
	function readInputFiles(evt) {
		//Retrieve all the files from the FileList object
		var files = evt.target.files;

		if (files) {
			for (var i = 0, f; f = files[i]; i++) {
				var r = new FileReader();
				r.onload = (function(f) {
					return function(e) {
						var contents = e.target.result;
						$("#inputDropFile").hide();
						$("#inputTextAreaDisplay").show();
						$('#inputTextArea').val(contents);
					};
				})(f);
				r.readAsText(f);
			}
		} else {
			alert("Failed to load files");
		}
	}
	function readOutputFiles(evt) {
		//Retrieve all the files from the FileList object
		var files = evt.target.files;

		if (files) {
			for (var i = 0, f; f = files[i]; i++) {
				var r = new FileReader();
				r.onload = (function(f) {
					return function(e) {
						var contents = e.target.result;
						$("#outputDropFile").hide();
						$("#outputTextAreaDisplay").show();
						$('#outputTextArea').val(contents);
					};
				})(f);
				r.readAsText(f);
			}
		} else {
			alert("Failed to load files");
		}
	}

	function addNewObject() {

		var fedoraObject = new Object();

		var metadata = new Object();
		
		metadata.title = document.getElementById("title_data").value;
		metadata.owner = document.getElementById("owner_data").value;
		metadata.description = document.getElementById("description_data").value;
		metadata.contributors = document.getElementById("contri_data").value;
		metadata.keywords = document.getElementById("keyword_data").value;
//		metadata.license = document.getElementById("license_data").value;

		var ctitle;
		var clink;
		var citations = []; 
		
		$("#citation_data_entry").find('input').each(function( index, element ) {
			var citation = new Object();
			ctitle=$(element).attr("title");
			clink = $(element).attr("url");
			console.log(ctitle + " "+clink);
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

		payloadDescriptor.functionName = document
				.getElementById("functionName").value;
		payloadDescriptor.engineType = document.getElementById("engineType").value;

		fedoraObject.payloadDescriptor = payloadDescriptor;

		var text = JSON.stringify(fedoraObject);
		
 		$("#entry_form").hide();
		$("#end_page").show();
		
 			$
				.ajax({
					beforeSend : function(xhrObj) {
						xhrObj.setRequestHeader("Content-Type",
								"application/json");
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
</script>
</head>
</head>
<body>
		<div class="ol_pane">
			<div class="sidebar_close">
				<h3>CLOSE</h3>
				<button class="greenroundbutton" id="close_overlay"
					onclick="overlaySlide('addObject',false)">
					<img src="<c:url value="/resources/images/Close_Icon.png" />">
				</button>
			</div>
	<div class="board" id="addObj">
		<div class="entryform" id="entry_form">
			<h3><spring:message code="ADD_OBJECT_TITLE"/></h3>
			<div style="height:98%;">
				<sf:form name="addObj_f" class="Add-content" id="addObj_f"
					method="POST" modelAttribute="fedoraObject" action="addNewObject">
					<div id="barcontainer">
						<ul id="progressbar">
						<li class="current-tab"><img
							src="<c:url value="/resources/images/checkmark.png" />"
							width="20px" style="display: none;"><spring:message code="METADATA_TAB"/></li>
						<li><img
							src="<c:url value="/resources/images/checkmark.png" />"
							width="20px" style="display: none;"><spring:message code="PAYLOAD_TAB"/></li>
						<li><img
							src="<c:url value="/resources/images/checkmark.png" />"
							width="20px" style="display: none;"><spring:message code="INPUT_MESSAGE"/></li>
						<li><img
							src="<c:url value="/resources/images/checkmark.png"/>"
							width="20px" style="display: none;"><spring:message code="OUTPUT_MESSAGE"/></li>
					</ul>
					</div>
					<fieldset class="fieldcontainer" id="first">
						<div id="metadata_fields">
						</div>
						<input class="next_btn" name="next" type="button" value="Next">
					</fieldset>
					<fieldset class="fieldcontainer">
					<div id="fields">
						<h4><spring:message code="PAYLOAD_FUNCTION" /> <spring:message code="REQUIRED_FIELD" /></h4>

						<div class="addtext">
							<input class="textbox" type="text" id="functionName" placeholder="one instance only" maxlength="140"
								><span>140/140</span>
						</div>
						<div class="addtext">

						<h4><spring:message code="PAYLOAD_TYPE" /> <spring:message code="REQUIRED_TO_SELECT"/></h4>
							<select required class="options" id="engineType">
								<option value="Python">PYTHON</option>
							</select> <br>
						</div>
						<div id="payloadDropFile" class="dropfile">

							<div class="upload-direction">
								<input type="file" name="file" id="file_payload"
									class="inputfile"
									data-multiple-caption="{count} files selected" multiple
									style="display: none;" /> <label for="file_payload"
									id="filecount"> <img
									src="<c:url value="/resources/images/Upload_Icon.png"/>">
									<br>
									<p class="green">
										<label for="file_payload"><spring:message code="CHOOSE_FILE" /></label>
									</p> <br>
									<p><spring:message code="FILE_TYPE" /></p>

								</label>
							</div>
							<p class="instruction">
								<spring:message code="CLICK" /><a href=#><spring:message code="HERE" /></a><spring:message code="PAYLOAD_DOWNLOAD_MESSAGE" />
							</p>
						</div>
						<div class="display-payload" id="payloadTextAreaDisplay">
						<button id="clearPayloadButton"><spring:message code="REMOVE_BTN" /></button>
							<textarea id="payloadTextArea"></textarea>
						</div>
						</div>
						<input class="pre_btn" name="previous" type="button"
							value="Previous"> <input class="next_btn" name="next"
							type="button" value="Next">


					</fieldset>
					<fieldset class="fieldcontainer">
					<div id="fields">
						<div id="inputDropFile" class="dropfile">

							<div class="upload-direction">
								<input type="file" name="file" id="file_input" class="inputfile"
									data-multiple-caption="{count} files selected" multiple
									style="display: none;" /> <label for="file_input"
									id="filecount"> <img
									src="<c:url value="/resources/images/Upload_Icon.png" />">
									<br>
									<p class="green">
										<label for="file_input"><span><spring:message code="CHOOSE_FILE" /></label>
									</p> <br>
									<p><spring:message code="FILE_TYPE" /></p>

								</label>
							</div>
							<p class="instruction">
								<spring:message code="CLICK" /> <a href=#><spring:message code="HERE" /></a> <spring:message code="INPUT_MESSAGE_DOWNLOAD" />
							</p>
						</div>
						<div class="display-input" id="inputTextAreaDisplay">
						<button id="clearInputButton"><spring:message code="REMOVE_BTN" /></button>
							<textarea id="inputTextArea"></textarea>
						</div>
						</div>
						<input class="pre_btn" name="previous" type="button"
							value="Previous"> <input class="next_btn" name="next"
							type="button" value="Next">

					</fieldset>
					<fieldset class="fieldcontainer">
						<div id="fields">
						<div id="outputDropFile" class="dropfile">

							<div class="upload-direction">
								<input type="file" name="file" id="file_output"
									class="inputfile"
									data-multiple-caption="{count} files selected" multiple
									style="display: none;" /> <label for="file_output"
									id="filecount"> <img
									src="<c:url value="/resources/images/Upload_Icon.png" />">
									<br>
									<p class="green">
										<label for="file_output"><spring:message code="CHOOSE_FILE" /></label>
									</p> <br>
									<p><spring:message code="FILE_TYPE" /></p>

								</label>
							</div>
							<p class="instruction">
								<spring:message code="CLICK" /> <a href=#><spring:message code="HERE" /></a> <spring:message code="OUTPUT_MESSAGE_DOWNLOAD" />
							</p>
						</div>
						<div class="display-output" id="outputTextAreaDisplay">
						<button id="clearOutputButton"><spring:message code="REMOVE_BTN" /></button>
							<textarea id="outputTextArea"></textarea>
						</div>
						</div>
						<input class="pre_btn" name="previous" type="button"
							value="Previous"> <input id="addObjButton" type="button" onclick="addNewObject()"
							value="Add Object" >
					</fieldset>

				</sf:form>
			</div>
		</div>
		<div class="Add-content" id="toggle_test" name="toggle_test"
			style="display: none;">
			<div>Processing ....</div>
			<button id="successDisp">Click to show the Status Page after
				object successfully created object</button>
			<button id="failureDisp">Click to show the Status Page after
				object unable to be created</button>

		</div>
		<div id="end_page" style="display: none;">
			<div class="creation_status"></div>
			<div>
				<p id="successResult" style="font-size: 28px; line-height: 2em; color: #666666;">
					
				</p>
			</div>

		</div>
	</div>
</div>
	<div id="citation" class="layered_overlay">
		<div id="citation_pane" class="ol_pane">
			<div class="sidebar_close">
				<h3>CLOSE</h3>
				<button class="greenroundbutton" id="close_overlay"
					onclick="overlaySlide('citation',false)">
										<img src="<c:url value="/resources/images/Close_Icon.png" />">

				</button>
			</div>
			<div class="board" id="addCitationEntry">
				<div class="entryform" id="entry_form">
					<h3>Add Citation</h3>

					<div style="height: 80%;">
						<form name="addObj_f" class="Add-content" id="addObj_f"
							method="post">
							<fieldset class="fieldcontainer" id="first">
			<div><h4>TITLE, PMID or DOI</h4>
								<div class="addtext">
									<input class="textbox" name="citation_title" id="citation_title"
										type="text" placeholder="Enter Article Title, PMID or DOI " maxlength="140"><span>140/140</span>
								</div></div>
											<div><h4>HYPERLINK</h4> <button class="inline edit" id="preview_btn">PREVIEW</button>
								
								<div class="addtext">
									<input class="textbox" name="citation_link" id="citation_link"
										type="text" placeholder="Please provide the URL for the article " maxlength="140"><span>140/140</span>
								</div></div>
											<div><h4>CITATION DETAIL</h4>
								
								<div class="addtext" >
									<iframe id="citation_detail"></iframe>
								</div>
								
		
								<input class="done_btn" name="done" id="addCitation" type="button" value="ADD">
							</fieldset>
						</form>
					</div>
				</div>

			</div>
		</div>


	</div>
		<div id="license" class="layered_overlay">
		<div id="license_pane" class="ol_pane">
			<div class="sidebar_close">
				<h3>CLOSE</h3>
				<button class="greenroundbutton" id="close_overlay"
					onclick="overlaySlide('license',false)">
										<img src="<c:url value="/resources/images/Close_Icon.png" />">

				</button>
			</div>
			<div class="board" id="selectLicense">
				<div class="entryform" id="entry_form">
					<h3>Add License</h3>

					<div style="height: 80%;">
						<form name="addObj_f" class="Add-content" id="addObj_f"
							method="post">
							<fieldset class="fieldcontainer" id="first">

<div class="addtext">

						<h4>License <spring:message code="REQUIRED_TO_SELECT"/></h4>
							<select required class="options" id="license_title">
								<option value="MIT">MIT</option>
								<option value="CC">Creative Commons</option>
							</select> <br>
						</div>
											<div><h4>HYPERLINK</h4> <button class="inline edit" id="license_preview_btn">PREVIEW</button>
								
								<div class="addtext">
									<input class="textbox" name="license_link" id="license_link"
										type="text" placeholder="Please provide the URL for the license " maxlength="140"><span>140/140</span>
								</div></div>
							
											<div><h4>LICENSE DETAIL</h4>
								
								<div class="addtext" >
									<iframe id="license_detail"></iframe>
								</div>
								
		
								<input class="done_btn" name="done" id="addLicense" type="button" value="ADD">
							</fieldset>
						</form>
					</div>
				</div>

			</div>
		</div>


	</div>
</body>
</html>