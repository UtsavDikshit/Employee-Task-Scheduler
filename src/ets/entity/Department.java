package ets.entity;

import ets.db.Database;
import ets.error.ExceptionHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class Department implements TableDetails {

    //FIELDS
    private String id, name;

    //CONSTRUCTORS
    public Department() {
    }

    public Department(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Department(String id) {
        this.id = id;
        loadDepartment(id);
    }

    //INSTANCE METHODS
    public void update() throws SQLException {
        Connection connection = Database.getConnection();
        Statement stmt = connection.createStatement();

        String QUERY = "update " + TABLE_DEPARTMENT + " set "
                + D_NAME + " = \"" + getName() + "\" "
                + " where " + D_ID + " = \"" + getId() + "\"";

        stmt.executeUpdate(QUERY);
        Database.commmit();
    }

    public void loadDepartment(String id) {
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();
            String QUERY = "select * from " + TABLE_DEPARTMENT + " where " + D_ID + " = \"" + id + "\"";
            ResultSet rs = statement.executeQuery(QUERY);
            while (rs.next()) {
                setId(rs.getString(rs.findColumn(D_ID)));
                setName(rs.getString(rs.findColumn(D_NAME)));
            }
            rs.close();
        } catch (SQLException ex) {
            ExceptionHandler.exception(ex);
        }
    }

    //STATIC METHODS
    public static int getNextDepartmentNumber() {
        int number = 0;
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();
            String QUERY = "select count(*) from " + TABLE_DEPARTMENT;
            ResultSet rs = statement.executeQuery(QUERY);
            while (rs.next()) {
                number = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException ex) {
            ExceptionHandler.exception(ex);
        }
        return number;
    }

    public static void saveDepartment(Department department) throws SQLException {
        try {

            String QUERY = " insert into " + TABLE_DEPARTMENT
                    + " (" + D_ID + ","
                    + " " + D_NAME + ") "
                    + " values (?, ?)";

            Connection connection = Database.getConnection();
            PreparedStatement preparedStmt = connection.prepareStatement(QUERY);

            preparedStmt.setString(1, department.getId());
            preparedStmt.setString(2, department.getName());

            preparedStmt.execute();
            Database.commmit();
        } catch (SQLException ex) {
            ExceptionHandler.exception(ex);
            throw ex;
        }
    }

    public static List<Department> get(String QUERY) {
        List<Department> list = new ArrayList<>();
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(QUERY);
            while (rs.next()) {
                Department d = new Department();
                d.setId(rs.getString(rs.findColumn(D_ID)));
                d.setName(rs.getString(rs.findColumn(D_NAME)));
                list.add(d);
            }
            rs.close();
        } catch (SQLException ex) {
            ExceptionHandler.exception(ex);
        }
        return list;
    }

    public static List<Department> getAll() {
        String QUERY = "select * from " + TABLE_DEPARTMENT;
        return get(QUERY);
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

}
