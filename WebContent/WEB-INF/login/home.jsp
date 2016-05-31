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

<link rel="stylesheet" href="<c:url value="/resources/css/w3.css" />"
	type="text/css" />
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
<title><spring:message code="LOGIN_TITLE" /></title>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
<script type="text/javascript" src="/ObjectTeller/resources/js/dropdown.js"></script>
<script src="/ObjectTeller/resources/js/scroll.js"></script>

<script>
function openAddObjOverlay(){
    document.getElementById("addObject").style.display = "block";
    document.getElementById("addObject").style.width = "100%";
    resetInputText();
}

function closeAddObjOverlay(){
    document.getElementById("addObject").style.display = "none";
    document.getElementById("addObject").style.width = "0%";

}

function resetInputText(){
 	 $('#payloadTextArea').val("");
	 $('#inputTextArea').val("");
	 $('#outputTextArea').val("");
	 $('#description_data').val("");	
	 $('#title_data').val("");
	 $('#keyword_data').val("");
	 $('#owner_data').val("");
	 $('#contri_data').val("");
}

function openNav() {
    document.getElementById("libsettings").style.display = "block";
    document.getElementById("libsettings").style.width = "100%";

}

function closeNav() {
    document.getElementById("libsettings").style.display = "none";
    document.getElementById("libsettings").style.width = "0%";
}
</script>    
<script>
   $(document).ready(function () {
  
       $('#backtotop').click(function () {
           $("html, body").animate({
               scrollTop: 0
           }, 600);
           return false;
       });
   });
</script> 
</head>
<body>


	
	<div id="libsettings"  class="overlay">
     	<%@ include file="../system/librarySetting.jsp" %>
	 </div>

	<div id="addObject" class="overlay">
		<%@ include file="../objects/createNewObject.jsp"%>
	</div>        
	   <button class="greenroundbutton" id="backtotop">
    <img src="<c:url value="/resources/images/Chevron_Icon.png"/>">
    </button>
    <div id="logo"><img src="<c:url value="/resources/images/logo.png"/>" width="200px" height="auto"></div>
	<div id="top-stuff">
		<%@ include file="../common/navbar.jsp" %>
	</div>

				<div class="active-links">
					<div id="logoutsession" class="logout-link">
					<ul>
						<li id="user">
						<a id="user-link" href="#"> <strong><spring:message code="HELLO_MESSAGE" />
								${DBUser.first_name} </strong>
						</a></li>
						<li id="icon" class="down"><img id="iconimg" class="down" src="<c:url value="/resources/images/Chevron_Icon.png"/>" />
						</li>
						</ul>
					</div>


					<div id="logout-dropdown" class="dropdown">
						<sf:form method="POST" class="signin" action="" >
							<fieldset class="links">
								<button id="settings" onclick="openNav()"><spring:message code="SETTINGS" /></button>
							</fieldset>
						</sf:form>
						<sf:form method="POST" class="signin" action="logout" >
							<fieldset class="links">
								<button id="logout" class="submit button" type="submit"><spring:message code="LOGOUT"/></button>
							</fieldset>
						</sf:form>

					</div>
				</div>


	
	<div class="banner-content">
 		<div id="bannercontent">
		<h1>
			<small>${LibraryName} <spring:message code="TOP_MENU_LIBRARY" /></small>
		</h1>
		<table>
			<tr>
				<td><spring:message code="SERVER_URL" />
					<div id="h5">${ServerURL}</div></td>
				<td><spring:message code="IP_ADDRESS" />
					<div id="h5">${FedoraIpAddress}</div></td>
			</tr>
			<tr>
				<td><spring:message code="PANEL_FIELD_NO_OF_OBJECTS" />
					<div id="h5">${TotalObjects}</div></td>
				<td><spring:message code="PANEL_FIELD_NO_OF_PUBLISHED_OBJECTS" />
					<div id="h5">${PublishedObjects}</div></td>
			</tr>
		</table>
	</div>
<div id="bannericons">
                <ul id="bannericonrow">
                    <button class="roundbutton" id="userlink" >
    <img src="<c:url value="/resources/images/Person_Icon.png"/> "/>
                    </button>                
                    
                    <button class="roundbutton" id="settinglink" onclick="openNav()">
    <img src="<c:url value="/resources/images/Gear_Icon.png"/> "/>
                    </button>
                </ul></div>
		</div>
	<div class="header">
	 <button class="greenroundbutton" id="addObjbutton" onclick="openAddObjOverlay()">
    <img src="<c:url value="/resources/images/Plus_Icon.png"/>" />
    </button>
		<div class="headercontainer">
			<div class="headercol">
			
				<ul>
					<li class="col-type"><spring:message code="PUBLISHED_TYPE" />
					</li>
					<li class="col-title"><spring:message
							code="OBJECT_TITLE" /></li>
					<li class="col-update"><spring:message code="UPDATE_DATE" /></li>
					<li class="col-addby"><spring:message code="CREATED_ON" /></li>
				</ul>

			</div>
		</div>
	</div>
	<div class="main-content">

		<div class="datagrid">


			<table class="rowcontainer">
				<c:forEach var="fedoraObject" items="${objects}"
					varStatus="loopStatus">
					<tr>
						<td>
							<div class="backrow">
								<ul>
									<li class="data-type"><c:choose>
											<c:when test="${not fedoraObject.published}">
												
											</c:when>
											<c:otherwise>
													<img src="<c:url value="/resources/images/LittleGreenDot.png" /> " />
											</c:otherwise>
										</c:choose></li>
									<li class="data-title"><a
										href="object.${fedoraObject.URI}">${fedoraObject.title}</a></li>
									<li class="data-update"><fmt:formatDate
											pattern="MMM, dd, yyyy" value="${fedoraObject.lastModified}" /></li>
									<li class="data-addby"><fmt:formatDate
											pattern="MMM, dd, yyyy" value="${fedoraObject.createdOn}" /></li>
								</ul>
							</div>
						</td>
					</tr>
				</c:forEach>
			</table>

		</div>
	</div>
</body>
</html>