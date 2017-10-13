<%-- 
    Document   : TopNextPlacesUI
    Created on : 19 Sep, 2017, 5:20:40 PM
    Author     : Ashley Tan
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="is203.se.DAO.LocationDAO"%>
<%@ include file="Protect.jsp" %> 

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Top Next Places</title>
        <!-- Bootstrap core CSS -->
        <link href="style/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container">

            <div class="starter-template">
                <h1 class="text-center">Top-K Next Places</h1>
            </div>

            <form>
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

                <div class="form-group">
                    <label for="semantic-places">Semantic Places</label>
                    <select id = "semantic-places" class="form-control">
                        <%
                            LocationDAO locDao = new LocationDAO();
                            ArrayList<String> semanticPlaces = locDao.retrieveSemanticPlaces();

                            for (String s : semanticPlaces) {
                                out.println("<option value='" + s + "'>" + s + "</option>");
                            }
                        %>
                    </select>
                </div>

                <div class="form-group">
                    <label for="datetime">Datetime</label>
                    <input type="datetime-local" name="datetime">
                </div>    

                <button type="submit" class="btn btn-default">Submit</button>
            </form>

        </div>
    </body>
</html>
