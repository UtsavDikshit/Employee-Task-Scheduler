package ets.core;

import ets.entity.Attendance;
import ets.entity.Employee;
import ets.entity.Task;
import ets.values.Settings;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JTextArea;

public class AutoTaskAssigner implements Settings {
    
    private Timer timer = new Timer();
    private JTextArea log = null;
    private String deptID = null;
    
    private TimerTask getTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                
                log.append("\n\n==================================================");
                log.append("\nATS Resumes : " + now());
                
                List<Employee> employeesInDepartment = Employee.getAllByDepartment(deptID);
                
                log.append("\n\nFound " + employeesInDepartment.size() + " Employees in Department :" + deptID);
                log.append("\n" + toTableEmployees(employeesInDepartment));
                
                List<Employee> allPresentEmployeesNow = Attendance.getAllPresentEmployees(Attendance.toDateNow());
                List<Employee> employeesPresentDepartment = intersection(employeesInDepartment, allPresentEmployeesNow);
                
                log.append("\n\nFound " + employeesPresentDepartment.size() + " Employees Present in Department :" + deptID);
                log.append("\n" + toTableEmployees(employeesPresentDepartment));
                
                List<Employee> employeesIdle = filterIdle(employeesPresentDepartment);
                Employee.Sorter.byLastTaskStamp(employeesIdle, Employee.Sorter.SORT_ASCENDING);
                log.append("\n\nFound " + employeesIdle.size() + " Employees in STATE='IDLE'");
                log.append("\nArranging Employees On Last Task Stamp Basis");
                log.append("\n" + toTableEmployees(employeesIdle));
                
                List<Task> tasksUnssignedDepartment = Task.getAllByDepartmentStatus(deptID, Task.STATUS_UNASSIGNED);
                log.append("\n\nFound " + tasksUnssignedDepartment.size() + " Task in STATE='UNASSIGNED' && Department : " + deptID);
                log.append("\n" + toTableTasks(tasksUnssignedDepartment));
                
                TaskQueue arrangedTask = new TaskQueue();
                arrangedTask.addAll(tasksUnssignedDepartment);
                log.append("\n\nArranging Task in Order of Deadline");
                log.append("\n" + toTableTasks(arrangedTask));
                
                if (employeesIdle.isEmpty() || arrangedTask.isEmpty()) {
                    log.append("\n\nQuitting Procedure\nCause : No Employees or Task");
                    log.append("\n\n==================================================");
                    return;
                }
                
                TaskAssigner.doAssign(arrangedTask.get(0), employeesIdle.get(0));
                log.append("\nAssigned Successfully\nTask : " + arrangedTask.get(0) + " To Employee : " + employeesIdle.get(0));
                log.append("\n\n==================================================");
                
            }
        };
    }
    
    public AutoTaskAssigner(String deptID, JTextArea log) {
        this.log = log;
        this.deptID = deptID;
    }
    
    public void start() {
        if (timer != null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(getTimerTask(), THREAD_INITIAL_DELAY, THREAD_DELAY);
        }
    }
    
    public void stop() {
        if (timer != null) {
            timer.cancel();
        }
    }
    
    private String now() {
        return new Date().toLocaleString();
    }
    
    private String toTableEmployees(List<Employee> employees) {
        return employees.toString();
    }
    
    private String toTableTasks(List<Task> tasks) {
        return tasks.toString();
    }
    
    private List<Employee> intersection(List<Employee> emp1, List<Employee> emp2) {
        List<Employee> list = new ArrayList<>();
        for (Employee e1 : emp1) {
            for (Employee e2 : emp2) {
                if (e1.getId().equalsIgnoreCase(e2.getId())) {
                    list.add(e1);
                }
            }
        }
        return list;
    }
    
    private List<Employee> filterIdle(List<Employee> employees) {
        List<Employee> list = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getStatus().equals(Employee.STATUS_IDLE)) {
                list.add(e);
            }
        }
        return list;
    }
}
