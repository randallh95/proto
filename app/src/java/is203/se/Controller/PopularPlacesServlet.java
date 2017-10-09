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
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
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
        int kValue = Integer.parseInt(request.getParameter("k"));

        try (PrintWriter out = response.getWriter()) {

            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            if (datetime.length() == 16) {
                datetime += ":00";
            }
            System.out.println(datetime);
            Date date = formatter.parse(datetime);
            
            // Retrieving list of LocationReport
            HashMap<String, Integer> semanticMapBeforeSorting = locReportDao.retrieveLocationReportByDateAndRank(date, kValue);
            
            // Begin the sorting process
            Set<Entry<String, Integer>> semanticSetBeforeSorting = semanticMapBeforeSorting.entrySet();
            List<Entry<String, Integer>> listOfEntries = new ArrayList<Entry<String, Integer>>(semanticSetBeforeSorting);
            Collections.sort(listOfEntries, valueComparator);
            LinkedHashMap<String, Integer> sortedByValue = new LinkedHashMap<>(listOfEntries.size());

            // copying entries from list to map
            for (Entry<String, Integer> entry : listOfEntries) {
                sortedByValue.put(entry.getKey(), entry.getValue());
            }
            
            request.setAttribute("semanticMap", sortedByValue);
            RequestDispatcher view = request.getRequestDispatcher("PopularPlaces.jsp");
            view.forward(request, response);

        }

    }

    Comparator<Entry<String, Integer>> valueComparator = new Comparator<Entry<String, Integer>>() {
        @Override
        public int compare(Entry<String, Integer> e1, Entry<String, Integer> e2) {
            Integer v1 = e1.getValue();
            Integer v2 = e2.getValue();
            return v2.compareTo(v1);
        }
    };

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
            Logger.getLogger(PopularPlacesServlet.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (SQLException ex) {
            Logger.getLogger(PopularPlacesServlet.class
                    .getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(PopularPlacesServlet.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (SQLException ex) {
            Logger.getLogger(PopularPlacesServlet.class
                    .getName()).log(Level.SEVERE, null, ex);
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
