<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><spring:message code="LIBRARY_LEVEL_USER_MNG" /></title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<link rel="stylesheet"
	href="<c:url value="/resources/css/formstyle.css" />" type="text/css" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/dndstyle.css" />">
<link href="https://fonts.googleapis.com/icon?family=Material+Icons"
	rel="stylesheet">
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script type="text/javascript"
	src="/ObjectTeller/resources/js/jquery-ui.js"></script>
<script type="text/javascript">
	var newUserId = 0;
	var users;
	var selectedCard = -1;

	function createUserCard(userID, user_fname, user_lname, user_title, cardId) {
		var userInitial = user_fname.substring(0, 1) + user_lname.substr(0, 1);
		var cardTag = '<div class="userCard" tabIndex="-1" name="'+userID+'" id="'+cardId+'">';
		var initialDiv = '<div class="userInitial">' + userInitial.toUpperCase() + '</div>';
		var userNameRow = '<div class="userInfoRow">' + user_fname + ' '
				+ user_lname + ' | ' + user_title + '</div>'
		var userIDDiv = '<div class="userid">' + userID + '</div>';
		var deleteButton = '<button class="edit deleteButton">Delete</button>';
		var viewButton = '<button class="viewButton"><i class="material-icons md-18">visibility</i></button>';
		var editButton = '<button class="changeButton"> <i class="material-icons md-18">create</i> </button>';
		var divEndTag = "</div>"

		var userCard = cardTag + initialDiv + userNameRow + userIDDiv
				+ deleteButton + divEndTag;

		return userCard;
	}

	function updateUserCard(userID, user_fname, user_lname, user_title, cardId) {
		var userInitial = user_fname.substring(0, 1) + user_lname.substr(0, 1);
		var cardTag = '<div class="userCard" tabIndex="-1" name="'+userID+'" id="'+cardId+'">';
		var initialDiv = '<div class="userInitial">'
				+ userInitial.toUpperCase() + '</div>';
		var userNameRow = '<div class="userInfoRow">' + user_fname + ' '
				+ user_lname + ' | ' + user_title + '</div>'
		var userIDDiv = '<div class="userid">' + userID + '</div>';
		var deleteButton = '<button class="edit deleteButton">Delete</button>';
		var viewButton = '<button class="viewButton"><i class="material-icons md-18">visibility</i></button>';
		var editButton = '<button class="changeButton"> <i class="material-icons md-18">create</i> </button>';
		var divEndTag = "</div>"

		var newUserCard = cardTag + initialDiv + userNameRow + userIDDiv
				+ deleteButton + divEndTag;

		var thisCard = document.getElementById(cardId);
		$(thisCard).replaceWith(newUserCard);
		return true;
	}

	function createEmptySlot(addUser) {
		var addSlot = '<div class="emptySlot" id="grantAccess" tabINdex="-1"><p> Drag over a <br> user in your library to give them access</p></div>';
		var revertSlot = '<div class="emptySlot" id="revokeAccess" tabINdex="-1"><p> Drag back a <br> user in your library to revoke them access</p></div>';
		var newUserSlot = '<div class="emptySlot" id="addNewUser" tabINdex="-1"><p> <i class="material-icons md-48">add</i></p></div>';
		var emptySlot = "";
		switch (addUser) {
		case 0:
			console.log("addSlot");
			emptySlot = addSlot;
			break;
		case 1:
			console.log("revertSlot");
			emptySlot = revertSlot;
			break;
		case 2:
			console.log("newUserSlot");
			emptySlot = newUserSlot;
			break;
		}
		return emptySlot;
	}

	function removeEmptySlot() {
		var empties = document.getElementById("addNewUser");
		if (empties != null) {
			empties.remove();
		}

	}

	function removeUsercard(cardId) {
		var card = document.getElementById(cardId);
		if (card != null) {
			card.remove();
		}
	}

	
	function addEventListenerToCard(divID) {
		console.log("Selected Card:" + divID);
		$("#" + divID)
				.focus(
						function(e) {
							var id = divID.substring(4);
							selectedCard = getUserIndexForId(id);

							if (selectedCard != -1) {
								console.log(selectedCard);
								document.getElementById("addUserButton").style.display = "block";
								$("#addUserButton").text("UPDATE");
								document.getElementById("cancelButton").style.display = "block";
								$("#role_data").val(users[selectedCard].role);
								$("#fname_data").val(
										users[selectedCard].first_name);
								$("#lname_data").val(
										users[selectedCard].last_name);
								$("#email_data").val(
										users[selectedCard].username);
								$("#pwd_data").prop('disabled', true);
							}
						});

		$("#" + divID + " > .deleteButton").click(function() {
			var id = divID.substring(4);
			var selectedUser = getUserIndexForId(id);
			$.ajax({
				type : 'DELETE',
				url : "deleteUser/" + users[selectedUser].id,

				success : function(response) {
					removeUsercard(divID);
					users.splice(id, 1);

				},

				error : function(response) {
					
					var test = JSON.stringify(response);
					var obj = JSON.parse(test);
					alert(obj.responseText);
				}

			});

		});

	}

	function getUserIndexForId(id) {
		for (var i = 0; i < users.length; ++i) {
			if (id == users[i].id) {
				return i;
			}
		}
		return -1;
	}
	
	function addUserValidation() {
		var success = false ;
		if ( $("#role_data").val() != '' && $("#email_data").val() != '' && $("#pwd_data").val() != '' ) {
			success = true;
		} 
		return success;
	}

	function updateUserValidation() {
		var success = false ;
		if ( $("#role_data").val() != '' && $("#email_data").val() != ''  ) {
			success = true;
		} 
		return success;		
	}
	
	function getUserData() {
		var usercard;

		$
				.ajax({
					type : 'GET',
					url : "getAllUsers",

					success : function(response) {
						if (response != 'empty') {
							var test = JSON.stringify(response);
							var obj = JSON.parse(test);
							users = obj;
							newUserId = obj.length;
							for (index = 0; index < obj.length; ++index) {
								usercard = createUserCard(obj[index].username,
										obj[index].first_name,
										obj[index].last_name, obj[index].role,
										"card" + obj[index].id);
								$(usercard).appendTo('#sort1');
								addEventListenerToCard("card" + obj[index].id);
							}

							var eSlot = createEmptySlot(2);
							$(eSlot).appendTo('#sort1');
							emptySlotClick();
							

						}
					}
				});
	}

	function prepareFormForAddUser(){
		$("#addUserButton").text("ADD USER");
		document.getElementById("addUserButton").style.display = "block";
		document.getElementById("cancelButton").style.display = "block";
		$("#role_data").val('');
		$("#fname_data").val('');					
		$("#lname_data").val('');
		$("#email_data").val('');
		$("#pwd_data").val('');
		$("#pwd_data").prop('disabled',false);
	}
	
	function emptySlotClick(){
		$('.emptySlot').focus(
				function(e) {
					console.log(e.target.parentElement.nodeName);
					prepareFormForAddUser();
				});
	}
	
	$(document)
			.ready(function() {

						prepareFormForAddUser();
				
						getUserData();

						$("#cancelButton").click(function() {
							$("#role_data").text(function(i, origText) {
								return origText;
							});
							$("#fname_data").text(function(i, origText) {
								return origText;
							});
							$("#lname_data").text(function(i, origText) {
								return origText;
							});
							$("#email_data").text(function(i, origText) {
								return origText;
							});
							$("#pwd_data").text(function(i, origText) {
								return origText;
							});

						});

						$("#addUserButton").click(function() {
							addOrUpdateUser();		
						});
						

					});
	
function addOrUpdateUser(){
	var act = $("#addUserButton").text();
	console.log(act);

	/* Code to add user to database*/
	/* Call createUserCard */
	/* delete emptySlot */
	/* appendto #slot1 */
	/* createEmptySlot and append to sort1 */
	
	var userObject = new Object();

	userObject.role = document.getElementById("role_data").value;
	userObject.first_name = document.getElementById("fname_data").value;
	userObject.last_name = document.getElementById("lname_data").value;
	userObject.username = document.getElementById("email_data").value;

	if (act == "ADD USER") {
		if(addUserValidation()) {
		userObject.passwd = document.getElementById("pwd_data").value;
		var text = JSON.stringify(userObject);
		
		$.ajax({
					beforeSend : function(xhrObj) {
						xhrObj.setRequestHeader("Content-Type","application/json");
						xhrObj.setRequestHeader("Accept","application/json");
					},
					type : 'POST',
					url : "addUser",
					data : text,
					dataType : "json",

					success : function(response) {
						if (response != 'empty') {
							var test = JSON.stringify(response);
							var obj = JSON.parse(test);
							removeEmptySlot();
							usercard = createUserCard(
									obj.username,
									obj.first_name,
									obj.last_name,
									obj.role,
									"card"+ obj.id);
							$(usercard).appendTo('#sort1');

							users[newUserId] = obj;

							addEventListenerToCard("card"+ obj.id);
							newUserId = newUserId + 1;

							var eSlot = createEmptySlot(2);
							$(eSlot).appendTo('#sort1');
							emptySlotClick();
							$("#role_data").val("");
							$("#fname_data").val("");
							$("#lname_data").val("");
							$("#email_data").val("");
							$("#pwd_data").val("");

						}
					},

					error : function(response) {
						alert("Error "+response.responseText);
					}
				});
		} else {
			alert("Please provide Role, Email Address and Password");
		}
	}
	if (act == "UPDATE") {
		if(updateUserValidation()) {
		userObject.id = users[selectedCard].id;
		var text = JSON.stringify(userObject);

		$
				.ajax({
					beforeSend : function(
							xhrObj) {
						xhrObj
								.setRequestHeader(
										"Content-Type",
										"application/json");
						xhrObj
								.setRequestHeader(
										"Accept",
										"application/json");
					},
					type : 'POST',
					url : "saveUser",
					data : text,
					dataType : "json",

					success : function(
							response) {
						if (response != 'empty') {
							var test = JSON
									.stringify(response);
							var obj = JSON
									.parse(test);
							users[selectedCard].role = obj.role;
							users[selectedCard].first_name = obj.first_name;
							users[selectedCard].last_name = obj.last_name;
							users[selectedCard].username = obj.username;
							updateUserCard(
									obj.username,
									obj.first_name,
									obj.last_name,
									obj.role,
									"card"+ obj.id);
							addEventListenerToCard("card"+ obj.id);

							$("#role_data").val("");
							$("#fname_data").val("");
							$("#lname_data").val("");
							$("#email_data").val("");
							$("#pwd_data").val("");
						}
					},

					error : function(response) {
						alert("Error "+response.responseText);
					}
				});
		}  else {
			alert("Please provide Role and  Email Address.");
		}
	}
}

$(document).keypress(function(e) {
    if(e.which == 13) {
    	return false;
    }
});
</script>

</head>
</head>
<body>
	<div class="board" id="addObj">
		<div class="entryform">
			<button class="greenroundbutton" id="closebutton"
				onclick="overlayToggle('libraryuser',false)">
				<img src="<c:url value="/resources/images/Close_Icon.png" />">
			</button>
			<div id="content">
				<div class="objTitle">
					<div id="title">
						<h3>Library Title</h3>
					</div>
					<div id="admin">
						<h4>
							<spring:message code="MANAGED_BY" />
							Admin
						</h4>
					</div>
				</div>
				<div class="userMngArea">
					<div class="leftpane" id="userInfo">
						<p class="secTitle">
							<spring:message code="BASIC_INFO" />
						</p>
						<form class="display-content" id="user_edit">

							<button class="saveChange" id="addUserButton" type="button">
								<spring:message code="ADD_USER_BTN" />
							</button>
							<button class="edit" id="cancelButton">
								<spring:message code="CANCEL_BTN" />
							</button>


							<div>
								<h4>
									<spring:message code="ROLE" />
								</h4>
								<select class="userEdit" id="role_data">
									<option value="ADMIN">ADMIN</option>
									<option value="USER">USER</option>
									<option value="INFORMATICIAN">INFORMATICIAN</option>
								</select>
							</div>

							<div>
								<h4>
									<spring:message code="FIRST_NAME" />
								</h4>
								<input type="text" class="userEdit" id="fname_data" />
							</div>

							<div>
								<h4>
									<spring:message code="LAST_NAME" />
								</h4>
								<input type="text" class="userEdit" id="lname_data" />
							</div>

							<div>
								<h4>
									<spring:message code="EMAIL" />
								</h4>
								<input type="text" class="userEdit"
									placeholder="Will be used as login ID" id="email_data" />
							</div>

							<div>
								<h4>
									<spring:message code="PASSWORD" />
								</h4>
								<input type="text" class="userEdit" id="pwd_data" />
							</div>

						</form>
					</div>
					<div class="userlist rightpane" id="cardPile">
						<p class="secTitle">
							<spring:message code="LIBRARY_USER" />
						</p>
						<div class="listContainer">
							<ul id="sort1">
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>