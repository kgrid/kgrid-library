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
<script src="<c:url value="/resources/js/iconbutton.js"/>"></script>
<script src="<c:url value="/resources/js/multi_step_form.js"/>"></script>
<script src="<c:url value="/resources/js/scroll.js"/>"></script>
<script src="<c:url value="/resources/js/jquery.validate.js"/>"></script>
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
		var kotitle = $("#ko-title h1 small").text();
		var title_length = kotitle.length;
		console.log("Title: " + kotitle + " Title Length: " + title_length);
		if (title_length > 74) {
			$("#ko-title h1 small").css("font-size", "24px");
		} else {
			$("#ko-title h1 small").css("font-size", "28px");
		}
		var accessLevel = 1;
		if (userObj == "null") {
			$(".accessLevelOne").css({
				"visibility" : "hidden"
			});
			$("#session").show();
			$("#logoutsession").hide();
		} else {
			$("#session").hide();
			$("#logoutsession").show();
			switch (accessLevel) {
			default:
				$(".accessLevelOne").css({
					"visibility" : "hidden"
				});
				break;
			case 1:
				$(".accessLevelOne").css({
					"visibility" : "visible"
				});
				break;
			}
		}

		$("#metadataeditBtn").click(function() {

			//console.log($(this).attr("id"));
			var uri = $("#fObj").val();
			console.log("Current URI:" + uri);
			setURI(uri);
			setActiveTab("metadata");
			overlaySlide('addObject', true);

		});

		$("[id$='EditBtn']").click(function() {
			var btn_id = this.id;
			switch (btn_id) {
			case "payloadEditBtn":
				var section = btn_id.replace("EditBtn", "");
				break;
			default:
				var section = btn_id.replace("EditBtn", "Message");
				break;
			}
			var uri = $("#fObj").val();
			console.log("Current URI:" + uri + " Section:" + section);
			//setActiveTab(section);
			overlaySlide('addObject', true);

		});

		/* 				$('[id^="file_"]').on('change', readMultipleFiles); */

	});
</script>
<script type="text/javascript"
	src="<c:url value="/resources/js/dropdown.js"/>"></script>
<script src="<c:url value="/resources/js/scroll.js"/>"></script>
<script src="<c:url value="/resources/js/tabs.js"/>"></script>
<title><c:out value="${fedoraObject.metadata.title}" /></title>
</head>
<body>
	<button class="greenroundbutton" id="backtotop">
		<img src="<c:url value="/resources/images/Chevron_Icon.png"/>">
	</button>
<%-- 	<div id="login_overlay" class="layered_overlay" >
		<%@ include file="../account/narrowOverlay.jsp"%>
	</div> --%>
	<div id="addObject" class="layered_overlay">
		<%@ include file="../objects/createNewObject.jsp"%>
	</div>
	<div id="topfixed">
		<%@ include file="../common/banner.jsp"%>
	</div>
	<div id="top-stuff">
		<%@ include file="../common/navbar.jsp"%>
	</div>
	<div class="banner-content display-banner">
		<div id="goback">
			<div id="backButton">
				<a href="<c:url value="/home"/>"> <spring:message
						code="BACK_RESULTS" />
				</a>
			</div>
		</div>
		<c:choose>
			<c:when test="${fedoraObject.metadata.published}">
				<div class="pri-pub accessLevelOne">
					<div class="pri-pub1"
						onclick="toggleObject('${fedoraObject.URI}','no')">
						<span><spring:message code="PRIVATE" /></span>
					</div>
					<div class="pri-pub2 current-tab">
						<div class="minitype-status"></div>
						<div>
							<span class="middleout"><spring:message code="PUBLIC" /></span>
						</div>
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<div class="pri-pub accessLevelOne">
					<div class="pri-pub1 current-tab">
						<span class="middleout"><spring:message code="PRIVATE" /></span>
					</div>
					<div class="pri-pub2 "
						onclick="toggleObject('${fedoraObject.URI}','yes')">
						<div class="minitype-status"></div>
						<div>
							<span><spring:message code="PUBLIC" /></span>
						</div>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
		<div id="ko-title">
			<div style="width: 10px; hieght: 60px; display: inline-block;">
				<c:if test="${fedoraObject.metadata.published}">
					<div class="type-status"></div>
				</c:if>
			</div>
			<h1>
				<small id="page_title_v"><c:out value="${fedoraObject.metadata.title}"></c:out></small>
			</h1>
		</div>
		<div class="date">
			<div class="id">
				<p class="date-title">Object ID:</p>
				<p class="date-data">
					<span>${fedoraObject.URI}</span>
				</p>
			</div>
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

	</div>
	<div class="header">
		<div class="objectcontainer">
			<div class="headercol">
				<ul class="view" id="tabs">
					<li class="labels"><spring:message code="METADATA_TAB" /></li>
					<li class="labels"><spring:message code="PAYLOAD_TAB" /></li>
					<li class="labels"><spring:message code="INPUT_TAB" /></li>
					<li class="labels"><spring:message code="OUTPUT_TAB" /></li>
					<li class="labels accessLevelOne"><spring:message
							code="LOG_DATA_TAB" /></li>
				</ul>
				<div id="ellipsis" class="labels iconBtn accessLevelOne">
					<img class="hover-out"
						src="<c:url value="/resources/images/More_Icon_Light-01.png"/> " />
					<img class="hover-in" style="display: none;"
						src="<c:url value="/resources/images/More_Icon_Dark-01.png"/> " />

				</div>
				<div class="inline editwrapper accessLevelOne" id="objBtns"
					style="top: 4px; right: 40px; position: absolute;">
					<button class="edit" id="metadataeditBtn">EDIT</button>
					<button class="edit" id="deleteButton"
						onclick="deleteObject('${fedoraObject.URI}')">DELETE</button>

					<a class="edit" id="downloadButton"
						href="../knowledgeObject/${fedoraObject.URI}/complete.json">DOWNLOAD</a>

				</div>
			</div>
		</div>
	</div>
	<div class="maincontentwrapper">
		<div class="main-content">
			<div class="datagrid">
				<ul class="view" id="tab">
					<li id="metadata">
						<div id="tab-content1" class="tab-content view-obj">
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
									<div class='entryArea' id='citation_data_entry_v'>
										<c:forEach var="citationEntry"
											items="${fedoraObject.metadata.citations}"
											varStatus="loopStatus">
											<div>
												<input type="text" class="ctitle" disabled
													value="${citationEntry.citation_title}"><input
													type="hidden" class="clink"
													value="${citationEntry.citation_at}">
											</div>
										</c:forEach>
									</div>
								</div>
								<div class="addtext">
									<h4>
										<spring:message code="OBJECT_LICENSE" />
									</h4>

									<input type="text" class="metaEdit" disabled
										id="license_data_v"
										value="${fedoraObject.metadata.license.licenseName}" /> <input
										type="hidden" class="metaEdit" id="licenseLink_v"
										value="${fedoraObject.metadata.license.licenseLink}">
								</div>
								<input type="hidden" id="fObj" value="${fedoraObject.URI}" />
							</form>
						</div>
					</li>
					<li id="payload">
						<div id="tab-content2" class="tab-content view-obj">
							<form class="display-content" id="displaypayload">
								<div>
									<h4>
										<spring:message code="PAYLOAD_FUNCTION" />
										<spring:message code="REQUIRED_FIELD" />
									</h4>
									<input type="text" class="metaEdit" id="functionname_data_v"
										disabled value="${fedoraObject.payload.functionName}">
								</div>
								<div>
									<h4>
										<spring:message code="PAYLOAD_TYPE" />
									</h4>
									<input type="text" class="metaEdit" id="enginetype_data_v"
										disabled value="${fedoraObject.payload.engineType}">
								</div>
								<div class="display-payload">
									<textarea class="autosize" spellcheck="false" id="payloadTextArea_v">${fedoraObject.payload.content}</textarea>
								</div>
							</form>
						</div>
					</li>
					<li id="input">
						<div id="tab-content3" class="tab-content view-obj">
							<form class="display-content" id="displayinput">
								<div class="display-payload">
									<textarea class="autosize" id="inputTextArea_v">${fedoraObject.inputMessage}</textarea>
								</div>
							</form>

						</div>
					</li>
					<li id="output">
						<div id="tab-content4" class="tab-content view-obj">
							<form class="display-content" id="displayoutput">
								<div class="display-payload">
									<textarea class="autosize" id="outputTextArea_v">${fedoraObject.outputMessage}</textarea>
								</div>
							</form>
							<sf:form class="display-content" id="outputEdit" method="POST"
								style="display:none;position: relative;"
								action="editOutputMessage" modelAttribute="fedoraObject">
								<button type="submit" class="done" id="saveOutputButton">
									<spring:message code="SAVE_CHANGES_BTN" />
								</button>
								<div id="outputDropFile" class="dropfile" style="display: none;">
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
									<button id="outputClearBtn">
										<spring:message code="REMOVE_BTN" />
									</button>
									<sf:textarea class="autosize" id="outputTextArea" spellcheck="false"
										path="outputMessage"></sf:textarea>
									<sf:input type="hidden" path="URI" value="${fedoraObject.URI}" />
									<sf:input type="hidden" id="fedoraObj" path="URI"
										value="${fedoraObject}" />
								</div>
							</sf:form>
						</div>
					</li>
					<li id="logdata">
						<div id="tab-content5" class="tab-content view-obj">
							<form class="display-content">
								<div class="display-payload">
									<textarea class="autosize" spellcheck="false" id="logTextArea"> ${processedLogData}</textarea>
										
								</div>
							</form>
						</div>
					</li>

				</ul>
			</div>
		</div>
	</div>
</body>
</html>