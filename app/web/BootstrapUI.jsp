<%-- 
    Document   : BootstrapUI
    Created on : Sep 15, 2017, 1:16:21 PM
    Author     : Bernice
--%>

<%@page import="is203.se.Entity.BootstrapError"%>
<!-- need to include protect.jsp -->

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
            function init() {
                console.log(window.location.href);
                if (window.location.href.indexOf("FileUpload") !== -1) {
                    window.history.replaceState("", "", "http://localhost:8084/app/BootstrapUI.jsp")
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
                    <label for="exampleInputFile">Upload .zip file only</label>
                    <input type="file" id="exampleInputFile" name="bootstrap-file" accept=".zip">
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
                
                Long timeTakenToBootstrap = (Long) session.getAttribute("timeTakenToBootstrap");
                if (timeTakenToBootstrap != null) {
                    out.println("<div class='alert alert-info' role='alert'>Time taken to Bootstrap: " + timeTakenToBootstrap + " seconds</div>");
                }

                ArrayList<BootstrapError> locationLookupFileErrors = (ArrayList<BootstrapError>) session.getAttribute("locationLookupFileErrors");
                ArrayList<BootstrapError> demographicsFileErrors = (ArrayList<BootstrapError>) session.getAttribute("demographicsFileErrors");
                ArrayList<BootstrapError> locationFileErrors = (ArrayList<BootstrapError>) session.getAttribute("locationFileErrors");

                if (locationLookupFileErrors != null && !locationLookupFileErrors.isEmpty()) {

                    out.println("<h1 class='page-header'>Errors for location-lookup.csv</h1>");

                    out.println("<table class='table table-striped table-bordered table-hover table-condensed'>");
                    out.println("<tr><th>Line</th><th>Message</th></tr>");
                    for (BootstrapError error : locationLookupFileErrors) {
                        out.println("<tr>");
                        out.println("<td>" + error.getRowNum() + "</td>");
                        out.println("<td>" + error.toString() + "</td>");
                        out.println("</tr>");
                    }
                    out.println("</table>");

                }

                if (demographicsFileErrors != null && !demographicsFileErrors.isEmpty()) {

                    out.println("<h1 class='page-header'>Errors for demographics.csv</h1>");

                    out.println("<table class='table table-striped table-bordered table-hover table-condensed'>");
                    out.println("<tr><th>Line</th><th>Message</th></tr>");
                    for (BootstrapError error : demographicsFileErrors) {
                        out.println("<tr>");
                        out.println("<td>" + error.getRowNum() + "</td>");
                        out.println("<td>" + error.toString() + "</td>");
                        out.println("</tr>");
                    }
                    out.println("</table>");

                }

                if (locationFileErrors != null && !locationFileErrors.isEmpty()) {

                    out.println("<h1 class='page-header'>Errors for location.csv</h1>");

                    out.println("<table class='table table-striped table-bordered table-hover table-condensed'>");
                    out.println("<tr><th>Line</th><th>Message</th></tr>");
                    for (BootstrapError error : locationFileErrors) {
                        out.println("<tr>");
                        out.println("<td>" + error.getRowNum() + "</td>");
                        out.println("<td>" + error.toString() + "</td>");
                        out.println("</tr>");
                    }
                    out.println("</table>");

                }


            %>

        </div><!-- /.container -->
    </body>
</html>
