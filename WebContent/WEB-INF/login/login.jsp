<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="<c:url value="/resources/css/w3.css" />"
	type="text/css" />
	<link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'/>
<link rel="stylesheet" href="<c:url value="/resources/css/box.css" />"
	type="text/css" />
<link rel="stylesheet" href="<c:url value="/resources/css/style.css" />"
	type="text/css" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/bannercontent.css" />"
	type="text/css" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/datagrid.css" />" type="text/css" />
<link rel="shortcut icon"
	href="<c:url value="/resources/images/OT.logo.4.tiny3.16x16.ico" />" />
<script type="text/javascript">
	function clearTextboxes() {
		document.getElementById("username").value = "";
		document.getElementById("passwd").value = "";
	}
</script>
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
<script type="text/javascript"
	src="/ObjectTeller/resources/js/dropdown.js"></script>
<script src="/ObjectTeller/resources/js/scroll.js"></script>

<title><spring:message code="LOGIN_TITLE" /></title>
</head>

<body onload="clearTextboxes()">

	<div id="top-stuff">
		<div id="top-bar-out">
			<div id="topnav">
				<div id="logo">KOGLIA</div>

				<div id="nav">
					<ul>
						<li><a href="#"><spring:message code="BANNER_ABOUT" /></a></li>
						<li><a href="#"><spring:message code="BANNER_FAQ" /></a></li>
						<li><a href="#"><spring:message code="BANNER_CONTACT_US" /></a></li>
					</ul>
				</div>

				<div class="active-links">
					<div id="session">
						<a id="signin-link" href="#"> <strong><spring:message
									code="LOG_IN_BUTTON" /></strong>
						</a>
					</div>


					<div id="signin-dropdown">
						<sf:form method="POST" modelAttribute="user" class="signin"
							action="home">
							<fieldset class="textbox">
								<h2>
									<spring:message code="LOGIN_ID" />
								</h2>
								<sf:input name="username" path="username" type="text"
									autocomplete="on"></sf:input>
								<h2>
									<spring:message code="PASSWORD" />
								</h2>
								<sf:input type="password" path="passwd" name="passwd"
									autocomplete="off" size="15" style="font-size:15px"></sf:input>
							</fieldset>

							<fieldset class="remb">
								<label class="remember"> <input type="checkbox"
									value="1" name="remember_me" /> <span><spring:message
											code="REMEMBER_ME_BUTTON" /></span>
								</label> <button id="login" class="submit button" type="submit"><spring:message code="LOG_IN_BUTTON" /></button>
							</fieldset>
							<p>
								<a href="retrievepw.html"> <spring:message
										code="PASSWORD_RESET" /></a>
							</p>
						</sf:form>

					</div>

				</div>
			</div>
		</div>

	</div>
	<div class="banner-content">
		<h1>
			<spring:message code="PRODUCT_NAME" />
			<small><spring:message code="PRODUCT_NAME_DESCRIPTION" /></small>
		</h1>
	</div>
	<div class="header">
		<div class="headercontainer">
			<div class="headercol">
				<ul>
					<li class="col-type"><spring:message code="PUBLISHED_TYPE" />
					</li>
					<li class="col-title"><spring:message
							code="OBJECTS_TABLE_HEADER_TITLE" /></li>
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
												<spring:message code="CHOICE_NO" />
											</c:when>
											<c:otherwise>
												<spring:message code="CHOICE_YES" />
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