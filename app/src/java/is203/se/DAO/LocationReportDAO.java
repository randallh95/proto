/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is203.se.DAO;

import is203.se.Utility.ConnectionManager;
import is203.se.Entity.LocationReport;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bernice
 */
public class LocationReportDAO {

    Connection conn;

    public LocationReportDAO() {
        try {
            conn = ConnectionManager.getConnection();
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int[] insertLocationReports(ArrayList<String[]> validLocationList) {
        String sql = "insert into location_report values(?, ?, ?)";
        int[] result = null;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (String[] validLocation : validLocationList) {

                DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = (Date) formater.parse(validLocation[0]);
                Timestamp timestamp = new Timestamp(date.getTime());

                ps.setTimestamp(1, timestamp);
                ps.setString(2, validLocation[1]);
                ps.setString(3, validLocation[2]);

                ps.addBatch();
            }
            result = ps.executeBatch();
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(LocationReportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public ArrayList<LocationReport> retrieveAllLocationReports() {
        ArrayList<LocationReport> locationReportArrList = new ArrayList<>();
        String sql = "select * from location_report";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                long locationId = rs.getLong("location_id");
                String macAddress = rs.getString("mac_address");
                Timestamp timestamp = rs.getTimestamp("time_stamp");
                LocationReport locationReport = new LocationReport(locationId, macAddress, timestamp);
                locationReportArrList.add(locationReport);
            }

        } catch (SQLException ex) {
            Logger.getLogger(LocationReportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return locationReportArrList;
    }

    public int retrieveNumberOfLocationReports(long locationId, Date dateTime) throws SQLException {
        int numOfLocationReports = 0;
        Timestamp timestamp = new Timestamp(dateTime.getTime());

        String sql = "select count(*) from location_report where location_id = ? and time_stamp = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "" + locationId);
        ps.setString(2, "" + timestamp);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            numOfLocationReports = rs.getInt("count(*)");
        }

        return numOfLocationReports;
    }

    public HashMap<String, Integer> retrieveLocationReportByDateAndRank(Date dateTime, int k) throws SQLException {

        HashMap<String, Integer> semanticMap = new HashMap<>();
        Timestamp timestamp = new Timestamp(dateTime.getTime());
        Long dateTime2 = dateTime.getTime() - 15 * 60 * 1000;
        Timestamp timestamp2 = new Timestamp(dateTime2);

        String sql = "SELECT semantic_place, COUNT(lr.location_id) AS num FROM location i, location_report lr WHERE i.location_id = lr.location_id AND lr.time_stamp >= ? AND lr.time_stamp < ? GROUP BY semantic_place ORDER BY num desc LIMIT ";
        sql = sql + "" + k;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "" + timestamp2);
        ps.setString(2, "" + timestamp);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String semanticName = rs.getString(1);
            int count = rs.getInt(2);
            semanticMap.put(semanticName, count);
        }

        return semanticMap;

    }

    public ArrayList<Date> retrieveDatesForUserSelection() throws SQLException {
        ArrayList<Date> availableDateList = new ArrayList<>();
        String sql = "select distinct time_stamp from location_report";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Timestamp timestamp = rs.getTimestamp("time_stamp");
            Date date = new Date(timestamp.getTime());
            availableDateList.add(date);
        }

        return availableDateList;
    }

    public int updateLocationReports(long locationId, String macAddress, Date dateTime) throws SQLException {
        String sql = "update location_report set time_stamp = ? where location_id = ? and mac_address = ?";

        Timestamp timestamp = new Timestamp(dateTime.getTime());

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setTimestamp(1, timestamp);
        ps.setLong(2, locationId);
        ps.setString(3, macAddress);
        int updateNum = ps.executeUpdate();

        ps.close();
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
