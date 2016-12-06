
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
<script>
	$(document)
			.ready(
					function() {
						var navItemID ="#faq";

						var title = [ "What is ObjectTeller?",
								"Is ObjectTeller free?",
								"Who built ObjectTeller?",
								"Can I help further develop ObjectTeller?" ];
						var info = [
								"It's an app for managing computable health knowledge.",
								"Yes. Download it and put it to use!",
								"A team of dedicated professioinals  at the University of Michigan.",
								"Yes! Join our technical community by sending us an email to objectteller@umich.edu." ];
						var description = [ "", "", "", "", "" ];
						var blockNumber = title.length;
						$(window).resize(function(){setIconPos()});
						setIconPos();
						loadContent(blockNumber, title, description, info);
						setActiveNavItem(navItemID);
					
					});
</script>
<title>Object Teller - FAQ</title>
</head>
<body>
		<div id="topfixed">
			<%@ include file="../common/banner.jsp"%>
	</div>
	<div id="top-stuff">
		<%@ include file="../common/navbar.jsp"%>
	</div>

	<div class="infocontentwrapper">
		<div class="info-content">
			<div class="infogrid">

			</div>
		</div>
		<div id="m_logo_footer">
		<img src="<c:url value="/resources/images/blue-transparentbg.png" />" width="60px"
						height="auto" />
		</div>
	</div>
</body>

</html>
