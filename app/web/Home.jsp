<%-- 
    Document   : home
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
    </head>
    <body>
        Welcome to SLOCA!
        <table>
            <tr>
                <td>
                    <a href="PopularPlaces.jsp">
                    <button type="button" onclick="window.location=PopularPlaces.jsp">Popular Places</button>
                </td>
                <td>
                    <a href="TopNextPlacesUI.jsp">
                    <button type="button" onclick="window.location=TopNextPlacesUI.jsp">Top Next Places</button>
                </td>
                <td>
                    <a href="TopCompanions.jsp">
                    <button type="button" onclick="window.location=TopCompanions.jsp">Top Companions</button>
                </td>
                <td>
                    <a href="AutomaticGroupDetection.jsp">
                    <button type="button" onclick="window.location=AutomaticGroupDetection.jsp">Automatic Group Detection</button>
                </td>
                <td>
                    <a href="HeatmapUI.jsp">
                    <button type="button" onclick="window.location=HeatmapUI.jsp">Heatmap</button>
                </td>
                <td>
                    <a href="Logout.jsp">
                    <button type="button" onclick="window.location=Logout.jsp">Logout</button>
                </td>
            </tr>
        </table>
    </body>
</html>
