<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet"
	href="<c:url value="/resources/css/OT2.2.v1.css" />" type="text/css" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/OT2.1.v1.css" />" type="text/css" />
<link rel="shortcut icon"
	href="<c:url value="/resources/images/OT.logo.4.tiny3.16x16.ico" />" />
<title><spring:message code="EDIT_SYSTEM_CONF_TITLE" /></title>
</head>
<body>
	<%@ include file="../common/header.jsp"%>
	<%@ include file="../common/topMenu.jsp"%>
	<%@ include file="../common/lhsMenu.jsp"%>


	<div class="content-right">

		<p>&nbsp;</p>

		<table>
			<tr>

				<td><sf:form method="POST" action="saveFedoraServerConf"
						modelAttribute="fedoraConf">
						<h2>
							<spring:message code="FEDORA_SERVER_CONFIGURATION" />
						</h2>
						<table>
							<tr>
								<td>
									<h2>
										<spring:message code="IP" />
									</h2>
								</td>

								<td>
									<h2>
										<div class="OBleftside">
											<sf:input class="openBox" type="text" name="IP" path="ip_address" value="${fedoraConf.ip_address}"/>
										</div>
									</h2>
								</td>
							</tr>
							
							<tr>
								<td>
									<h2>
										<spring:message code="SERVER" />
									</h2>
								</td>

								<td>
									<h2>
										<div class="OBleftside">
											<sf:input class="openBox" type="text" name="SERVER" path="server_name" value="${fedoraConf.server_name}"/>
										</div>
									</h2>
								</td>
							</tr>
							

							<tr>
								<td>
									<h2>
										<spring:message code="USERNAME" />
									</h2>
								</td>

								<td>
									<h2>
										<div class="OBleftside">
											<sf:input class="openBox" type="text" name="username" path="username" value="${fedoraConf.username}"/>
										</div>
									</h2>
								</td>

							</tr>

							<tr>
								<td>
									<h2>
										<spring:message code="PASSWORD" />
									</h2>
								</td>

								<td>
									<h2>
										<div class="OBleftside">
											<sf:input class="openBox" type="text" name="password" path="passwd" value="${fedoraConf.passwd}"/>
										</div>
									</h2>
								</td>
								<td>
									<h2>
										<input type="image"
											src="<c:url value="/resources/images/SaveButton1.png"/>"
											alt="saveBtn" width="53" height="30" hspace="20"
											align="middle" />
									</h2>
								</td>
							</tr>
						</table>
					</sf:form></td>
				<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				</td>
				<td>
				<sf:form method="POST" action="saveSMTPServerConf" modelAttribute="SMTPConf">
						<h2>
							<spring:message code="SMTP_SERVER_CONFIGURATION" />
						</h2>
						<table>
							<tr>
								<td>
									<h2>
										<spring:message code="IP" />
									</h2>
								</td>

								<td>
									<h2>
										<div class="OBleftside">
											<sf:input class="openBox" type="text" name="IP" path="ip_address" value="${SMTPConf.ip_address}"/>
										</div>
									</h2>
							</tr>
							<tr>
								<td>
									<h2>
										<spring:message code="SERVER" />
									</h2>
								</td>

								<td>
									<h2>
										<div class="OBleftside">
											<sf:input class="openBox" type="text" name="SERVER" path="server_name" value="${SMTPConf.server_name}"/>
										</div>
									</h2>
								</td>
							</tr>

							<tr>
								<td>
									<h2>
										<spring:message code="USERNAME" />
									</h2>
								</td>

								<td>
									<h2>
										<div class="OBleftside">
											<sf:input class="openBox" type="text" name="username" path="username" value="${SMTPConf.username}"/>
										</div>
									</h2>
								</td>

							</tr>

							<tr>
								<td>
									<h2>
										<spring:message code="PASSWORD" />
									</h2>
								</td>

								<td>
									<h2>
										<div class="OBleftside">
											<sf:input class="openBox" type="text" name="password" path="passwd" value="${SMTPConf.passwd}"/>
										</div>
									</h2>
								</td>
								<td>
									<h2>
										<input type="image"
											src="<c:url value="/resources/images/SaveButton1.png"/>"
											alt="saveBtn" width="53" height="30" hspace="20"
											align="middle" />
									</h2>
								</td>
							</tr>
						</table>
					</sf:form></td>
			</tr>

		</table>
	</div>
	<%@ include file="../common/footer.jsp"%>
</body>
</html>