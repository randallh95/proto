/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is203.se.Entity;

/**
 *
 * @author JunMing
 */
import is203.se.DAO.LocationDAO;
import is203.se.DAO.LocationReportDAO;
import is203.se.Entity.Location;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Heatmap {

    LocationDAO locationDao;
    LocationReportDAO locationReportDao;

    public Heatmap() {
        locationDao = new LocationDAO();
        locationReportDao = new LocationReportDAO();
    }

    public HashMap<String, Integer> retrieveHeatMap(Date date, int floor) throws SQLException {
        HashMap<String, Integer> locationDensityMap = new HashMap<>();
        ArrayList<Location> locationList = locationDao.retrieveLocationsByFloor(floor); // retrieve list of location objects at this floor

        // for each location object in locationList
        for (Location location : locationList) {
            long locationId = location.getLocationId(); // get locationId of location object
            String semanticPlace = location.getSemanticPlace(); // get semanticPlace of location objet

            // if locationDensityMap contains a location object with the same semantic place
            if (locationDensityMap.containsKey(semanticPlace)) {
                // Update value (numOfPeople+1) of key (semanicPlace)
                int numOfPeople = locationDensityMap.get(semanticPlace);
                locationDensityMap.put(semanticPlace, ++numOfPeople);

            } else { // else add new key (semanticPlace) and value (numOfPeople = 1)
                locationDensityMap.put(semanticPlace, 1);
            }
        }
        return locationDensityMap;
    }
}
