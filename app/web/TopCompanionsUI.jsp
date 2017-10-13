<%-- 
    Document   : TopCompanions
    Created on : 19 Sep, 2017, 5:21:02 PM
    Author     : Ashley Tan
--%>

<%@page import="is203.se.Entity.User"%>
<%@page import="java.util.ArrayList"%>
<%@page import="is203.se.DAO.UserDAO"%>
<%@ include file="Protect.jsp" %> 

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Top Companions</title>
        <!-- Bootstrap core CSS -->
        <link href="style/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    </head>
    <body>
        <div class="container">

            <div class="starter-template">
                <h1 class="text-center">Top-K Companions</h1>
            </div>
            
            <form method="POST" action="TopCompanionsServlet">
                <div class="form-group">
                    <label for="user">Student's Email Address</label>
                    <input id="txtAutoComplete" list="dlAutoComplete" type="text" class="form-control">
                    <datalist id="dlAutoComplete">
                        <option></option>
                        <%
                            UserDAO userDao = new UserDAO();
                            ArrayList<User> allUsers = userDao.retrieveAllUsers();

                            for (User u : allUsers) {
                                out.println("<option value='" + u.getEmail() + "'>" + u.getEmail() + "</option>");
                            }
                        %>
                    </datalist>
                </div>
            
            
                    
                <div class="form-group">
                    <label for="k">Top-K</label>
                    <select id = "k" class="form-control">
                        <%                    for (int i = 1; i <= 10; i++) {
                                if (i == 3) {
                                    out.println("<option  selected='selected' value='" + i + "'>" + i + "</option>");
                                } else {
                                    out.println("<option value='" + i + "'>" + i + "</option>");
                                }

                            }
                        %>
                    </select>
                </div>
                
                <!-- Input DateTime -->
                <div class="form-group">
                    <label for="datetime">Datetime</label>
                    <input type="datetime-local" name="datetime">
                </div>    

                <!-- Submit and reset buttons -->
                <button type="submit" class="btn btn-default">Submit</button>
                <button type="reset" class="btn btn-default">Reset</button>
            </form>

        </div>
    </body>
</html>
