<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="/ObjectTeller/resources/js/nav.js"></script>
</head>
<body>
		<div id="top-bar-out">
			<div id="topnav">
				<div id="nav">
					<ul>
						<li class=""><a href=<%= "about" %>><spring:message code="BANNER_ABOUT" /></a></li>
						<li><a href="faq"><spring:message code="BANNER_FAQ" /></a></li>
						<li><a href="contactus"><spring:message code="BANNER_CONTACT_US" /></a></li>
					</ul>
				</div>
			</div>
		</div>

</body>
</html>