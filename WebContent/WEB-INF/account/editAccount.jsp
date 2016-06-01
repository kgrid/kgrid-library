<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet"
	href="<c:url value="/resources/css/OT2.2.v1.css" />" type="text/css" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/OT2.1.v1.css" />" type="text/css" />
<link rel="stylesheet" href="<c:url value="/resources/css/box.css" />" type="text/css" />
<link rel="shortcut icon" href="<c:url value="/resources/images/OT.logo.4.tiny3.16x16.ico" />" />
<title><spring:message code="EDIT_ACCOUNT_TITLE"/> </title>
</head>
<body>
	<%-- <%@ include file="../common/header.jsp"%>
	<%@ include file="../common/topMenu.jsp"%>
	<%@ include file="../common/lhsMenu.jsp"%> --%>


	<div class="content-right">
		<sf:form method="POST" action="saveUser" modelAttribute="user">
		<p>&nbsp;</p>
		<table>
			<tr>
				<td>
					<h2><spring:message code="LOGIN_ID"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</h2>
				</td>
				<td>
					<h2>
						<div class="filledInBox">
							<div class="OBleftside"><sf:hidden path="username"/>   ${user.username} </div>
						</div>
					</h2>
				</td>
			</tr>

			<tr>
				<td>

					<h2>
						<spring:message code="ROLE"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</h2>
				</td>
				<td>
					<h2>
						<div class="filledInBox">
							<div class="OBleftside"><sf:hidden path="role"/>${user.role}</div>
						</div>

					</h2>
				</td>
			</tr>

			<tr>
				<td>
					<h2><spring:message code="PASSWORD"/>&nbsp;&nbsp;&nbsp;</h2>
				</td>

				<td>
					<h2>
						<div>
							<div class="OBleftside">
								<sf:input class="openBox" type="text" name="password" path="passwd" value="${user.passwd}" style="font-size:15px"/>
							</div>
						</div>
					
					</h2>
				</td>
				<td align="left">
					<sf:errors path="passwd" cssClass="alert-box error"/>
				</td>
				<c:if test="${not UserSaved}" >
				<td>
					<h2>
						<input type="image" src="<c:url value="/resources/images/SaveButton1.png"/>"
							alt="saveBtn" width="53" height="30" hspace="20" align="middle" />
					</h2>
				</td>
				</c:if>
			</tr>
		</table>
			<c:if test="${UserSaved}">
				<table>
					<tr>
						<td>
							<c:if test="${not empty SuccessMessage}">
								<div class="alert-box success"><span>Success: </span>${SuccessMessage} </div>
							</c:if>
						</td>
					</tr>
				</table>
			</c:if>
		</sf:form>
	</div>

	<%-- <%@ include file="../common/footer.jsp"%> --%>
</body>
</html>