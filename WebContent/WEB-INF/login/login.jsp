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
<link rel="shortcut icon"
	href="<c:url value="/resources/images/MiniIconObjectTeller.ico" />" />
<link rel="stylesheet" href="<c:url value="/resources/css/style.css" />"
	type="text/css" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/bannercontent.css" />"
	type="text/css" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/datagrid.css" />" type="text/css" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/button.css" />" type="text/css" />

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
<script>
	$(document).ready(function() {
		$('#backtotop').click(function() {
			$("html, body").animate({
				scrollTop : 0
			}, 600);
			return false;
		});
	});
</script>
<title><spring:message code="LOGIN_TITLE" /></title>
</head>

<body onload="clearTextboxes()">
	<button class="greenroundbutton" id="backtotop">
		<img src="<c:url value="/resources/images/Chevron_Icon.png"/>">
	</button>
	<div id="bannerbk">
		<img src="<c:url value="/resources/images/bannerBackground.png"/>">
	</div>

	<div id="topfixed">
			<%@ include file="../common/banner.jsp"%>
	</div>
	<div id="top-stuff">
		<%@ include file="../common/navbar.jsp"%>
	</div>



	<div class="banner-content" id="landing">
		<h1>
			<spring:message code="PRODUCT_NAME" />
			<small><spring:message code="PRODUCT_NAME_DESCRIPTION" /></small>
		</h1>
	</div>
	<div class="header">
		<button class="greenroundbutton" id="infoicon">
			<img src="<c:url value="/resources/images/Information_Icon.png"/>">
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
								<div class="backrow clickable"><a
											href="object.${fedoraObject.URI}">

									<ul>

										<li class="col-data col-type"><c:choose>
												<c:when test="${not fedoraObject.published}">
												</c:when>
												<c:otherwise>
													<img
														src="<c:url value="/resources/images/LittleGreenDot.png"/>"
														width="10px" height="auto">
												</c:otherwise>
											</c:choose></li>
										<li class="col-data col-title">${fedoraObject.title}</li>
										<li class="col-data col-update"><fmt:formatDate
												pattern="MMM, dd, yyyy" value="${fedoraObject.lastModified}" /></li>
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