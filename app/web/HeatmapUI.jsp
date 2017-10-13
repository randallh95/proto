<%-- 
    Document   : Heatmap
    Created on : 19 Sep, 2017, 5:21:25 PM
    Author     : Ashley Tan
--%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.Timestamp"%>
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
        <title>HeatMap UI</title>
    </head>
    <body>
        <h1>HeatMap UI</h1>

        <form action="HeatmapController.jsp" method="post">
            <select name="date_time">
<%
                LocationReportDAO locationReportDao = new LocationReportDAO();
                ArrayList<Date> datesForSelection = locationReportDao.retrieveDatesForUserSelection();
                for (Date date : datesForSelection) {
                    DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateStr = formater.format(date);
%>
                    <option value="<%=date%>"><%=dateStr%></option>
<%
                }  
%>
            </select>
            <br><br>
            <select name="floor">
                <option value="0">B1</option>
                <option value="1">L1</option>
                <option value="2">L2</option>
                <option value="3">L3</option>
                <option value="4">L4</option>
                <option value="5">L5</option>
            </select>
            <input type="submit">
        </form>
    </body>
</html>
<%    
    LocationDAO locationDao = new LocationDAO();
    ArrayList<Location> locList = locationDao.retrieveLocationsByFloor(0);
    out.print(locList.size() + "<br>");

    int i = 0;
    DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = (Date) formater.parse("2017-07-10 14:00:00");
    out.println(formater.format(date) + "<br>");
    i = locationReportDao.retrieveNumberOfLocationReports(1010400001, date);
    out.println(i);
%>