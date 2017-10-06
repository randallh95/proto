<%-- 
    Document   : TopPopularPlacesUI
    Created on : 4 Oct, 2017, 12:01:39 PM
    Author     : Randall
--%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@page import="is203.se.DAO.LocationDAO"%>
<%@ include file="Protect.jsp" %> 


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Top Popular Places</title>
        <!-- Bootstrap core CSS -->
        <link href="style/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container">
            <div class="starter-template">
                <h1 class="text-center">Top Popular Places</h1>
            </div>

            <form method="GET" action="PopularPlacesServlet">
                <!-- Choosing Top K -->
                <div class="form-group">
                    <label for="k">Top-K</label>
                    <select class="form-control" id = "k" name="k">
                        <% for (int i = 1; i <= 10; i++) {
                                if (i == 3) {
                                    out.println("<option selected='selected' value='" + i + "'>" + i + "</option>");
                                } else {
                                    out.println("<option value='" + i + "'>" + i + "</option>");
                                }
                            }
                        %>
                    </select>
                </div>

                <!-- Choosing DateTime -->
                <div class="form-group">
                    <label for="datetime">Datetime</label>
                    <input type="datetime-local" step="1" name="datetime">
                </div>  

                <!-- Submit and reset -->
                <button type="submit" class="btn btn-default">Submit</button>
                <button type="reset" class="btn btn-default">Reset</button>
            </form>
        </div>
        <%
            Object semanticMapObj = request.getAttribute("map");
            HashMap<String, Integer> semanticMap = (HashMap<String, Integer>) semanticMapObj;

        %>
    </body>
</html>
