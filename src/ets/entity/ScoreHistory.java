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

public final class ScoreHistory implements TableDetails {

    //FIELDS
    private String empID, taskID, description;
    private long currentScore, transaction, stamp;

    //CONSTRUCTORS
    public ScoreHistory() {
    }

    public ScoreHistory(String empID, String taskID, String description, long currentScore, long transaction, long stamp) {
        this.empID = empID;
        this.taskID = taskID;
        this.description = description;
        this.currentScore = currentScore;
        this.transaction = transaction;
        this.stamp = stamp;
    }

    //STATIC METHODS
    public static void saveScoreHistory(ScoreHistory history) throws SQLException {
        try {

            String QUERY = " insert into " + TABLE_SCORE_HISTORY
                    + " (" + SH_EMP_ID + ","
                    + " " + SH_TASK_ID + ","
                    + " " + SH_DESCRIPTION + ","
                    + " " + SH_CURRENT_SCORE + ","
                    + " " + SH_TRANSACTION + ", "
                    + " " + SH_STAMP + ") "
                    + " values (?, ?, ?, ?, ?, ?)";

            Connection connection = Database.getConnection();
            PreparedStatement preparedStmt = connection.prepareStatement(QUERY);

            preparedStmt.setString(1, history.getEmpID());
            preparedStmt.setString(2, history.getTaskID());
            preparedStmt.setString(3, history.getDescription());
            preparedStmt.setLong(4, history.getCurrentScore());
            preparedStmt.setLong(5, history.getTransaction());
            preparedStmt.setLong(6, history.getStamp());
            
            preparedStmt.execute();
            Database.commmit();
        } catch (SQLException ex) {
            ExceptionHandler.exception(ex);
            throw ex;
        }
    }

    public static List<ScoreHistory> get(String QUERY) {
        List<ScoreHistory> list = new ArrayList<>();
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(QUERY);
            while (rs.next()) {
                ScoreHistory sh = new ScoreHistory();
                sh.setEmpID(rs.getString(rs.findColumn(SH_EMP_ID)));
                sh.setTaskID(rs.getString(rs.findColumn(SH_TASK_ID)));
                sh.setDescription(rs.getString(rs.findColumn(SH_DESCRIPTION)));
                sh.setCurrentScore(rs.getLong(rs.findColumn(SH_CURRENT_SCORE)));
                sh.setTransaction(rs.getLong(rs.findColumn(SH_TRANSACTION)));
                sh.setStamp(rs.getLong(rs.findColumn(SH_STAMP)));
                list.add(sh);
            }
            rs.close();
        } catch (SQLException ex) {
            ExceptionHandler.exception(ex);
        }
        return list;
    }

    public static List<ScoreHistory> getAll() {
        String QUERY = "select * from " + TABLE_SCORE_HISTORY;
        return get(QUERY);
    }

    public static List<ScoreHistory> getAllForEmployee(String empID) {
        String QUERY = "select * from " + TABLE_SCORE_HISTORY + " where " + SH_EMP_ID + " = \"" + empID + "\"";
        return get(QUERY);
    }
    
    //GETTERS SETTERS
    public String getEmpID() {
        return empID;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(long currentScore) {
        this.currentScore = currentScore;
    }

    public long getTransaction() {
        return transaction;
    }

    public void setTransaction(long transaction) {
        this.transaction = transaction;
    }

    public long getStamp() {
        return stamp;
    }

    public void setStamp(long stamp) {
        this.stamp = stamp;
    }
    
    public static class Sorter {

        public static final int SORT_ASCENDING = 1;
        public static final int SORT_DESCENDING = -1;

        public static void byStamp(List<ScoreHistory> scoreHistorys, int order) {
            Collections.sort(scoreHistorys, new Comparator<ScoreHistory>() {
                @Override
                public int compare(ScoreHistory t, ScoreHistory t1) {
                    return (int) ((t.getStamp()- t1.getStamp()) * order);
                }
            });
        }

    }

}
