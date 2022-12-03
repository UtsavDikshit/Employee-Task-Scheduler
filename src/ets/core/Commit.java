package ets.core;

import ets.entity.Employee;
import ets.entity.Notification;
import ets.entity.ScoreHistory;
import ets.entity.Task;
import ets.error.ExceptionHandler;
import ets.utils.Dialogs;
import ets.utils.Utils;
import ets.values.Settings;
import java.sql.SQLException;

public class Commit implements Settings {

    public void commitWork(String remarks, String empID, String taskID) {

        try {
            Employee employee = new Employee(empID);
            Task task = new Task(taskID);
            ScoreHistory scoreHistory = new ScoreHistory();

            long now = System.currentTimeMillis();
            long thisTaskScore = getscoreAfterDeduction(task, now);

            scoreHistory.setCurrentScore(employee.getScore());
            scoreHistory.setEmpID(employee.getId());
            scoreHistory.setTaskID(task.getId());
            scoreHistory.setStamp(now);
            scoreHistory.setTransaction(thisTaskScore);
            scoreHistory.setDescription(task.getExit() >= now ? "Early" : "Late");
            ScoreHistory.saveScoreHistory(scoreHistory);

            task.setStatus(Task.STATUS_COMPLETED);
            task.setSubmit(now);
            task.update();

            employee.setStatus(Employee.STATUS_IDLE);
            employee.setCurrent_task_id(null);
            employee.setLast_task_stamp(now);
            employee.setScore(employee.getScore() + thisTaskScore);
            employee.update();

            String message = Utils.readFile(NotificationParser.PATH_TASK_EVALUATED_ACCEPT);
            message = NotificationParser.parse(message, task, employee);
            message = message.replaceAll(NotificationParser.REMARKS, remarks);
            Notification notification = new Notification();
            notification.setType(Notification.TYPE_TASK_EVALUATION);
            notification.setEmpid(employee.getId());
            notification.setRemarks(remarks == null || remarks.isEmpty() ? "None" : remarks);
            notification.setStamp(System.currentTimeMillis());
            notification.setDescription(message);
            Notification.saveNotification(notification);

        } catch (SQLException ex) {
            ExceptionHandler.exception(ex);
            Dialogs.error("Critical Error Occured. Kindly contact system admin for details");
            System.exit(5);
        }

    }

    private long getscoreAfterDeduction(Task task, long now) {
        if (task.getExit() >= now) {
            return task.getScore();
        }
        int percent = (int) ((now - task.getExit()) / FACTOR_PER_MILLISECOND_DEDUCTION);
        if (percent >= 100) {
            return 0;
        } else {
            return task.getScore() - (task.getScore() * percent / 100);
        }
    }

}
