package ets.db;

import ets.listeners.Refreshable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Database extends Credentials {

    public static int CALLS = 0;
    private static Connection connection = null;

    public static Connection getConnection() {

        if (connection != null) {
            CALLS++;
            return connection;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            connection.setAutoCommit(false);
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Database INIT Error : " + ex.getMessage());
            System.exit(0);
        }
        CALLS++;
        return connection;
    }

    public static void commmit() throws SQLException{
        if (connection != null) {
            connection.commit();
            Refreshable.invoke();
        }
    }

    public static boolean isConnectionActive() {
        try {
            if (connection == null) {
                return false;
            } else if (connection.isClosed()) {
                return false;
            }
            return true;
        } catch (SQLException ignore) {
            return false;
        }
    }

    public static void setConnection(Connection connection) {
        Database.connection = connection;
    }

}
