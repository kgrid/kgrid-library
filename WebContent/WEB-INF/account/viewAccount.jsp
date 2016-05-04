<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="<c:url value="/resources/css/OT2.2.v1.css" />" type="text/css" />
<link rel="stylesheet" href="<c:url value="/resources/css/OT2.1.v1.css" />" type="text/css" />
<link rel="shortcut icon" href="<c:url value="/resources/images/OT.logo.4.tiny3.16x16.ico" />" />
<title><spring:message code="VIEW_ACCOUNT_TITLE"/></title>
</head>
<body>
<%@ include file="../common/header.jsp" %>
<%@ include file="../common/topMenu.jsp" %>
<%@ include file="../common/lhsMenu.jsp" %>
	
<div class="content-right">
  <p>&nbsp;</p>
  
  <h2><spring:message code="LOGIN_ID"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<div class="filledInBox">
  <div class="FIBleftside">${DBUser.username}</div></div></h2>
  
  
	<h2><spring:message code="PASSWORD"/>&nbsp;&nbsp;&nbsp;<div class="filledInBox">
  	<div class="FIBleftside">${DBUser.passwd}</div></div></h2>
     
     <h2><spring:message code="ROLE"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<div class="filledInBox">
  	<div class="FIBleftside">${DBUser.role}</div></div></h2>
     
  </div>
  <%@ include file="../common/footer.jsp" %>
</body>
</html>