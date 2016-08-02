<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link rel="stylesheet" href="<c:url value="/resources/css/style.css" />"
	type="text/css" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/bannercontent.css" />"
	type="text/css" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/datagrid.css" />" type="text/css" />

<link rel="stylesheet"
	href="<c:url value="/resources/css/button.css" />" type="text/css" />
<%-- <link rel="stylesheet"
	href="<c:url value="/resources/css/overlay.css" />" type="text/css" /> --%>
<link rel="stylesheet"
	href="<c:url value="/resources/css/l_overlay.css" />" type="text/css" />
<link rel="shortcut icon"
	href="<c:url value="/resources/images/MiniIconObjectTeller.ico" />" />
<link rel="stylesheet"
	href="<c:url value="/resources/css/navigation.css" />" 	type="text/css" />
	<link rel="stylesheet"
	href="<c:url value="/resources/css/librarysetting.css" />"	type="text/css" />
	<link rel="stylesheet"
	href="<c:url value="/resources/css/formstyle.css" />" type="text/css" />
<title><spring:message code="LOGIN_TITLE" /></title>
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/dropdown.js"/>"></script>
<script src="<c:url value="/resources/js/scroll.js"/>"></script>
<script src="<c:url value="/resources/js/iconbutton.js"/>"></script>

<script>
	
/* 	function overlayToggle(overlayID, open){
		var olElement = document.getElementById(overlayID);
		if(open){
			olElement.style.display="block";
			olElement.style.width="100%";
		}else{
			olElement.style.display="none";
			olElement.style.width="0%";
		}
	    document.body.classList.toggle('noscroll', open);
		if(overlayID=="addObject"){
			resetInputText();
		}
		if(overlayID=="libraryuser"){
			resetUserInfoText();
		}
		
	} */

	function overlayHeightResize(overlayID, window_height){
		var overlayPane =$('#'+overlayID).find("> .ol_pane");
		var entryform = overlayPane.find(".entryform");
		var ol_pane_height =window_height;
		var calcHeight = (window_height-120);
		entryform.css("height",calcHeight+"px");
		var ef_margin = (ol_pane_height-calcHeight)/2;
		var addContent = entryform.find(".Add-content");
		addContent.css("height",(calcHeight-70)+"px");
		return ol_pane_height;
	}

	function overlaySlide(overlayID, open){
	    document.body.classList.toggle('noscroll', open);
		var overlayPane =$('#'+overlayID).find("> .ol_pane");
		var window_width= $(window).width();
		var window_height = $(window).height();
		var overlayPane_width=overlayPane.width();
		var overlayPane_height=	overlayHeightResize(overlayID, window_height);

		console.log("Window Width="+ window_width+" Window Height="+window_height+" olPane Width="+overlayPane_width+" olPane Height="+overlayPane_height);

		var overlayPane_left = window_width-overlayPane_width;
		if(overlayPane_left<=(window_width*0.2)){
			overlayPane_left=(window_width*0.2);
		}

		if(open){
			$('#'+overlayID).css("display","block");
	        $('#'+overlayID).fadeIn('fast',function(){
	            overlayPane.animate({'left':overlayPane_left+"px"},1000);
	        });
	    }else{
			$('#'+overlayID).css("display","none");
	    	overlayPane.animate({'left':'100%'},1000,function(){
	            $('#'+overlayID).fadeOut('fast');
	        });
	    }

		if(overlayID=="addObject"){
			resetInputText();
		}
		if(overlayID=="libraryuser"){
			resetUserInfoText();
		}
		if(overlayID=="citation"){
			resetCitationText();
		}
		
	}
	
	function resetCitationText(){
		$( "#citation_title" ).val( "" );
		$( "#citation_link" ).val( "" );
		$("#citation_detail").attr("src","");
	}
	
	function resetUserInfoText(){
		$( "input[id$='_data']" ).val( "" );
		/* $("#role_data").val(''); */
		$("#pwd_data").prop('disabled',false);
		$("#addUserButton").text("ADD USER");
		document.getElementById("addUserButton").style.display = "block";
		document.getElementById("cancelButton").style.display = "block";
	}
	
	
	function resetInputText() {
		$(".current-tab").removeClass("current-tab");
		$("#progressbar li:first-child").addClass("current-tab");
		$('#progressbar li').children("img").css("display","none");
		$('#addObj .fieldcontainer').css("display","none");
		$('#first').css("display",	"block");
		$("#entry_form").show();
		$("#end_page").hide();		
		$('#payloadTextArea').val("");
		$('#functionName').val("");
		$('#inputTextArea').val("");
		$('#outputTextArea').val("");
		$( "input[id$='_data']" ).val( "" );
		$("#description_data").val("");
		$("div[id$='_entry']").children().remove();
		
	}


</script>
</head>
<body>

	<c:if test="${loggedInUser.role == 'ADMIN' }">
		<div id="libsettings" class="layered_overlay" aria-hidden="true">
			<%@ include file="../system/librarySetting.jsp"%>
		</div>
	</c:if>

	<div id="addObject" class="layered_overlay" aria-hidden="true">
		<%@ include file="../objects/createNewObject.jsp"%>
	</div>
	
	<div id="libraryuser"  class="layered_overlay" aria-hidden="true">
     	<%@ include file="../account/libraryUser.jsp" %>
	 </div> 

	<button class="greenroundbutton" id="backtotop">
		<img src="<c:url value="/resources/images/Chevron_Icon.png"/>">
	</button>

	<div id="topfixed">
			<%@ include file="../common/banner.jsp"%>
	</div>
	<div id="top-stuff">
		<%@ include file="../common/navbar.jsp"%>
	</div>


	<c:choose>
		<c:when test="${ empty loggedInUser }">
			<div id="bannerbk">
 				<img src="<c:url value="/resources/images/bannerBackground.png"/>"> 
			</div>

			<div class="banner-content" id="landing">
				<h1>
					<spring:message code="PRODUCT_NAME" />
					<small><spring:message code="PRODUCT_NAME_DESCRIPTION" /></small>
				</h1>
			</div>
		</c:when>
		<c:otherwise>
			<div id="homebanner" class="banner-content">
				<div id="bannercontent">
					<h1>
						<small>${LibraryName} <spring:message
								code="TOP_MENU_LIBRARY" /></small>
					</h1>
					<table>
						<tr>
							<td><spring:message code="SERVER_URL" />
								<div id="h5">${ServerURL}</div></td>
						</tr>
					</table>
					<table>
						<tr>
							<td class="align-type"><spring:message
									code="PANEL_FIELD_NO_OF_OBJECTS" />
								<div id="h5">${TotalObjects}</div></td>
							<td class="align-title"><spring:message
									code="PANEL_FIELD_NO_OF_PUBLISHED_OBJECTS" />
								<div id="h5">${PublishedObjects}</div></td>
							<td class="aign-update"><spring:message code="IP_ADDRESS" />
								<div id="h5">${FedoraIpAddress}</div></td>
						</tr>
					</table>
				</div>
				<div id="bannericons">
					<ul id="bannericonrow">
						<c:if test="${loggedInUser.informatician  }">
						<li><div style="position: relative">
								<button class="roundbutton iconBtn" id="userlink"
									onclick="overlaySlide('libraryuser',true)">
									<img src="<c:url value="/resources/images/Person_Icon.png"/> " />
								</button>
								<button class="greenroundbutton iconBtn" id="newuser">
									<img src="<c:url value="/resources/images/Plus_Icon.png" />"
										width="10px">
								</button>
							</div></li>
						</c:if>
						<c:if test="${loggedInUser.admin}">
						<li>
							<button class="roundbutton open-overlay iconBtn" type="button"
								id="settinglink" onclick="overlaySlide('libsettings',true)">
								<img src="<c:url value="/resources/images/Gear_Icon.png"/> " />
							</button>
						</li>
						</c:if>
					</ul>
					<div class="floatingInfo" id="homeIcons">
						<span></span>
					</div>
				</div>

			</div>
		</c:otherwise>
	</c:choose>

	<div class="header">
		<c:if test="${not empty loggedInUser }">
			<button class="greenroundbutton open-overlay" type="button"
				id="addObjbutton" onclick="overlaySlide('addObject',true)">
				<img src="<c:url value="/resources/images/Plus_Icon.png"/>" />
			</button>
		</c:if>
		<div class="headercontainer">
			<div class="headercol">

				<ul>
					<li class="col-header col-type"><spring:message
							code="PUBLISHED_TYPE" /></li>
					<li class="col-header col-title"><spring:message
							code="OBJECTS_TABLE_HEADER_TITLE" /></li>
					<li class="col-header col-update"><spring:message
							code="UPDATE_DATE" /></li>
					<li class="col-header col-addby"><spring:message
							code="CREATED_ON" /></li>
				</ul>

			</div>
		</div>
	</div>
	<div class="maincontentwrapper">
		<div class="main-content">

			<div class="datagrid">


				<table class="rowcontainer">
					<c:forEach var="fedoraObject" items="${objects}"
						varStatus="loopStatus">
						<tr>
							<td>
								<div class="backrow clickable">
									<a href="object.${fedoraObject.URI}">
										<ul>
											<li class="col-data col-type"><c:choose>
													<c:when test="${not fedoraObject.metadata.published}">

													</c:when>
													<c:otherwise>
														<img
															src="<c:url value="/resources/images/LittleGreenDot.png" /> "
															width="10px" height="auto" />
													</c:otherwise>
												</c:choose></li>
											<li class="col-data col-title">${fedoraObject.metadata.title}</li>
											<li class="col-data col-update"><fmt:formatDate
													pattern="MMM, dd, yyyy"
													value="${fedoraObject.metadata.lastModified}" /></li>
											<li class="col-data col-addby"><fmt:formatDate
													pattern="MMM, dd, yyyy" value="${fedoraObject.metadata.createdOn}" /></li>
										</ul>
									</a>
								</div>
							</td>
						</tr>
					</c:forEach>
				</table>

			</div>
		</div>
	</div>
</body>
</html>