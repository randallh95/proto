/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is203.se.Controller;

import is203.se.DAO.LocationDAO;
import is203.se.DAO.LocationReportDAO;
import is203.se.Entity.Location;
import is203.se.Entity.LocationReport;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author randa
 */
public class PopularPlacesServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.text.ParseException
     * @throws java.sql.SQLException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, SQLException {
        response.setContentType("text/html;charset=UTF-8");

        LocationReportDAO locReportDao = new LocationReportDAO();

        String datetime = request.getParameter("datetime");
        String kValue = request.getParameter("k");
        
        try (PrintWriter out = response.getWriter()) {
            
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
            Date date = (Date) formatter.parse(datetime);
            // Retrieving list of LocationReport
            ArrayList<LocationReport> locReportList = locReportDao.retrieveLocationReportByDate(date);
            
            // Creating a map of locationIDs and their counts
            Map<Long, Integer> locMap = new HashMap<>();
            locReportList.stream().map((r) -> r.getLocationId()).forEachOrdered((locID) -> {
                Integer count = locMap.get(locID);
                if(count == null){
                    locMap.put(locID, 1);
                } else {
                    locMap.put(locID, count + 1);
                }
            });
            
            // Printing for testing
            locMap.forEach((k,v) -> out.println("key: "+k+" value:"+v+"</br>"));
            
            LocationDAO locDao = new LocationDAO();
            ArrayList<Location> locList = locDao.retrieveAllLocations();
            
            Map<String, Integer> semanticMap = new HashMap<>();
            semanticMap.forEach((k,v) -> {
                for(Location loc:locList){
                    locMap.forEach((key,value)->{
                        
                    });
                    String semanticPlace = loc.getSemanticPlace();
                    
                }
                //Integer count = semanticMap.get(k)
            });
            
            
            
            
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, NullPointerException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(PopularPlacesServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PopularPlacesServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(PopularPlacesServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PopularPlacesServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
