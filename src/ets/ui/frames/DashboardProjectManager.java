package ets.ui.frames;

import ets.listeners.Refreshable;
import ets.ui.tables.TableModelConverter;
import ets.ui.base.BaseProjectManagerFrame;
import ets.ui.tables.AdapterTables;
import ets.core.AutoTaskAssigner;
import ets.core.Commit;
import ets.core.NotificationParser;
import ets.core.TaskAssigner;
import ets.entity.Employee;
import ets.entity.ProjectManager;
import ets.entity.Task;
import ets.utils.Dialogs;
import ets.entity.Attendance;
import ets.entity.Department;
import ets.entity.Notification;
import ets.error.ExceptionHandler;
import ets.utils.Environment;
import ets.persistance.SettingsPersistance;
import ets.ui.tables.MyTableCellRenderer;
import ets.utils.Utils;
import ets.values.Permissions;
import ets.values.Settings;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JTable;

public final class DashboardProjectManager extends BaseProjectManagerFrame implements Refreshable.Database, Refreshable.UI, Settings, Permissions {

    private boolean isATSActive = false;
    private ProjectManager _pm = null;
    private String _pmID = null;
    private int _mode = -1;
    private AutoTaskAssigner autoTaskAssigner = null;

    static {
        Environment.loadTheme();
        background_unclicked = SettingsPersistance.StringtoColor(
                SettingsPersistance.getProperty(SettingsPersistance.KEY_COLOR_PROJECT_MANAGER));
    }

    @Override
    public void dispose() {
        Refreshable.unregister(this);
    }

    @Override
    public void updateUI() {
        Environment.loadTheme();
        initColors();
        initMouseListener();
    }

    @Override
    public void updateDatabase() {

        //SHOW PROGRESS PANEL
        CardLayout card = (CardLayout) dashboard.getLayout();
        card.show(dashboard, "cardProgress");

        //START BACKGROUND THREAD
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    loadDatabase();
                } catch (Exception ex) {
                }

                //SHOW MAIN PANEL
                CardLayout card = (CardLayout) dashboard.getLayout();
                card.show(dashboard, "cardMain");
            }
        });
        t.start();
    }

    private void loadDatabase() throws Exception {
        //GET PROJECT MANAGER
        _pm = new ProjectManager(_pmID);

        //UPDATE MY PROFILE
        loadMyProfile();

        //UPDATE WELCOME
        welcome.setText("WELCOME, " + _pm.getName().toUpperCase());

        //UPDATE TASKS
        List<Task> tasks = Task.getAllByDepartment(_pm.getDepartment_id());
        Task.Sorter.byEntry(tasks, Task.Sorter.SORT_DESCENDING);
        tableTasks.setModel(TableModelConverter.forTasks(tasks));

        //UPDATE EMPLOYEES
        List<Employee> employees = Employee.getAllByDepartment(_pm.getDepartment_id());
        List<Employee> presentEmployees = Attendance.getAllPresentEmployees(Attendance.toDateNow());
        for (Employee e : employees) {
            for (Employee e1 : presentEmployees) {
                if (e.getId().equals(e1.getId())) {
                    e.setAttendance(Attendance.STATUS_PRESENT);
                }
            }
        }
        Employee.Sorter.byScore(employees, Employee.Sorter.SORT_DESCENDING);
        tableEmployees.setModel(TableModelConverter.forEmployees(employees));

        //UPDATE MAT EMPLOYEES
        List<Employee> temp = Employee.getAllByDepartment(_pm.getDepartment_id());
        List<Employee> idles = new ArrayList<>();
        for (Employee e : temp) {
            if (e.getStatus().equals(Employee.STATUS_IDLE)) {
                idles.add(e);
            }
        }
        tableMATEmployee.setModel(TableModelConverter.forEmployeesSmall(idles));

        //UPDATE MAT EMPLOYEES
        List<Task> unassigned = Task.getAllByDepartmentStatus(_pm.getDepartment_id(), Task.STATUS_UNASSIGNED);
        tableMATTasks.setModel(TableModelConverter.forTasksSmall(unassigned));

        //UPDATE SUBMISSIONS
        List<Task> tasksReview = Task.getAllByDepartmentStatus(_pm.getDepartment_id(), Task.STATUS_UNDER_REVIEW);
        tableSubmissions.setModel(TableModelConverter.forSubmissions(tasksReview));
    }

    private void loadMyProfile() throws Exception {
        id.setText(_pm.getId());
        name.setText(_pm.getName());
        Department _department = new Department(Environment.department);
        department.setText(_department.getName());
        password.setText(_pm.getPassword());
        email.setText(_pm.getEmail());
    }

    public DashboardProjectManager(String pmID, int mode) {

        //REGSITER DATABASE LISTENER
        Refreshable.register(this);
        //==========================

        //INIT PARAMETERS
        this._pmID = pmID;
        this._mode = mode;
        //===============

        //LOAD DATA AND UI
        initComponents();
        updateUI();
        updateDatabase();
        //================

        //LOAD MY PROFILE BY DEFAULT
        mpClicked(null);
        //==========================

        if (_mode == MODE_VIEW) {
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        }

    }

    private void initColors() {
        mainContainer.setBackground(background_unclicked);
        mpc.setBackground(background_unclicked);
        mp.setBackground(background_unclicked);
        mtc.setBackground(background_unclicked);
        mt.setBackground(background_unclicked);
        mec.setBackground(background_unclicked);
        me.setBackground(background_unclicked);
        subc.setBackground(background_unclicked);
        sub.setBackground(background_unclicked);
        thc.setBackground(background_unclicked);
        th.setBackground(background_unclicked);
        atc.setBackground(background_unclicked);
        at.setBackground(background_unclicked);
        assignTaskBtn.setBackground(background_unclicked);
        logoff.setBackground(background_unclicked);
        refreshIcon.setBackground(background_unclicked);
        settings.setBackground(background_unclicked);
        addtasksBtn.setBackground(general_background_unclicked);
        addemployeeBtn.setBackground(general_background_unclicked);
        atsToggle.setBackground(red);
    }

    private void initMouseListener() {

        //TABLE LISTENERS===========================================
        tableEmployees.addMouseListener(new AdapterTables(this::employeesTable));
        tableTasks.addMouseListener(new AdapterTables(this::tasksTable));
        tableMATEmployee.addMouseListener(new AdapterTables(this::employeesTableMAT));
        tableMATTasks.addMouseListener(new AdapterTables(this::tasksTableMAT));
        tableSubmissions.addMouseListener(new AdapterTables(this::submissionsTable));
        //==========================================================

        //BUTTONS BACKGROUND MOUSE LISTENERS===============
        mp.addMouseListener(adapterAll);
        mt.addMouseListener(adapterAll);
        me.addMouseListener(adapterAll);
        th.addMouseListener(adapterAll);
        at.addMouseListener(adapterAll);
        sub.addMouseListener(adapterAll);
        assignTaskBtn.addMouseListener(adapterAll);
        logoff.addMouseListener(adapterAll);
        settings.addMouseListener(adapterAll);
        refreshIcon.addMouseListener(adapterAll);
        addtasksBtn.addMouseListener(adapterButtons);
        addemployeeBtn.addMouseListener(adapterButtons);
        //=================================================

        //ADD TABLE NAMES==============================================
        tableTasks.setName(MyTableCellRenderer.TABLE_TASKS);
        tableSubmissions.setName(MyTableCellRenderer.TABLE_SUBMISSIONS);
        tableEmployees.setName(MyTableCellRenderer.TABLE_EMPLOYEES);
        //=============================================================

        //ADD RENDERER TO TABLE========================================
        MyTableCellRenderer renderer = new MyTableCellRenderer();
        tableTasks.setDefaultRenderer(Object.class, renderer);
        tableSubmissions.setDefaultRenderer(Object.class, renderer);
        tableEmployees.setDefaultRenderer(Object.class, renderer);
        //=============================================================
    }

    //CLICK HANDLERS FOR TABLES
    private void employeesTable(JTable table, Object data) {
        switch (table.getSelectedColumn()) {
            case 0:
                DashboardEmployee de = new DashboardEmployee(data.toString(), MODE_VIEW);
                de.setVisible(true);
                break;
            case 2:
                PanelTaskView panelTaskView = new PanelTaskView(data.toString(), MODE_VIEW);
                panelTaskView.setVisible(true);
                break;
            default:
                Dialogs.desciption(data.toString());
        }
    }

    private void tasksTable(JTable table, Object data) {
        switch (table.getSelectedColumn()) {
            case 0:
                PanelTaskView panelTaskView = new PanelTaskView(data.toString(), MODE_VIEW);
                panelTaskView.setVisible(true);
                break;
            case 4:
                DashboardEmployee de = new DashboardEmployee(data.toString(), MODE_VIEW);
                de.setVisible(true);
                break;
            default:
                Dialogs.desciption(data.toString());
        }
    }

    private void employeesTableMAT(JTable table, Object data) {
        switch (table.getSelectedColumn()) {
            case 0:
                se.setText(data.toString());
                break;
            default:
                Dialogs.desciption(data.toString());
        }
    }

    private void tasksTableMAT(JTable table, Object data) {
        switch (table.getSelectedColumn()) {
            case 0:
                st.setText(data.toString());
                break;
            default:
                Dialogs.desciption(data.toString());
        }
    }

    private void submissionsTable(JTable table, Object data) {
        switch (table.getSelectedColumn()) {
            case 0:
                PanelTaskView panelTaskView = new PanelTaskView(data.toString(), MODE_VIEW);
                panelTaskView.setVisible(true);
                break;
            case 1:
                DashboardEmployee de = new DashboardEmployee(data.toString(), MODE_VIEW);
                de.setVisible(true);
                break;
            case 3:
                acceptWork(table);
                break;
            case 4:
                declineWork(table);
                break;
            default:
                Dialogs.desciption(data.toString());
                break;
        }
    }

    private void acceptWork(JTable table) {
        String taskID = (String) table.getModel().getValueAt(table.getSelectedRow(), 0);
        String empID = (String) table.getModel().getValueAt(table.getSelectedRow(), 1);

        if (taskID == null || taskID.equals("null") || taskID.isEmpty()) {
            return;
        }
        if (empID == null || empID.equals("null") || empID.isEmpty()) {
            return;
        }

        String remarks = Dialogs.input("Any Remarks");

        if (Dialogs.confirm("Are you sure want to accept work?")) {
            Commit commit = new Commit();
            commit.commitWork(remarks, empID, taskID);
        }
    }

    private void declineWork(JTable table) {
        String taskID = (String) table.getModel().getValueAt(table.getSelectedRow(), 0);
        String empID = (String) table.getModel().getValueAt(table.getSelectedRow(), 1);

        if (taskID == null || taskID.equals("null") || taskID.isEmpty()) {
            return;
        }
        if (empID == null || empID.equals("null") || empID.isEmpty()) {
            return;
        }

        String remarks = Dialogs.input("Any Remarks");

        if (Dialogs.confirm("Are you sure want to reject work?")) {
            try {
                Task task = new Task(taskID);
                task.setStatus(Task.STATUS_ASSIGNED_UNCOMPLETE);
                task.update();

                Employee e = new Employee(empID);
                String message = Utils.readFile(NotificationParser.PATH_TASK_EVALUATED_DECLINE);
                message = NotificationParser.parse(message, task, e);
                message = message.replaceAll(NotificationParser.REMARKS, remarks);
                Notification notification = new Notification();
                notification.setType(Notification.TYPE_TASK_EVALUATION);
                notification.setEmpid(e.getId());
                notification.setRemarks(remarks == null || remarks.isEmpty() ? "None" : remarks);
                notification.setStamp(System.currentTimeMillis());
                notification.setDescription(message);
                Notification.saveNotification(notification);

            } catch (SQLException ex) {
                ExceptionHandler.exception(ex);
            }
        }
    }

    //===========================
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dashboard = new javax.swing.JPanel();
        progressContainer = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        mainContainer = new javax.swing.JPanel();
        containerPanel = new javax.swing.JPanel();
        panelMyProfile = new javax.swing.JPanel();
        id = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        name = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        password = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        department = new javax.swing.JTextField();
        taskContainer = new javax.swing.JPanel();
        addtasksBtn = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tableTasks = new javax.swing.JTable();
        employeeContainer = new javax.swing.JPanel();
        addemployeeBtn = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tableEmployees = new javax.swing.JTable();
        panelMAT = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableMATTasks = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableMATEmployee = new javax.swing.JTable();
        se = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        st = new javax.swing.JTextField();
        assignTaskBtn = new javax.swing.JButton();
        panelATS = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        ats = new javax.swing.JTextArea();
        panelSubmissions = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tableSubmissions = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        mpc = new javax.swing.JPanel();
        mp = new javax.swing.JLabel();
        mtc = new javax.swing.JPanel();
        mt = new javax.swing.JLabel();
        mec = new javax.swing.JPanel();
        me = new javax.swing.JLabel();
        subc = new javax.swing.JPanel();
        sub = new javax.swing.JLabel();
        thc = new javax.swing.JPanel();
        th = new javax.swing.JLabel();
        atc = new javax.swing.JPanel();
        at = new javax.swing.JLabel();
        logoff = new javax.swing.JLabel();
        welcome = new javax.swing.JLabel();
        atsToggle = new javax.swing.JLabel();
        refreshIcon = new javax.swing.JLabel();
        settings = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Project Manager Dashboard");
        setResizable(false);
        setSize(new java.awt.Dimension(1200, 700));

        dashboard.setLayout(new java.awt.CardLayout());

        progressContainer.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/progress.gif"))); // NOI18N

        jLabel11.setFont(new java.awt.Font("Roboto Black", 0, 30)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("LOADING PLEASE WAIT...");

        javax.swing.GroupLayout progressContainerLayout = new javax.swing.GroupLayout(progressContainer);
        progressContainer.setLayout(progressContainerLayout);
        progressContainerLayout.setHorizontalGroup(
            progressContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(progressContainerLayout.createSequentialGroup()
                .addGroup(progressContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(progressContainerLayout.createSequentialGroup()
                        .addGap(568, 568, 568)
                        .addComponent(jLabel4))
                    .addGroup(progressContainerLayout.createSequentialGroup()
                        .addGap(361, 361, 361)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 503, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(373, Short.MAX_VALUE))
        );
        progressContainerLayout.setVerticalGroup(
            progressContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, progressContainerLayout.createSequentialGroup()
                .addGap(318, 318, 318)
                .addComponent(jLabel4)
                .addGap(36, 36, 36)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(235, 235, 235))
        );

        dashboard.add(progressContainer, "cardProgress");

        mainContainer.setBackground(new java.awt.Color(204, 0, 204));

        containerPanel.setBackground(new java.awt.Color(255, 255, 255));
        containerPanel.setLayout(new java.awt.CardLayout());

        panelMyProfile.setBackground(new java.awt.Color(255, 255, 255));

        id.setEditable(false);
        id.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        id.setName("pmid"); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/email.png"))); // NOI18N
        jLabel3.setText("Email : ");
        jLabel3.setIconTextGap(10);
        jLabel3.setMinimumSize(new java.awt.Dimension(20, 20));

        name.setEditable(false);
        name.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        name.setName("pmname"); // NOI18N

        email.setEditable(false);
        email.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        email.setName("pmemail"); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/id.png"))); // NOI18N
        jLabel6.setText("ID : ");
        jLabel6.setIconTextGap(10);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/name.png"))); // NOI18N
        jLabel7.setText("Name : ");
        jLabel7.setIconTextGap(10);
        jLabel7.setMinimumSize(new java.awt.Dimension(20, 20));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/password.png"))); // NOI18N
        jLabel8.setText("Password : ");
        jLabel8.setIconTextGap(10);
        jLabel8.setMinimumSize(new java.awt.Dimension(20, 20));

        password.setEditable(false);
        password.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        password.setName("pmpassword"); // NOI18N

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/department.png"))); // NOI18N
        jLabel9.setText("Department : ");
        jLabel9.setIconTextGap(10);

        department.setEditable(false);
        department.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        department.setName("pmdept"); // NOI18N

        javax.swing.GroupLayout panelMyProfileLayout = new javax.swing.GroupLayout(panelMyProfile);
        panelMyProfile.setLayout(panelMyProfileLayout);
        panelMyProfileLayout.setHorizontalGroup(
            panelMyProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMyProfileLayout.createSequentialGroup()
                .addGap(156, 156, 156)
                .addGroup(panelMyProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMyProfileLayout.createSequentialGroup()
                        .addGroup(panelMyProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelMyProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelMyProfileLayout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(department, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(198, Short.MAX_VALUE))
        );
        panelMyProfileLayout.setVerticalGroup(
            panelMyProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMyProfileLayout.createSequentialGroup()
                .addGap(122, 122, 122)
                .addGroup(panelMyProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(panelMyProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(panelMyProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(panelMyProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(department, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(panelMyProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(219, Short.MAX_VALUE))
        );

        containerPanel.add(panelMyProfile, "card2");

        taskContainer.setBackground(new java.awt.Color(255, 255, 255));
        taskContainer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        addtasksBtn.setBackground(new java.awt.Color(255, 255, 255));
        addtasksBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/plus.png"))); // NOI18N
        addtasksBtn.setOpaque(true);
        addtasksBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addtasksBtnMouseClicked(evt);
            }
        });

        tableTasks.setAutoCreateRowSorter(true);
        tableTasks.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tableTasks.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        tableTasks.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableTasks.setRowHeight(50);
        tableTasks.setRowMargin(10);
        jScrollPane6.setViewportView(tableTasks);

        javax.swing.GroupLayout taskContainerLayout = new javax.swing.GroupLayout(taskContainer);
        taskContainer.setLayout(taskContainerLayout);
        taskContainerLayout.setHorizontalGroup(
            taskContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(taskContainerLayout.createSequentialGroup()
                .addContainerGap(882, Short.MAX_VALUE)
                .addComponent(addtasksBtn))
            .addGroup(taskContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 932, Short.MAX_VALUE))
        );
        taskContainerLayout.setVerticalGroup(
            taskContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(taskContainerLayout.createSequentialGroup()
                .addComponent(addtasksBtn)
                .addContainerGap(605, Short.MAX_VALUE))
            .addGroup(taskContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, taskContainerLayout.createSequentialGroup()
                    .addGap(0, 61, Short.MAX_VALUE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 594, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        containerPanel.add(taskContainer, "card3");

        employeeContainer.setBackground(new java.awt.Color(255, 255, 255));

        addemployeeBtn.setBackground(new java.awt.Color(255, 255, 255));
        addemployeeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/plus.png"))); // NOI18N
        addemployeeBtn.setOpaque(true);
        addemployeeBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addemployeeBtnMouseClicked(evt);
            }
        });

        tableEmployees.setAutoCreateRowSorter(true);
        tableEmployees.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tableEmployees.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        tableEmployees.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableEmployees.setRowHeight(50);
        tableEmployees.setRowMargin(10);
        jScrollPane7.setViewportView(tableEmployees);

        javax.swing.GroupLayout employeeContainerLayout = new javax.swing.GroupLayout(employeeContainer);
        employeeContainer.setLayout(employeeContainerLayout);
        employeeContainerLayout.setHorizontalGroup(
            employeeContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, employeeContainerLayout.createSequentialGroup()
                .addContainerGap(884, Short.MAX_VALUE)
                .addComponent(addemployeeBtn)
                .addGap(0, 0, 0))
            .addGroup(employeeContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 934, Short.MAX_VALUE))
        );
        employeeContainerLayout.setVerticalGroup(
            employeeContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, employeeContainerLayout.createSequentialGroup()
                .addComponent(addemployeeBtn)
                .addContainerGap(607, Short.MAX_VALUE))
            .addGroup(employeeContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, employeeContainerLayout.createSequentialGroup()
                    .addGap(0, 60, Short.MAX_VALUE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 597, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        containerPanel.add(employeeContainer, "card4");

        panelMAT.setBackground(new java.awt.Color(255, 255, 255));

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Unassigned Tasks"));

        tableMATTasks.setAutoCreateRowSorter(true);
        tableMATTasks.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tableMATTasks.setFont(new java.awt.Font("Monospaced", 0, 20)); // NOI18N
        tableMATTasks.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableMATTasks.setRowHeight(25);
        tableMATTasks.setRowMargin(5);
        jScrollPane3.setViewportView(tableMATTasks);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Employees"));

        tableMATEmployee.setAutoCreateRowSorter(true);
        tableMATEmployee.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tableMATEmployee.setFont(new java.awt.Font("Monospaced", 0, 20)); // NOI18N
        tableMATEmployee.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableMATEmployee.setRowHeight(25);
        tableMATEmployee.setRowMargin(5);
        jScrollPane4.setViewportView(tableMATEmployee);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );

        se.setEditable(false);
        se.setBackground(new java.awt.Color(204, 204, 204));
        se.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Selected Employee : ");
        jLabel5.setMinimumSize(new java.awt.Dimension(20, 20));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Selected Task : ");
        jLabel10.setMinimumSize(new java.awt.Dimension(20, 20));

        st.setEditable(false);
        st.setBackground(new java.awt.Color(204, 204, 204));
        st.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N

        assignTaskBtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        assignTaskBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/add.png"))); // NOI18N
        assignTaskBtn.setText("Assign Task");
        assignTaskBtn.setIconTextGap(10);
        assignTaskBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                assignTaskBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelMATLayout = new javax.swing.GroupLayout(panelMAT);
        panelMAT.setLayout(panelMATLayout);
        panelMATLayout.setHorizontalGroup(
            panelMATLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMATLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMATLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMATLayout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelMATLayout.createSequentialGroup()
                        .addGroup(panelMATLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelMATLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(st)
                            .addComponent(se)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMATLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(assignTaskBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelMATLayout.setVerticalGroup(
            panelMATLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMATLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMATLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(panelMATLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(se, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelMATLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(st, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(assignTaskBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        containerPanel.add(panelMAT, "card5");

        panelATS.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane5.setBackground(new java.awt.Color(255, 255, 255));

        ats.setEditable(false);
        ats.setColumns(20);
        ats.setFont(new java.awt.Font("Monospaced", 1, 20)); // NOI18N
        ats.setLineWrap(true);
        ats.setRows(5);
        ats.setText("==========================LEFT DOUBLE CLICK TO EXPORT LOGS=================\n==========================RIGHT DOUBLE CLICK TO CLEAR LOGS=================");
        ats.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                atsMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(ats);

        javax.swing.GroupLayout panelATSLayout = new javax.swing.GroupLayout(panelATS);
        panelATS.setLayout(panelATSLayout);
        panelATSLayout.setHorizontalGroup(
            panelATSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 934, Short.MAX_VALUE)
        );
        panelATSLayout.setVerticalGroup(
            panelATSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 657, Short.MAX_VALUE)
        );

        containerPanel.add(panelATS, "card6");

        panelSubmissions.setBackground(new java.awt.Color(255, 255, 255));

        tableSubmissions.setAutoCreateRowSorter(true);
        tableSubmissions.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tableSubmissions.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        tableSubmissions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableSubmissions.setRowHeight(50);
        tableSubmissions.setRowMargin(10);
        jScrollPane8.setViewportView(tableSubmissions);

        javax.swing.GroupLayout panelSubmissionsLayout = new javax.swing.GroupLayout(panelSubmissions);
        panelSubmissions.setLayout(panelSubmissionsLayout);
        panelSubmissionsLayout.setHorizontalGroup(
            panelSubmissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 934, Short.MAX_VALUE)
            .addGroup(panelSubmissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 934, Short.MAX_VALUE))
        );
        panelSubmissionsLayout.setVerticalGroup(
            panelSubmissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 657, Short.MAX_VALUE)
            .addGroup(panelSubmissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 657, Short.MAX_VALUE))
        );

        containerPanel.add(panelSubmissions, "card7");

        jLabel1.setFont(new java.awt.Font("Monospaced", 1, 26)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/home.png"))); // NOI18N
        jLabel1.setText("Dashboard");
        jLabel1.setIconTextGap(20);

        jLabel2.setFont(new java.awt.Font("Monospaced", 1, 22)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/employee.png"))); // NOI18N
        jLabel2.setText("Project Manager");
        jLabel2.setToolTipText("");
        jLabel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        mpc.setBackground(new java.awt.Color(204, 0, 204));

        mp.setBackground(new java.awt.Color(204, 0, 204));
        mp.setFont(new java.awt.Font("Monospaced", 1, 20)); // NOI18N
        mp.setForeground(new java.awt.Color(255, 255, 255));
        mp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/profile.png"))); // NOI18N
        mp.setText("My Profile");
        mp.setIconTextGap(5);
        mp.setOpaque(true);
        mt.setOpaque(true);
        mp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mpClicked(evt);
            }
        });

        javax.swing.GroupLayout mpcLayout = new javax.swing.GroupLayout(mpc);
        mpc.setLayout(mpcLayout);
        mpcLayout.setHorizontalGroup(
            mpcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mpcLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(mp, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
        mpcLayout.setVerticalGroup(
            mpcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mpcLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(mp, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        mtc.setBackground(new java.awt.Color(204, 0, 204));

        mt.setBackground(new java.awt.Color(204, 0, 204));
        mt.setFont(new java.awt.Font("Monospaced", 1, 20)); // NOI18N
        mt.setForeground(new java.awt.Color(255, 255, 255));
        mt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/task.png"))); // NOI18N
        mt.setText("Manage Tasks");
        mt.setIconTextGap(5);
        mt.setOpaque(true);
        mt.setOpaque(true);
        mt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mtClicked(evt);
            }
        });

        javax.swing.GroupLayout mtcLayout = new javax.swing.GroupLayout(mtc);
        mtc.setLayout(mtcLayout);
        mtcLayout.setHorizontalGroup(
            mtcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mtcLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(mt, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
        mtcLayout.setVerticalGroup(
            mtcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mtcLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(mt, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        mec.setBackground(new java.awt.Color(204, 0, 204));

        me.setBackground(new java.awt.Color(204, 0, 204));
        me.setFont(new java.awt.Font("Monospaced", 1, 20)); // NOI18N
        me.setForeground(new java.awt.Color(255, 255, 255));
        me.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/profile.png"))); // NOI18N
        me.setText("Manage Employees");
        me.setIconTextGap(5);
        me.setOpaque(true);
        mt.setOpaque(true);
        me.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                meClicked(evt);
            }
        });

        javax.swing.GroupLayout mecLayout = new javax.swing.GroupLayout(mec);
        mec.setLayout(mecLayout);
        mecLayout.setHorizontalGroup(
            mecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mecLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(me, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
        mecLayout.setVerticalGroup(
            mecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mecLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(me, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        subc.setBackground(new java.awt.Color(204, 0, 204));

        sub.setBackground(new java.awt.Color(204, 0, 204));
        sub.setFont(new java.awt.Font("Monospaced", 1, 20)); // NOI18N
        sub.setForeground(new java.awt.Color(255, 255, 255));
        sub.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/task.png"))); // NOI18N
        sub.setText("Submissions");
        sub.setIconTextGap(5);
        sub.setOpaque(true);
        mt.setOpaque(true);
        sub.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                subClicked(evt);
            }
        });

        javax.swing.GroupLayout subcLayout = new javax.swing.GroupLayout(subc);
        subc.setLayout(subcLayout);
        subcLayout.setHorizontalGroup(
            subcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subcLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(sub, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
        subcLayout.setVerticalGroup(
            subcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subcLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(sub, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        thc.setBackground(new java.awt.Color(204, 0, 204));

        th.setBackground(new java.awt.Color(204, 0, 204));
        th.setFont(new java.awt.Font("Monospaced", 1, 20)); // NOI18N
        th.setForeground(new java.awt.Color(255, 255, 255));
        th.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/history.png"))); // NOI18N
        th.setText("Manual Tasks");
        th.setIconTextGap(5);
        th.setOpaque(true);
        mt.setOpaque(true);
        th.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                thClicked(evt);
            }
        });

        javax.swing.GroupLayout thcLayout = new javax.swing.GroupLayout(thc);
        thc.setLayout(thcLayout);
        thcLayout.setHorizontalGroup(
            thcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(thcLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(th, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        thcLayout.setVerticalGroup(
            thcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(thcLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(th, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );

        atc.setBackground(new java.awt.Color(204, 0, 204));

        at.setBackground(new java.awt.Color(204, 0, 204));
        at.setFont(new java.awt.Font("Monospaced", 1, 20)); // NOI18N
        at.setForeground(new java.awt.Color(255, 255, 255));
        at.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/attendance.png"))); // NOI18N
        at.setText("ATS Logs");
        at.setIconTextGap(5);
        at.setOpaque(true);
        mt.setOpaque(true);
        at.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                atClicked(evt);
            }
        });

        javax.swing.GroupLayout atcLayout = new javax.swing.GroupLayout(atc);
        atc.setLayout(atcLayout);
        atcLayout.setHorizontalGroup(
            atcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(atcLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(at, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
        atcLayout.setVerticalGroup(
            atcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(atcLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(at, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        logoff.setBackground(new java.awt.Color(204, 0, 204));
        logoff.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/logoff.png"))); // NOI18N
        logoff.setOpaque(true);
        logoff.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoffMouseClicked(evt);
            }
        });

        welcome.setFont(new java.awt.Font("Perpetua Titling MT", 1, 20)); // NOI18N
        welcome.setForeground(new java.awt.Color(255, 255, 255));
        welcome.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        welcome.setText("Welcome, ");
        welcome.setToolTipText("");

        atsToggle.setBackground(new java.awt.Color(0, 0, 0));
        atsToggle.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        atsToggle.setForeground(new java.awt.Color(255, 255, 255));
        atsToggle.setText("Auto Task Mode : CLOSED");
        atsToggle.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        atsToggle.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        atsToggle.setOpaque(true);
        atsToggle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                atsToggleMouseClicked(evt);
            }
        });

        refreshIcon.setBackground(new java.awt.Color(204, 0, 204));
        refreshIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/refresh.png"))); // NOI18N
        refreshIcon.setOpaque(true);
        refreshIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshIconMouseClicked(evt);
            }
        });

        settings.setBackground(new java.awt.Color(204, 0, 204));
        settings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/settings.png"))); // NOI18N
        settings.setOpaque(true);
        settings.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                settingsMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout mainContainerLayout = new javax.swing.GroupLayout(mainContainer);
        mainContainer.setLayout(mainContainerLayout);
        mainContainerLayout.setHorizontalGroup(
            mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mpc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mtc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mec, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(thc, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(atc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(subc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainContainerLayout.createSequentialGroup()
                        .addComponent(welcome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(185, 185, 185)
                        .addComponent(settings)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(refreshIcon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(atsToggle)
                        .addGap(20, 20, 20)
                        .addComponent(logoff)
                        .addGap(5, 5, 5))
                    .addGroup(mainContainerLayout.createSequentialGroup()
                        .addComponent(containerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        mainContainerLayout.setVerticalGroup(
            mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainContainerLayout.createSequentialGroup()
                .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainContainerLayout.createSequentialGroup()
                        .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainContainerLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(welcome, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainContainerLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(logoff)
                                    .addComponent(atsToggle, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(refreshIcon)
                                    .addComponent(settings))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addComponent(containerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(mainContainerLayout.createSequentialGroup()
                        .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainContainerLayout.createSequentialGroup()
                                .addGap(90, 90, 90)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(mpc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(mtc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(subc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)
                                .addComponent(mec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(thc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(atc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(mainContainerLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(jLabel1)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(13, 13, 13))
        );

        dashboard.add(mainContainer, "cardMain");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dashboard, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dashboard, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void mpClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mpClicked
        mpc.setBackground(background_clicked_container);
        mtc.setBackground(background_unclicked);
        mec.setBackground(background_unclicked);
        thc.setBackground(background_unclicked);
        atc.setBackground(background_unclicked);
        subc.setBackground(background_unclicked);
        CardLayout card = (CardLayout) containerPanel.getLayout();
        card.show(containerPanel, "card2");
    }//GEN-LAST:event_mpClicked

    private void mtClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mtClicked
        mpc.setBackground(background_unclicked);
        mtc.setBackground(background_clicked_container);
        mec.setBackground(background_unclicked);
        thc.setBackground(background_unclicked);
        atc.setBackground(background_unclicked);
        subc.setBackground(background_unclicked);
        CardLayout card = (CardLayout) containerPanel.getLayout();
        card.show(containerPanel, "card3");
    }//GEN-LAST:event_mtClicked

    private void meClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_meClicked
        mpc.setBackground(background_unclicked);
        mtc.setBackground(background_unclicked);
        mec.setBackground(background_clicked_container);
        thc.setBackground(background_unclicked);
        atc.setBackground(background_unclicked);
        subc.setBackground(background_unclicked);
        CardLayout card = (CardLayout) containerPanel.getLayout();
        card.show(containerPanel, "card4");
    }//GEN-LAST:event_meClicked

    private void thClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_thClicked
        mpc.setBackground(background_unclicked);
        mtc.setBackground(background_unclicked);
        mec.setBackground(background_unclicked);
        thc.setBackground(background_clicked_container);
        atc.setBackground(background_unclicked);
        subc.setBackground(background_unclicked);
        CardLayout card = (CardLayout) containerPanel.getLayout();
        card.show(containerPanel, "card5");
    }//GEN-LAST:event_thClicked

    private void atClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_atClicked
        mpc.setBackground(background_unclicked);
        mtc.setBackground(background_unclicked);
        mec.setBackground(background_unclicked);
        thc.setBackground(background_unclicked);
        atc.setBackground(background_clicked_container);
        subc.setBackground(background_unclicked);
        CardLayout card = (CardLayout) containerPanel.getLayout();
        card.show(containerPanel, "card6");
    }//GEN-LAST:event_atClicked

    private void logoffMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoffMouseClicked
        if (_mode == Permissions.MODE_FULL) {
            if (Dialogs.confirm("Are you sure want to Logout and quit ? ")) {
                Login l = new Login();
                l.setVisible(true);
                dispose();
            }
        } else {
            dispose();
        }
    }//GEN-LAST:event_logoffMouseClicked

    private void addemployeeBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addemployeeBtnMouseClicked
        AddEmployee addEmployee = new AddEmployee();
        addEmployee.setVisible(true);
    }//GEN-LAST:event_addemployeeBtnMouseClicked

    private void assignTaskBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_assignTaskBtnActionPerformed
        String _se = se.getText();
        String _st = st.getText();
        if (_se.isEmpty() || _st.isEmpty()) {
            Dialogs.error("Some fields are missing");
            return;
        }
        TaskAssigner.doAssign(new Task(_st), new Employee(_se));
        Dialogs.success("Task Assigned Successfully");
    }//GEN-LAST:event_assignTaskBtnActionPerformed

    private void atsToggleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_atsToggleMouseClicked

        if (isATSActive) {
            if (autoTaskAssigner != null) {
                autoTaskAssigner.stop();
                atsToggle.setText("Auto Task Mode : CLOSED");
                atsToggle.setBackground(red);
            }
        } else {
            if (autoTaskAssigner == null) {
                autoTaskAssigner = new AutoTaskAssigner(Environment.department, ats);
            }
            autoTaskAssigner.start();
            atsToggle.setText("Auto Task Mode : ACTIVE");
            atsToggle.setBackground(green);
        }
        isATSActive = !isATSActive;
    }//GEN-LAST:event_atsToggleMouseClicked

    private void subClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subClicked
        mpc.setBackground(background_unclicked);
        mtc.setBackground(background_unclicked);
        mec.setBackground(background_unclicked);
        thc.setBackground(background_unclicked);
        atc.setBackground(background_unclicked);
        subc.setBackground(background_clicked_container);
        CardLayout card = (CardLayout) containerPanel.getLayout();
        card.show(containerPanel, "card7");
    }//GEN-LAST:event_subClicked

    private void addtasksBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addtasksBtnMouseClicked
        AddTask addTask = new AddTask();
        addTask.setVisible(true);
    }//GEN-LAST:event_addtasksBtnMouseClicked

    private void refreshIconMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshIconMouseClicked
        updateUI();
        updateDatabase();
    }//GEN-LAST:event_refreshIconMouseClicked

    private void settingsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsMouseClicked

        String temp = SettingsPersistance.getProperty(SettingsPersistance.KEY_COLOR_PROJECT_MANAGER);
        Color color = SettingsPersistance.StringtoColor(temp);
        String theme = SettingsPersistance.getProperty(SettingsPersistance.KEY_THEME);

        PanelSettings panelSettings = new PanelSettings(color, theme);
        panelSettings.setOnSettingsChanged(new PanelSettings.OnSettingsChanged() {
            @Override
            public void onChanged(Color color, String theme) {

                SettingsPersistance.setProperty(
                        SettingsPersistance.KEY_COLOR_PROJECT_MANAGER,
                        SettingsPersistance.ColortoString(color));

                SettingsPersistance.setProperty(
                        SettingsPersistance.KEY_THEME,
                        theme);

                background_unclicked = color;
                updateUI();
                updateDatabase();

            }
        });
        panelSettings.setVisible(true);
    }//GEN-LAST:event_settingsMouseClicked

    private void atsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_atsMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON3 && evt.getClickCount() == 2) {
            ats.setText("==========================LEFT DOUBLE CLICK TO EXPORT LOGS=================\n"
                    + "==========================RIGHT DOUBLE CLICK TO CLEAR LOGS=================");
        } else if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Location");

            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {

                try {
                    File fileToSave = fileChooser.getSelectedFile();
                    fileToSave.createNewFile();
                    FileWriter writer = new FileWriter(fileToSave);
                    writer.write(ats.getText());
                    writer.close();
                } catch (IOException e) {
                    Dialogs.error("Error while writing : " + e);
                }
            }
        }
    }//GEN-LAST:event_atsMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel addemployeeBtn;
    private javax.swing.JLabel addtasksBtn;
    private javax.swing.JButton assignTaskBtn;
    private javax.swing.JLabel at;
    private javax.swing.JPanel atc;
    private javax.swing.JTextArea ats;
    private javax.swing.JLabel atsToggle;
    private javax.swing.JPanel containerPanel;
    private javax.swing.JPanel dashboard;
    private javax.swing.JTextField department;
    private javax.swing.JTextField email;
    private javax.swing.JPanel employeeContainer;
    private javax.swing.JTextField id;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JLabel logoff;
    private javax.swing.JPanel mainContainer;
    private javax.swing.JLabel me;
    private javax.swing.JPanel mec;
    private javax.swing.JLabel mp;
    private javax.swing.JPanel mpc;
    private javax.swing.JLabel mt;
    private javax.swing.JPanel mtc;
    private javax.swing.JTextField name;
    private javax.swing.JPanel panelATS;
    private javax.swing.JPanel panelMAT;
    private javax.swing.JPanel panelMyProfile;
    private javax.swing.JPanel panelSubmissions;
    private javax.swing.JTextField password;
    private javax.swing.JPanel progressContainer;
    private javax.swing.JLabel refreshIcon;
    private javax.swing.JTextField se;
    private javax.swing.JLabel settings;
    private javax.swing.JTextField st;
    private javax.swing.JLabel sub;
    private javax.swing.JPanel subc;
    private javax.swing.JTable tableEmployees;
    private javax.swing.JTable tableMATEmployee;
    private javax.swing.JTable tableMATTasks;
    private javax.swing.JTable tableSubmissions;
    private javax.swing.JTable tableTasks;
    private javax.swing.JPanel taskContainer;
    private javax.swing.JLabel th;
    private javax.swing.JPanel thc;
    private javax.swing.JLabel welcome;
    // End of variables declaration//GEN-END:variables

}
