/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is203.se.DAO;

import is203.se.Entity.Interval;
import is203.se.Utility.ConnectionManager;
import is203.se.Entity.LocationReport;
import is203.se.Entity.User;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
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

    public void loadDataInfile(String absoluteFilePath) {
        String sql = "LOAD DATA LOCAL INFILE '" + absoluteFilePath + "' INTO TABLE location_report\n"
                + "FIELDS TERMINATED BY ',' \n"
                + "ENCLOSED BY '\"' \n"
                + "LINES TERMINATED BY '\\n';";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(LocationReportDAO.class.getName()).log(Level.SEVERE, null, ex);
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

    public ArrayList<User> retrieveUsersLocationReportBaseOnDateTime(Date dateTime) {
        ArrayList<User> userArrList = new ArrayList<>();
        Interval prevInterval = null;
        Date prevEndDateTime = null;

        String sql1 = "SELECT * FROM user";
        String sql2 = "SELECT * FROM location_report WHERE time_stamp >= ? && time_stamp < ? && mac_address = ? ORDER BY time_stamp ASC";

        try {
            PreparedStatement ps = conn.prepareStatement(sql1);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String macAddress = rs.getString("mac_address");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String email = rs.getString("email");
                char gender = rs.getString("gender").charAt(0);

                User user = new User(macAddress, name, password, email, gender);
                userArrList.add(user);
            }
            for (User user : userArrList) {
                ps = conn.prepareStatement(sql2);
                ps.setTimestamp(1, new Timestamp(dateTime.getTime() - 1000 * 60 * 15));
                ps.setTimestamp(2, new Timestamp(dateTime.getTime()));
                ps.setString(3, user.getMacAddress());
                rs = ps.executeQuery();

                while (rs.next()) {
                    System.out.println(prevEndDateTime + ", " + prevInterval);
                    //if there is no previous start time
                    if (prevEndDateTime == null) {
                        Interval interval = new Interval();
                        long locationId = rs.getLong("location_id");
                        prevEndDateTime = new Date(rs.getTimestamp("time_stamp").getTime());

                        interval.setLocationId(locationId);
                        interval.setStartTime(prevEndDateTime);

                        prevInterval = interval;
                    } // if there is a previous start time
                    else {
                        long endTime = rs.getTimestamp("time_stamp").getTime();
                        Date endDateTime = new Date(endTime);
                        long locationId = rs.getLong("location_id");
                        System.out.println(prevInterval.getLocationId() + ", " + locationId + ", " + (prevInterval.getLocationId() == locationId));

                        if (prevInterval.getLocationId() == locationId) {
                            prevEndDateTime = endDateTime;
                        } else {
                            Date date = new Date(prevEndDateTime.getTime() + 1000);
                            prevInterval.setEndTime(date);
                            user.addInterval(prevInterval);

                            prevInterval = new Interval();

                            prevInterval.setStartTime(prevEndDateTime);
                            prevInterval.setLocationId(locationId);
                        }
                    }
                }
                Date date = new Date(prevEndDateTime.getTime());
                prevInterval.setEndTime(date);
                user.addInterval(prevInterval);

                prevEndDateTime = null;
                prevInterval = null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(LocationReportDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return userArrList;
        }
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

    public ArrayList<LocationReport> retrieveLocationReportsWithin15MinWindow(Date endOf15MinWindow) {
        ArrayList<LocationReport> locationReportArrList = new ArrayList<>();
        String sql = "SELECT * FROM `location_report` WHERE `time_stamp` >= ? AND `time_stamp` < ?";
        Date startOf15MinWindow = new Date(endOf15MinWindow.getTime() - TimeUnit.MINUTES.toMillis(15));
//        DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp startOfWindow = new Timestamp(startOf15MinWindow.getTime());
        Timestamp endOfWindow = new Timestamp(endOf15MinWindow.getTime());

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, startOfWindow);
            ps.setTimestamp(2, endOfWindow);

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
        } finally {
            return locationReportArrList;
        }
    }

    public ArrayList<LocationReport> retrieveLocationReportsForDatetimeAndLocationIds(Date endOf15MinWindow, ArrayList<Long> locationIdList) {
        ArrayList<LocationReport> locationReportArrList = new ArrayList<>();
        String sql = "SELECT * FROM `location_report` WHERE `time_stamp` >= ? AND `time_stamp` < ? AND location_id IN (%s)";
        String sqlFormatted = String.format(sql, preparePlaceHolders(locationIdList.size()));

        Date startOf15MinWindow = new Date(endOf15MinWindow.getTime() - TimeUnit.MINUTES.toMillis(15));
//        DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp startOfWindow = new Timestamp(startOf15MinWindow.getTime());
        Timestamp endOfWindow = new Timestamp(endOf15MinWindow.getTime());
        ArrayList<String> locationIdStringList = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sqlFormatted)) {
            ps.setTimestamp(1, startOfWindow);
            ps.setTimestamp(2, endOfWindow);

            setValues(ps, locationIdList.toArray());

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
        } finally {
            return locationReportArrList;
        }
    }

    public ArrayList<LocationReport> retrieveLatestLocationReportsForDatetimeAndLocationIds(Date endOf15MinWindow, ArrayList<Long> locationIdList) {
        ArrayList<LocationReport> locationReportArrList = new ArrayList<>();
        String sql = "SELECT MAX(time_stamp), mac_address, location_id FROM `location_report` "
                + "WHERE time_stamp >= ? AND time_stamp < ? AND location_id IN (%s) "
                + "GROUP BY mac_address, location_id";

        String oldSql = "SELECT * FROM `location_report` WHERE `time_stamp` >= ? AND `time_stamp` < ? AND location_id IN (%s)";
        String sqlFormatted = String.format(sql, preparePlaceHolders(locationIdList.size()));

        Date startOf15MinWindow = new Date(endOf15MinWindow.getTime() - TimeUnit.MINUTES.toMillis(15));
//        DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp startOfWindow = new Timestamp(startOf15MinWindow.getTime());
        Timestamp endOfWindow = new Timestamp(endOf15MinWindow.getTime());
        ArrayList<String> locationIdStringList = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sqlFormatted)) {
            ps.setTimestamp(1, startOfWindow);
            ps.setTimestamp(2, endOfWindow);

            setValues(ps, locationIdList.toArray());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                long locationId = rs.getLong("location_id");
                String macAddress = rs.getString("mac_address");
                Timestamp timestamp = rs.getTimestamp("MAX(time_stamp)");
                LocationReport locationReport = new LocationReport(locationId, macAddress, timestamp);
                locationReportArrList.add(locationReport);
            }

        } catch (SQLException ex) {
            Logger.getLogger(LocationReportDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return locationReportArrList;
        }
    }

    public ArrayList<LocationReport> retrieveLocationReportsForDatetimeAndMacAddresses(Date endOf15MinWindow, ArrayList<String> macAddressList) {
        ArrayList<LocationReport> locationReportArrList = new ArrayList<>();
        String sql = "SELECT * FROM `location_report` WHERE `time_stamp` >= ? AND `time_stamp` < ? AND mac_address IN (%s) ORDER BY mac_address ASC, time_stamp ASC";
        String sqlFormatted = String.format(sql, preparePlaceHolders(macAddressList.size()));

        Date startOf15MinWindow = new Date(endOf15MinWindow.getTime() - TimeUnit.MINUTES.toMillis(15));
//        DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp startOfWindow = new Timestamp(startOf15MinWindow.getTime());
        Timestamp endOfWindow = new Timestamp(endOf15MinWindow.getTime());
        ArrayList<String> locationIdStringList = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sqlFormatted)) {
            ps.setTimestamp(1, startOfWindow);
            ps.setTimestamp(2, endOfWindow);

            setValues(ps, macAddressList.toArray());

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
        } finally {
            return locationReportArrList;
        }
    }

    private static String preparePlaceHolders(int length) {
        return String.join(",", Collections.nCopies(length, "?"));
    }

    private static void setValues(PreparedStatement preparedStatement, Object... values) throws SQLException {
        for (int i = 0; i < values.length; i++) {
            preparedStatement.setObject(i + 3, values[i]);
        }
    }

    public ArrayList<String> retrieveMacAddressesAt(int floor, Date endOf15MinWindow) throws SQLException {
        String sql = "Select distinct mac_address from location_report \n"
                + "where location_id in (select location_id from location where semantic_place like ?)\n"
                + "and time_stamp >= ?  AND time_stamp < ?";
        
        ArrayList<String> macAddressList = new ArrayList<>();
        String floorString = String.valueOf(floor);
        Date startOf15MinWindow = new Date(endOf15MinWindow.getTime() - TimeUnit.MINUTES.toMillis(15));
        Timestamp startOfWindow = new Timestamp(startOf15MinWindow.getTime());
        Timestamp endOfWindow = new Timestamp(endOf15MinWindow.getTime());

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (floor == 0) {
                ps.setString(1, "%B1%");
            } else {
                ps.setString(1, "%L" + floorString + "%");
            }
            ps.setTimestamp(2, startOfWindow);
            ps.setTimestamp(3, endOfWindow);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String macAddress = rs.getString("mac_address");
                macAddressList.add(macAddress);
            }

        } catch (SQLException ex) {
            Logger.getLogger(LocationReportDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return macAddressList;
        }
    }
    
    public long retrieveLatestLocationIdOf(String macAddress, int floor, Date endOf15MinWindow) throws SQLException {
        String sql = "select location_id from location_report where mac_address = ?\n" +
"and location_id in (select location_id from location where semantic_place like ?)\n" +
"and time_stamp >= ? AND time_stamp < ?\n" +
"order by time_stamp DESC\n" +
"limit 1";
        
        long locationId = 0;
        String floorString = String.valueOf(floor);
        Date startOf15MinWindow = new Date(endOf15MinWindow.getTime() - TimeUnit.MINUTES.toMillis(15));
        Timestamp startOfWindow = new Timestamp(startOf15MinWindow.getTime());
        Timestamp endOfWindow = new Timestamp(endOf15MinWindow.getTime());

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, macAddress);
            if (floor == 0) {
                ps.setString(2, "%B1%");
            } else {
                ps.setString(2, "%L" + floorString + "%");
            }
            ps.setTimestamp(3, startOfWindow);
            ps.setTimestamp(4, endOfWindow);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                locationId = rs.getLong("location_id");
            }

        } catch (SQLException ex) {
            Logger.getLogger(LocationReportDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return locationId;
        }
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
