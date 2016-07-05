<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="javax.servlet.http.HttpSession" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="<c:url value="/resources/css/w3.css" />"
	type="text/css" />
<link rel="stylesheet" href="<c:url value="/resources/css/box.css"/>"
	type="text/css" />
<link rel="stylesheet" href="<c:url value="/resources/css/style.css"/>"
	type="text/css" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/bannercontent.css"/>"
	type="text/css" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/datagrid.css"/>" type="text/css" />
<link rel="stylesheet" href="<c:url value="/resources/css/button.css"/>"
	type="text/css" />
<link rel="stylesheet" href="<c:url value="/resources/css/navigation.css"/>" type="text/css" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/overlay.css"/>" type="text/css" />
<link rel="stylesheet" href="<c:url value="/resources/css/tab.css"/>"
	type="text/css" />
<link rel="shortcut icon"
	href="<c:url value="/resources/images/MiniIconObjectTeller.ico" /> " />
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
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
					var key = "first_name=";
					var n = userObj.indexOf(key);
				    var newStr = userObj.substring(n+key.length, userObj.length);
				    var commaIndex = newStr.indexOf(",");
				    var name = newStr.substring(0,commaIndex);
				
					$("#user").append(name);
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
					var buttonText = $("#editButton").text();
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
					if (buttonText == "EDIT") {
						$("#displayPayload").hide();
						$("#payloadTextArea").hide();
						$("#payloadEdit").show();
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
					if (buttonText == "EDIT") {
						$("#displayInput").hide();
						$("#inputTextArea").hide();
						$("#inputEdit").show();
						$("#editInputButton").text("CANCEL");
						$("#editInputButton").css("left", "540px");
					} else {
						$("#displayInput").show();
						$("#inputEdit").hide();
						$("#editInputButton").text("EDIT");
						$("#editInputButton").css("left", "0%");

					}
				});
				$("#editOutputButton").click(function() {
					var buttonText = $("#editOutputButton").text();
					if (buttonText == "EDIT") {
						$("#displayOutput").hide();
						$("#outputTextArea").hide();
						$("#outputEdit").show();
						$("#editOutputButton").text("CANCEL");
						$("#editOutputButton").css("left", "540px");
					} else {
						$("#displayOutput").show();
						$("#outputEdit").hide();
						$("#editOutputButton").text("EDIT");
						$("#editOutputButton").css("left", "0%");

					}
				});
				$("#login").click( function login() {
					
					
					var user = new Object();

					user.username = document.getElementById("username").value;
					user.passwd = document.getElementById("passwd").value;

					var text = JSON.stringify(user);

					$.ajax({
								beforeSend : function(xhrObj) {
									xhrObj.setRequestHeader("Content-Type",
											"application/json");
									xhrObj.setRequestHeader("Accept", "application/json");
								},
								type : 'POST',
								url : "Onlylogin",
								data : text,
								dataType : "json",

								success : function(response) {
									 if(response!='empty') {
										  var test = JSON.stringify(response);
									      var obj = JSON.parse(test);
									      
											location.reload();
									    }
								} ,
								
								error : function(response) {
									alert("failure " +response.status);
								}
							});
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

/* 	function disablePayloadEdit() {
		document.getElementById("editPayloadButton").style.display = "inline-block";
		document.getElementById("savePayloadButton").style.display = "none";
		document.getElementById("cancelPayloadButton").style.display = "none";
		$("#payloadEdit").hide();
		$("#displayPayload").show();
	} */


	function toggleObject(uri, param) {
		$.ajax({
			type : 'GET',
			url : "publishObject." + uri + "/" + param,

			success : function(response) {

				location.reload();
			}
		});

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
							$("#payloadTextArea").show();
							$('#payloadTextArea').val(contents);
						} else {
							if (field_id.endsWith('input')) {
								$("#inputDropFile").hide();
								$("#inputTextArea").show();
								$('#inputTextArea').val(contents);
							} else {
								if (field_id.endsWith('output')) {
									$("#outputDropFile").hide();
									$("#outputTextArea").show();
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
	
	function userLogout(){
		
	
		$.ajax({
			type : 'POST',
			url : "logout" ,

			success : function(response) {
				
				 window.location = "/ObjectTeller/login";
			}
		});
	}
	
	function goBack() {
		var userObj ="<%=session.getAttribute("DBUser")%>";
		if (userObj == "null") {
			$.ajax({
				type : 'GET',
				url : "login",

				success : function(response) {
					 window.location = "/ObjectTeller/login";
				}
			});
		} else {
			$.ajax({
				type : 'GET',
				url : "objects",

				success : function(response) {
					 window.location = "/ObjectTeller/objects";
				}
			});
		}
	}
</script>
<script type="text/javascript"
	src="/ObjectTeller/resources/js/dropdown.js"></script>
<script src="/ObjectTeller/resources/js/scroll.js"></script>
<script src="/ObjectTeller/resources/js/tabs.js"></script>
<script src="/ObjectTeller/resources/js/custom-file-input.js"></script>
<title><c:out value="${fedoraObject.title}" /></title>
</head>
<body>
	<div id="logo">
		<img src="<c:url value="/resources/images/logo.png"/>" width="200px"
			height="auto">
	</div>

	<div id="top-stuff">
		<%@ include file="../common/navbar.jsp"%>
	</div>
	<div class="active-links">

		<div id="session" class="login-link">
			<a id="signin-link" href="#"> <strong><spring:message
						code="LOG_IN_BUTTON" /></strong>
			</a>
		</div>
		<div id="signin-dropdown" class="login-link">

			<form class="signin" id="loginForm">
				<fieldset class="textbox">
					<input name="username" type="text" id="username" autocomplete="on"
						placeholder="Username"></input> <input type="password"
						placeholder="Password" id="passwd" name="passwd"
						autocomplete="off"></input>
				</fieldset>
				<fieldset class="remb">
					<label class="remember"> <input type="checkbox" value="1"
						name="remember_me" /> <span><spring:message
								code="REMEMBER_ME_BUTTON" /></span>
					</label>
					<button id="login" class="submit button" type="button">
						<spring:message code="LOG_IN_BUTTON" />
					</button>
				</fieldset>
				<p>
					<a href="retrievepw.html"> <spring:message
							code="PASSWORD_RESET" /></a>
				</p>
			</form>
		</div>


		<div id="logoutsession" class="logout-link">
			<ul>
				<li id="user"><a id="user-link" href="#"> <strong><spring:message
								code="HELLO_MESSAGE" /> </strong>
				</a></li>
				<li id="icon" class="down"><img
					src="<c:url value="/resources/images/Chevron_Icon.png"/>" /></li>
			</ul>
		</div>
		<div id="logout-dropdown" class="logout-link">
			<form class="signin" >
				<fieldset class="links">
					<button id="settings" onclick="openNav()">
						<spring:message code="SETTINGS" />
					</button>
				</fieldset>
			</form>
			<sf:form method="POST" class="signin" action="logout" id="logoutForm">
				<fieldset class="links">
					<button id="logout" class="submit button" type="submit" onclick="userLogout()"><spring:message code="LOGOUT" />
					</button>
				</fieldset>
			</sf:form>
		</div>

	</div>



	<div class="banner-content display-banner">
		<div id="goback">
			 <div id="leadarrow">
    </div>

			<button onclick="goBack()" id="backButton"><spring:message code="BACK_RESULTS" /></button>
		</div>

		<c:choose>
			<c:when test="${fedoraObject.published}">
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
			<div style="width: 10px; display: inline-block; margin-left:22px;">
				<c:if test="${fedoraObject.published}">
					<div class="type-status"></div>
				</c:if>
			</div>
			<small><c:out value="${fedoraObject.title}"></c:out></small>
		</h1>

		<div class="date">
			<div class="date1">
				<p class="date-title"><spring:message code="DATE_CREATED" /></p>
				<p class="date-data">
					<fmt:formatDate pattern="MMM, dd, yyyy"
						value="${fedoraObject.createdOn}" />
				</p>
			</div>
			<div class="date2">
				<p class="date-title"><spring:message code="UPDATE_DATE" />:</p>
				<p class="date-data">
					<fmt:formatDate pattern="MMM, dd, yyyy"
						value="${fedoraObject.lastModified}" />
				</p>
			</div>

		</div>
		<div class="userbutton accessLevelOne">
						<ul id="bannericonrow">
				<li>
				<div style="position:relative"><button class="roundbutton" style="z-index: 10" id="userlink">
				<img src="<c:url value="/resources/images/Person_Icon.png"/> " />
			</button>
			<button class="greenroundbutton" id="newuser">
						<img src="<c:url value="/resources/images/Plus_Icon.png" />"
							width="10px">
					</button></div></li>
			<li><button class="roundbutton" style="z-index: 9">JR</button></li>
			<li><button class="roundbutton" style="z-index: 8">AF</button></li>
			<li><button class="roundbutton" style="z-index: 7" id="settinglink">
				<img src="<c:url value="/resources/images/Gear_Icon.png"/> " />
			</button></li></ul>
		</div>
	</div>
	
	<div class="header">
		<div class="objectcontainer">
			<div class="headercol">
				<ul id="tabs">
					<li class="labels"><spring:message code="METADATA_TAB"/></li>
					<li class="labels"><spring:message code="PAYLOAD_TAB"/></li>
					<li class="labels"><spring:message code="INPUT_TAB"/></li>
					<li class="labels"><spring:message code="OUTPUT_TAB"/></li>
					<li class="labels accessLevelOne"><spring:message code="LOG_DATA_TAB" /></li>
					<li class="labels accessLevelOne"><img
						src="<c:url value="/resources/images/more.png"/> " /></li>

				</ul>
			</div>
		</div>
	</div>

	<div class="main-content">
		<div class="datagrid">
			<ul id="tab">
				<li id="metadata">
					<div id="tab-content1" class="tab-content view-obj">
						<h3 class="fieldName inline"><spring:message code="METADATA_TAB"/></h3>
						<div class="inline editwrapper accessLevelOne">
							<button class="inline edit" id="editButton"
								style="position: relative; left: 0%"><spring:message code="EDIT_BTN" /></button>
						</div>

						<form class="display-content" id="metadata_view">
							<div>
								<h4><spring:message code="OBJECT_TITLE" /></h4>
								<input type="text" class="metaEdit" id="title_data_v" disabled
									value="${fedoraObject.title}" />
							</div>

							<div>
								<h4><spring:message code="OBJECT_DESCRIPTION" /></h4>
								<input type="text" class="metaEdit" id="description_data_v"
									disabled value="${fedoraObject.description}" />
							</div>

							<div>
								<h4><spring:message code="OBJECT_KEYWORD" /></h4>
								<input type="text" class="metaEdit" id="keyword_data_v" disabled
									value="${fedoraObject.keywords}" />
							</div>

							<div>
								<h4><spring:message code="OBJECT_OWNERS" /></h4>
								<input type="text" class="metaEdit" id="owner_data_v" disabled
									value="${fedoraObject.owner}" />
							</div>

							<div>
								<h4><spring:message code="OBJECT_CONTRIBUTORS" /></h4>
								<input type="text" class="metaEdit" id="contributor_data_v"
									disabled value="${fedoraObject.contributors}" /> <input
									type="hidden" path="URI" value="${fedoraObject.URI}" />
							</div>

						</form>
						<sf:form class="display-content" method="POST"
							action="editMetadata" modelAttribute="fedoraObject"
							style="display:none; position: relative;" id="metadata_edit">

							<button class="done" id="saveButton"
								style="position: absolute;right: 250px; top:-65px;" type="submit"><spring:message code="SAVE_CHANGES_BTN" /></button>


							<div>
								<h4><spring:message code="OBJECT_TITLE" /></h4>
								<sf:input type="text" class="metaEdit" id="title_data"
									path="title" value="${fedoraObject.title}" />
							</div>

							<div>
								<h4><spring:message code="OBJECT_DESCRIPTION" /></h4>
								<sf:input type="text" class="metaEdit" id="description_data"
									path="description" value="${fedoraObject.description}" />
							</div>

							<div>
								<h4><spring:message code="OBJECT_KEYWORD" /></h4>
								<sf:input type="text" class="metaEdit" id="keyword_data"
									path="keywords" value="${fedoraObject.keywords}" />
							</div>

							<div>
								<h4><spring:message code="OBJECT_OWNERS" /></h4>
								<sf:input type="text" class="metaEdit" id="owner_data"
									path="owner" value="${fedoraObject.owner}" />
							</div>

							<div>
								<h4><spring:message code="OBJECT_CONTRIBUTORS" /></h4>
								<sf:input type="text" class="metaEdit" id="contributor_data"
									path="contributors" value="${fedoraObject.contributors}" />
								<sf:input type="hidden" path="URI" value="${fedoraObject.URI}" />
							</div>

						</sf:form>
					</div>
				</li>
				<li id="payload">
					<div id="tab-content2" class="tab-content view-obj">
						<h3 class="fieldName inline" ><spring:message code="PAYLOAD_TAB"/></h3>
						<div class="inline editwrapper accessLevelOne">
							<button class="inline edit" id="editPayloadButton"
								style="position: relative; left: 0%"><spring:message code="EDIT_BTN" /></button>
						</div>

						<sf:form class="display-content" id="payloadEdit" method="POST"
							style="display:none;position: relative;" action="editPayload"
							modelAttribute="fedoraObject">
							<button type="submit" class="done"
								style="position: absolute; right: 250px; top:-65px;" id="savePayloadButton"><spring:message code="SAVE_CHANGES_BTN" />
								</button>


							<div class="Add-content">


								<h4><spring:message code="PAYLOAD_FUNCTION" /> <spring:message code="REQUIRED_FIELD" /></h4>
								<sf:input class="textbox" type="text"
									placeholder="one instance only" maxlength="140"
									id="payload_function_name"
									path="payloadDescriptor.functionName"
									value="${fedoraObject.payloadDescriptor.functionName}" />
								<h4><spring:message code="PAYLOAD_TYPE" /> <spring:message code="REQUIRED_TO_SELECT"/></h4>
								<sf:select path="payloadDescriptor.engineType">
									<sf:option value="Python">PYTHON</sf:option>
								</sf:select>

								<div id="payloadDropFile" class="dropfile">
									<div class="upload-direction">
										<input type="file" name="file" id="file_payload"
											class="inputfile"
											data-multiple-caption="{count} files selected" multiple
											style="display: none;" /> <label for="file_payload">
											<img
											src="<c:url value="/resources/images/Upload_Icon.png" />" />
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
							</div>

							<div class="display-payload">
								<sf:textarea id="payloadTextArea"
									style="width:100%; height:200px;background:transparent;"
									path="payload"></sf:textarea>
								<sf:input type="hidden" path="URI" value="${fedoraObject.URI}" />
							</div>



						</sf:form>

						<form class="display-content" id="displayPayload">

							<div>
								<h4><spring:message code="PAYLOAD_FUNCTION" /> <spring:message code="REQUIRED_FIELD" /></h4>
								<input type="text" class="metaEdit" id="title_data" disabled
									value="${fedoraObject.payloadDescriptor.functionName}">
							</div>

							<div>
								<h4><spring:message code="PAYLOAD_TYPE" /></h4>
								<input type="text" class="metaEdit" id="description_data"
									disabled value="${fedoraObject.payloadDescriptor.engineType}">
							</div>

							<section class="display-content">
							<div class="display-payload">
								<p>${fedoraObject.payload}</p>
							</div>
							</section>
						</form>

					</div>
				</li>
				<li id="input">
					<div id="tab-content3" class="tab-content view-obj">
						<h3 class="fieldName inline"><spring:message code="INPUT_MESSAGE" /></h3>
						<div class="inline editwrapper accessLevelOne">
							<button class="inline edit" id="editInputButton"
								style="position: relative; left: 0%"><spring:message code="EDIT_BTN" /></button>
						</div>

						<form class="display-content" id="displayInput">

							<div class="display-payload">
								<p>${processedStringInput}</p>
							</div>
						</form>
						<sf:form class="display-content" id="inputEdit" method="POST"
							style="display:none;position: relative;" action="editInputMessage"
							modelAttribute="fedoraObject">
							<button type="submit" class="done" style="position: absolute; right: 250px; top:-65px;" id="saveInputButton"><spring:message code="SAVE_CHANGES_BTN" /></button>

							<div id="inputDropFile" class="dropfile">
								<div class="upload-direction">
									<input type="file" name="file_input" id="file_input"
										class="inputfile"
										data-multiple-caption="{count} files selected" multiple
										style="display: none;" /> <label for="file_input"> <img
										src="<c:url value="/resources/images/Upload_Icon.png"/> " />
										<br>
										<p class="green"><label for="file_payload"><spring:message code="CHOOSE_FILE" /></label></p> <br>
										<p><spring:message code="FILE_TYPE" /></p>
									</label>
								</div>
								<p class="instruction">
									<spring:message code="CLICK" /><a href=#><spring:message code="HERE" /></a><spring:message code="INPUT_MESSAGE_DOWNLOAD" />
								</p>
							</div>

							<div class="display-payload">
								<sf:textarea id="inputTextArea"
									style="width:100%; height:200px;background:transparent;"
									path="inputMessage"></sf:textarea>
								<sf:input type="hidden" path="URI" value="${fedoraObject.URI}" />
							</div>
						</sf:form>


					</div>
				</li>
				<li id="output">
					<div id="tab-content4" class="tab-content view-obj">
						<h3 class="fieldName inline"><spring:message code="OUTPUT_MESSAGE" /></h3>
						<div class="inline editwrapper accessLevelOne">
							<button class="inline edit" id="editOutputButton"
								style="position: relative; left: 0%"><spring:message code="EDIT_BTN" /></button>
						</div>
						<form class="display-content" id="displayOutput">

							<div class="display-payload" >
								<p>${processedStringOutput}</p>
							</div>
						</form>
						<sf:form class="display-content" id="outputEdit" method="POST"
							style="display:none;position: relative;" action="editOutputMessage"
							modelAttribute="fedoraObject">
							<button type="submit" class="done" style="position: absolute; right: 250px; top:-65px;" id="saveOutputButton"><spring:message code="SAVE_CHANGES_BTN" /></button>

							<div id="outputDropFile" class="dropfile">
								<div class="upload-direction">
									<input type="file" name="file_output" id="file_output"
										class="inputfile"
										data-multiple-caption="{count} files selected" multiple
										style="display: none;" /> <label class="upload_label"
										for="file_output"> <img
										src="<c:url value="/resources/images/Upload_Icon.png"/> " />
										<br>
										<p class="green"><label for="file_payload"><spring:message code="CHOOSE_FILE" /></label></p> <br>
										<p><spring:message code="FILE_TYPE" /></p>
									</label>
								</div>
								<p class="instruction">
									<spring:message code="CLICK" /><a href=#><spring:message code="HERE" /></a><spring:message code="OUTPUT_MESSAGE_DOWNLOAD" />
								</p>
							</div>

							<div class="display-payload">
								<sf:textarea id="outputTextArea"
									style="width:100%; height:200px;background:transparent;"
									path="outputMessage"></sf:textarea>
								<sf:input type="hidden" path="URI" value="${fedoraObject.URI}" />
							</div>
						</sf:form>
					</div>
				</li>
				<li id="logdata">
					<div id="tab-content5" class="tab-content view-obj">
						<h3 class="fieldName inline "><spring:message code="LOG_DATA_TAB" /></h3>

						<section class="display-content">
						<div class="display-payload">
							<p>
								${processedLogData}
							</p>
						</div>
						</section>
					</div>
				</li>
				<li id="rfu">
					<div id="tab-content6" class="tab-content view-obj">
						<h3 class="fieldName inline">RFU</h3>
						<div class="inline edit"><spring:message code="EDIT_BTN" /></div>
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
</body>
</html>