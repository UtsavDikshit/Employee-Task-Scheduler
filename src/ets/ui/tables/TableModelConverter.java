package ets.ui.tables;

import ets.entity.Attendance;
import ets.entity.Department;
import ets.entity.Employee;
import ets.entity.Notification;
import ets.entity.ProjectManager;
import ets.entity.ScoreHistory;
import ets.entity.Task;
import ets.utils.Utils;
import java.util.List;

public class TableModelConverter {

    //PROJECT MANAGER MODEL
    public static MyDefaultTableModel forProjectManagers(List<ProjectManager> projectManagers) {

        if (projectManagers == null) {
            return emptyTable();
        }

        Object[][] data = new Object[projectManagers.size()][4];
        int temp = 0;
        for (ProjectManager e : projectManagers) {
            data[temp][0] = e.getId();
            data[temp][1] = e.getName();
            data[temp][2] = e.getDepartment_id();
            data[temp][3] = e.getEmail();
            temp++;
        }
        Object[] columns = {"ID", "Name", "Department", "Email"};
        MyDefaultTableModel tableModel = new MyDefaultTableModel(data, columns);
        return tableModel;

    }

    //DEPARTMENT MODEL
    public static MyDefaultTableModel forDepartments(List<Department> departments) {

        if (departments == null) {
            return emptyTable();
        }
        Object[][] data = new Object[departments.size()][2];
        int temp = 0;
        for (Department e : departments) {
            data[temp][0] = e.getId();
            data[temp][1] = e.getName();
            temp++;
        }
        Object[] columns = {"ID", "Name"};
        MyDefaultTableModel tableModel = new MyDefaultTableModel(data, columns);
        return tableModel;

    }

    //ATTENDANCE HISTORY MODEL
    public static MyDefaultTableModel forAttendances(List<Attendance> attendances) {

        if (attendances == null) {
            return emptyTable();
        }

        Object[][] data = new Object[attendances.size()][3];
        int temp = 0;
        for (Attendance attendance : attendances) {
            data[temp][0] = attendance.getDate();
            data[temp][1] = attendance.getEmpid();
            data[temp][2] = attendance.getStatus();
            temp++;
        }
        Object[] columns = {"Date", "EmpID", "Status"};
        MyDefaultTableModel tableModel = new MyDefaultTableModel(data, columns);
        return tableModel;

    }

    //SCORE HISTORY MODEL
    public static MyDefaultTableModel forScores(List<ScoreHistory> scoreHistorys) {

        if (scoreHistorys == null) {
            return emptyTable();
        }

        Object[][] data = new Object[scoreHistorys.size()][4];
        int temp = 0;
        for (ScoreHistory sh : scoreHistorys) {
            data[temp][0] = sh.getCurrentScore();
            data[temp][1] = sh.getTransaction();
            data[temp][2] = sh.getDescription();
            data[temp][3] = sh.getTaskID();
            temp++;
        }
        Object[] columns = {"Current Score", "Transaction", "Description", "Task ID"};
        MyDefaultTableModel tableModel = new MyDefaultTableModel(data, columns);
        return tableModel;

    }

    //EMPLOYEE MODEL
    public static MyDefaultTableModel forSubmissions(List<Task> tasks) {

        if (tasks == null) {
            return emptyTable();
        }

        Object[][] data = new Object[tasks.size()][5];
        int temp = 0;
        for (Task task : tasks) {
            data[temp][0] = task.getId();
            data[temp][1] = task.getEmpid();
            data[temp][2] = Utils.convertStampLong(task.getSubmit());
            data[temp][3] = "Accept";
            data[temp][4] = "Decline";
            temp++;
        }
        Object[] columns = {"Task ID", "Emp ID", "Submitted At", "Accept", "Decline"};
        MyDefaultTableModel tableModel = new MyDefaultTableModel(data, columns);
        return tableModel;

    }

    //EMPLOYEE MODEL
    public static MyDefaultTableModel forEmployees(List<Employee> employees) {

        if (employees == null) {
            return emptyTable();
        }

        Object[][] data = new Object[employees.size()][5];
        int temp = 0;
        for (Employee employee : employees) {
            data[temp][0] = employee.getId();
            data[temp][1] = employee.getStatus();
            data[temp][2] = employee.getCurrent_task_id();
            data[temp][3] = Utils.convertStampLong(employee.getLast_task_stamp());
            data[temp][4] = employee.getAttendance();
            temp++;
        }
        Object[] columns = {"ID", "Status", "Current Task", "Last Task At", "Attendance"};
        MyDefaultTableModel tableModel = new MyDefaultTableModel(data, columns);
        return tableModel;

    }

    //TASK MODEL
    public static MyDefaultTableModel forTasks(List<Task> tasks) {

        if (tasks == null) {
            return emptyTable();
        }

        Object[][] data = new Object[tasks.size()][6];
        int temp = 0;
        for (Task task : tasks) {
            data[temp][0] = task.getId();
            data[temp][1] = task.getHead();
            data[temp][2] = task.getScore();
            data[temp][3] = task.getStatus();
            data[temp][4] = task.getEmpid();
            data[temp][5] = Utils.convertStampLong(task.getAssigned());
            temp++;
        }
        Object[] columns = {"ID", "Header", "Score", "Status", "Employee", "Assigned"};
        MyDefaultTableModel tableModel = new MyDefaultTableModel(data, columns);
        return tableModel;

    }
    
    //TASK MODEL
    public static MyDefaultTableModel forNotifications(List<Notification> notifications) {

        if (notifications == null) {
            return emptyTable();
        }

        Object[][] data = new Object[notifications.size()][4];
        int temp = 0;
        for (Notification notification : notifications) {
            data[temp][0] = notification.getType();
            data[temp][1] = notification.getDescription();
            data[temp][2] = notification.getRemarks();
            data[temp][3] = Utils.convertStamp(notification.getStamp());
            temp++;
        }
        Object[] columns = {"Type", "Description", "Remarks", "Stamp"};
        MyDefaultTableModel tableModel = new MyDefaultTableModel(data, columns);
        return tableModel;

    }

    //EMPLOYEE MODEL (ID,NAME)
    public static MyDefaultTableModel forEmployeesSmall(List<Employee> employees) {

        if (employees == null) {
            return emptyTable();
        }
        Object[][] data = new Object[employees.size()][2];
        int temp = 0;
        for (Employee e : employees) {
            data[temp][0] = e.getId();
            data[temp][1] = e.getName();
            temp++;
        }
        Object[] columns = {"ID", "Name"};
        MyDefaultTableModel tableModel = new MyDefaultTableModel(data, columns);
        return tableModel;

    }

    //TASK MODEL (ID,SCORE)
    public static MyDefaultTableModel forTasksSmall(List<Task> tasks) {
        if (tasks == null) {
            return emptyTable();
        }
        Object[][] data = new Object[tasks.size()][3];
        int temp = 0;
        for (Task t : tasks) {
            data[temp][0] = t.getId();
            data[temp][1] = t.getScore();
            temp++;
        }
        Object[] columns = {"ID", "Score"};
        MyDefaultTableModel tableModel = new MyDefaultTableModel(data, columns);
        return tableModel;
    }

    private static MyDefaultTableModel emptyTable() {
        return new MyDefaultTableModel(new Object[][]{}, new Object[]{});
    }

}