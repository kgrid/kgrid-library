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
<script src="<c:url value="/resources/js/multi_step_form.js"/>"></script>
<title>Add Object</title>
<script>

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
			<div id="begin_page">
			<h3> Create New Knowledge Object</h3>
				<div class="addtext" id="newTitle">
					<h4>
						Please enter a title for the new knowledge object, then click on "Create Object".
					</h4>
				
					<input type="text" maxlength="140" class="metaEdit"
						id="new_title_data" path="metadata.title"
						value="${fedoraObject.metadata.title}" />
					<span>140/140</span>
				</div>
				<input id="addObjButton" type="button"
					onclick="updateObject('new')" value="Create Object">
			</div>
			<div class="entryform" id="entry_form1">
				<h3 id="page_title">
					<spring:message code="ADD_OBJECT_TITLE" />
				</h3>
				<div>
					<sf:form name="addObj_f" class="Add-content" id="addObj_f"
						method="POST" modelAttribute="fedoraObject" action="addNewObject">
						<div id="barcontainer">
							<ul id="progressbar">
								<li class="current-tab"><img
									src="<c:url value="/resources/images/checkmark.png" />"
									width="20px" style="display: none;"> <spring:message
										code="METADATA_TAB" /></li>
								<li><img
									src="<c:url value="/resources/images/checkmark.png" />"
									width="20px" style="display: none;"> <spring:message
										code="PAYLOAD_TAB" /></li>
								<li><img
									src="<c:url value="/resources/images/checkmark.png" />"
									width="20px" style="display: none;"> <spring:message
										code="INPUT_MESSAGE" /></li>
								<li><img
									src="<c:url value="/resources/images/checkmark.png"/>"
									width="20px" style="display: none;"> <spring:message
										code="OUTPUT_MESSAGE" /></li>
							</ul>
						</div>
						<fieldset class="fieldcontainer" id="first">
							<div id="metadata_fields"></div>
							<input class="next_btn" name="next" type="button" value="Next">
						</fieldset>
						<fieldset class="fieldcontainer">
							<div id="fields">
								<h4>
									<spring:message code="PAYLOAD_FUNCTION" />
									<spring:message code="REQUIRED_FIELD" />
								</h4>
								<div class="addtext">
									<input class="textbox" type="text" id="functionName"
										placeholder="one instance only" maxlength="140"><span>140/140</span>
								</div>
								<div class="addtext">
									<h4>
										<spring:message code="PAYLOAD_TYPE" />
										<spring:message code="REQUIRED_TO_SELECT" />
									</h4>
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
												<label for="file_payload"><spring:message
														code="CHOOSE_FILE" /></label>
											</p> <br>
											<p>
												<spring:message code="FILE_TYPE" />
											</p>
										</label>
									</div>
									<p class="instruction">
										<spring:message code="CLICK" />
										<a href=#><spring:message code="HERE" /></a>
										<spring:message code="PAYLOAD_DOWNLOAD_MESSAGE" />
									</p>
								</div>
								<div class="display-payload" id="payloadTextAreaDisplay">
									<button id="payloadClearBtn">
										<spring:message code="REMOVE_BTN" />
									</button>
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
										<input type="file" name="file" id="file_input"
											class="inputfile"
											data-multiple-caption="{count} files selected" multiple
											style="display: none;" /> <label for="file_input"
											id="filecount"> <img
											src="<c:url value="/resources/images/Upload_Icon.png" />">
											<br>
											<p class="green">
												<label for="file_input"><span><spring:message
															code="CHOOSE_FILE" /></label>
											</p> <br>
											<p>
												<spring:message code="FILE_TYPE" />
											</p>
										</label>
									</div>
									<p class="instruction">
										<spring:message code="CLICK" />
										<a href=#><spring:message code="HERE" /></a>
										<spring:message code="INPUT_MESSAGE_DOWNLOAD" />
									</p>
								</div>
								<div class="display-input" id="inputTextAreaDisplay">
									<button id="inputClearBtn">
										<spring:message code="REMOVE_BTN" />
									</button>
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
												<label for="file_output"><spring:message
														code="CHOOSE_FILE" /></label>
											</p> <br>
											<p>
												<spring:message code="FILE_TYPE" />
											</p>
										</label>
									</div>
									<p class="instruction">
										<spring:message code="CLICK" />
										<a href=#><spring:message code="HERE" /></a>
										<spring:message code="OUTPUT_MESSAGE_DOWNLOAD" />
									</p>
								</div>
								<div class="display-output" id="outputTextAreaDisplay">
									<button id="outputClearBtn">
										<spring:message code="REMOVE_BTN" />
									</button>
									<textarea id="outputTextArea"></textarea>
								</div>
							</div>
							<input class="pre_btn" name="previous" type="button"
								value="Previous"> <input id="addObjButton" type="button"
								onclick="updateObject('metadata')" value="Apply Changes">
						</fieldset>
					</sf:form>
				</div>
			</div>
			<div class="Add-content" id="toggle_test" name="toggle_test"
				style="display: none;">
				<div>Processing ....</div>
				<button id="successDisp">Click to show the Status Page
					after object successfully created object</button>
				<button id="failureDisp">Click to show the Status Page
					after object unable to be created</button>
			</div>
			<div id="end_page" style="display: none;">
				<div class="creation_status"></div>
				<div>
					<p id="successResult"
						style="font-size: 28px; line-height: 2em; color: #666666;"></p>
					<input id="editObjButton" type="button"
								onclick="addObjContent()" value="Edit Content">
					<input id="doneBtn" type="button"
								onclick="overlaySlide('addObject',false)" value="Done">
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
								<div>
									<h4>TITLE, PMID or DOI</h4>
									<div class="addtext">
										<input class="textbox" name="citation_title"
											id="citation_title" type="text"
											placeholder="Enter Article Title, PMID or DOI "
											maxlength="140"><span>140/140</span>
									</div>
								</div>
								<div>
									<h4>HYPERLINK</h4>
									<button class="inline edit" id="preview_btn">PREVIEW</button>
									<div class="addtext">
										<input class="textbox" name="citation_link" id="citation_link"
											type="text"
											placeholder="Please provide the URL for the article "
											maxlength="140"><span>140/140</span>
									</div>
								</div>
								<div>
									<h4>CITATION DETAIL</h4>
									<div class="addtext">
										<iframe id="citation_detail"></iframe>
									</div>
									<input class="done_btn" name="done" id="addCitation"
										type="button" value="ADD">
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
									<h4>
										License
										<spring:message code="REQUIRED_TO_SELECT" />
									</h4>
									<select required class="options" id="license_title">
										<option value="MIT">MIT</option>
										<option value="CC">Creative Commons</option>
									</select> <br>
								</div>
								<div>
									<h4>HYPERLINK</h4>
									<button class="inline edit" id="license_preview_btn">PREVIEW</button>
									<div class="addtext">
										<input class="textbox" name="license_link" id="license_link"
											type="text"
											placeholder="Please provide the URL for the license "
											maxlength="140"><span>140/140</span>
									</div>
								</div>
								<div>
									<h4>LICENSE DETAIL</h4>
									<div class="addtext">
										<iframe id="license_detail"></iframe>
									</div>
									<input class="done_btn" name="done" id="addLicense"
										type="button" value="ADD">
							</fieldset>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>