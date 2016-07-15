<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="<c:url value="/resources/css/style.css" />"
	type="text/css" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/navigation.css" />" 	type="text/css" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/infogrid.css" />" 	type="text/css" />

<link rel="shortcut icon"
	href="<c:url value="/resources/images/MiniIconObjectTeller.ico" />" />
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
<script src="/ObjectTeller/resources/js/info.js"></script>
<script type="text/javascript"
	src="/ObjectTeller/resources/js/dropdown.js"></script>
<script>
	$(document)
			.ready(
					function() {
						
						var title = [ "Who We Are", "Open Source" ];
						var description = [
								"",
								"" ];
						var info = [
								"ObjectTeller has been developed at the University of Michigan Medical School for one purpose,<br>to speed up the rate at which new health knowledge is routinely used to help people be well.<br>Here in the Department of Learning Health Sciences we are dedicated to the scientific pursuit of<br>infrastructures - comprised of people, processes, and technologies - for routine, sustainable learning<br>about how to continuously improve human health. ObjectTeller has been created within our<br>Division of Learning and Knowledge Systems to address the challenge of managing massive amounts<br>of rapidly changing computable health knowledge. It is for storing, curating, preserving, and protecting<br>health knowledge, and for making it accessible on demand for all people to use at any time.<br>ObjectTeller enables its users to be effective movers of health knoweldge into practice.",
								"ObjectTeller is an open source software system for managing computable health knowledge. As such,<br>its code can be inspected and extended, and its uses can multiply. ObjectTeller has been designed to<br> promote open sharing of the computable health knowledge in its libraries too. And ObjectTeller maintains<br>detailed records and audit trails that its users are encouraged to open to the world of linked data.<br>At the University of Michigan Medical School, we understand that openness begets trust and that is why<br>with ObjectTeller, we are committed to supporting openness in all things." ];
						var blockNumber = title.length;
						$(window).resize(function(){setIconPos()});
						setIconPos();
						loadContent(blockNumber, title,description,info);
					});
</script>
<title>Object Teller - About</title>
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
			<div class="infogrid" id="about_grid">

			</div>
		</div>

		<div id="m_logo_footer">
		<img src="<c:url value="/resources/images/blue-transparentbg.png" />" width="60px"
						height="auto" />
		</div>
	
	</div>
</body>

</html>
