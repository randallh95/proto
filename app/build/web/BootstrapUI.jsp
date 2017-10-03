<%-- 
    Document   : BootstrapUI
    Created on : Sep 15, 2017, 1:16:21 PM
    Author     : Bernice
--%>

<%@ include file="Protect.jsp" %> 


<%@page import="java.util.Collections"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bootstrap UI</title>
        <!-- Bootstrap core CSS -->
        <link href="style/css/bootstrap.min.css" rel="stylesheet">
        <script>
            document.onload = init();
            function init(){
                console.log(window.location.href);
                if(window.location.href.indexOf("FileUpload") !== -1){
                    window.history.replaceState("","", "http://localhost:8084/app/BootstrapUI.jsp")
                }
            }
        </script>
    </head>
    <body>

        <div class="container">

            <div class="starter-template">
                <h1 class="text-center">Bootstrap UI</h1>
            </div>

            <br>

            <form action = "BootstrapServlet" method="post" enctype = "multipart/form-data">
                <div class="form-group">
                    <label for="exampleInputFile">File input</label>
                    <input type="file" id="exampleInputFile" name="bootstrap-file">
                    <p class="help-block">Please upload a .zip file containing 'location.csv', 'location-lookup.csv' & 'demographics.csv'.</p>
                </div>
                <input type="submit" class="btn btn-default" value="Bootstrap" name="submitBtn">
                <input type="submit" class="btn btn-default" value="Additional" name="submitBtn">
            </form>

            <br>
            <%                
                String uploadError = (String) session.getAttribute("uploadErrorMsg");

                if (uploadError != null) {
                    out.println("<font size = '3' color = red>" + uploadError + "</font><br>");
                }

            %>
            
            <%                
                HashMap<Integer, String[]> locationLookupFileErrors = (HashMap) request.getAttribute("invalidLocations");

                if (locationLookupFileErrors != null) {
                    out.println("<div class='page-header'><h1>location-lookup.csv</h1></div>");
                    
                    int locationLookupFileValidRows = Integer.parseInt(request.getAttribute("validLocationsCount").toString());
                    out.println("<div class='alert alert-info' role='alert'><strong>" + locationLookupFileValidRows + "</strong> records were added successfully.</div>");

                    if (!locationLookupFileErrors.isEmpty()) {
                        out.println("<table class='table table-bordered table-hover table-condensed'>");
                        out.println("<tr class='danger'>");
                        out.println("<th>Row Number</th>");
                        out.println("<th>Error</th>");
                        out.println("</tr>");
                        Set<Integer> locationLookupFileErrorsKeys = locationLookupFileErrors.keySet();
                        for (Integer key : locationLookupFileErrorsKeys) {
                            String[] locationErrorRow = locationLookupFileErrors.get(key);
                            out.println("<tr>");
                            out.println("<td>" + key + "</td>");
                            String errors = "";
                            int length = locationErrorRow.length;
                            for (int i = 0; i < length; i++) {
                                if (i == length - 1) {
                                    errors += locationErrorRow[i];
                                } else {
                                    errors += locationErrorRow[i] + ", ";
                                }
                            }
                            out.println("<td>" + errors + "</td>");
                            out.println("</tr>");
                        }
                        out.println("</table>");
                    } else {
                        out.println("<div class='alert alert-success' role='alert'><strong>Success! </strong>No errors while bootstrapping 'location-lookup.csv'.</div>");
                    }
                }

                HashMap<Integer, String[]> demographicsFileErrors = (HashMap) request.getAttribute("invalidUsers");

                if (demographicsFileErrors != null) {
                    out.println("<div class='page-header'><h1>demographics.csv</h1></div>");

                    int demograohicsFileValidRows = Integer.parseInt(request.getAttribute("validUsersCount").toString());
                    out.println("<div class='alert alert-info' role='alert'><strong>" + demograohicsFileValidRows + "</strong> records were added successfully.</div>");

                    if (!demographicsFileErrors.isEmpty()) {
                        out.println("<table class='table table-bordered table-hover table-condensed'>");
                        out.println("<tr class='danger'>");
                        out.println("<th>Row Number</th>");
                        out.println("<th>Error</th>");
                        out.println("</tr>");
                        Object[] keys = demographicsFileErrors.keySet().toArray();
                        Arrays.sort(keys);
                        
                        for (Object key : keys) {
                            String[] userErrorRow = demographicsFileErrors.get(key);
                            out.println("<tr>");
                            out.println("<td>" + key + "</td>");
                            String errors = "";
                            int length = userErrorRow.length;
                            for (int i = 0; i < length; i++) {
                                if (i == length - 1) {
                                    errors += userErrorRow[i];
                                } else {
                                    errors += userErrorRow[i] + ", ";
                                }
                            }
                            out.println("<td>" + errors + "</td>");
                            out.println("</tr>");
                        }
                        out.println("</table>");
                    } else {
                        out.println("<div class='alert alert-success' role='alert'><strong>Success! </strong>No errors while bootstrapping 'demographics.csv'.</div>");
                    }

                }

                HashMap<Integer, String[]> locationReportFileErrors = (HashMap) request.getAttribute("invalidLocationReports");

                if (locationReportFileErrors != null) {
                    out.println("<div class='page-header'><h1>location.csv</h1></div>");

                    int locationFileValidRows = Integer.parseInt(request.getAttribute("validLocationReportsCount").toString());
                    out.println("<div class='alert alert-info' role='alert'><strong>" + locationFileValidRows + "</strong> records were added successfully.</div>");

                    if (!locationReportFileErrors.isEmpty()) {
                        out.println("<table class='table table-bordered table-hover table-condensed'>");
                        out.println("<tr class='danger'>");
                        out.println("<th>Row Number</th>");
                        out.println("<th>Error</th>");
                        out.println("</tr>");
                        Object[] keys = locationReportFileErrors.keySet().toArray();
                        Arrays.sort(keys);
                        
                        for (Object key : keys) {
                            String[] userErrorRow = locationReportFileErrors.get(key);
                            out.println("<tr>");
                            out.println("<td>" + key + "</td>");
                            String errors = "";
                            int length = userErrorRow.length;
                            for (int i = 0; i < length; i++) {
                                if (i == length - 1) {
                                    errors += userErrorRow[i];
                                } else {
                                    errors += userErrorRow[i] + ", ";
                                }
                            }
                            out.println("<td>" + errors + "</td>");
                            out.println("</tr>");
                        }
                        out.println("</table>");
                    } else {
                        out.println("<div class='alert alert-success' role='alert'><strong>Success! </strong>No errors while bootstrapping 'location.csv'.</div>");
                    }

                }

            %>

            <!--
            <table class="table table-bordered table-hover table-condensed">
                <tr>
                    <th>Company</th>
                    <th>Contact</th>
                    <th>Country</th>
                </tr>
                <tr>
                    <td>Alfreds Futterkiste</td>
                    <td>Maria Anders</td>
                    <td>Germany</td>
                </tr>
                <tr>
                    <td>Centro comercial Moctezuma</td>
                    <td>Francisco Chang</td>
                    <td>Mexico</td>
                </tr>
                <tr>
                    <td>Ernst Handel</td>
                    <td>Roland Mendel</td>
                    <td>Austria</td>
                </tr>
                <tr>
                    <td>Island Trading</td>
                    <td>Helen Bennett</td>
                    <td>UK</td>
                </tr>
            </table>
            -->
        </div><!-- /.container -->
    </body>
</html>
