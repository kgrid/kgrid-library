<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="<c:url value="/resources/css/OT2.1.v1.css" />" type="text/css" />
<link rel="stylesheet" href="<c:url value="/resources/css/OT2.2.v1.css" />" type="text/css" />
<link rel="stylesheet" href="<c:url value="/resources/css/box.css" />" type="text/css" />
<link rel="shortcut icon" href="<c:url value="/resources/images/OT.logo.4.tiny3.16x16.ico" />" />
<script type="text/javascript">
function clearTextboxes(){
	document.getElementById("username").value = "";
	document.getElementById("passwd").value = "";
}
</script>
<title><spring:message code="LOGIN_TITLE"/></title>
</head>

<body onload="clearTextboxes()">	
	<div class="container">
		<div class="header">
			<h3>
				<spring:message code="OBJECTTELLER"/><br/> <a href="#"><img src="<c:url value="/resources/images/logo.png" />"
					width="56" height="56" hspace="14" /></a>
			</h3>
		</div>


		<div class="content">
			<p></p>
			<table>
				<tr>
					<td>
						<h1>
							<spring:message code="LIBRARY"/> 
						</h1>
					</td>
					<td>
						<h1>
							<input type="text" name="library" value="${library}" autocomplete="on" size="50" style="font-size:23px" autofocus">
						</h1>
					</td>
				</tr>
			</table>
			<p>&nbsp;</p>
			<p>&nbsp;</p>
			<p>&nbsp;</p>
			<p>&nbsp;</p>
			<p>&nbsp;</p>
		
			<sf:form method="POST" modelAttribute="user" action="home">	
			<table>
				<tr>
					<td>
						<h2> <spring:message code="LOGIN_ID"/></h2>
					</td>
					<td>
						<h2>
							<sf:input type="text" path="username" name="username"  autocomplete="on" size="15" style="font-size:15px" ></sf:input>
						</h2>
					</td>
					<td align="left">
						<sf:errors path="username" cssClass="alert-box error"/>
					</td>
				</tr>
				
				<tr>
					<td>
						<h2> <spring:message code="PASSWORD"/> </h2>
					</td>
					<td>
						<h2>
							<sf:input type="password" path="passwd" name="passwd"  autocomplete="on" size="15" style="font-size:15px"></sf:input>
							
						</h2>
					</td>
					<td align="left">
						<sf:errors path="passwd" cssClass="alert-box error"/>
					</td>
					<td>
						<h2>	
							<input type="image" src="<c:url value="/resources/images/login/LoginButton1.png" />"
								alt="LoginButton1" width="59" height="30" vspace="0"
								align="absbottom" />
						</h2>
					</td>
				</tr>
			</table>
			<table>
				<tr>
					<td>
					<c:if test="${not empty IncorrectLoginErrorMessage}">
						
							<div class="alert-box error"><span>error: </span>${IncorrectLoginErrorMessage} </div>
						
					</c:if>
					</td>
				</tr>
			</table>	
			</sf:form>
			<p>
				<a href="retrievepw.html"> <spring:message code="PASSWORD_RESET"/></a>
			</p>
			<p>&nbsp;</p>
		</div>

		<%@ include file="../common/footer.jsp" %>
	
		
	</div>
</body>
</html>