package ets.core;

import ets.entity.Employee;
import ets.entity.Notification;
import ets.entity.Task;
import ets.error.ExceptionHandler;
import ets.utils.Dialogs;
import ets.utils.Utils;
import java.sql.SQLException;

public class TaskAssigner {

    public static void doAssign(Task task, Employee employee) {
        try {

            //UPDATE TASK
            task.setEmpid(employee.getId());
            task.setStatus(Task.STATUS_ASSIGNED_UNCOMPLETE);
            task.setAssigned(System.currentTimeMillis());
            task.update();

            //UPDATE EMPLOYEE
            employee.setStatus(Employee.STATUS_WORKING);
            employee.setCurrent_task_id(task.getId());
            employee.update();

            //SEND NOTIFICATION
            String message = Utils.readFile(NotificationParser.PATH_ASSIGNED_TASK_NOTIFICATION);
            message = NotificationParser.parse(message, task, employee);
            Notification notification = new Notification();
            notification.setType(Notification.TYPE_TASK_ASSIGNED);
            notification.setEmpid(employee.getId());
            notification.setRemarks("None");
            notification.setStamp(System.currentTimeMillis());
            notification.setDescription(message);
            Notification.saveNotification(notification);
            
        } catch (SQLException ex) {
            Dialogs.error("Error Occured. Check System logs for more details");
            ExceptionHandler.exception(ex);
        }
    }

}
