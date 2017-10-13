<%-- 
    Document   : HeatmapResult
    Created on : 7 Oct, 2017, 8:17:57 PM
    Author     : JunMing
--%>

<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.HashMap"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Heatmap Result</h1>
        <%
            DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = (Date) request.getAttribute("selected.date");
            int floor = (Integer) request.getAttribute("selected.floor");
            out.println("Date/Time = " + formater.format(date) + "<br>");
            out.println("Floor = " + floor + "<br>");
        %>

        <table border="1">
            <tr>
                <td><b>Semantic Place</b></td>
                <td><b>Number of People</b></td>
                <td><b>Density</b></td>
            </tr>
            <%
                // Display Heatmap Result in table format
                Object obj = (Object) request.getAttribute("locationDensityMap");
                HashMap<String, Integer> locationDensityMap = (HashMap<String, Integer>) obj;

                int totalNumOfPeople = 0;
                int totalCrowdDensity = 0;

                for (String semPlace : locationDensityMap.keySet()) {
                    int numOfPeople = locationDensityMap.get(semPlace);
                    int crowdDensity = 0;

                    if (numOfPeople == 0) {
                        crowdDensity = 0;
                    } else if (numOfPeople >= 1 && numOfPeople <= 2) {
                        crowdDensity = 1;
                    } else if (numOfPeople >= 3 && numOfPeople <= 5) {
                        crowdDensity = 2;
                    } else if (numOfPeople >= 6 && numOfPeople <= 10) {
                        crowdDensity = 3;
                    } else if (numOfPeople >= 11 && numOfPeople <= 20) {
                        crowdDensity = 4;
                    } else if (numOfPeople >= 21 && numOfPeople <= 30) {
                        crowdDensity = 5;
                    } else {
                        crowdDensity = 6;
                    }
            %>
            <tr>
                <td><%=semPlace%></td>
                <td><%=numOfPeople%></td>
                <td><%=crowdDensity%></td>
            </tr>
            <%
                    totalNumOfPeople += numOfPeople;
                }
                
                // Calculate total number of people in semantic place and its density
                if (totalNumOfPeople == 0) {
                    totalCrowdDensity = 0;
                } else if (totalNumOfPeople >= 1 && totalNumOfPeople <= 2) {
                    totalCrowdDensity = 1;
                } else if (totalNumOfPeople >= 3 && totalNumOfPeople <= 5) {
                    totalCrowdDensity = 2;
                } else if (totalNumOfPeople >= 6 && totalNumOfPeople <= 10) {
                    totalCrowdDensity = 3;
                } else if (totalNumOfPeople >= 11 && totalNumOfPeople <= 20) {
                    totalCrowdDensity = 4;
                } else if (totalNumOfPeople >= 21 && totalNumOfPeople <= 30) {
                    totalCrowdDensity = 5;
                } else {
                    totalCrowdDensity = 6;
                }
            %>
            <tr>
                <td><b>Total</b></td>
                <td><%=totalNumOfPeople%></td>
                <td><%=totalCrowdDensity%></td>
            </tr>
        </table>
    </body>
</html>
