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
				<h3>Create New Knowledge Object</h3>
				<div class="addtext" id="newTitle">
					<h4>Please enter a title for the new knowledge object, then
						click on "Create Object".</h4>
					<input type="text" maxlength="140" class="metaEdit"
						id="new_title_data" path="metadata.title"
						value="${fedoraObject.metadata.title}" /> <span>140/140</span>
				</div>
				<input id="addObjButton" type="button" onclick="updateObject('new')"
					value="Create Object">
			</div>
			<div class="entryform" id="entry_form1">
				
		<div id="ko-title">
			<div id="pubIndicator">
				<div class="type-status" id="preTitle"></div>
			</div>
			<h1><small  id="page_title">
					</small>
			</h1>		
		</div>	
		<div class="pri-pub accessLevelOne">
					<div class="pri-pub1"
						onclick="toggleObject('no')">
						<span><spring:message code="PRIVATE" /></span>
					</div>
					<div class="pri-pub2" onclick="toggleObject('yes')">
						<div class="minitype-status"></div>
						<div>
							<span><spring:message code="PUBLIC" /></span>
						</div>
					</div>
				</div>
				
				<div class="date">
			<div class="date1">
				<p class="date-title">
					<spring:message code="DATE_CREATED" />
				</p>
				<p class="date-data" id="cDate">
								</p>
			</div>
			<div class="date2">
				<p class="date-title">
					<spring:message code="UPDATE_DATE" />
				</p>
				<p class="date-data" id="uDate">
			</p>
			</div>

		</div>	
		<button class="inline edit" id="deleteButton"
									style="position: relative; left: 84%; bottom:-30px"
									onclick="deleteObject()">
									<spring:message code="DELETE_OBJ_BUTTON" />
								</button>		
				<div>
					<sf:form name="addObj_f" class="Add-content" id="addObj_f"
						method="POST" modelAttribute="fedoraObject" action="addNewObject1">
						<div id="barcontainer">
							<ul id="progressbar">
								<li class="current-tab" id="metadata"><img
									src="<c:url value="/resources/images/checkmark.png" />"
									width="20px" style="display: none;"> <spring:message
										code="METADATA_TAB" /></li>
								<li id="payload"><img
									src="<c:url value="/resources/images/checkmark.png" />"
									width="20px" style="display: none;"> <spring:message
										code="PAYLOAD_TAB" /></li>
								<li id="inputMessage"><img
									src="<c:url value="/resources/images/checkmark.png" />"
									width="20px" style="display: none;"> <spring:message
										code="INPUT_MESSAGE" /></li>
								<li id="outputMessage"><img
									src="<c:url value="/resources/images/checkmark.png"/>"
									width="20px" style="display: none;"> <spring:message
										code="OUTPUT_MESSAGE" /></li>
							</ul>
						</div>
						<fieldset class="fieldcontainer" id="first">
							<div id="metadata_fields"></div>
							<input class="next_btn" name="next" type="button" value="Next"><input
								id="addObjButton" type="button"
								onclick="updateObject('metadata')" value="Apply Changes">
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
								type="button" value="Next"><input id="addObjButton"
								type="button" onclick="updateObject('payload')"
								value="Apply Changes">
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
								type="button" value="Next"><input id="addObjButton"
								type="button" onclick="updateObject('inputMessage')"
								value="Apply Changes">
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
								onclick="updateObject('outputMessage')" value="Apply Changes">
						</fieldset>
					</sf:form>
				</div>
			</div>
			<div id="end_page" style="display: none;">
				<div class="creation_status"></div>
				<div>
					<p id="successResult"
						style="font-size: 28px; line-height: 2em; color: #666666;"></p>
					<input id="editObjButton" type="button" onclick="addObjContent()"
						value="Edit Content"> <input id="doneBtn" type="button"
						onclick="overlaySlide('addObject',false)" value="Done">
				</div>
			</div>
		</div>
	</div>
	<div id="overlay2">
		<%@ include file="secondaryOverlay.jsp"%>
	</div>
</body>
</html>