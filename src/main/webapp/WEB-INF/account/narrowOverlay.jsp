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
<title>METADATA EDITING</title>
</head>
<body>
		<div id="login_pane" class="ol_pane">
			<div class="sidebar_close">
				<h3>CLOSE</h3>
				<button class="greenroundbutton" id="close_overlay"
					onclick="overlaySlide('login_overlay',false)">
					<img src="<c:url value="/resources/images/Close_Icon.png" />">
				</button>
			</div>
			<div class="board" id="loginEntry">
				<div class="entryform" id="entry_form">
					<h3>Log In</h3>
					<div>
						<form name="addObj_f" class="Add-content" id="login_f"
							method="post">
							<fieldset class="fieldcontainer" id="first">
								<div class="loginField">
									<h4 title="username">USERNAME</h4>
									<div class="addtext">
										<input class="textbox login" name="username"
											id="username" type="text" spellcheck="false"
											placeholder="Enter your email"
											maxlength="140">
<!-- 											<button class="edit_btn" >Need to create an account?</button>
 -->									</div>
									<label class="errorLabel" for="username"> </label>
								</div>
								<div class="loginField">
									<h4 title="password">PASSWORD</h4>
									<div class="addtext">
										<input class="textbox login" name="password" id="password"
											type="password" spellcheck="false"
											placeholder="Enter your password"
											maxlength="140">
											<!-- <button class="edit_btn">Forgot Password?</button> -->
									</div>
									<label class="errorLabel" for="password"> </label>
								</div>
							
								<input class="done_btn login" name="done" id="login_btn"
									type="button" value="Login">
							</fieldset>
							<div class="alert-box processing">Log in ...</div>
				<div class="alert-box success">Update Successful !!!</div>
<div class="alert-box failure">Unable to login. Please check your username and password! </div>
<div class="alert-box warning">Warning !!!</div>
						</form>
					</div>
				</div>
			</div>
		</div>

</body>
</html>