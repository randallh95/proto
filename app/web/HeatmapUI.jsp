<%-- 
    Document   : HeatmapUI
    Created on : 19 Sep, 2017, 5:21:25 PM
    Author     : Ashley Tan
--%>
<%@page import="java.util.HashMap"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="is203.se.DAO.LocationReportDAO"%>
<%@page import="is203.se.Entity.Location"%>
<%@page import="java.util.ArrayList"%>
<%@page import="is203.se.DAO.LocationDAO"%>
<%@include file="Protect.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>HeatMap</title>
        <!-- Bootstrap core CSS -->
        <link href="style/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container">

            <div class="starter-template">
                <h1 class="text-center">HeatMap</h1>
            </div>

            <form action="HeatmapServlet" method="post">

                <div class="form-group">
                    <label for="floor">Floor level</label>
                    <select id = "floor" class="form-control" name="floor">
                        <option value="0">B1</option>
                        <option value="1">L1</option>
                        <option value="2">L2</option>
                        <option value="3">L3</option>
                        <option value="4">L4</option>
                        <option value="5">L5</option>
                    </select>
                </div>


                <div class="form-group">
                    <label for="date_time">Datetime</label>
                    <input type="datetime-local" name="date_time" step="1">
                </div>    

                <button type="submit" class="btn btn-default">Submit</button>
                <button type="reset" class="btn btn-default">Reset</button>
            </form>

            <br>

            <%                            
                // Display Heatmap Result in table format
                Object obj = (Object) request.getAttribute("locationDensityMap");
                HashMap<String, Integer> locationDensityMap = (HashMap<String, Integer>) obj;
                
                if (locationDensityMap != null) {
                    Date date = (Date) request.getAttribute("selected.date");
                    int floor = (Integer) request.getAttribute("selected.floor");
            %>
            
            <b>Date & Time:</b> 
            <%
                DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                out.println(formater.format(date));
            %>
            <br>
            
            <b>Floor:</b>
            <% if(floor==0){
                out.println("B1");
            } else {
                out.println("L" + floor);
            }%> 
            <br>
            
            <div class="col-md-6">
                <table class="table table-condensed text-center">
                    <thead>
                        <tr>
                            <th>Semantic Place</th>
                            <th>Number of People</th>
                            <th>Density</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
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
                    </tbody>
                </table>
            </div>
            <%                
                }
            %>
        </div>    
    </body>
</html>