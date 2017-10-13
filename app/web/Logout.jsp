<%-- 
    Document   : Logout
    Created on : 19 Sep, 2017, 5:21:40 PM
    Author     : Ashley Tan
--%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
<%
    session.removeAttribute("user");
    
    session.invalidate();
    
%> 

        <h1>Logout was done successfully</h1>
    </body>
</html>