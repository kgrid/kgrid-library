<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet"
	href="<c:url value="/resources/css/navigation.css" />" 	type="text/css" />
</head>
<body>
	<div id="top-bar-out">
		<div id="topnav">
			<div id="nav">
				<ul>
					<li><a href="#"><spring:message code="BANNER_ABOUT" /></a></li>
					<li><a href="#"><spring:message code="BANNER_FAQ" /></a></li>
					<li><a href="#"><spring:message code="BANNER_CONTACT_US" /></a></li>
				</ul>
			</div>
		</div>
	</div>
</body>
</html>