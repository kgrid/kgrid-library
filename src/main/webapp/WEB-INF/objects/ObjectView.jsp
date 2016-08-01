<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="javax.servlet.http.HttpSession"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="<c:url value="/resources/css/style.css"/>"
	type="text/css" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/bannercontent.css"/>"
	type="text/css" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/datagrid.css"/>" type="text/css" />
<link rel="stylesheet" href="<c:url value="/resources/css/button.css"/>"
	type="text/css" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/navigation.css"/>" type="text/css" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/l_overlay.css"/>" type="text/css" />
<link rel="stylesheet" href="<c:url value="/resources/css/tab.css"/>"
	type="text/css" />
	<link rel="stylesheet"
	href="<c:url value="/resources/css/formstyle.css" />" type="text/css" />
<link rel="shortcut icon"
	href="<c:url value="/resources/images/MiniIconObjectTeller.ico" /> " />
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
<script src="/ObjectTeller/resources/js/iconbutton.js"></script>
<script src="/ObjectTeller/resources/js/scroll.js"></script>
<script src="/ObjectTeller/resources/js/obj_edit_form.js"></script>
<script src="/ObjectTeller/resources/js/jquery.validate.js"></script>
<script>

	$(document).ready(	
			function() {
				
				var userObj ="<%=session.getAttribute("DBUser")%>";
				
				var prefixhash = "#";
				var activeTab = prefixhash.concat('${ActiveTab}');
				var tabNum = $(activeTab.toLowerCase()).index();
				var nthChild = 1;
				if (tabNum >= 0) {
					var nthChild = tabNum + 1;
				}
			
				$("ul#tabs li.active").removeClass("active");
				$("ul#tabs li:nth-child(" + nthChild + ")").addClass("active");
				$("ul#tab li:nth-child(" + nthChild + ")").addClass("active");

				var accessLevel=1;
				
				if( userObj  == "null" ){
					
					$(".accessLevelOne").css({
						"visibility":"hidden"
					});
					$("#session").show();
					$("#logoutsession").hide();
				
				}else{
					$("#session").hide();
					$("#logoutsession").show();
					
					switch(accessLevel){

					default:
						$(".accessLevelOne").css({
							"visibility":"hidden"
						});
						break;
					
					case 1:
					$(".accessLevelOne").css({
						"visibility":"visible"
					});
					break;
					}
				}				
				
			$("#editButton").click(function() {
					console.log($(this).attr("id"));
					var buttonText = $("#editButton").text();
					console.log(buttonText);
					var data_t = $("#title_data_v").val();
					var data_d = $("#description_data_v").val();
					var data_k = $("#keyword_data_v").val();
					var data_o = $("#owner_data_v").val();
					var data_c = $("#contributor_data_v").val();
					if (buttonText == "EDIT") {
						$("#metadata_view").hide();
						$("#metadata_edit").show();
						$("#editButton").text("CANCEL");
						$("#editButton").css("left", "640px");
						$("#title_data").val(data_t);
						$("#description_data").val(data_d);
						$("#keyword_data").val(data_k);
						$("#owner_data").val(data_o);
						$("#contributor_data").val(data_c);
					} else {
						$("#metadata_view").show();
						$("#metadata_edit").hide();
						$("#editButton").text("EDIT");
						$("#editButton").css("left", "0%");
						$("#title_data").text(function(i, origText) {
							return origText;
						});
						$("#description_data").text(function(i, origText) {
							return origText;
						});
						$("#keyword_data").text(function(i, origText) {
							return origText;
						});
						$("#owner_data").text(function(i, origText) {
							return origText;
						});
						$("#contributor_data").text(function(i, origText) {
							return origText;
						});
					}
				});
				$("#editPayloadButton").click(function() {
					var buttonText = $("#editPayloadButton").text();
					var data_p = $("#payloadTextArea-v").val();
					if (buttonText == "EDIT") {
						$("#displayPayload").hide();
						$("#payloadTextArea").val(data_p); 
						$("#payloadEdit").show();
						$("#payloadTextArea").show();
						$("#payloadTextAreaDisplay").show();
						$("#payloadDropFile").hide();
						$("#editPayloadButton").text("CANCEL");
						$("#editPayloadButton").css("left", "640px");
					} else {
						$("#displayPayload").show();
						$("#payloadEdit").hide();
						$("#editPayloadButton").text("EDIT");
						$("#editPayloadButton").css("left", "0%");
						$("#payload_function_name").text(function(i, origText) {
							return origText;
						});
					}
				});
				$("#editInputButton").click(function() {
					var buttonText = $("#editInputButton").text();
					var data_p = $("#inputTextArea-v").val();
					if (buttonText == "EDIT") {
						$("#displayInput").hide();
						$("#inputTextArea").val(data_p); 
						$("#inputEdit").show();
						$("#inputTextArea").show();
						$("#inputTextAreaDisplay").show();
						$("#inputDropFile").hide();
						$("#editInputButton").text("CANCEL");
						$("#editInputButton").css("left", "580px");
					} else {
						$("#displayInput").show();
						$("#inputEdit").hide();
						$("#editInputButton").text("EDIT");
						$("#editInputButton").css("left", "0%");

					}
				});
				$("#editOutputButton").click(function() {
					var buttonText = $("#editOutputButton").text();
					var data_p = $("#outputTextArea-v").val();
					if (buttonText == "EDIT") {
						$("#displayOutput").hide();
						$("#outputTextArea").val(data_p);
						$("#outputEdit").show();
						$("#outputTextArea").show();
						$("#outputTextAreaDisplay").show();
						$("#outputDropFile").hide();
						$("#editOutputButton").text("CANCEL");
						$("#editOutputButton").css("left", "580px");
					} else {
						$("#displayOutput").show();
						$("#outputEdit").hide();
						$("#editOutputButton").text("EDIT");
						$("#editOutputButton").css("left", "0%");

					}
				});
	


				document.getElementById('file_payload').addEventListener(
						'change', readMultipleFiles, false);

				document.getElementById('file_input').addEventListener(
						'change', readMultipleFiles, false);

				document.getElementById('file_output').addEventListener(
						'change', readMultipleFiles, false);
			});
	
	
</script>
<script type="text/javascript">

	function toggleObject(uri, param) {
		$.ajax({
			type : 'GET',
			url : "publishObject." + uri + "/" + param,

			success : function(response) {

				location.reload();
			}
		});

	}

	function overlaySlide(overlayID, open){
		var overlayPane =$('#'+overlayID).find("> .ol_pane");
		if(open){
			$('#'+overlayID).css("display","block");
	        $('#'+overlayID).fadeIn('fast',function(){
	            overlayPane.animate({'left':'40%'},1000);
	        });
	    }else{
			$('#'+overlayID).css("display","none");
	    	overlayPane.animate({'left':'100%'},1000,function(){
	            $('#'+overlayID).fadeOut('fast');
	        });
		    $("span.error").remove();
		    $(".error").removeClass("error");
	    }
		//$('#'+overlayID+" input").val("");
	    document.body.classList.toggle('noscroll', open);

		if(overlayID=="addObject"){
			resetInputText();
		}
		if(overlayID=="libraryuser"){
			resetUserInfoText();
		}
		if(overlayID=="addCitationEntry"){
			resetCitationText();
		}
	}
	

	
	function readMultipleFiles(evt) {

		var field_id = this.id;
		//Retrieve all the files from the FileList object
		var files = evt.target.files;

		if (files) {
			for (var i = 0, f; f = files[i]; i++) {
				var r = new FileReader();
				r.onload = (function(f) {
					return function(e) {
						var contents = e.target.result;
						if (field_id.endsWith('payload')) {
							$("#payloadDropFile").hide();
							$("#payloadTextAreaDisplay").show();
							$('#payloadTextArea').val(contents);
						} else {
							if (field_id.endsWith('input')) {
								$("#inputDropFile").hide();
								$("#inputTextAreaDisplay").show();
								$('#inputTextArea').val(contents);
							} else {
								if (field_id.endsWith('output')) {
									$("#outputDropFile").hide();
									$("#outputTextAreaDisplay").show();
									$('#outputTextArea').val(contents);
								}
							}
						}
					};
				})(f);
				r.readAsText(f);
			}
		} else {
			alert("Failed to load files");
		}
	}
	
</script>
<script type="text/javascript"
	src="/ObjectTeller/resources/js/dropdown.js"></script>
<script src="/ObjectTeller/resources/js/scroll.js"></script>
<script src="/ObjectTeller/resources/js/tabs.js"></script>
<script src="/ObjectTeller/resources/js/custom-file-input.js"></script>
<title><c:out value="${fedoraObject.metadata.title}" /></title>
</head>
<body>
	<button class="greenroundbutton" id="backtotop">
		<img src="<c:url value="/resources/images/Chevron_Icon.png"/>">
	</button>
	<div id="overlay2">
		<%@ include file="secondaryOverlay.jsp"%>
	</div>
	<div id="topfixed">
		<%@ include file="../common/banner.jsp"%>
	</div>
	<div id="top-stuff">
		<%@ include file="../common/navbar.jsp"%>
	</div>
	<div class="banner-content display-banner">
		<div id="goback">
			<div id="leadarrow"></div>

			<a href="/ObjectTeller/home" id="backButton">
				<spring:message code="BACK_RESULTS" />
			</a>
		</div>

		<c:choose>
			<c:when test="${fedoraObject.metadata.published}">
				<div class="pri-pub accessLevelOne">
					<div class="pri-pub1"
						onclick="toggleObject('${fedoraObject.URI}','no')">
						<spring:message code="PRIVATE" />
					</div>
					<div class="pri-pub2 current-tab">
						<div class="minitype-status"></div>
						<spring:message code="PUBLIC" />
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<div class="pri-pub accessLevelOne">
					<div class="pri-pub1 current-tab">

						<spring:message code="PRIVATE" />
					</div>
					<div class="pri-pub2 "
						onclick="toggleObject('${fedoraObject.URI}','yes')">
						<div class="minitype-status"></div>
						<spring:message code="PUBLIC" />
					</div>
				</div>
			</c:otherwise>
		</c:choose>
		<h1>
			<div style="width: 10px; display: inline-block;">
				<c:if test="${fedoraObject.metadata.published}">
					<div class="type-status"></div>
				</c:if>
			</div>
			<small><c:out value="${fedoraObject.metadata.title}"></c:out></small>
		</h1>

		<div class="date">
			<div class="date1">
				<p class="date-title">
					<spring:message code="DATE_CREATED" />
				</p>
				<p class="date-data">
					<fmt:formatDate pattern="MMM, dd, yyyy"
						value="${fedoraObject.metadata.createdOn}" />
				</p>
			</div>
			<div class="date2">
				<p class="date-title">
					<spring:message code="UPDATE_DATE" />
					:
				</p>
				<p class="date-data">
					<fmt:formatDate pattern="MMM, dd, yyyy"
						value="${fedoraObject.metadata.lastModified}" />
				</p>
			</div>

		</div>
<%-- 		<div class="userbutton accessLevelTwo" display="none">
			<ul id="bannericonrow">
				<li>
					<div style="position: relative">
						<button class="roundbutton iconBtn" style="z-index: 10"
							id="objuserlink">
							<img src="<c:url value="/resources/images/Person_Icon.png"/> " />
						</button>
						<button class="greenroundbutton" id="newuser">
							<img src="<c:url value="/resources/images/Plus_Icon.png" />"
								width="10px">
						</button>
					</div>
				</li>
				<li><button class="roundbutton iconBtn" id="JR"
						style="z-index: 9">JR</button></li>
				<li><button class="roundbutton iconBtn" id="AF"
						style="z-index: 8">AF</button></li>
				<li><button class="roundbutton iconBtn" style="z-index: 7"
						id="objsettinglink">
						<img src="<c:url value="/resources/images/Gear_Icon.png"/> " />
					</button></li>
			</ul>
			<div class="floatingInfo" id="objIcons">
				<span></span>
			</div>
		</div> --%>
	</div>

	<div class="header">
		<div class="objectcontainer">
			<div class="headercol">
				<ul id="tabs">
					<li class="labels"><spring:message code="METADATA_TAB" /></li>
					<li class="labels"><spring:message code="PAYLOAD_TAB" /></li>
					<li class="labels"><spring:message code="INPUT_TAB" /></li>
					<li class="labels"><spring:message code="OUTPUT_TAB" /></li>
					<li class="labels accessLevelOne"><spring:message
							code="LOG_DATA_TAB" /></li>


				</ul>
				<div id="ellipsis" class="labels accessLevelOne"><img
					src="<c:url value="/resources/images/more.png"/> " /></div>
			</div>
		</div>
	</div>
	<div class="maincontentwrapper">
		<div class="main-content">
			<div class="datagrid">
				<ul id="tab">
					<li id="metadata">
						<div id="tab-content1" class="tab-content view-obj">
							<h3 class="fieldName inline">
								<spring:message code="METADATA_TAB" />
							</h3>
							<div class="inline editwrapper accessLevelOne">
								<button class="inline edit" id="editButton"
									style="position: relative; left: 0%"><spring:message code="EDIT_BTN" /></button>
							</div>

							<form class="display-content" id="metadata_view">
								<div class="addtext">
									<h4>
										<spring:message code="OBJECT_TITLE" />
									</h4>
									<input type="text" class="metaEdit" id="title_data_v" disabled
										value="${fedoraObject.metadata.title}" />
								</div>

								<div class="addtext">
									<h4>
										<spring:message code="OBJECT_DESCRIPTION" />
									</h4>
									<textarea class="metaEdit" id="description_data_v" disabled>${fedoraObject.metadata.description}</textarea>
								</div>

								<div class="addtext">
									<h4>
										<spring:message code="OBJECT_KEYWORD" />
									</h4>
									<input type="text" class="metaEdit" id="keyword_data_v"
										disabled value="${fedoraObject.metadata.keywords}" />
								</div>

								<div class="addtext">
									<h4>
										<spring:message code="OBJECT_OWNERS" />
									</h4>
									<input type="text" class="metaEdit" id="owner_data_v" disabled
										value="${fedoraObject.metadata.owner}" />
								</div>

								<div class="addtext">
									<h4>
										<spring:message code="OBJECT_CONTRIBUTORS" />
									</h4>
									<input type="text" class="metaEdit" id="contributor_data_v"
										disabled value="${fedoraObject.metadata.contributors}" />
										
									
								</div>

								<div class="addtext">
									<h4>
										<spring:message code="OBJECT_CITATIONS" />
									</h4>
									
									<c:forEach var="citationEntry" items="${fedoraObject.metadata.citations}"
																	varStatus="loopStatus">
										<div><input type="text" class="metaEdit" disabled
												value="${citationEntry.citation_title}" ><input type="hidden" class="metaEdit" 
												value="${citationEntry.citation_at}" ></div>
										</c:forEach>
										<!-- <input type="text" class="metaEdit" id="citation_data_v"
										disabled value="       " /> -->
								</div>
								
									<%-- <div class="addtext">
									<h4>
										<spring:message code="OBJECT_LICENSE" />
									</h4>
									
									<input type="text" class="metaEdit" id="license_data_v"
										disabled value="--------" />
								</div> --%>


								<input type="hidden" path="URI" value="${fedoraObject.URI}" />
							</form>
							<sf:form class="display-content" method="POST"
								action="editMetadata" modelAttribute="fedoraObject"
								style="display:none; position: relative;" id="metadata_edit">

								<button class="done" id="saveButton" type="submit">
									<spring:message code="SAVE_CHANGES_BTN" />
								</button>


								<div class="addtext">
									<h4>
										<spring:message code="OBJECT_TITLE" />
									</h4>
									<sf:input type="text" maxlength="140" class="metaEdit"
										id="title_data" path="metadata.title" value="${fedoraObject.metadata.title}" />
									<span>140/140</span>
								</div>

								<div class="addtext">
									<h4>
										<spring:message code="OBJECT_DESCRIPTION" />
									</h4>
									<sf:textarea maxlength="500" class="metaEdit"
										id="description_data" path="metadata.description"
										value="${fedoraObject.metadata.description}" />
									<span>500/500</span>
								</div>

								<div class="addtext">
									<h4>
										<spring:message code="OBJECT_KEYWORD" />
									</h4>
									<sf:input type="text" maxlength="140" class="metaEdit"
										id="keyword_data" path="metadata.keywords"
										value="${fedoraObject.metadata.keywords}" />
									<span>140/140</span>
								</div>

								<div class="addtext">
									<h4>
										<spring:message code="OBJECT_OWNERS" />
									</h4>
									<sf:input type="text" maxlength="140" class="metaEdit"
										id="owner_data" path="metadata.owner" value="${fedoraObject.metadata.owner}" />
									<span>140/140</span>
								</div>

								<div class="addtext">
									<h4>
										<spring:message code="OBJECT_CONTRIBUTORS" />
									</h4>
									<sf:input type="text" maxlength="140" class="metaEdit"
										id="contributor_data" path="metadata.contributors"
										value="${fedoraObject.metadata.contributors}" />
									<span>140/140</span>
									<sf:input type="hidden" path="URI" value="${fedoraObject.URI}" />
								</div>

								<div class="addtext">
									<h4>
										<spring:message code="OBJECT_CITATIONS" />
									</h4>
									<div class='entryArea' id='citation_data_entry'>
									<c:forEach var="citationEntry" items="${fedoraObject.metadata.citations}"
varStatus="loopStatus">
       <div class="addtext"><sf:input type="text" class="metaEdit" id="citation${loopStatus.index}" value="${citationEntry.citation_title}" path="metadata.citations[${loopStatus.index}].citation_title" />
              <sf:input type="hidden" id="citation${loopStatus.index}_link" class="metaEdit" path="metadata.citations[${loopStatus.index}].citation_at" value="${citationEntry.citation_at}" />
              <sf:input type="hidden" path="metadata.citations[${loopStatus.index}].citation_id" value="${citationEntry.citation_id}" />
              <button class="redroundbutton delete_btn" type="button"><img src="resources/images/Close_Icon.png" width="12px"></button></div>
</c:forEach>
 
									
 									
 									
 										</div>
										<input type="text" class="metaEdit" id="citation_data"
										 placeholder="Click here to add citations.       " />
								</div>
								
<%-- 									<div class="addtext">
									<h4>
										<spring:message code="OBJECT_LICENSE" />
									</h4>
									
									<input type="text" class="metaEdit" id="license_data"
										value="LICENSE" />
								</div> --%>


							</sf:form>
						</div>
					</li>
					<li id="payload">
						<div id="tab-content2" class="tab-content view-obj">
							<h3 class="fieldName inline">
								<spring:message code="PAYLOAD_TAB" />
							</h3>
							<div class="inline editwrapper accessLevelOne">
								<button class="inline edit" id="editPayloadButton"
									style="position: relative; left: 0%"><spring:message code="EDIT_BTN" /></button>
							</div>

							<sf:form class="display-content" id="payloadEdit" method="POST"
								style="display:none;position: relative;" action="editPayload"
								modelAttribute="fedoraObject">
								<button type="submit" class="done" id="savePayloadButton">
									<spring:message code="SAVE_CHANGES_BTN" />
								</button>
								<div class="addtext">
									<h4>
										<spring:message code="PAYLOAD_FUNCTION" />
										<spring:message code="REQUIRED_FIELD" />
									</h4>
									<sf:input class="textbox" type="text"
										placeholder="one instance only" maxlength="140"
										id="payload_function_name"
										path="payloadDescriptor.functionName"
										value="${fedoraObject.payloadDescriptor.functionName}" />
									<span>140/140</span>

								</div>
								<div class="addtext">
									<h4>
										<spring:message code="PAYLOAD_TYPE" />
										<spring:message code="REQUIRED_TO_SELECT" />
									</h4>
									<sf:select path="payloadDescriptor.engineType" class="options" id="engineType">
										<sf:option value="Python">PYTHON</sf:option>
									</sf:select>
								</div>


								<div id="payloadDropFile" class="dropfile" style="display:none;">
									<div class="upload-direction">
										<input type="file" name="file" id="file_payload"
											class="inputfile"
											data-multiple-caption="{count} files selected" multiple
											style="display: none;" /> <label for="file_payload">
											<img
											src="<c:url value="/resources/images/Upload_Icon.png" />" />
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
																	<button id="clearPayloadButton"><spring:message code="REMOVE_BTN" /></button>
									<sf:textarea id="payloadTextArea"
										path="payload"></sf:textarea>
									<sf:input type="hidden" path="URI" value="${fedoraObject.URI}" />
								</div>



							</sf:form>

							<form class="display-content" id="displayPayload">

								<div>
									<h4>
										<spring:message code="PAYLOAD_FUNCTION" />
										<spring:message code="REQUIRED_FIELD" />
									</h4>
									<input type="text" class="metaEdit" id="functionname_data" disabled
										value="${fedoraObject.payloadDescriptor.functionName}">
								</div>

								<div>
									<h4>
										<spring:message code="PAYLOAD_TYPE" />
									</h4>
									<input type="text" class="metaEdit" id="enginetype_data"
										disabled value="${fedoraObject.payloadDescriptor.engineType}">
								</div>


								<div class="display-payload">
									<textarea class="autosize" id="payloadTextArea-v">${fedoraObject.payload}</textarea>
								</div>
							</form>

						</div>
					</li>
					<li id="input">
						<div id="tab-content3" class="tab-content view-obj">
							<h3 class="fieldName inline">
								<spring:message code="INPUT_MESSAGE" />
							</h3>
							<div class="inline editwrapper accessLevelOne">
								<button class="inline edit" id="editInputButton" style="position:relative;"><spring:message code="EDIT_BTN" /></button>
							</div>

							<form class="display-content" id="displayInput">

								<div class="display-payload">
										<textarea class="autosize" id="inputTextArea-v">${processedStringInput}</textarea>
								</div>
							</form>
							<sf:form class="display-content" id="inputEdit" method="POST"
								style="display:none;position: relative;"
								action="editInputMessage" modelAttribute="fedoraObject">
								<button type="submit" class="done" id="saveInputButton">
									<spring:message code="SAVE_CHANGES_BTN" />
								</button>

								<div id="inputDropFile" class="dropfile" style="display:none;">
									<div class="upload-direction">
										<input type="file" name="file_input" id="file_input"
											class="inputfile"
											data-multiple-caption="{count} files selected" multiple
											style="display: none;" /> <label for="file_input"> <img
											src="<c:url value="/resources/images/Upload_Icon.png"/> " />
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
										<spring:message code="INPUT_MESSAGE_DOWNLOAD" />
									</p>
								</div>

								<div class="display-payload" id="inputTextAreaDisplay">
																		<button id="clearInputButton"><spring:message code="REMOVE_BTN" /></button>
									<sf:textarea class="autosize" id="inputTextArea"
									
										path="inputMessage"></sf:textarea>
									<sf:input type="hidden" path="URI" value="${fedoraObject.URI}" />
								</div>
							</sf:form>


						</div>
					</li>
					<li id="output">
						<div id="tab-content4" class="tab-content view-obj">
							<h3 class="fieldName inline">
								<spring:message code="OUTPUT_MESSAGE" />
							</h3>
							<div class="inline editwrapper accessLevelOne">
								<button class="inline edit" id="editOutputButton"
									style="position: relative; left: 0%"><spring:message code="EDIT_BTN" /></button>
							</div>
							<form class="display-content" id="displayOutput">

								<div class="display-payload">
									<textarea class="autosize" id="outputTextArea-v">${processedStringOutput}</textarea>
								
								</div>
							</form>
							<sf:form class="display-content" id="outputEdit" method="POST"
								style="display:none;position: relative;"
								action="editOutputMessage" modelAttribute="fedoraObject">
								<button type="submit" class="done" id="saveOutputButton">
									<spring:message code="SAVE_CHANGES_BTN" />
								</button>

								<div id="outputDropFile" class="dropfile" style="display:none;">
									<div class="upload-direction">
										<input type="file" name="file_output" id="file_output"
											class="inputfile"
											data-multiple-caption="{count} files selected" multiple
											style="display: none;" /> <label class="upload_label"
											for="file_output"> <img
											src="<c:url value="/resources/images/Upload_Icon.png"/> " />
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
										<spring:message code="OUTPUT_MESSAGE_DOWNLOAD" />
									</p>
								</div>

			<div class="display-output" id="outputTextAreaDisplay">
									<button id="clearOutputButton"><spring:message code="REMOVE_BTN" /></button>
									<sf:textarea class="autosize" id="outputTextArea"
										path="outputMessage"></sf:textarea>
									<sf:input type="hidden" path="URI" value="${fedoraObject.URI}" />
								</div>
							</sf:form>
						</div>
					</li>
					<li id="logdata">
						<div id="tab-content5" class="tab-content view-obj">
							<h3 class="fieldName inline ">
								<spring:message code="LOG_DATA_TAB" />
							</h3>

							<section class="display-content">
							<div class="display-payload">
								<p>${processedLogData}</p>
							</div>
							</section>
						</div>
					</li>
					<li id="rfu">
						<div id="tab-content6" class="tab-content view-obj">
							<h3 class="fieldName inline">RFU</h3>
							<div class="inline edit">
								<spring:message code="EDIT_BTN" />
							</div>
							<section class="display-content">
							<div>
								<p>
									{<br> Code from database <br>}
								</p>
							</div>
							</section>
						</div>
					</li>
				</ul>
			</div>
		</div>
	</div>
</body>
</html>