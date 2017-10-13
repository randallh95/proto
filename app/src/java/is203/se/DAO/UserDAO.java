/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is203.se.DAO;

import is203.se.Utility.ConnectionManager;
import is203.se.Entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Randall Heng, Ashley Tan
 */
public class UserDAO {

    Connection conn;

    public UserDAO() {

        try {
            conn = ConnectionManager.getConnection();
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void loadDataInfile(String absoluteFilePath) {
        String sql = "LOAD DATA LOCAL INFILE '" + absoluteFilePath + "' INTO TABLE user\n"
                + "FIELDS TERMINATED BY ',' \n"
                + "ENCLOSED BY '\"' \n"
                + "LINES TERMINATED BY '\\n';";
        try (Statement stmt  = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(LocationReportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public User retrieve(String username) throws SQLException{
        User user = null;
        String sql = "SELECT mac_address, name, password, email, gender FROM user WHERE email = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String macAddress = rs.getString(1);
            String name = rs.getString(2);
            String password = rs.getString(3);
            String email = rs.getString(4);
            char gender = rs.getString(5).charAt(0);
            user = new User(macAddress, name, password, email, gender);
        }

        return user;
    }

    public int[] insertUsers(ArrayList<String[]> validUserList) {

        String sql = "insert into user values(?, ?, ?, ?, ?)";
        int[] result = null;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (String[] validUser : validUserList) {
                ps.setString(1, validUser[0]);
                ps.setString(2, validUser[1]);
                ps.setString(3, validUser[2]);
                ps.setString(4, validUser[3]);
                ps.setString(5, validUser[4]);
                
                ps.addBatch();
            }
            result = ps.executeBatch();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;

    }

    public ArrayList<User> retrieveAllUsers() throws SQLException {
        String sql = "select * from user";
        ArrayList<User> userArrList = new ArrayList<>();;

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String macAddress = rs.getString("mac_address");
            String name = rs.getString("name");
            String password = rs.getString("password");
            String email = rs.getString("email");
            String gender = rs.getString("gender");
            User user = new User(macAddress, name, password, email, gender.charAt(0));
            userArrList.add(user);
        }
        ps.close();
        return userArrList;
    }

    public int updateUsers(String macAddress, String name, String password, String email, String gender) throws SQLException {
        String sql = "update user set name = ?, password = ?, email = ?, gender = ? where mac_address = ?";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, name);
        ps.setString(2, password);
        ps.setString(3, email);
        ps.setString(4, gender);
        ps.setString(5, macAddress);
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
