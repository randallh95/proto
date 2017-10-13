<%-- 
    Document   : TopNextPlacesUI
    Created on : 19 Sep, 2017, 5:20:40 PM
    Author     : Ashley Tan
--%>

<%@page import="java.lang.Integer"%>
<%@page import="java.util.*"%>
<%@page import="is203.se.DAO.LocationDAO"%>
<%@ include file="Protect.jsp" %> 

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Top-K Next Places</title>
        <!-- Bootstrap core CSS -->
        <link href="style/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container">

            <div class="starter-template">
                <h1 class="text-center">Top-K Next Places</h1>
            </div>

            <form action = "TopNextPlacesServlet" method="post" enctype = "multipart/form-data">
                <div class="form-group">
                    <label for="k">Top-K</label>
                    <select id = "k" class="form-control" name="k-value">
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
                    <label for="semantic-places">Semantic Place</label>
                    <select id = "semantic-places" class="form-control" name="semantic-place">
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
                    <input type="datetime-local" name="datetime" step="1">
                </div>    

                <button type="submit" class="btn btn-default">Submit</button>
                <button type="reset" class="btn btn-default">Reset</button>
            </form>

            <br>

            <%
                HashMap<Integer, String> topNextPlaces = (HashMap<Integer, String>) request.getAttribute("topNextPlaces");
                if (topNextPlaces!=null){
                    
                
                Set<Integer> unrankedKeySet = topNextPlaces.keySet();
                Integer[] sortedKeysArray = new Integer[unrankedKeySet.size()];
                unrankedKeySet.toArray(sortedKeysArray);

                Arrays.sort(sortedKeysArray);
                int count = 1;
            %>
            
            <div class="col-md-6">
                <table class="table table-condensed text-center">
                    <thead>
                        <tr>
                            <th>Rank</th>
                            <th>Next Semantic Place</th>
                            <th>Users Visited</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            for (Integer i : sortedKeysArray) {
                                out.println("<tr>");
                                out.println("<th scope=\"row\">" + count + "</td>");
                                out.println("<td>" + topNextPlaces.get(i) + "</td>");
                                out.println("<td>" + i + "</td>");
                                out.println("</tr>");
                                count++;
                            }
                        %>
                    </tbody>
                </table>
            </div>
            <%
                }
            %>
        </div>
    </body>
</html>
