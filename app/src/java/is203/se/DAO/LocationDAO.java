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
import java.sql.Statement;
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
    
    public void loadDataInfile(String absoluteFilePath) {
        String sql = "LOAD DATA LOCAL INFILE '" + absoluteFilePath + "' INTO TABLE location\n"
                + "FIELDS TERMINATED BY ',' \n"
                + "ENCLOSED BY '\"' \n"
                + "LINES TERMINATED BY '\\n';";
        try (Statement stmt  = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(LocationReportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Long> retrieveLocationIdsForSpecifiedSemanticPlace(String semanticPlace) {
        ArrayList<Long> result = new ArrayList<>();
        String sql = "SELECT location_id FROM `location` WHERE semantic_place = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, semanticPlace);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                long locationId = rs.getLong("location_id");
                result.add(locationId);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LocationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return result;
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

    public ArrayList<Location> retrieveAllLocations() {
        ArrayList<Location> locationArrList = new ArrayList<>();
        try {
            String sql = "select * from location";
            

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
        } catch (SQLException ex) {
            Logger.getLogger(LocationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return locationArrList;
        }
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

    public String retrieveSemanticPlace(long locationId) throws SQLException {
        String semanticPlace = "";
        String sql = "select semantic_place from location where location_id = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, locationId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                semanticPlace = rs.getString("semantic_place");
            }
        } catch (SQLException ex) {
            Logger.getLogger(LocationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return semanticPlace;
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
