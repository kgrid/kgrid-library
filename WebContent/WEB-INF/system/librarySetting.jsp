<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
<script type="text/javascript">
	
	function getData(){
		$.ajax({
			type : 'GET',
			url : "getLibSettings",
			
			success : function(response) {
			    if(response!='empty')
			    {
				  var test = JSON.stringify(response);
			      var obj = JSON.parse(test);
			      
			      document.getElementById("URL_Fuseki").value = obj.fusekiConfig.complete_url;
			      document.getElementById("URL_Fedora").value = obj.fedoraConfig.complete_url;
			      document.getElementById("Name_SMTP").value = obj.SMTPConfig.svr_name;
			      
			      document.getElementById("IP_Fedora").value = obj.fedoraConfig.ip_address;
			      document.getElementById("IP_Fuseki").value = obj.fusekiConfig.ip_address;
			      document.getElementById("IP_SMTP").value = obj.SMTPConfig.ip_address;
			      
			      document.getElementById("Login_Fedora").value = obj.fedoraConfig.svr_username;
			      document.getElementById("Login_Fuseki").value = obj.fusekiConfig.svr_username;
			      document.getElementById("Login_SMTP").value = obj.SMTPConfig.svr_username;

			      document.getElementById("Password_Fedora").value = obj.fedoraConfig.svr_passwd;
			      document.getElementById("Password_Fuseki").value = obj.fusekiConfig.svr_passwd;
			      document.getElementById("Password_SMTP").value = obj.SMTPConfig.svr_passwd;
			    }
			    
			} ,
		
			error : function (response) {
				alert(response.responseText);		
			}
		});
	}
	
	function saveData() {
		var JSONObj = new Object();
		
		var fusekiObj = new Object();
		
		fusekiObj.complete_url = document.getElementById("URL_Fuseki").value ;
		fusekiObj.ip_address = document.getElementById("IP_Fuseki").value ;
		fusekiObj.svr_username = document.getElementById("Login_Fuseki").value ;
		fusekiObj.svr_passwd = document.getElementById("Password_Fuseki").value ;
		
		var fedoraObj = new Object();
		
		fedoraObj.complete_url = document.getElementById("URL_Fedora").value ;
		fedoraObj.ip_address = document.getElementById("IP_Fedora").value ;
		fedoraObj.svr_username = document.getElementById("Login_Fedora").value ;
		fedoraObj.svr_passwd = document.getElementById("Password_Fedora").value ;

		var SMTPObj = new Object();
		
		SMTPObj.svr_name = document.getElementById("Name_SMTP").value ;
		SMTPObj.ip_address = document.getElementById("IP_SMTP").value ;
		SMTPObj.svr_username = document.getElementById("Login_SMTP").value ;
		SMTPObj.svr_passwd = document.getElementById("Password_SMTP").value ;	
		
		JSONObj.fusekiConfig = fusekiObj;
		
		JSONObj.fedoraConfig = fedoraObj;
			
		JSONObj.SMTPConfig = SMTPObj;
		 
		var text = JSON.stringify(JSONObj);
		 
		
		$.ajax({
			beforeSend: function(xhrObj){
		        xhrObj.setRequestHeader("Content-Type","application/json");
		        xhrObj.setRequestHeader("Accept","application/json");
		    },
			type : 'POST',
			url : "saveLibSettings",
			data: text,
			dataType: "json",
			
			success : function(response) {
				alert("Success !! Data saved successfully.");
			    if(response!='empty')
			    {
				  var test = JSON.stringify(response);
			      var obj = JSON.parse(test);
			      
			      document.getElementById("URL_Fuseki").value = obj.fusekiConfig.complete_url;
			      document.getElementById("URL_Fedora").value = obj.fedoraConfig.complete_url;
			      document.getElementById("Name_SMTP").value = obj.SMTPConfig.svr_name;
			      
			      document.getElementById("IP_Fedora").value = obj.fedoraConfig.ip_address;
			      document.getElementById("IP_Fuseki").value = obj.fusekiConfig.ip_address;
			      document.getElementById("IP_SMTP").value = obj.SMTPConfig.ip_address;
			      
			      document.getElementById("Login_Fedora").value = obj.fedoraConfig.svr_username;
			      document.getElementById("Login_Fuseki").value = obj.fusekiConfig.svr_username;
			      document.getElementById("Login_SMTP").value = obj.SMTPConfig.svr_username;

			      document.getElementById("Password_Fedora").value = obj.fedoraConfig.svr_passwd;
			      document.getElementById("Password_Fuseki").value = obj.fusekiConfig.svr_passwd;
			      document.getElementById("Password_SMTP").value = obj.SMTPConfig.svr_passwd;
			    }
			    
			}
			});
		 
	}
	
	function testFusekiURL() {
		var str = document.getElementById("URL_Fuseki").value;
		var n = str.lastIndexOf("/");
		var fusekiURL = str.substring(0,n+1);

		var xmlHttp = new XMLHttpRequest();
		xmlHttp.open("GET", fusekiURL, false); 
		xmlHttp.send();
		if (xmlHttp.status  == 200 ) {
			 document.getElementById("fusekiresult").innerHTML = "Successful" ;
		} else {
			document.getElementById("fusekiresult").innerHTML = "Failed! Please check fuseki URL. " ;
		}
	}
	
	function make_base_auth(user, password) {
		  var tok = user + ':' + password;
		  var hash = btoa(tok);
		  return "Basic " + hash;
		}
	
	function testFedoraURL() {
		
		
		/*   $.ajax({
	         type: "GET",
	         url: "http://localhost:8080/fcrepo/rest/",
	      xhrFields: {
	             withCredentials: true
	         },
	          beforeSend: function (xhr){ 
	             xhr.setRequestHeader('Authorization', 'Basic ' + btoa('fedoraAdmin:secret3')); 
				 xhr.setRequestHeader("Accept","text/html");
	         },  
	        
	         beforeSend: function (xhr){ 
				 xhr.setRequestHeader("Accept","text/html");
	         },
	         
	         success: function(response) {
					alert("Success !! Fedora is working ");
	         }, 
	         error: function (request, textStatus, errorThrown) {
	        	 alert("hi "+textStatus);
	        	 alert("hi "+errorThrown);
	             alert("Hello "+request);
	             var test = JSON.stringify(request);
	             alert(test);
	        },
	         dataType: 'text',
	         data: ''
	     });  */ 
	     
	     
	     var xmlHttp = new XMLHttpRequest();
		xmlHttp.open("GET", "http://localhost:8080/fcrepo/rest/", true); 
		xmlHttp.send();
		if (xmlHttp.readyState == 4 ) {
			alert(xmlHttp.status );
			alert(xmlttp.responseText);
		  }
		alert("Hello " + xmlHttp.status  ); 
		
	}
	$(document).ready(function(){
		var vp_height=$(window).height();
		var top_value_offset=(vp_height-640)/2;
		if(top_value_offset<=60){
			top_value=60;
		}else{
			top_value=top_value_offset;
		}
		var css_top=top_value.toString()+"px";
		$(".settingoverlay").css("top",css_top);
	});
</script>

</head>
  
  	

	<body onload="getData()" >
 
	
     <div class="board" id="libsetting">
            <div class="entryform">
             <button class="greenroundbutton" id="closebutton"  onclick="overlayToggle('libsettings',false)">
    <img src="<c:url value="/resources/images/Close_Icon.png"/>" />
    </button>
        <h3 style="text-align:center;"> ${loggedInUser}</h3>
                <form id="libsetting"  >
           <table id="libsettable">
            <tr>
                <td class="cheader"> </td>
               <td class="cheader">  <spring:message code="FEDORA_SERVER_CONFIGURATION"/></td>
                <td class="cheader">  <spring:message code="FUSEKI_SERVER_CONFIGURATION"/> </td>
                <td class="cheader">  <spring:message code="SMTP_SERVER_CONFIGURATION"/> </td>
               </tr>
                <tr>
                 <td class="rheader"><spring:message code="FIELD_IDENTIFIER"/> </td>
                <td> <input type="text" placeholder="URL " class="libset" id="URL_Fedora" name="URL_Fedora" ></td>
                
                <td>  <input type="text" placeholder="URL " class="libset" id="URL_Fuseki" name="URL_Fuseki" ></td>
                <td> <input type="text" placeholder="Name " class="libset" id="Name_SMTP" name="Name_SMTP" ></td>
                </tr>            <tr>
             <td class="rheader"><spring:message code="IP_ADDRESS"/></td>
                <td> <input type="text" placeholder="XX.XX.XX.XX " class="libset" id="IP_Fedora" name="IP_Fedora" > </td>
                <td> <input type="text" class="libset" id="IP_Fuseki" name="IP_Fuseki">  </td>
                <td> <input type="text" placeholder="XX.XX.XX.XX " class="libset" id="IP_SMTP" name="IP_SMTP" > </td>
                    </tr>            <tr>
             <td class="rheader"><spring:message code="FIELD_LOGIN" /></td>
                <td> <input type="text" placeholder="Login ID " class="libset" id="Login_Fedora" name="Login_Fedora" >  </td>
                <td> <input type="text" id="Login_Fuseki" class="libset" name="Login_Fuseki">   </td>
                <td>  <input type="text" placeholder="Login ID " class="libset" id="Login_SMTP" name="Login_SMTP" >  </td>
               </tr>            <tr>
                <td class="rheader"><spring:message code="PASSWORD"/></td>
                <td>  <input type="password" placeholder="Password " class="libset" id="Password_Fedora" name="Password_Fedora" >  </td>
               <td>  <input type="password"  id="Password_Fuseki" class="libset" name="Password_Fuseki">  </td> 
               <td>  <input type="password" placeholder="Password " class="libset" id="Password_SMTP" name="Password_SMTP" >  </td>
                </tr>
               <tr>
                <td class="cheader">   </td>
                <td class="cheader">  <input type="button" id="fedoratest" class="testbutton" onclick="testFedoraURL()" value="Test">  </td>
                <td class="cheader">  <input type="button" id="fusekitest" class="testbutton" onclick="testFusekiURL()" value="Test">   </td>
                <td class="cheader">  <input type="button" id="SMTPtest" class="testbutton" onclick="testFusekiURL()" value="Test">  </td>
               </tr>
               <tr>
                <td class="cheader">  </td>
                <td class="cheader" id="fedoraresult"> Untested </td>
                <td class="cheader" id="fusekiresult">  Untested </td>
                <td class="cheader" id="smtpresult">  Untested </td>
               </tr>
            </table>
             <input type="button" id="saveaction" class="savebutton"  onclick="saveData()" value="Save Changes">   
            </form>
            </div> 
</div>
</body>
</html>