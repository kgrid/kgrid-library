<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%-- <link rel="stylesheet" href="<c:url value="/resources/css/librarysetting.css" />"
	type="text/css" /> --%>
<link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'/>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
<script type="text/javascript">
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
</script>
<title>Insert title here</title>
</head>
<body>
   <div class="settingcontainer">
        <div class="settingoverlay">
              <button class="greenroundbutton" id="closebutton">
    <img src="img/Close_Icon.png">
    </button>
            <div class="settingcontent">
        <h1 style="text-align:center;"> Library Settings </h1>
          <sf:form id="libsetting" method="POST" action="saveSettings" modelAttribute="librarySetting">
           <table>
            <tr>
               
                <td class="cheader"> </td>
                <td class="cheader">  <spring:message code="FEDORA_SERVER_CONFIGURATION"/></td>
                <td class="cheader">  <spring:message code="FUSEKI_SERVER_CONFIGURATION"/> </td>
                <td class="cheader">  <spring:message code="SMTP_SERVER_CONFIGURATION"/> </td>
                
               
               </tr>
                        <tr>
               
                <td class="rheader"><spring:message code="FIELD_IDENTIFIER"/> </td>
                <td> <sf:input type="text" placeholder="URL " id="URL_Fedora" name="URL_Fedora" path="fedoraConfig.complete_url" value="${fedoraConf.complete_url}"/></td>
                <td>  <sf:input type="text" placeholder="URL " id="URL_Fuseki" name="URL_Fuseki" path="fusekiConfig.complete_url" value="${fusekiConf.complete_url}"/></td>
                <td> <sf:input type="text" placeholder="Name " id="Name_SMTP" name="Name_SMTP" path="SMTPConfig.svr_name" value="${SMTPConf.svr_name}"/></td>
                
               
               </tr>            <tr>
               
                <td class="rheader"><spring:message code="IP_ADDRESS"/></td>
                <td> <sf:input type="text" placeholder="XX.XX.XX.XX " id="IP_Fedora" name="IP_Fedora" path="fedoraConfig.ip_address" value="${fedoraConf.ip_address}"/> </td>
                <td> <sf:input type="text" id="IP_Fuseki" name="IP_Fuseki" path="fusekiConfig.ip_address"/>  </td>
                <td> <sf:input type="text" placeholder="XX.XX.XX.XX " id="IP_SMTP" name="IP_SMTP" path="SMTPConfig.ip_address" value="${SMTPConf.ip_address}"/> </td>
                
               
               </tr>            <tr>
               
                <td class="rheader"><spring:message code="FIELD_LOGIN" /></td>
                <td> <sf:input type="text" placeholder="Login ID " id="Login_Fedora" name="Login_Fedora" path="fedoraConfig.svr_username" value="${fedoraConf.svr_username}"/>  </td>
                <td> <sf:input type="text" id="Login_Fuseki" name="Login_Fuseki" path="fusekiConfig.svr_username" />   </td>
                <td>  <sf:input type="text" placeholder="Login ID " id="Login_SMTP" name="Login_SMTP" path="SMTPConfig.svr_username"   value="${SMTPConf.svr_username}" />  </td>
                
               
               </tr>            <tr>
               
                <td class="rheader"><spring:message code="PASSWORD"/></td>
                <td>  <sf:input type="password" placeholder="Password " id="Password_Fedora" name="Password_Fedora" path="fedoraConfig.svr_passwd" value="${fedoraConf.svr_passwd}"/>  </td>
               <td>  <sf:input type="password"  id="Password_Fuseki" name="Password_Fuseki" path="fusekiConfig.svr_passwd"/>  </td> 
               <td>  <sf:input type="password" placeholder="Password " id="Password_SMTP" name="Password_SMTP" path="SMTPConfig.svr_passwd" value="${SMTPConf.svr_passwd}"/>  </td>
               
               
               </tr>
               
               <tr>
               
                <td class="cheader">   </td>
                <td class="cheader">  <input type="button" id="fedoratest" class="testbutton"  onclick="myFunction()" value="Test"></input> </td>
                <td class="cheader">  <input type="button" id="fusekitest" class="testbutton" onclick="testFusekiURL()" value="Test"></input>  </td>
                <td class="cheader">  <input type="button" id="SMTPtest" class="testbutton" onclick="myFunction()" value="Test"></input>  </td>
                
               
               </tr>
               
               <tr>
               
                <td class="cheader">  </td>
                <td class="cheader" id="fedoraresult"> </td>
                <td class="cheader" id="fusekiresult"> </td>
                <td class="cheader" id="smtpresult"> Successful </td>
                
               
               </tr>
            
            </table>
            
            <button id="saveaction" class="savebutton" type="submit" onclick="myFunction()" ><spring:message code="SAVE_CHANGES_BTN"/> </button>
            </sf:form>
            </div>
       
  
    
 </div>
    </div>
    

</body>
</html>