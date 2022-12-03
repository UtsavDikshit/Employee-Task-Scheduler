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

public final class ProjectManager implements TableDetails {

    //FIELDS
    private String id, name, password, email, department_id;

    //CONSTRUCTORS
    public ProjectManager() {
    }

    public ProjectManager(String id, String name, String password, String email, String department_id) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.department_id = department_id;
    }
    
    public ProjectManager(String id) {
        this.id = id;
        loadProjectManager(id);
    }

    //INSTANCE METHODS
    public void loadProjectManager(String id) {
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();
            String QUERY = "select * from " + TABLE_PROJECT_MANAGER + " where " + PM_ID + " = \"" + id + "\"";
            ResultSet rs = statement.executeQuery(QUERY);
            while (rs.next()) {
                setId(rs.getString(rs.findColumn(PM_ID)));
                setName(rs.getString(rs.findColumn(PM_NAME)));
                setDepartment_id(rs.getString(rs.findColumn(PM_DEPT_ID)));
                setPassword(rs.getString(rs.findColumn(PM_PASSWORD)));
                setEmail(rs.getString(rs.findColumn(PM_EMAIL)));
            }
            rs.close();
        } catch (SQLException ex) {
            ExceptionHandler.exception(ex);
        }
    }

    //STATIC METHODS
    
    public static int getNextProjectManagerNumber() {
        int number = 0;
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();
            String QUERY = "select count(*) from " + TABLE_PROJECT_MANAGER;
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

    public static List<ProjectManager> get(String QUERY) {
        List<ProjectManager> list = new ArrayList<>();

        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(QUERY);
            while (rs.next()) {
                ProjectManager pm = new ProjectManager();
                pm.setId(rs.getString(rs.findColumn(PM_ID)));
                pm.setName(rs.getString(rs.findColumn(PM_NAME)));
                pm.setDepartment_id(rs.getString(rs.findColumn(PM_DEPT_ID)));
                pm.setEmail(rs.getString(rs.findColumn(PM_EMAIL)));
                pm.setPassword(rs.getString(rs.findColumn(PM_PASSWORD)));
                list.add(pm);
            }
            rs.close();
        } catch (SQLException ex) {
            ExceptionHandler.exception(ex);
        }
        return list;
    }

    public static List<ProjectManager> getAll() {
        String QUERY = "select * from " + TABLE_PROJECT_MANAGER;
        return get(QUERY);
    }

    public static List<ProjectManager> getAllByDepartment(String id) {
        String QUERY = "select * from " + TABLE_PROJECT_MANAGER + " where " + PM_DEPT_ID + " = \"" + id + "\"";
        return get(QUERY);
    }

    public static void saveProjectManager(ProjectManager manager) throws SQLException {
        try {

            String QUERY = " insert into " + TABLE_PROJECT_MANAGER
                    + " (" + PM_ID + ","
                    + " " + PM_NAME + ","
                    + " " + PM_DEPT_ID + ","
                    + " " + PM_EMAIL + ","
                    + " " + PM_PASSWORD + ") "
                    + " values (?, ?, ?, ?, ?)";

            Connection connection = Database.getConnection();
            PreparedStatement preparedStmt = connection.prepareStatement(QUERY);

            preparedStmt.setString(1, manager.getId());
            preparedStmt.setString(2, manager.getName());
            preparedStmt.setString(3, manager.getDepartment_id());
            preparedStmt.setString(4, manager.getEmail());
            preparedStmt.setString(5, manager.getPassword());

            preparedStmt.execute();
            Database.commmit();
        } catch (SQLException ex) {
            ExceptionHandler.exception(ex);
            throw ex;
        }
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

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }
}
