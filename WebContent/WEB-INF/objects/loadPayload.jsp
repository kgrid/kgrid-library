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
<link rel="stylesheet" href="<c:url value="/resources/css/box.css" />"
	type="text/css" />
<link rel="stylesheet" href="<c:url value="/resources/css/w3.css" />"
	type="text/css" />
<link rel="shortcut icon"
	href="<c:url value="/resources/images/OT.logo.4.tiny3.16x16.ico" />" />
<title><spring:message code="LOAD_PAYLOAD_TITLE"/></title>

</head>
<body>
	<%@ include file="../common/header.jsp"%>
	<%@ include file="../common/topMenu.jsp"%>
	<%@ include file="../common/lhsMenu.jsp"%>
	<div class="content-right">

		<p><spring:message code="LOAD_PAYLOAD_MESSAGE_1"/></p>
		&nbsp;&nbsp;&nbsp;&nbsp;

		
	<form method="POST" enctype="multipart/form-data"
		action="uploadPayload">
		<input type="file" name="file" class="w3-btn w3-dlol-teal"> 
		<input type="submit" value="Upload Paylod" class="w3-btn w3-dlol-teal">
	</form>


		<sf:form class="w3-container" modelAttribute="fedoraObject" action="loadAPI" >
			<p>
				<br />  <label><spring:message code="LOAD_PAYLOAD_TEXTAREA_TITLE"/></label>
				<sf:textarea class="w3-input-long w3-border" path="payload"></sf:textarea>
			</p>

			<p>
				<br /> <br />
				<table>
				<tr>
						<td>
							<div style="text-align: left;">
								
									<button type="submit"
										class="w3-btn w3-small w3-green w3-round-xlarge">
										<spring:message code="LHS_MENU_OBJECT_NEXT_STEP" />
									</button>
							
							</div>
						</td>

						<td>
							<p>
								<button id="endCreateObject"
									class="w3-btn w3-dlol-green w3-right w3-fluid">
									<spring:message code="END" />
								</button>
							</p>
						</td>
					</tr>
				</table>
			
		</sf:form>

	</div>
	<%@ include file="../common/footer.jsp"%>
</body>
</html>