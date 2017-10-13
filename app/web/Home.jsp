<%-- 
    Document   : Home
    Created on : 19 Sep, 2017, 5:06:28 PM
    Author     : Ashley Tan
--%>

<%--
try {
    response.setHeader("Cache-Control","no-cache");
    response.setHeader("Cache-Control","no-store");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader ("Expires", 0);
    if (session.getAttribute("username")==null) {
        response.sendRedirect("LoginUI.jsp");
    }
    else {}
}
catch(Exception ex) {
    out.println(ex);
}
--%>
<%@ include file="Protect.jsp" %> 

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Homepage - SLOCA</title>
    <!-- Bootstrap core CSS -->
        <link href="style/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container">

            <div class="starter-template">
                <h1 class="text-center">Welcome to SLOCA!</h1>
            </div>
            <center>
                <table>
                    <tr>
                        <td>
                            <a href="BreakdownUI.jsp">
                                <button type="button" class="btn btn-default btn-lg" onclick="window.location = BreakdownUI.jsp">Breakdown</button>
                        </td>
                        <td>
                            <a href="TopPopularPlacesUI.jsp">
                                <button type="button" class="btn btn-default btn-lg" onclick="window.location = TopPopularPlacesUI.jsp">Top-K Popular Places</button>
                        </td>
                        <td>
                            <a href="TopNextPlacesUI.jsp">
                                <button type="button" class="btn btn-default btn-lg" onclick="window.location = TopNextPlacesUI.jsp">Top-K Next Places</button>
                        </td>
                        <td>
                            <a href="TopCompanionsUI.jsp">
                                <button type="button" class="btn btn-default btn-lg" onclick="window.location = TopCompanionsUI.jsp">Top-K Companions</button>
                        </td>
                        <td>
                            <a href="AutomaticGroupDetection.jsp">
                                <button type="button" class="btn btn-default btn-lg" onclick="window.location = AutomaticGroupDetection.jsp">Automatic Group Detection</button>
                        </td>
                        <td>
                            <a href="HeatmapUI.jsp">
                                <button type="button" class="btn btn-default btn-lg" onclick="window.location = HeatmapUI.jsp">Heatmap</button>
                        </td>
                        <td>
                            <a href="Logout.jsp">
                                <button type="button" class="btn btn-default btn-lg" onclick="window.location = Logout.jsp">Logout</button>
                        </td>
                    </tr>
                </table>
            </center>
        </div>
    </body>
</html>
