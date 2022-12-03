package ets.ui.tables;

import ets.entity.Attendance;
import ets.entity.Employee;
import ets.entity.Notification;
import ets.entity.Task;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class MyTableCellRenderer extends DefaultTableCellRenderer {

    public static final String TABLE_EMPLOYEES = "employee";
    public static final String TABLE_TASKS = "task";
    public static final String TABLE_SCORE_HISTORYS = "scorehistory";
    public static final String TABLE_TASK_HISTORYS = "taskhistory";
    public static final String TABLE_ATTENDANCES = "attendance";
    public static final String TABLE_SUBMISSIONS = "submissions";
    public static final String TABLE_NOTIFICATIONS = "notifications";

    public MyTableCellRenderer() {
        super();
        setOpaque(true);
    }

    private void setDefaultTableStyle(Object value) {
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setText(value != null ? value.toString() : "null");
        setFont(new Font("Monospaced", Font.BOLD, 20));
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {

        setDefaultTableStyle(value);

        if (table.getName() != null) {
            switch (table.getName()) {
                case TABLE_ATTENDANCES:
                    decorateAttendances(value, column);
                    break;
                case TABLE_SUBMISSIONS:
                    decorateSubmissions(value, column);
                    break;
                case TABLE_TASK_HISTORYS:
                case TABLE_TASKS:
                    decorateTasks(value, column);
                    break;
                case TABLE_SCORE_HISTORYS:
                    decorateScoreHistorys(value, column);
                    break;
                case TABLE_EMPLOYEES:
                    decorateEmployees(value, column);
                    break;
                case TABLE_NOTIFICATIONS:
                    decorateNotifications(value, column);
                    break;
            }
        }
        return this;
    }

    private static final Color COLOR_PRESENT = new Color(51, 102, 0);
    private static final Color COLOR_ABSENT = new Color(225, 25, 25);

    private void decorateAttendances(Object value, int column) {
        //CHECK ONLY FOR 3RD COLUMN
        if (column == 2) {
            String status = value.toString();
            setBackground(
                    status.equals(Attendance.STATUS_PRESENT)
                    ? COLOR_PRESENT
                    : COLOR_ABSENT);
            setForeground(Color.WHITE);
        }
    }

    private static final Color COLOR_ACCEPT = new Color(51, 102, 0);
    private static final Color COLOR_REJECT = new Color(225, 25, 25);

    private void decorateSubmissions(Object value, int column) {
        //CHECK FOR 4TH AND 5TH COLUMN
        if (column == 3) {
            setBackground(COLOR_ACCEPT);
            setForeground(Color.WHITE);
        } else if (column == 4) {
            setBackground(COLOR_REJECT);
            setForeground(Color.WHITE);
        }
    }

    private static final Color COLOR_TASK_COMPLETED = new Color(51, 102, 0);
    private static final Color COLOR_TASK_UNASSIGNED = new Color(225, 25, 25);
    private static final Color COLOR_TASK_ASSIGNED_UNCOMPLETE = new Color(7, 22, 225);
    private static final Color COLOR_TASK_UNDER_REVIEW = new Color(225, 130, 21);

    private void decorateTasks(Object value, int column) {
        //CHECK ONLY FOR 4TH COLUMN
        if (column == 3) {
            String status = value.toString();
            if (status.equals(Task.STATUS_COMPLETED)) {
                setBackground(COLOR_TASK_COMPLETED);
            } else if (status.equals(Task.STATUS_UNASSIGNED)) {
                setBackground(COLOR_TASK_UNASSIGNED);
            } else if (status.equals(Task.STATUS_ASSIGNED_UNCOMPLETE)) {
                setBackground(COLOR_TASK_ASSIGNED_UNCOMPLETE);
            } else if (status.equals(Task.STATUS_UNDER_REVIEW)) {
                setBackground(COLOR_TASK_UNDER_REVIEW);
            }
            setForeground(Color.WHITE);
        }
    }

    private static final Color COLOR_SCORE_POSITIVE = new Color(51, 102, 0);
    private static final Color COLOR_SCORE_NEGATIVE_ZERO = new Color(225, 25, 25);

    private void decorateScoreHistorys(Object value, int column) {
        //CHECK ONLY FOR 2ND COLUMN
        if (column == 1) {
            try {
                long ans = Long.parseLong(value.toString());
                setBackground(ans > 0 ? COLOR_SCORE_POSITIVE : COLOR_SCORE_NEGATIVE_ZERO);
                setForeground(Color.WHITE);
            } catch (NumberFormatException ignored) {
            }
        }
    }

    private static final Color COLOR_EMPLOYEE_IDLE = new Color(51, 102, 0);
    private static final Color COLOR_EMPLOYEE_WORKING = new Color(225, 25, 25);
    private static final Color COLOR_ATTENDANCE_PRESENT = new Color(51, 102, 0);
    private static final Color COLOR_ATTENDANCE_ABSENT = new Color(225, 25, 25);

    private void decorateEmployees(Object value, int column) {
        if (column == 1) {
            String status = value.toString();
            setBackground(
                    status.equals(Employee.STATUS_IDLE)
                    ? COLOR_EMPLOYEE_IDLE
                    : COLOR_EMPLOYEE_WORKING);
            setForeground(Color.WHITE);
        } else if (column == 4) {
            String status = value.toString();
            setBackground(status.equals(
                    Attendance.STATUS_PRESENT)
                            ? COLOR_ATTENDANCE_PRESENT
                            : COLOR_ATTENDANCE_ABSENT);
            setForeground(Color.WHITE);
        }
    }

    private static final Color COLOR_NOTIFICATION_TASK_ASSIGNED = new Color(51, 102, 0);
    private static final Color COLOR_NOTIFICATION_TASK_SUBMITTED = new Color(225, 130, 21);
    private static final Color COLOR_NOTIFICATION_TASK_EVALUATION = new Color(7, 22, 225);

    private void decorateNotifications(Object value, int column) {
        //CHECK ONLY FOR 4TH COLUMN
        if (column == 0) {
            String type = value.toString();
            if (type.equals(Notification.TYPE_TASK_ASSIGNED)) {
                setBackground(COLOR_NOTIFICATION_TASK_ASSIGNED);
            } else if (type.equals(Notification.TYPE_TASK_SUBMITTED)) {
                setBackground(COLOR_NOTIFICATION_TASK_SUBMITTED);
            } else if (type.equals(Notification.TYPE_TASK_EVALUATION)) {
                setBackground(COLOR_NOTIFICATION_TASK_EVALUATION);
            }
            setForeground(Color.WHITE);
        }
    }
}
