/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is203.se.Utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author suman
 */
public class DatabaseUtility {
    
    private static Connection conn = null;
    
    public static void clearAllData() {

        Connection conn = null;
        PreparedStatement stmtLocation = null;
        PreparedStatement stmtUser = null;
        PreparedStatement stmtLocationReport = null;

        try {
            conn = ConnectionManager.getConnection();

            //Drop all tables
            stmtLocation = conn.prepareStatement("truncate location;");
            stmtUser = conn.prepareStatement("truncate user;");
            stmtLocationReport = conn.prepareStatement("truncate location_report;");

            stmtLocation.executeUpdate();
            stmtUser.executeUpdate();
            stmtLocationReport.executeUpdate();

            stmtLocationReport.close();
            stmtUser.close();
            stmtLocation.close();

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseUtility.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionManager.close(conn); // Do we need to close connection here?

        }
    }
    
    public static void closeConnection(){
        ConnectionManager.close(conn);
    }
    
    public static void revertAction() throws SQLException{
        conn.rollback();
    }
    
    public static void commitAction() throws SQLException{
        conn.commit();
    }
}
