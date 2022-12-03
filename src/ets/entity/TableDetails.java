package ets.entity;

public interface TableDetails {

    //EMPLOYEE TABLE DETAILS
    String TABLE_EMPLOYEE = "employee";
    String E_ID = "id";
    String E_NAME = "name";
    String E_DEPT_ID = "deptid";
    String E_EMAIL = "email";
    String E_PASSWORD = "password";
    String E_SCORE = "score";
    String E_STATUS = "status";
    String E_CURRENT_TASK_ID = "current";
    String E_LAST_TASK_STAMP = "last_task_stamp";
    String E_STAMP = "stamp";

    //TASK TABLE DETAILS
    String TABLE_TASK = "task";
    String T_ID = "id";
    String T_HEAD = "head";
    String T_DESC = "_desc";
    String T_DEPT_ID = "deptid";
    String T_WORK = "work";
    String T_STATUS = "status";
    String T_EMP_ID = "empid";
    String T_ENTRY = "entry";
    String T_EXIT = "_exit";
    String T_SUBMIT_STAMP = "submit";
    String T_ASSIGNED_STAMP = "assigned";
    String T_SCORE = "score";

    //PROJECT MANAGER TABLE DETAILS
    String TABLE_PROJECT_MANAGER = "projectmanager";
    String PM_ID = "id";
    String PM_NAME = "name";
    String PM_DEPT_ID = "deptid";
    String PM_EMAIL = "email";
    String PM_PASSWORD = "password";

    //ADMIN TABLE DETAILS
    String TABLE_ADMIN = "admin";
    String A_ID = "id";
    String A_NAME = "name";
    String A_EMAIL = "email";
    String A_PASSWORD = "password";

    //ATTENDANCE TABLE DETAILS
    String TABLE_ATTENDANCE = "attendance";
    String AT_DATE = "date";
    String AT_EMP_ID = "empid";
    String AT_STATUS = "status";

    //DEPARTMENT TABLE DETAILS
    String TABLE_DEPARTMENT = "department";
    String D_ID = "id";
    String D_NAME = "name";

    //SCORE HISTORY TABLE DETAILS
    String TABLE_SCORE_HISTORY = "scorehistory";
    String SH_EMP_ID = "empid";
    String SH_CURRENT_SCORE = "cscore";
    String SH_TRANSACTION = "transaction";
    String SH_TASK_ID = "taskid";
    String SH_DESCRIPTION = "description";
    String SH_STAMP = "stamp";
    
    //NOTIFICATION TABLE DETAILS
    String TABLE_NOTIFICATION = "notification";
    String N_EMP_ID = "empid";
    String N_TYPE = "type";
    String N_DESCRIPTION = "description";
    String N_REMARKS = "remarks";
    String N_STAMP = "stamp";
}
