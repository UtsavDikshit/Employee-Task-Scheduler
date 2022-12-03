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
import java.util.List;

public class Notification implements TableDetails {

    //CONSTANTS
    public static final String TYPE_TASK_ASSIGNED = "Task Assigned";
    public static final String TYPE_TASK_SUBMITTED = "Task Submitted";
    public static final String TYPE_TASK_EVALUATION = "Task Evaluated";

    //FIELDS
    private String empid, type, description, remarks;
    private long stamp;

    //CONSTRUCTORS
    public Notification(String empid, String type, String description, String remarks, long stamp) {
        this.empid = empid;
        this.type = type;
        this.description = description;
        this.remarks = remarks;
        this.stamp = stamp;
    }

    public Notification() {
    }

    public static void saveNotification(Notification notification) {
        try {

            String QUERY = " insert into " + TABLE_NOTIFICATION
                    + " (" + N_EMP_ID + ","
                    + " " + N_TYPE + ","
                    + " " + N_DESCRIPTION + ","
                    + " " + N_REMARKS + ","
                    + " " + N_STAMP + ") "
                    + " values (?, ?, ?, ?, ?)";

            Connection connection = Database.getConnection();
            PreparedStatement preparedStmt = connection.prepareStatement(QUERY);

            preparedStmt.setString(1, notification.getEmpid());
            preparedStmt.setString(2, notification.getType());
            preparedStmt.setString(3, notification.getDescription());
            preparedStmt.setString(4, notification.getRemarks());
            preparedStmt.setLong(5, notification.getStamp());

            preparedStmt.execute();

            Database.commmit();
        } catch (SQLException ex) {
            ExceptionHandler.exception(ex);
        }
    }

    public static List<Notification> get(String QUERY) {

        List<Notification> notifications = new ArrayList<>();

        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(QUERY);
            while (rs.next()) {
                Notification notification = new Notification();
                notification.setEmpid(rs.getString(rs.findColumn(N_EMP_ID)));
                notification.setType(rs.getString(rs.findColumn(N_TYPE)));
                notification.setDescription(rs.getString(rs.findColumn(N_DESCRIPTION)));
                notification.setRemarks(rs.getString(rs.findColumn(N_REMARKS)));
                notification.setStamp(rs.getLong(rs.findColumn(N_STAMP)));
                notifications.add(notification);
            }
            rs.close();
        } catch (SQLException ex) {
            ExceptionHandler.exception(ex);
        }
        return notifications;

    }

    public static List<Notification> getForEmployee(String empid) {
        String query = "select * from " + TABLE_NOTIFICATION + " where " + N_EMP_ID + " = \"" + empid + "\"";
        return get(query);
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public long getStamp() {
        return stamp;
    }

    public void setStamp(long stamp) {
        this.stamp = stamp;
    }

    //SORTER CLASS
    public static class Sorter {

        public static final int SORT_ASCENDING = 1;
        public static final int SORT_DESCENDING = -1;

        public static void byStamp(List<Notification> notifications, int order) {
            Collections.sort(notifications, new Comparator<Notification>() {
                @Override
                public int compare(Notification t, Notification t1) {
                    return (int) ((t.getStamp() - t1.getStamp()) * order);
                }
            });
        }
    }
}
