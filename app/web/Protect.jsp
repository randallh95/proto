<%-- 
    Document   : protect
    Created on : 30 Sep, 2017, 3:20:08 PM
    Author     : Ashley
--%>

<%    
    // not authenticated, force user to authenticate
    if (session.getAttribute("user") == null){
        response.sendRedirect("LoginUI.jsp");
    } 
%>