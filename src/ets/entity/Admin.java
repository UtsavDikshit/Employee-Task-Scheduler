
package ets.entity;

import ets.db.Database;
import ets.error.ExceptionHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class Admin implements TableDetails {

    //FIELDS
    
    private String id, name, password, email;

    //CONSTRUCTORS
    
    public Admin() {
    }

    public Admin(String id, String name, String password, String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
    }
    
    public Admin(String id) {
        this.id = id;
        loadAdmin(id);
    }

    //INSTANCE METHODS
    
    public void loadAdmin(String id) {
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();
            String QUERY = "select * from " + TABLE_ADMIN + " where " + A_ID + " = \"" + id + "\"";
            ResultSet rs = statement.executeQuery(QUERY);
            while (rs.next()) {
                setId(rs.getString(rs.findColumn(A_ID)));
                setName(rs.getString(rs.findColumn(A_NAME)));
                setPassword(rs.getString(rs.findColumn(A_PASSWORD)));
                setEmail(rs.getString(rs.findColumn(A_EMAIL)));
            }
            rs.close();
        } catch (SQLException ex) {
            ExceptionHandler.exception(ex);
        }
    }

    //STATIC METHODS
    
    public static boolean checkLogin(String id, String password) {
        Admin admin = new Admin(id);
        if (admin.getId() != null && admin.getId().equalsIgnoreCase(id)) {
            if (admin.getPassword() != null && admin.getPassword().equalsIgnoreCase(password)) {
                return true;
            }
        }
        return false;
    }

    //GETTERS SETTERS
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
