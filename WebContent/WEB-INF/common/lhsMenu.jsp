<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	<div class="content-left">
		<c:choose>
			<c:when test="${(ActiveTab == 'Objects') && (PageType == 'List')}">
				<div id="h4">
					<spring:message code="TOP_MENU_OBJECTS" />
					(${TotalObjects})<br /> <br /> <br />
				</div>
				<h3>
					<spring:message code="OBJECT_TYPE_QUERY" />
					(${QueryObjects})<br />
					<spring:message code="OBJECT_TYPE_RESULT" />
					(${ResultObjects})<br />
					<spring:message code="OBJECT_TYPE_KNOWLEDGE" />
					(${KnowledgeObjects})<br />
					<spring:message code="OBJECT_TYPE_TAILORING" />
					(${TailoringObjects})<br />
				</h3>
				<br />
				<br />
				<div style="text-align: center;">
					<a href="selectObjectType">
						<button class="w3-btn w3-small w3-green w3-round-xlarge"><spring:message code="LHS_MENU_OBJECT_NEW_OBJECT" /></button>
					</a>
				</div>
			</c:when>
			<c:when test="${(ActiveTab == 'Objects') && (PageType == 'Single')}">
				<div id="navcontainerV">
					<ul id="navlistV">
						<li id="${ActiveSubTab == 'Summary' ? 'activeV' : ''}"><a
							href="#" id="current"><spring:message
									code="LHS_MENU_OBJECT_SINGLE_SUMMARY" /></a></li>
						<li><a href="#"><spring:message
									code="LHS_MENU_OBJECT_SINGLE_METADATA" /></a></li>
						<li><a href="#"><spring:message
									code="LHS_MENU_OBJECT_SINGLE_LICENSE" /></a></li>
						<li><a href="#"><spring:message
									code="LHS_MENU_OBJECT_SINGLE_ANNOTATION" /></a></li>
						<li><a href="#"><spring:message
									code="LHS_MENU_OBJECT_SINGLE_LINKAGES" /></a></li>
						<li><a href="#"><spring:message
									code="LHS_MENU_OBJECT_SINGLE_CODE" /></a></li>
						<li><a href="#"><spring:message
									code="LHS_MENU_OBJECT_SINGLE_API" /></a></li>
						<li><a href="#"><spring:message
									code="LHS_MENU_OBJECT_SINGLE_REFERENCES" /></a></li>
						<li><a href="#"><spring:message
									code="LHS_MENU_OBJECT_SINGLE_HISTORY" /></a></li>
					</ul>
				</div>
			</c:when>
			<c:when test="${(ActiveTab == 'Objects') && (PageType == 'SelectObjectType')}">
				<div class="content-left">
					<p>
					<spring:message code="LHS_MENU_OBJECT_STEP_1_MSG" />
					
				</div>
			</c:when>
			<c:when test="${(ActiveTab == 'Objects') && (PageType == 'LoadPayload')}">
				<div class="content-left">
					<p>
					<spring:message code="LHS_MENU_OBJECT_STEP_2_MSG" />	
				</div>
			</c:when>
			
			<c:when test="${(ActiveTab == 'Objects') && (PageType == 'LoadAPI')}">
				<div class="content-left">
					<p>
					<spring:message code="LHS_MENU_OBJECT_STEP_3_MSG" />
					
				</div>
			</c:when>
			
			<c:when test="${(ActiveTab == 'Objects') && (PageType == 'LinkNewObject')}">
				<div class="content-left">
					<p>
					<spring:message code="LHS_MENU_OBJECT_STEP_4_MSG" />
					
				</div>
			</c:when>

			<c:when test="${ActiveTab == 'Account'}">
							<div id="navcontainerV">
					<ul id="navlistV">
						<li id="${ActiveSubTab == 'View' ? 'activeV' : ''}"><a
										href="viewAccount" id="current"><spring:message
												code="LHS_MENU_VIEW" /></a></li>
						<li id="${ActiveSubTab == 'Edit' ? 'activeV' : ''}"><a
										href="editAccount"><spring:message code="LHS_MENU_EDIT" /></a></li>
					</ul>
				</div>
			</c:when>
			<c:when test="${ActiveTab == 'System'}">
				<div id="navcontainerV">
					<ul id="navlistV">
						<li id="${ActiveSubTab == 'View' ? 'activeV' : ''}"><a
										href="viewSystemConf" id="current"><spring:message
												code="LHS_MENU_VIEW" /></a></li>
						<li id="${ActiveSubTab == 'Edit' ? 'activeV' : ''}"><a
										href="editSystemConf"><spring:message code="LHS_MENU_EDIT" /></a></li>
					</ul>
				</div>
			</c:when>
	</c:choose>
	</div>

</body>
</html>