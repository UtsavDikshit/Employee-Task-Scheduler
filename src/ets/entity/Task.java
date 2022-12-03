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

public final class Task implements TableDetails {

    //CONSTANTS
    
    public static final String STATUS_UNASSIGNED = "Unassigned";
    public static final String STATUS_ASSIGNED_UNCOMPLETE = "Assigned, Not Completed";
    public static final String STATUS_COMPLETED = "Completed";
    public static final String STATUS_UNDER_REVIEW = "Under Review";

    //FIELDS
    
    private String id, head, desc, work, status, empid, department_id;
    private long score, entry, exit, submit, assigned;

    //CONSTRUCTORS
    
    public Task() {
    }

    public Task(String id, String head, String desc, String work, String status, String empid, String department_id, long score, long entry, long exit, long submit, long assigned) {
        this.id = id;
        this.head = head;
        this.desc = desc;
        this.work = work;
        this.status = status;
        this.empid = empid;
        this.department_id = department_id;
        this.score = score;
        this.entry = entry;
        this.exit = exit;
        this.submit = submit;
        this.assigned = assigned;
    }

    public Task(String id) {
        this.id = id;
        loadTask(id);
    }

    //INSTANCE METHODS
    
    public void update() throws SQLException {
        Connection connection = Database.getConnection();
        Statement stmt = connection.createStatement();
        
        String QUERY = "update " + TABLE_TASK + " set "
                + T_HEAD + " = \"" + getHead() + "\", "
                + T_DESC + " = \"" + getDesc() + "\", "
                + T_WORK + " = \"" + getWork() + "\", "
                + T_DEPT_ID + " = \"" + getDepartment_id()+ "\", "
                + T_STATUS + " = \"" + getStatus() + "\", "
                + T_EMP_ID + " = \"" + getEmpid() + "\", "
                + T_ENTRY + " = " + getEntry() + ", "
                + T_EXIT + " = " + getExit() + ", "
                + T_SUBMIT_STAMP + " = " + getSubmit() + ", "
                + T_ASSIGNED_STAMP + " = " + getAssigned()+ ", "
                + T_SCORE + " = " + getScore() + " where " + T_ID + " = \"" + getId() + "\"";

        stmt.executeUpdate(QUERY);
        Database.commmit();
    }
    
    public void loadTask(String id) {

        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();
            String QUERY = "select * from " + TABLE_TASK + " where " + T_ID + " = \"" + id + "\"";
            ResultSet rs = statement.executeQuery(QUERY);
            while (rs.next()) {
                setId(rs.getString(rs.findColumn(T_ID)));
                setHead(rs.getString(rs.findColumn(T_HEAD)));
                setDesc(rs.getString(rs.findColumn(T_DESC)));
                setWork(rs.getString(rs.findColumn(T_WORK)));
                setDepartment_id(rs.getString(rs.findColumn(T_DEPT_ID)));
                setStatus(rs.getString(rs.findColumn(T_STATUS)));
                setEmpid(rs.getString(rs.findColumn(T_EMP_ID)));
                setScore(rs.getLong(rs.findColumn(T_SCORE)));
                setEntry(rs.getLong(rs.findColumn(T_ENTRY)));
                setExit(rs.getLong(rs.findColumn(T_EXIT)));
                setAssigned(rs.getLong(rs.findColumn(T_ASSIGNED_STAMP)));
                setSubmit(rs.getLong(rs.findColumn(T_SUBMIT_STAMP)));
            }
            rs.close();
        } catch (SQLException ex) {
            ExceptionHandler.exception(ex);
        }
    }
    
    //STATIC METHODS
    
    public static int getNextTaskNumber() {
        int number = 0;
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();
            String QUERY = "select count(*) from " + TABLE_TASK;
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

    public static void saveTask(Task task) throws SQLException {
        try {

            String QUERY = " insert into " + TABLE_TASK
                    + " (" + T_ID + ","
                    + " " + T_HEAD + ","
                    + " " + T_DESC + ","
                    + " " + T_WORK + ","
                    + " " + T_DEPT_ID + ","
                    + " " + T_STATUS + ","
                    + " " + T_EMP_ID + ","
                    + " " + T_ENTRY + ","
                    + " " + T_EXIT + ","
                    + " " + T_ASSIGNED_STAMP + ","
                    + " " + T_SUBMIT_STAMP + ","
                    + " " + T_SCORE + ") "
                    + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            Connection connection = Database.getConnection();
            PreparedStatement preparedStmt = connection.prepareStatement(QUERY);

            preparedStmt.setString(1, task.getId());
            preparedStmt.setString(2, task.getHead());
            preparedStmt.setString(3, task.getDesc());
            preparedStmt.setString(4, task.getWork());
            preparedStmt.setString(5, task.getDepartment_id());
            preparedStmt.setString(6, task.getStatus());
            preparedStmt.setString(7, task.getEmpid());
            preparedStmt.setLong(8, task.getEntry());
            preparedStmt.setLong(9, task.getExit());
            preparedStmt.setLong(10, task.getAssigned());
            preparedStmt.setLong(11, task.getSubmit());
            preparedStmt.setLong(12, task.getScore());

            preparedStmt.execute();

            Database.commmit();
        } catch (SQLException ex) {
            ExceptionHandler.exception(ex);
            throw ex;
        }
    }

    public static List<Task> get(String QUERY) {

        List<Task> list = new ArrayList<>();

        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(QUERY);
            while (rs.next()) {
                Task t = new Task();
                t.setId(rs.getString(rs.findColumn(T_ID)));
                t.setHead(rs.getString(rs.findColumn(T_HEAD)));
                t.setDesc(rs.getString(rs.findColumn(T_DESC)));
                t.setWork(rs.getString(rs.findColumn(T_WORK)));
                t.setDepartment_id(rs.getString(rs.findColumn(T_DEPT_ID)));
                t.setStatus(rs.getString(rs.findColumn(T_STATUS)));
                t.setEmpid(rs.getString(rs.findColumn(T_EMP_ID)));
                t.setEntry(rs.getLong(rs.findColumn(T_ENTRY)));
                t.setExit(rs.getLong(rs.findColumn(T_EXIT)));
                t.setAssigned(rs.getLong(rs.findColumn(T_ASSIGNED_STAMP)));
                t.setSubmit(rs.getLong(rs.findColumn(T_SUBMIT_STAMP)));
                t.setScore(rs.getLong(rs.findColumn(T_SCORE)));
                list.add(t);
            }
            rs.close();
        } catch (SQLException ex) {
            ExceptionHandler.exception(ex);
        }
        return list;

    }

    public static List<Task> getAll() {
        String QUERY = "Select * from " + TABLE_TASK;
        return get(QUERY);
    }

    public static List<Task> getAllByEmployee(String id) {
        String QUERY = "Select * from " + TABLE_TASK + " where " + T_EMP_ID + " = \"" + id + "\"";
        return get(QUERY);
    }
    
    public static List<Task> getAllByStatus(String status){
        String QUERY = "Select * from " + TABLE_TASK + " where " + T_STATUS + " = \"" + status + "\"";
        return get(QUERY);
    }
    
    public static List<Task> getAllByDepartment(String department){
        String QUERY = "Select * from " + TABLE_TASK + " where " + T_DEPT_ID + " = \"" + department + "\"";
        return get(QUERY);
    }

    public static List<Task> getAllByEmployeeStatus(String employee, String Status) {
        String QUERY = "Select * from " + TABLE_TASK + " where " + T_EMP_ID + " = \"" + employee + "\" && " + T_STATUS + " = \"" + Status + "\"";
        return get(QUERY);
    }

    public static List<Task> getAllByDepartmentStatus(String department, String Status) {
        String QUERY = "Select * from " + TABLE_TASK + " where " + T_DEPT_ID + " = \"" + department + "\" && " + T_STATUS + " = \"" + Status + "\"";
        return get(QUERY);
    }

    //GETTERS SETTERS
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public long getEntry() {
        return entry;
    }

    public void setEntry(long entry) {
        this.entry = entry;
    }

    public long getExit() {
        return exit;
    }

    public void setExit(long exit) {
        this.exit = exit;
    }

    public long getSubmit() {
        return submit;
    }

    public void setSubmit(long submit) {
        this.submit = submit;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public long getAssigned() {
        return assigned;
    }

    public void setAssigned(long assigned) {
        this.assigned = assigned;
    }

    //TO STRING FOR DEBUG
    
    @Override
    public String toString() {
        return "(id = " + id + ')';
    }
    
    //SORTER CLASS
    
    public static class Sorter{
        
        public static final int SORT_ASCENDING = 1;
        public static final int SORT_DESCENDING = -1;

        public static void byDeadline(List<Task> tasks,int order){
            Collections.sort(tasks,new Comparator<Task>(){
                @Override
                public int compare(Task t, Task t1) {
                    return (int) ((t.getExit() - t1.getExit()) * order);
                }  
            });
        }
        
        public static void byEntry(List<Task> tasks,int order){
            Collections.sort(tasks,new Comparator<Task>(){
                @Override
                public int compare(Task t, Task t1) {
                    return (int) ((t.getEntry()- t1.getEntry()) * order);
                }  
            });
        }
        
        public static void bySubmittedStamp(List<Task> tasks,int order){
            Collections.sort(tasks,new Comparator<Task>(){
                @Override
                public int compare(Task t, Task t1) {
                    return (int) ((t.getSubmit()- t1.getSubmit()) * order);
                }  
            });
        }
        
        public static void byAssignedStamp(List<Task> tasks,int order){
            Collections.sort(tasks,new Comparator<Task>(){
                @Override
                public int compare(Task t, Task t1) {
                    return (int) ((t.getAssigned()- t1.getAssigned()) * order);
                }  
            });
        }
        
        public static void byScore(List<Task> tasks,int order){
            Collections.sort(tasks,new Comparator<Task>(){
                @Override
                public int compare(Task t, Task t1) {
                    return (int) ((t.getScore()- t1.getScore()) * order);
                }  
            });
        }
        
    }
}
