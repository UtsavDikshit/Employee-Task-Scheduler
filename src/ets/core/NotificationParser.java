package ets.core;

import ets.entity.Employee;
import ets.entity.Task;
import ets.utils.Utils;

public class NotificationParser implements NotificationConstants {

    public static final String PATH_ASSIGNED_TASK_NOTIFICATION
            = "templates/task_assigned_notification.txt";

    public static final String PATH_SUBMITTED_TASK_NOTIFICATION
            = "templates/task_submitted_notification.txt";

    public static final String PATH_TASK_EVALUATED_ACCEPT
            = "templates/task_evaluated_accept.txt";

    public static final String PATH_TASK_EVALUATED_DECLINE
            = "templates/task_evaluated_decline.txt";

    public static String parse(String data, Task task, Employee employee) {
        data = data.replaceAll(EMP_NAME, employee.getName());
        data = data.replaceAll(TASK_ID, task.getId());
        data = data.replaceAll(TASK_SCORE, String.valueOf(task.getScore()));
        data = data.replaceAll(TASK_HEADER, task.getHead());
        data = data.replaceAll(TASK_DEADLINE, Utils.convertStampLong(task.getExit()));
        data = data.replaceAll(TASK_ASSIGNED_STAMP, Utils.convertStampLong(task.getAssigned()));
        return data;
    }

}

interface NotificationConstants {

    String EMP_NAME = "====EMP_NAME====";
    String TASK_ID = "====TASK_ID====";
    String TASK_ASSIGNED_STAMP = "====ASSIGNED_STAMP====";
    String TASK_DEADLINE = "====DEADLINE====";
    String TASK_SCORE = "====SCORE====";
    String TASK_HEADER = "====HEADER====";
    String REMARKS = "====REMARKS====";
}
