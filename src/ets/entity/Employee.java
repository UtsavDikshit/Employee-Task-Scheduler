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
import java.util.Collections;
import java.util.Comparator;

public final class Employee implements TableDetails {

    //CONSTANTS
    public static final String STATUS_WORKING = "Working";
    public static final String STATUS_IDLE = "Idle";
    public static final String SORT_ASCENDING = "ASC";
    public static final String SORT_DESCENDING = "DESC";

    //FIELDS
    private String id, name, email, password, status, department_id, current_task_id, attendance;
    private long score, stamp, last_task_stamp;

    //CONSTRUCTORS
    public Employee() {
    }

    public Employee(String id, String name, String email, String password, String status, String department_id, String current_task_id, long score, long stamp, long last_task_stamp) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.status = status;
        this.department_id = department_id;
        this.current_task_id = current_task_id;
        this.score = score;
        this.stamp = stamp;
        this.last_task_stamp = last_task_stamp;
    }

    public Employee(String id) {
        this.id = id;
        loadEmployee(id);
    }

    //INSTANCE METHODS
    public void update() throws SQLException {
        Connection connection = Database.getConnection();
        Statement stmt = connection.createStatement();

        String QUERY = "update " + TABLE_EMPLOYEE + " set "
                + E_NAME + " = \"" + getName() + "\", "
                + E_EMAIL + " = \"" + getEmail() + "\", "
                + E_DEPT_ID + " = \"" + getDepartment_id() + "\", "
                + E_CURRENT_TASK_ID + " = \"" + getCurrent_task_id() + "\", "
                + E_PASSWORD + " = \"" + getPassword() + "\", "
                + E_SCORE + " = " + getScore() + ", "
                + E_STATUS + " = \"" + getStatus() + "\", "
                + E_LAST_TASK_STAMP + " = " + getLast_task_stamp() + ", "
                + E_STAMP + " = " + getStamp() + " where " + E_ID + " = \"" + getId() + "\"";

        stmt.executeUpdate(QUERY);
        Database.commmit();
    }

    public void loadEmployee(String id) {

        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();
            String QUERY = "select * from " + TABLE_EMPLOYEE + " where " + E_ID + " = \"" + id + "\"";
            ResultSet rs = statement.executeQuery(QUERY);
            while (rs.next()) {
                setId(rs.getString(rs.findColumn(E_ID)));
                setName(rs.getString(rs.findColumn(E_NAME)));
                setEmail(rs.getString(rs.findColumn(E_EMAIL)));
                setDepartment_id(rs.getString(rs.findColumn(E_DEPT_ID)));
                setCurrent_task_id(rs.getString(rs.findColumn(E_CURRENT_TASK_ID)));
                setPassword(rs.getString(rs.findColumn(E_PASSWORD)));
                setScore(rs.getLong(rs.findColumn(E_SCORE)));
                setStatus(rs.getString(rs.findColumn(E_STATUS)));
                setLast_task_stamp(rs.getLong(rs.findColumn(E_LAST_TASK_STAMP)));
                setStamp(rs.getLong(rs.findColumn(E_STAMP)));
            }
            rs.close();
        } catch (SQLException ex) {
            ExceptionHandler.exception(ex);
        }
    }

    //STATIC METHODS
    public static int getNextEmployeeNumber() {
        int number = 0;
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();
            String QUERY = "select count(*) from " + TABLE_EMPLOYEE;
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

    public static void saveEmployee(Employee employee) throws SQLException {
        try {

            String QUERY = " insert into " + TABLE_EMPLOYEE
                    + " (" + E_ID + ","
                    + " " + E_NAME + ","
                    + " " + E_EMAIL + ","
                    + " " + E_DEPT_ID + ","
                    + " " + E_CURRENT_TASK_ID + ","
                    + " " + E_PASSWORD + ","
                    + " " + E_SCORE + ","
                    + " " + E_STATUS + ","
                    + " " + E_LAST_TASK_STAMP + ","
                    + " " + E_STAMP + ") "
                    + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            Connection connection = Database.getConnection();
            PreparedStatement preparedStmt = connection.prepareStatement(QUERY);

            preparedStmt.setString(1, employee.getId());
            preparedStmt.setString(2, employee.getName());
            preparedStmt.setString(3, employee.getEmail());
            preparedStmt.setString(4, employee.getDepartment_id());
            preparedStmt.setString(5, employee.getCurrent_task_id());
            preparedStmt.setString(6, employee.getPassword());
            preparedStmt.setLong(7, employee.getScore());
            preparedStmt.setString(8, employee.getStatus());
            preparedStmt.setLong(9, employee.getLast_task_stamp());
            preparedStmt.setLong(10, employee.getStamp());

            preparedStmt.execute();
            
            Database.commmit();

        } catch (SQLException ex) {
            ExceptionHandler.exception(ex);
            throw ex;
        }
    }

    public static List<Employee> get(String QUERY) {
        List<Employee> list = new ArrayList<>();
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(QUERY);
            while (rs.next()) {
                Employee e = new Employee();
                e.setId(rs.getString(rs.findColumn(E_ID)));
                e.setName(rs.getString(rs.findColumn(E_NAME)));
                e.setEmail(rs.getString(rs.findColumn(E_EMAIL)));
                e.setDepartment_id(rs.getString(rs.findColumn(E_DEPT_ID)));
                e.setCurrent_task_id(rs.getString(rs.findColumn(E_CURRENT_TASK_ID)));
                e.setPassword(rs.getString(rs.findColumn(E_PASSWORD)));
                e.setScore(rs.getLong(rs.findColumn(E_SCORE)));
                e.setStatus(rs.getString(rs.findColumn(E_STATUS)));
                e.setLast_task_stamp(rs.getLong(rs.findColumn(E_LAST_TASK_STAMP)));
                e.setStamp(rs.getLong(rs.findColumn(E_STAMP)));
                list.add(e);
            }
            rs.close();
        } catch (SQLException ex) {
            ExceptionHandler.exception(ex);
        }
        return list;
    }

    public static List<Employee> getAllEmployees() {
        String QUERY = "select * from " + TABLE_EMPLOYEE;
        return get(QUERY);
    }

    public static List<Employee> getAllByStatus(String status) {
        String QUERY = "select * from " + TABLE_EMPLOYEE + " where " + E_STATUS + " = \"" + status + "\"";
        return get(QUERY);
    }

    public static List<Employee> getAllByDepartment(String deptID) {
        String QUERY = "select * from " + TABLE_EMPLOYEE + " where " + E_DEPT_ID + " = \"" + deptID + "\"";
        return get(QUERY);
    }

    public static List<Employee> getAllByLastTaskStamp(String order) {
        String QUERY = "select * from " + TABLE_EMPLOYEE + " order by " + E_LAST_TASK_STAMP + " " + order;
        return get(QUERY);
    }

    public static List<Employee> getAllByScore(String order) {
        String QUERY = "select * from " + TABLE_EMPLOYEE + " order by " + E_SCORE + " " + order;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public long getStamp() {
        return stamp;
    }

    public void setStamp(long stamp) {
        this.stamp = stamp;
    }

    public long getLast_task_stamp() {
        return last_task_stamp;
    }

    public void setLast_task_stamp(long last_task_stamp) {
        this.last_task_stamp = last_task_stamp;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getCurrent_task_id() {
        return current_task_id;
    }

    public void setCurrent_task_id(String current_task_id) {
        this.current_task_id = current_task_id;
    }

    public String getAttendance() {
        return attendance == null ? Attendance.STATUS_ABSENT : attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    //SPECIAL FIELDS VALUES
    public long getEmployeeScoreRank(){
        String QUERY = "select * from " + TABLE_EMPLOYEE + " where " + E_DEPT_ID + " = \"" + getDepartment_id() + "\" order by " + E_SCORE + " DESC";
        List<Employee> employees = get(QUERY);
        int rank = 1;
        for(Employee temp : employees){
             if(temp.getId().equals(getId())){
                 return rank;
             }
             rank++;
        }
        return -1;
    }
    
    //TO STRING FOR DEBUG
    @Override
    public String toString() {
        return "(id = " + id + ')';
    }

    //SORTER CLASS
    public static class Sorter {

        public static final int SORT_ASCENDING = 1;
        public static final int SORT_DESCENDING = -1;

        public static void byScore(List<Employee> employees, int order) {
            Collections.sort(employees, new Comparator<Employee>() {
                @Override
                public int compare(Employee t, Employee t1) {
                    return (int) ((t.getScore() - t1.getScore()) * order);
                }
            });
        }

        public static void byLastTaskStamp(List<Employee> employees, int order) {
            Collections.sort(employees, new Comparator<Employee>() {
                @Override
                public int compare(Employee t, Employee t1) {
                    return (int) ((t.getLast_task_stamp() - t1.getLast_task_stamp()) * order);
                }
            });
        }

    }

}
