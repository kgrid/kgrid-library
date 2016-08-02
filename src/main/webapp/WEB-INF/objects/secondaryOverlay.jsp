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
	<div id="citation" class="layered_overlay">
		<div id="citation_pane" class="ol_pane">
			<div class="sidebar_close">
				<h3>CLOSE</h3>
				<button class="greenroundbutton" id="close_overlay"
					onclick="overlaySlide('citation',false)">
					<img src="<c:url value="/resources/images/Close_Icon.png" />">

				</button>
				<input type="hidden" id="citation_idx">
			</div>
			<div class="board" id="addCitationEntry">
				<div class="entryform" id="entry_form">
					<h3>Manage Citation</h3>
					<div>
						<form name="addObj_f" class="Add-content" id="citation_f"
							method="post">
							<fieldset class="fieldcontainer" id="first">
								<div class="citationField">
									<h4 title="citation_title">TITLE, PMID or DOI</h4>
									<div class="addtext">
										<input class="textbox" name="citation_title"
											id="citation_title" type="text" spellcheck="false"
											placeholder="Enter Article Title, PMID or DOI "
											maxlength="140"><span>140/140</span>
									</div>
									<label class="errorLabel" for="citation_title"> </label>

								</div>
								<div class="citationField">
									<h4 title="citation_link">HYPERLINK</h4>
									<button class="inline edit" type="button" id="preview_btn">PREVIEW</button>

									<div class="addtext">
										<input class="textbox" name="citation_link" id="citation_link"
											type="text" spellcheck="false"
											placeholder="Please provide the URL for the article "
											maxlength="140"><span>140/140</span>
									</div>
									<label class="errorLabel" for="citation_link"> </label>

								</div>
								<div>
									<h4>CITATION DETAIL</h4>
									<div class="addtext">
										<iframe id="citation_detail"></iframe>
									</div>
								</div>
								<input class="done_btn" name="done" id="addCitation"
									type="button" value="ADD">
							</fieldset>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="license" class="layered_overlay">
		<div id="license_pane" class="ol_pane">
			<div class="sidebar_close">
				<h3>CLOSE</h3>
				<button class="greenroundbutton" id="close_overlay"
					onclick="overlaySlide('license',false)">
					<img src="<c:url value="/resources/images/Close_Icon.png" />">

				</button>
			</div>
			<div class="board" id="selectLicense">
				<div class="entryform" id="entry_form">
					<h3>Add License</h3>
					<div>
						<form name="addObj_f" class="Add-content" id="addObj_f"
							method="post">
							<fieldset class="fieldcontainer" id="first">
								<div class="addtext">
									<h4>
										License
										<spring:message code="REQUIRED_TO_SELECT" />
									</h4>
									<select required class="options" id="license_title">
										<option value="MIT">MIT</option>
										<option value="CC">Creative Commons</option>
									</select> <br>
								</div>
								<div>
									<h4>HYPERLINK</h4>
									<button class="inline edit" id="license_preview_btn">PREVIEW</button>

									<div class="addtext">
										<input class="textbox" name="license_link" id="license_link"
											type="text"
											placeholder="Please provide the URL for the license "
											maxlength="140"><span>140/140</span>
									</div>
								</div>

								<div>
									<h4>LICENSE DETAIL</h4>

									<div class="addtext">
										<iframe id="license_detail"></iframe>
									</div>


									<input class="done_btn" name="done" id="addLicense"
										type="button" value="ADD">
							</fieldset>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>