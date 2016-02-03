<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet"
	href="<c:url value="/resources/css/OT2.2.v1.css" />" type="text/css" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/OT2.1.v1.css" />" type="text/css" />
<link rel="shortcut icon" href="<c:url value="/resources/images/OT.logo.4.tiny3.16x16.ico" />" />
<title><spring:message code="LOGIN_TITLE"/> </title>
</head>
<body align="top">
	<%@ include file="../common/header.jsp"%>
	<%@ include file="../common/topMenu.jsp"%>
	<%@ include file="../common/lhsMenu.jsp"%>

	<div class="content-right">
		<div class="datagrid">
			<table>
				<thead>
					<tr>
						<th><spring:message code="OBJECTS_TABLE_HEADER_TITLE"/> </th>
						<th><spring:message code="OBJECTS_TABLE_HEADER_URI"/> </th>
						<th><spring:message code="OBJECTS_TABLE_HEADER_URI"/> </th>
						<th><spring:message code="PUBLISHED"/> </th>
					</tr>
				</thead>

				<tfoot>
					<tr>
						<td colspan="4"><div id="no-paging">${TotalObjects}
								<spring:message code="OBJECTS_TABLE_FOOTER"/> </div>
					</tr>
				</tfoot>

				<c:forEach var="fedoraObject" items="${objects}" varStatus="loopStatus">
					<tr class="${loopStatus.index % 2 == 0 ? '' : 'alt'}">
						<td><a href="object.${fedoraObject.URI}">${fedoraObject.title}</a></td>
						<td>${fedoraObject.URI}</td>
						<td>${fedoraObject.bins}</td>
						<td><c:choose>
								<c:when test="${not fedoraObject.published}">
									<spring:message code="CHOICE_NO"/> 
								</c:when>
								<c:otherwise>
									<spring:message code="CHOICE_YES"/> 
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>

	<%@ include file="../common/footer.jsp"%>
</body>
</html>
