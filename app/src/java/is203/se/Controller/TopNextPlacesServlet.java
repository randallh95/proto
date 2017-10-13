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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author suman
 */
@WebServlet(name = "TopNextPlacesServlet", urlPatterns = {"/TopNextPlacesServlet"})
@MultipartConfig
public class TopNextPlacesServlet extends HttpServlet {

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

        String kValueAsString = request.getParameter("k-value");
        int k = Integer.parseInt(kValueAsString);
        String semanticPlace = request.getParameter("semantic-place");
        String date = request.getParameter("datetime");
        String dateFormatted = date.replaceAll("T", " ");

        // if user didn't select milliseonds in UI (e.g.12:34:00 , milliseconds left as :00),
        // date would not have milliseconds specified (e.g. 12:34)
        // this causes problems while parsing date into our format "yyyy-MM-dd HH:mm:ss"
        // therefore, append ":00" for such cases
        if (dateFormatted.length() != 19) {
            dateFormatted += ":00";
        }
        DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date previousWindowDate = null;
        // end time for the previous window
        try {
            previousWindowDate = (Date) formater.parse(dateFormatted);
        } catch (ParseException ex) {
            Logger.getLogger(TopNextPlacesServlet.class.getName()).log(Level.SEVERE, null, ex);
            return; // stop execution
        }
        Date nextWindowDate = new Date(previousWindowDate.getTime() + TimeUnit.MINUTES.toMillis(15));
        // end time for the next window

        LocationDAO locDao = new LocationDAO();
        ArrayList<Long> locationIdList = locDao.retrieveLocationIdsForSpecifiedSemanticPlace(semanticPlace);
        // locationIdList contains ALL the locationId's for the user selected semantic place

        LocationReportDAO locRepDao = new LocationReportDAO();
        ArrayList<LocationReport> previousWindowReports = locRepDao.retrieveLatestLocationReportsForDatetimeAndLocationIds(previousWindowDate, locationIdList);
        /*
            previousWindowReports contains all location reports that:
            - are in previous window
            - are for the specified semantic place (have locationId inside our locationIdList)
            - are the latest for each user (if a user has multiple LocationReports, we only take the one with latest timestamp within window)
            
            Check our the SQL query for 'retrieveLatestLocationReportsForDatetimeAndLocationIds' to witness the magic
         */
        // System.out.println("previousWindowReports size is: " + previousWindowReports.size());

        ArrayList<String> macAddressesInPreviousWindow = new ArrayList<>();
        for (LocationReport locRep : previousWindowReports) {
            String macAddress = locRep.getMacAddress();
            macAddressesInPreviousWindow.add(macAddress);
        }

        ArrayList<LocationReport> nextWindowReports = locRepDao.retrieveLocationReportsForDatetimeAndMacAddresses(nextWindowDate, macAddressesInPreviousWindow);
        /* 
            nextWindowReports contains all location reports that:
            - are in the next window
            - are for macAddresses that had a locationReport in previous window (regardless of whether this macAddress maps to a user or not)
            
            Once again, all done through the SQL query.
         */
        // System.out.println("nextWindowReports size is: " + nextWindowReports.size());

        printNumUniqueMacAddressesForLocationReports(nextWindowReports);
        // this test method helps to identify the number of unique macAddresses inside nextWindowReports 

        HashMap<String, ArrayList<LocationReport>> macAddressMappedToLocationReports = new HashMap<>();
        // maps a unique macAddress to the list of LocationReports (in next window) for that macAddress
        // use the key (macAddress) to retrieve all LocationReports for that macAddress
        for (LocationReport locRep : nextWindowReports) {
            String macAddress = locRep.getMacAddress();
            ArrayList<LocationReport> locRepList;
            if (!macAddressMappedToLocationReports.containsKey(macAddress)) {
                locRepList = new ArrayList<>();
            } else {
                locRepList = macAddressMappedToLocationReports.get(macAddress);
            }
            locRepList.add(locRep);
            macAddressMappedToLocationReports.put(macAddress, locRepList);
        }
        Set<String> macAddrKeySet = macAddressMappedToLocationReports.keySet();

        ArrayList<Location> locationList = locDao.retrieveAllLocations();
        HashMap<Long, String> locationIdMappedToSemanticPlace = new HashMap<>();
        // maps locationIds to a semantic place. is used as parameter for processNextWindowPerUser() method
        for (Location loc : locationList) {
            Long locationId = loc.getLocationId();
            String semPlace = loc.getSemanticPlace();
            locationIdMappedToSemanticPlace.put(locationId, semPlace);
        }

        
        HashMap<String, Integer> nextPlaces = new HashMap<>();
        // mapes each Semantic Place (that is inside next window) to the count of number of people who have that particular Semantic Placa as their 'next place'

        for (String macAddr : macAddrKeySet) {
            ArrayList<LocationReport> locRepForThisMacAddr = macAddressMappedToLocationReports.get(macAddr);
            // ^ all the LocationReport for this macAddress
            String nextPlace = findNextPlace(locRepForThisMacAddr, locationIdMappedToSemanticPlace);
            if (nextPlace != null) {
                if (nextPlaces.containsKey(nextPlace)) {
                    int count = nextPlaces.get(nextPlace);
                    nextPlaces.put(nextPlace, ++count); // increment the count
                } else {
                    nextPlaces.put(nextPlace, 1);
                }
            }
        }

        Set<String> keySetForNextPlaces = nextPlaces.keySet();
        HashMap<Integer, String> rankedNextPlaces = new HashMap<>();
        for (String semanticPlaceKey : keySetForNextPlaces) {
            int count = nextPlaces.get(semanticPlaceKey);
            if (rankedNextPlaces.get(count) == null) {
                rankedNextPlaces.put(count, semanticPlaceKey);
            } else {
                String currentRankedSemanticPlace = rankedNextPlaces.get(count);
                currentRankedSemanticPlace += ", " + semanticPlaceKey;
                rankedNextPlaces.put(count, currentRankedSemanticPlace);
            }
        }

        request.setAttribute("topNextPlaces", rankedNextPlaces);
        RequestDispatcher view = request.getRequestDispatcher("TopNextPlacesUI.jsp");
        view.forward(request, response);

    }
    
    /*
        This method helps to find the next semantic place for each user. ROughly, the steps are:
        - find eligible places (fella spends at least 5 mins continously at the place)
        - return the *last* eligible place
        - if no eligilbe places, will return null
    */
    private String findNextPlace(ArrayList<LocationReport> locationReportList, HashMap<Long, String> locationIdMappedToSemanticPlace) {
        // locationReportList should already be sorted in ascending order of timestamp - verified true
        ArrayList<String> eligibleVisitedPlaces = new ArrayList<>();

        int index = 0;
        int locationReportListSize = locationReportList.size();
        String potentiallyEligibleVisitedPlace = "";
        long startTime = 0;

        while (index < locationReportListSize) {

            LocationReport locRep = locationReportList.get(index);
            long locId = locRep.getLocationId();
            String semanticPlace = locationIdMappedToSemanticPlace.get(locId);

            if (potentiallyEligibleVisitedPlace.equals("")) {
                potentiallyEligibleVisitedPlace = semanticPlace;
                startTime = locRep.getTimestamp().getTime();
                index++;
                continue;
            }

            if (!potentiallyEligibleVisitedPlace.equals(semanticPlace)) {
                LocationReport previousLocRep = locationReportList.get(index - 1);
                String previousSemanticPlace = locationIdMappedToSemanticPlace.get(previousLocRep.getLocationId());
                Date previousLocRepDate = previousLocRep.getTimestamp();
                long previousLocRepTime = previousLocRepDate.getTime();
                long milliSecondsSpentAtSemanticPlace = previousLocRepTime - startTime;
                long minsSpentAtSemanticPlace = TimeUnit.MILLISECONDS.toMinutes(milliSecondsSpentAtSemanticPlace);

                if (minsSpentAtSemanticPlace >= 5) {
                    eligibleVisitedPlaces.add(previousSemanticPlace);
                }

                potentiallyEligibleVisitedPlace = semanticPlace;
                startTime = locRep.getTimestamp().getTime();

            }

            index++;
        }

        if (eligibleVisitedPlaces.isEmpty()) {
            return null;
        }
        return eligibleVisitedPlaces.get(eligibleVisitedPlaces.size() - 1);
    }

    // * for testing purposes *
    private void printNumUniqueMacAddressesForLocationReports(ArrayList<LocationReport> locRepList) {
        HashSet<String> macAddresses = new HashSet<String>();
        for (LocationReport locRep : locRepList) {
            String macAddress = locRep.getMacAddress();
            macAddresses.add(macAddress);
        }
        System.out.println("number of unique macAddresses is: " + macAddresses.size());
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
