<%-- 
    Document   : LoginUI
    Created on : 15 Sep, 2017, 3:54:38 PM
    Author     : Randall Heng, Ashley Tan
--%>

<%@page import="java.util.ArrayList"%>


<html>
    <center>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>SLOCA </title>
        <h1>Welcome to SLOCA</h1></head>
    <body>
        <%
            String errorMsg = request.getParameter("errorMsg");

            if (errorMsg == null) {
                errorMsg = "";
            }
        %>
        <%=errorMsg%>
        <form action='LoginController.jsp' method='post'>
            <table>
                <tr>
                    <td>
                        Email ID:
                    </td>
                    <td>
                        <input type='text' name='username' />
                    </td>
                </tr>
                <tr>
                    <td>
                        Password:
                    </td>
                    <td>
                        <input type='password' name='pwd' />
                    </td>
                </tr>
            </table>
            <input type='submit' value='Login!' />
        </form>
    </body>
</center>
</html>
