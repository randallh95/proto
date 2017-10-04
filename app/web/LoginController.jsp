<%-- 
    Document   : LoginController
    Created on : 15 Sep, 2017, 4:07:02 PM
    Author     : Randall Heng, Ashley Tan
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="is203.se.DAO.UserDAO"%>
<%@page import="is203.se.Entity.User"%>
<%
    UserDAO userDAO = new UserDAO();
    //Retrieve values from LoginUI
    String username = request.getParameter("username");
    String enteredPassword = request.getParameter("pwd");

    // Checking for null and empty strings in username and password field
    if (username == null || username.trim().equals("")) {
%>
<jsp:forward page="LoginUI.jsp">
    <jsp:param name="errorMsg" value="Please enter username!" />
</jsp:forward>
<%
    }

    if (enteredPassword == null || enteredPassword.trim().equals("")) {
%>
<jsp:forward page="LoginUI.jsp">
    <jsp:param name="errorMsg" value="Please enter password!" />
</jsp:forward>
<%
    }

    User user = userDAO.retrieve(username);
    // user will be null if username is invalid
    if (user == null || !user.validateUser(enteredPassword)) {
%>
<jsp:forward page="LoginUI.jsp">
    <jsp:param name="errorMsg" value="Invalid username/password" />
</jsp:forward>
<%
    } else {
        session.setAttribute("user", user);
        response.sendRedirect("Home.jsp");
    }


%>