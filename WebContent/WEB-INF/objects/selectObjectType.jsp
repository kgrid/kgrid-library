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
<link rel="stylesheet" href="<c:url value="/resources/css/box.css" />"
	type="text/css" />
<link rel="stylesheet" href="<c:url value="/resources/css/w3.css" />"
	type="text/css" />
<link rel="shortcut icon"
	href="<c:url value="/resources/images/OT.logo.4.tiny3.16x16.ico" />" />
<title><spring:message code="SELECT_OBJECT_TYPE_TITLE" /></title>
<script type="text/javascript">
function giveTypeDescription(type){
	if(type == 'Query'){
		document.getElementById("objType").value = "Query";
		document.getElementById("typeDesc").innerHTML = "Query Description";
	} else{
		if(type == 'Result'){
			document.getElementById("objType").value = "Result";
			document.getElementById("typeDesc").innerHTML = "Result Description";
		} else{
			if(type == 'Calculator'){
				document.getElementById("objType").value = "Calculator";
				document.getElementById("typeDesc").innerHTML = "Calculator Description";
			} else {
				if(type == 'Guideline'){
					document.getElementById("objType").value = "Guideline";
					document.getElementById("typeDesc").innerHTML = "Guideline Description";
				} else {
					if(type == 'Tailoring'){
						document.getElementById("objType").value = "Tailoring";
						document.getElementById("typeDesc").innerHTML = "Tailoring Description";
					}
				}
			}
		}
	}
} </script>
</head>
<body>
	<%@ include file="../common/header.jsp"%>
	<%@ include file="../common/topMenu.jsp"%>
	<%@ include file="../common/lhsMenu.jsp"%>
	<div class="content-right">

		<p></p>
		&nbsp;&nbsp;&nbsp;&nbsp;


		<button class="w3-btn w3-dlol-teal" onclick="giveTypeDescription('Query')" >
			<spring:message code="OBJECT_TYPE_QUERY" />
		</button>

		<button class="w3-btn  w3-dlol-teal" onclick="giveTypeDescription('Result')" >
			<spring:message code="OBJECT_TYPE_RESULT" />
		</button>

		<div class="w3-dropdown-hover">
			<button class="w3-btn  w3-dlol-teal" >
				<spring:message code="OBJECT_TYPE_KNOWLEDGE" />
			</button>
			<div class="w3-dropdown-content w3-border">
				<a href="#" onclick="giveTypeDescription('Calculator')"><spring:message code="OBJECT_TYPE_CALCULATOR" /></a> <a
					href="#" onclick="giveTypeDescription('Guideline')"><spring:message code="OBJECT_TYPE_GUIDELINE" /></a> <a
					href="#" onclick="giveTypeDescription('Tailoring')"><spring:message code="OBJECT_TYPE_TAILORING" /></a>
			</div>
		</div>
		<p/>
		<p id="typeDesc"></p>
		<p>
		<p>
			<sf:form method="POST" modelAttribute="fedoraObject" action="loadPayload">
				<table>
					<tr>
						<td>
							<h2>
								<spring:message code="TITLE" />
							</h2>
						</td>
						<td>
							<h2>
								<sf:input type="text" id="title" class="openBox2" name="title"
									autocomplete="on" size="15" style="font-size: 15px" path="title"></sf:input>
							</h2>
						</td>
						<td>
							<h2>
								<sf:input type="hidden" id="objType" name="objectType" path="objectType" ></sf:input>
							</h2>
						</td>
					</tr>


					<tr>
						<td>
							<h2>
								<spring:message code="SUBJECT" />
							</h2>
						</td>
						<td>
							<h2>
								<sf:input type="text" class="openBox2" name="owner"
									autocomplete="on" size="15" style="font-size: 15px" path="owner"></sf:input>
							</h2>
						</td>
					</tr>

					<tr>
						<td>
							<h2>
								<spring:message code="DESCRIPTION" />
							</h2>
						</td>
						<td>
							<h2>
								<sf:textarea class="openBox3" name="description" path="description" autocomplete="on"
									size="15" style="font-size: 15px"></sf:textarea>
							</h2>
						</td>
					</tr>

					<tr>
						<td>
							<h2>
								<spring:message code="CONTRIBUTORS" />
							</h2>
						</td>
						<td>
							<h2>
								<sf:textarea class="openBox3" name="contributors" path="contributors" autocomplete="on"
									size="15" style="font-size: 15px"></sf:textarea>
							</h2>
						</td>
					</tr>

					<tr>
						<td>
							<h2>
								<spring:message code="KEYWORDS" />
							</h2>
						</td>
						<td>
							<h2>
								<sf:textarea class="openBox3" name="keywords"  path="keywords" autocomplete="on"
									size="15" style="font-size: 15px"></sf:textarea>
							</h2>
						</td>
					</tr>

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