<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>

 <div class="footer">
    <p><spring:message code="DATE"/> <%=new SimpleDateFormat("MM/dd/yy").format(new Date()) %><br /><spring:message code="TIME"/> <%=new SimpleDateFormat("HH:mm:ss").format(new Date()) %></p>
  </div>
