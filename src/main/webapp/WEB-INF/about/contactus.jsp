<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="<c:url value="/resources/css/style.css" />"
	type="text/css" />
	<link rel="stylesheet"
	href="<c:url value="/resources/css/l_overlay.css" />" type="text/css" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/navigation.css" />" 	type="text/css" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/infogrid.css" />" 	type="text/css" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/datagrid.css" />" type="text/css" />

<link rel="stylesheet"
	href="<c:url value="/resources/css/button.css" />" type="text/css" />
	<link rel="stylesheet"
	href="<c:url value="/resources/css/formstyle.css" />" type="text/css" />
<link rel="shortcut icon"
	href="<c:url value="/resources/images/MiniIconObjectTeller.ico" />" />
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
<script src="<c:url value="/resources/js/info.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/dropdown.js"/>"></script>
	<script src="<c:url value="/resources/js/jquery-ui.js"/>"></script>
<script src="<c:url value="/resources/js/jquery.validate.js"/>"></script>
<script src="<c:url value="/resources/js/multi_step_form.js"/>"></script>
</head>
<body>
	<script>
		$(document).ready(function() {
			var padLeft = 280;
			var navItemID ="#contactus";
			$(window).resize(function() {
				setIconPos()
			});
			setIconPos();
			setActiveNavItem(navItemID);
		});
	</script>
		<div id="topfixed">
			<%@ include file="../common/banner.jsp"%>
	</div>
	<div id="top-stuff">
		<%@ include file="../common/navbar.jsp"%>
	</div>


	<div class="infocontentwrapper">
		<div class="info-content">
			<div class="infogrid">
				<div class="infoblock">
					<div class="block_title">
						<div class="line">
							<hr>
						</div>
						We are here
						<div class="line">
							<hr>
						</div>
					</div>
					<div class="block_info" id="divisionname">
						<span class="firstline">Department of Learning Health Sciences</span><br> Division of
						Learning and Knowledge Systems<br> University of Michigan
						Medical School<br> Ann Arbor, MI 48109<br>
					</div>
					<div class="block_info" id="contactus">
						<span class="firstline">Knowledge Grid Development Team</span><br>
						<!--  300 North Ingalls Building<br>
						Suite 1161-A<br> Ann Arbor, MI 48109<br> 734 936 1644<br> -->
						dlhs.knowledge.grid@umich.edu
					</div>
				</div>
			</div>
		</div>
		<div id="m_logo_footer">
		<img src="<c:url value="/resources/images/blue-transparentbg.png" />" width="60px"
						height="auto" />
		</div>
	</div>
</body>

</html>
