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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author JunMing
 */
@WebServlet(name = "HeatmapServlet", urlPatterns = {"/HeatmapServlet"})
public class HeatmapServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            /* TODO output your page here. You may use following sample code. */

            //Retrieve values from HeatmapUI.jsp
            String dateStr = (String) request.getParameter("date_time");
            String dateFormatted = dateStr.replaceAll("T", " ");
            if (dateFormatted.length() != 19) {
                dateFormatted += ":00";
            }
            int floor = Integer.parseInt(request.getParameter("floor"));

            // set floor in request
            request.setAttribute("selected.floor", floor);

            // set date in request
            DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = (Date) formater.parse(dateFormatted);
            request.setAttribute("selected.date", date);

            // Call retrieveHeatMap to return HashMap<String, Integer> of key SemanticPlace & value numberOfPeople
            HashMap<String, Integer> locationDensityMap = new HashMap<>();
            locationDensityMap = retrieveHeatMap(date, floor);
            request.setAttribute("locationDensityMap", locationDensityMap);

            RequestDispatcher view = request.getRequestDispatcher("HeatmapUI.jsp");
            view.forward(request, response);
        } catch (ParseException | SQLException ex) {
            Logger.getLogger(HeatmapServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected HashMap<String, Integer> retrieveHeatMap(Date date, int floor) throws SQLException {
        LocationDAO locationDao = new LocationDAO();
        LocationReportDAO locationReportDao = new LocationReportDAO();
        // Create locationDensityMap to store Semantic Place (key) & number of people at semantic place (value)
        HashMap<String, Integer> locationDensityMap = new HashMap<>();
        ArrayList<String> macAddressList = locationReportDao.retrieveMacAddressesAt(floor, date);
        
        for (String macAddress : macAddressList) {
            long locationId = locationReportDao.retrieveLatestLocationIdOf(macAddress, floor, date);
            String semanticPlace = locationDao.retrieveSemanticPlace(locationId);
            System.out.println(semanticPlace);
            
            // if locationDensityMap contains semanticPlace
            if (locationDensityMap.containsKey(semanticPlace)) {
                int totalNumOfPeopleAtSemPlace = locationDensityMap.get(semanticPlace);
                locationDensityMap.put(semanticPlace, totalNumOfPeopleAtSemPlace+1);
            } else {
                locationDensityMap.put(semanticPlace, 1);
            }
        }
        
        /*// for each location object in locationList
        for (Location location : locationList) {
            long locationId = location.getLocationId(); // get locationId of location object
            String semanticPlace = location.getSemanticPlace(); // get semanticPlace of location object
            int numOfPeopleAtLocationId = locationReportDao.retrieveNumberOfLocationReports(locationId, date); // get number of people at locationId
            
            // if locationDensityMap contains semanticPlace
            if (locationDensityMap.containsKey(semanticPlace)) {
                int totalNumOfPeopleAtSemPlace = locationDensityMap.get(semanticPlace);
                locationDensityMap.put(semanticPlace, totalNumOfPeopleAtSemPlace + numOfPeopleAtLocationId);
            } else {
                locationDensityMap.put(semanticPlace, numOfPeopleAtLocationId);
            }

        }*/
        return locationDensityMap;
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
            throws ServletException, IOException {
        processRequest(request, response);
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
        processRequest(request, response);
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
