/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is203.se.DAO;

import is203.se.Utility.ConnectionManager;
import is203.se.Entity.Location;
import java.sql.Connection;
import static java.sql.JDBCType.BIGINT;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bernice
 */
public class LocationDAO {

    Connection conn;

    public LocationDAO() {
        try {
            conn = ConnectionManager.getConnection();
            conn.setAutoCommit(false);
        } catch (SQLException ex) {
            Logger.getLogger(LocationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int[] insertLocations(ArrayList<String[]> validLocationList) {

        String sql = "insert into location values(?, ?)";
        int[] result = null;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            for (String[] validLocation : validLocationList) {
                ps.setString(1, validLocation[0]);
                ps.setString(2, validLocation[1]);

                ps.addBatch();
            }
            result = ps.executeBatch();

        } catch (SQLException ex) {
            Logger.getLogger(LocationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public ArrayList<Location> retrieveAllLocations() throws SQLException {
        String sql = "select * from location";
        ArrayList<Location> locationArrList = new ArrayList<>();

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            long locationId = rs.getLong("location_id");
            String semanticPlace = rs.getString("semantic_place");
            Location location = new Location(locationId, semanticPlace);
            locationArrList.add(location);
        }
        ps.close();
        return locationArrList;
    }

    public int retrieveLocation(String locationId) throws SQLException {
        String sql = "select count(*) from location where location_id = ?";
        int num = 0;

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, Long.parseLong(locationId));
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            num = rs.getInt(1);
        }

        ps.close();
        return num;
    }

    public ArrayList<String> retrieveSemanticPlaces() {
        String sql = "SELECT DISTINCT semantic_place from location";
        ArrayList<String> result = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(LocationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public ArrayList<Location> retrieveLocationsByFloor(int floor) throws SQLException {
        ArrayList<Location> semList = new ArrayList<>();
        String floorString = String.valueOf(floor);
        System.out.println(floorString);
        String sql = "select * from location where semantic_place like ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        if (floor == 0) {
            ps.setString(1, "%B1%");
        } else {
            ps.setString(1, "%L" + floorString + "%");
        }
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            long locationId = rs.getLong("location_id");
            String semanticPlace = rs.getString("semantic_place");
            semList.add(new Location(locationId, semanticPlace));
        }

        return semList;
    }

    public int updateLocations(long locationId, String semanticPlace) throws SQLException {
        String sql = "update location set semantic_place = ? where location_id = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, semanticPlace);
        ps.setString(2, String.valueOf(locationId));
        int updateNum = ps.executeUpdate();

        return updateNum;
    }

    public void closeConnection() {
        ConnectionManager.close(conn);
    }

    public void revertAction() throws SQLException {
        conn.rollback();
    }

    public void commitAction() throws SQLException {
        conn.commit();
    }
}
