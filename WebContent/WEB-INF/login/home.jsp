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

<link rel="stylesheet" href="<c:url value="/resources/css/style.css" />"
	type="text/css" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/bannercontent.css" />"
	type="text/css" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/datagrid.css" />" type="text/css" />

<link rel="stylesheet"
	href="<c:url value="/resources/css/button.css" />" type="text/css" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/overlay.css" />" type="text/css" />
<link rel="shortcut icon"
	href="<c:url value="/resources/images/MiniIconObjectTeller.ico" />" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/navigation.css" />" 	type="text/css" />
	<link rel="stylesheet"
	href="<c:url value="/resources/css/librarysetting.css" />"	type="text/css" />
	<link rel="stylesheet"
	href="<c:url value="/resources/css/formstyle.css" />" type="text/css" />
<title><spring:message code="LOGIN_TITLE" /></title>
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
<script type="text/javascript"
	src="/ObjectTeller/resources/js/dropdown.js"></script>
<script src="/ObjectTeller/resources/js/scroll.js"></script>

<script>
	
	function overlayToggle(overlayID, open){
		var olElement = document.getElementById(overlayID);
		if(open){
			olElement.style.display="block";
			olElement.style.width="100%";
		}else{
			olElement.style.display="none";
			olElement.style.width="0%";
		}
	    document.body.classList.toggle('noscroll', open);
		if(overlayID=="addObject"){
			resetInputText();
		}
		if(overlayID=="libraryuser"){
			resetUserInfoText();
		}
		
	}

	function resetUserInfoText(){
		$( "input[id$='_data']" ).val( "" );
	}
	
	
	function resetInputText() {
		$('#payloadTextArea').val("");
		$('#inputTextArea').val("");
		$('#outputTextArea').val("");
		$( "input[id$='_data']" ).val( "" );

	}


</script>
</head>
<body>


	<div id="libsettings"  class="overlay" aria-hidden="true">
     	<%@ include file="../system/librarySetting.jsp" %>
	 </div>

	<div id="addObject" class="overlay" aria-hidden="true">
		<%@ include file="../objects/createNewObject.jsp"%>
	</div>
	<div id="libraryuser"  class="overlay" aria-hidden="true">
     	<%@ include file="../account/libraryUser.jsp" %>
	 </div> 
	<button class="greenroundbutton" id="backtotop">
		<img src="<c:url value="/resources/images/Chevron_Icon.png"/>">
	</button>

	<div id="topfixed">
			<%@ include file="../common/banner.jsp"%>
	</div>
	<div id="top-stuff">
		<%@ include file="../common/navbar.jsp"%>
	</div>

	<c:choose>
		<c:when test="${ empty loggedInUser }">
			<div id="bannerbk">
 				<img src="<c:url value="/resources/images/bannerBackground.png"/>"> 
			</div>

			<div class="banner-content" id="landing">
				<h1>
					<spring:message code="PRODUCT_NAME" />
					<small><spring:message code="PRODUCT_NAME_DESCRIPTION" /></small>
				</h1>
			</div>
		</c:when>
		<c:otherwise>
			<div id="homebanner" class="banner-content">
				<div id="bannercontent">
					<h1>
						<small>${LibraryName} <spring:message
								code="TOP_MENU_LIBRARY" /></small>
					</h1>
					<table>
						<tr>
							<td><spring:message code="SERVER_URL" />
								<div id="h5">${ServerURL}</div></td>
						</tr>
					</table>
					<table>
						<tr>
							<td class="align-type"><spring:message
									code="PANEL_FIELD_NO_OF_OBJECTS" />
								<div id="h5">${TotalObjects}</div></td>
							<td class="align-title"><spring:message
									code="PANEL_FIELD_NO_OF_PUBLISHED_OBJECTS" />
								<div id="h5">${PublishedObjects}</div></td>
							<td class="aign-update"><spring:message code="IP_ADDRESS" />
								<div id="h5">${FedoraIpAddress}</div></td>
						</tr>
					</table>
				</div>
				<div id="bannericons">
					<ul id="bannericonrow">
						<li><div style="position: relative">
								<button class="roundbutton iconBtn" id="userlink"
									onclick="overlayToggle('libraryuser',true)">
									<img src="<c:url value="/resources/images/Person_Icon.png"/> " />
								</button>
								<button class="greenroundbutton iconBtn" id="newuser">
									<img src="<c:url value="/resources/images/Plus_Icon.png" />"
										width="10px">
								</button>
							</div></li>
						<li>
							<button class="roundbutton open-overlay iconBtn" type="button"
								id="settinglink" onclick="overlayToggle('libsettings',true)">
								<img src="<c:url value="/resources/images/Gear_Icon.png"/> " />
							</button>
					</ul>
					<div class="floatingInfo" id="homeIcons">
						<span></span>
					</div>
				</div>

			</div>
		</c:otherwise>
	</c:choose>

	<div class="header">
		<button class="greenroundbutton open-overlay"  type="button" id="addObjbutton"
			 onclick="overlayToggle('addObject',true)">
			<img src="<c:url value="/resources/images/Plus_Icon.png"/>" />
		</button>
		<div class="headercontainer">
			<div class="headercol">

				<ul>
					<li class="col-header col-type"><spring:message
							code="PUBLISHED_TYPE" /></li>
					<li class="col-header col-title"><spring:message
							code="OBJECTS_TABLE_HEADER_TITLE" /></li>
					<li class="col-header col-update"><spring:message
							code="UPDATE_DATE" /></li>
					<li class="col-header col-addby"><spring:message
							code="CREATED_ON" /></li>
				</ul>

			</div>
		</div>
	</div>
	<div class="maincontentwrapper">
		<div class="main-content">

			<div class="datagrid">


				<table class="rowcontainer">
					<c:forEach var="fedoraObject" items="${objects}"
						varStatus="loopStatus">
						<tr>
							<td>
								<div class="backrow clickable">
									<a href="object.${fedoraObject.URI}">
										<ul>
											<li class="col-data col-type"><c:choose>
													<c:when test="${not fedoraObject.published}">

													</c:when>
													<c:otherwise>
														<img
															src="<c:url value="/resources/images/LittleGreenDot.png" /> "
															width="10px" height="auto" />
													</c:otherwise>
												</c:choose></li>
											<li class="col-data col-title">${fedoraObject.title}</li>
											<li class="col-data col-update"><fmt:formatDate
													pattern="MMM, dd, yyyy"
													value="${fedoraObject.lastModified}" /></li>
											<li class="col-data col-addby"><fmt:formatDate
													pattern="MMM, dd, yyyy" value="${fedoraObject.createdOn}" /></li>
										</ul>
									</a>
								</div>
							</td>
						</tr>
					</c:forEach>
				</table>

			</div>
		</div>
	</div>
</body>
</html>