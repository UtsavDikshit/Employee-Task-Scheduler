package ets.entity;

import ets.db.Database;
import ets.error.ExceptionHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Attendance implements TableDetails {

    //CONSTANTS
    public static final String STATUS_PRESENT = "Present";
    public static final String STATUS_ABSENT = "Absent";

    //FIELDS
    private String date, empid, status;

    //STATIC METHODS
    public static boolean exists(String date, String empID) {
        try {
            String QUERY = "select count(*) from " + TABLE_ATTENDANCE + " where " + AT_DATE + " = \"" + date + "\" && " + AT_EMP_ID + " = \"" + empID + "\"";
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(QUERY);
            if (resultSet != null && resultSet.next()) {
                int temp = resultSet.getInt(1);
                resultSet.close();
                return temp > 0;
            }
        } catch (SQLException ex) {
            ExceptionHandler.exception(ex);
        }
        return false;
    }

    public static void update(Attendance attendance) {
        try {
            Connection connection = Database.getConnection();
            Statement stmt = connection.createStatement();

            String QUERY = "update " + TABLE_ATTENDANCE + " set "
                    + AT_STATUS + " = \"" + attendance.getStatus() + "\" "
                    + " where " + AT_DATE + " = \"" + attendance.getDate() + "\" && " + AT_EMP_ID + " = \"" + attendance.getEmpid() + "\"";

            stmt.executeUpdate(QUERY);
            Database.commmit();
        } catch (SQLException ex) {
            ExceptionHandler.exception(ex);
        }
    }

    public static void add(Attendance attendance) {
        try {

            String QUERY = " insert into " + TABLE_ATTENDANCE
                    + " (" + AT_DATE + ","
                    + " " + AT_EMP_ID + ","
                    + " " + AT_STATUS + ") "
                    + " values (?, ?, ?)";

            Connection connection = Database.getConnection();
            PreparedStatement preparedStmt = connection.prepareStatement(QUERY);

            preparedStmt.setString(1, attendance.getDate());
            preparedStmt.setString(2, attendance.getEmpid());
            preparedStmt.setString(3, attendance.getStatus());

            preparedStmt.execute();
            Database.commmit();
        } catch (SQLException ex) {
            ExceptionHandler.exception(ex);
        }
    }

    public static void addOrUpdate(Attendance attendance) {
        if (exists(attendance.getDate(), attendance.getEmpid())) {
            update(attendance);
        } else {
            add(attendance);
        }
    }

    public static List<Attendance> get(String QUERY) {
        List<Attendance> list = new ArrayList<>();
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(QUERY);
            while (rs.next()) {
                Attendance at = new Attendance();
                at.setDate(rs.getString(rs.findColumn(AT_DATE)));
                at.setEmpid(rs.getString(rs.findColumn(AT_EMP_ID)));
                at.setStatus(rs.getString(rs.findColumn(AT_STATUS)));
                list.add(at);
            }
            rs.close();
        } catch (SQLException ex) {
            ExceptionHandler.exception(ex);
        }
        return list;
    }

    public static List<Attendance> getForDate(String date) {
        String QUERY = "select * from " + TABLE_ATTENDANCE + " where " + AT_DATE + " = \"" + date + "\"";
        return get(QUERY);
    }

    public static List<Attendance> getForEmployeeId(String id) {
        String QUERY = "select * from " + TABLE_ATTENDANCE + " where " + AT_EMP_ID + " = \"" + id + "\"";
        return get(QUERY);
    }

    public static List<Employee> getAllPresentEmployees(String date) {
        String QUERY = "select * from " + TABLE_ATTENDANCE + " where " + AT_DATE + " = \"" + date + "\" && " + AT_STATUS + " = \"" + STATUS_PRESENT + "\"";
        List<Attendance> attendances = get(QUERY);
        List<Employee> employees = new ArrayList<>();
        for (Attendance attendance : attendances) {
            employees.add(new Employee(attendance.getEmpid()));
        }
        return employees;
    }

    //GETTERS SETTERS
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getDateStamp() {
        return toLong(date);
    }

    //SPECIAL METHODS
    public static long toLong(String date) {
        String temp[] = date.split("/");
        Date d = new Date();
        d.setDate(Integer.parseInt(temp[0]));
        d.setMonth(Integer.parseInt(temp[1]) - 1);
        d.setYear(Integer.parseInt(temp[2]) - 1900);
        return d.getTime();
    }
    
    public static String toDateNow(){
        Date d = new Date();
        String ans = d.getDate() + "/" + (d.getMonth() + 1) + "/" + (d.getYear() + 1900);
        return ans;
    }

    //SORTER
    public static class Sorter {

        public static final int SORT_ASCENDING = 1;
        public static final int SORT_DESCENDING = -1;

        public static void byDate(List<Attendance> attendances, int order) {
            Collections.sort(attendances, new Comparator<Attendance>() {
                @Override
                public int compare(Attendance t, Attendance t1) {
                    return (int) ((t.getDateStamp() - t1.getDateStamp()) * order);
                }
            });
        }
    }

}
