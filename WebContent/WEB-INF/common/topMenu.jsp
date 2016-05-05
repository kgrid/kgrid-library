<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
<div class="container">
  <div class="control-bar">
    <div id="navcontainer">
     <ul id="navlist">
      <li id="${ActiveTab == 'Account' ? 'active' : ''}"><a href="viewAccount"><spring:message code="TOP_MENU_ACCOUNT"/></a></li>
      <li id="${ActiveTab == 'Objects' ? 'active' : ''}"><a href="objects" ><spring:message code="TOP_MENU_OBJECTS"/></a></li>
      <li><a href="#"><spring:message code="TOP_MENU_OBJECTMAP"/> </a></li>
      <c:if test="${DBUser.role == 'ADMIN'}">
 	     <li><a href="#"><spring:message code="TOP_MENU_LIBRARY"/></a></li>
 	     <li id="${ActiveTab == 'System' ? 'active' : ''}"><a href="viewSystemConf"><spring:message code="TOP_MENU_SYSTEM"/></a></li>
      </c:if>
     </ul>
    </div>
   </div>
   </div>
    
</body>
</html>