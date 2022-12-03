package ets.ui.frames;

import ets.listeners.Refreshable;
import ets.ui.tables.TableModelConverter;
import ets.tools.Editor;
import ets.ui.base.BaseEmployeeFrame;
import ets.ui.tables.AdapterTables;
import ets.core.Badges;
import ets.viewcontroller.ViewControllerTask;
import ets.entity.Attendance;
import ets.entity.Employee;
import ets.entity.ScoreHistory;
import ets.entity.Task;
import ets.utils.Dialogs;
import ets.entity.Department;
import ets.entity.Notification;
import ets.utils.Environment;
import ets.persistance.SettingsPersistance;
import ets.ui.tables.MyTableCellRenderer;
import ets.utils.Utils;
import ets.values.Permissions;
import ets.values.Settings;
import java.awt.CardLayout;
import java.awt.Color;
import java.util.Date;
import java.util.List;
import javax.swing.JTable;

public final class DashboardEmployee extends BaseEmployeeFrame implements Refreshable.Database, Refreshable.UI, Settings, Permissions {
    
    static {
        Environment.loadTheme();
        background_unclicked = SettingsPersistance.StringtoColor(
                SettingsPersistance.getProperty(SettingsPersistance.KEY_COLOR_EMPLOYEE));
    }
    
    private Employee _emp = null;
    private String _empID = null;
    private int _mode = -1;
    private ViewControllerTask taskViewController;
    
    @Override
    public void updateUI() {
        Environment.loadTheme();
        initColors();
        initMouseListener();
    }
    
    @Override
    public void updateDatabase() {

        //CREATE VIEW CONTROLLERS
        taskViewController = new ViewControllerTask(ctpanel);
        //=======================
        
        //SHOW PROGRESS PANEL
        CardLayout card = (CardLayout) main.getLayout();
        card.show(main, "cardProgress");

        //START BACKGROUND THREAD
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                
                try {
                    loadDatabase();
                } catch (Exception ex) {
                    System.out.println(ex);
                }

                //SHOW MAIN PANEL
                CardLayout card = (CardLayout) main.getLayout();
                card.show(main, "cardMain");
            }
        });
        t.start();
    }
    
    private void loadDatabase() throws Exception {
        //GET EMPLOYEE
        _emp = new Employee(_empID);

        //LOAD MY PROFILE
        loadProfile();

        //UPDATE WELCOME TEXT
        welcome.setText("WELCOME, " + _emp.getName().toUpperCase());

        //DISPATCH TASK DETAILS CONTROLLER
        if (_emp.getCurrent_task_id() != null) {
            taskViewController.loadTask(new Task(_emp.getCurrent_task_id()), _mode);
        } else {
            taskViewController.loadTask(new Task(), _mode);
        }

        //SET SCORE HISTORY TABLE
        List<ScoreHistory> scoreHistorys = ScoreHistory.getAllForEmployee(_emp.getId());
        ScoreHistory.Sorter.byStamp(scoreHistorys, ScoreHistory.Sorter.SORT_DESCENDING);
        tablesh.setModel(TableModelConverter.forScores(scoreHistorys));

        //SET TASK HISTORY TABLE
        List<Task> tasks = Task.getAllByEmployeeStatus(_empID, Task.STATUS_COMPLETED);
        tasks.addAll(Task.getAllByEmployeeStatus(_empID, Task.STATUS_UNDER_REVIEW));
        tasks.addAll(Task.getAllByEmployeeStatus(_empID, Task.STATUS_ASSIGNED_UNCOMPLETE));
        Task.Sorter.bySubmittedStamp(tasks, Task.Sorter.SORT_DESCENDING);
        tableth.setModel(TableModelConverter.forTasks(tasks));

        //ATTENDANCE HISTORY TABLE
        List<Attendance> attendances = Attendance.getForEmployeeId(_empID);
        Attendance.Sorter.byDate(attendances, Attendance.Sorter.SORT_DESCENDING);
        tableat.setModel(TableModelConverter.forAttendances(attendances));

        //LOAD NOTIFICATIONS
        List<Notification> notifications = Notification.getForEmployee(_empID);
        Notification.Sorter.byStamp(notifications, Notification.Sorter.SORT_DESCENDING);
        tablenot.setModel(TableModelConverter.forNotifications(notifications));
    }
    
    private void initColors() {
        mainContainer.setBackground(background_unclicked);
        mpc.setBackground(background_unclicked);
        mp.setBackground(background_unclicked);
        ctc.setBackground(background_unclicked);
        ct.setBackground(background_unclicked);
        perc.setBackground(background_unclicked);
        per.setBackground(background_unclicked);
        thc.setBackground(background_unclicked);
        th.setBackground(background_unclicked);
        atc.setBackground(background_unclicked);
        at.setBackground(background_unclicked);
        logoff.setBackground(background_unclicked);
        refreshIcon.setBackground(background_unclicked);
        settings.setBackground(background_unclicked);
        notifications.setBackground(background_unclicked);
    }
    
    private void loadProfile() {
        id.setText(_emp.getId());
        name.setText(_emp.getName());
        Department d = new Department(_emp.getDepartment_id());
        department.setText(d.getName());
        email.setText(_emp.getEmail());
        score.setText(String.valueOf(_emp.getScore()));
        password.setText(_emp.getPassword());
        doj.setText(Utils.convertStampLong(_emp.getStamp()));
        status.setText(_emp.getStatus());
        current_task.setText(_emp.getCurrent_task_id());
        lasttask.setText(Utils.convertStampLong(_emp.getLast_task_stamp()));
        
        String currentBadge = Badges.getBadgeCurrent(_emp.getScore());
        long empRank = _emp.getEmployeeScoreRank();
        
        rankLabel.setText("Score Rank : " + empRank);
        badgeLabel.setText("Badge : " + currentBadge);
        badgeIcon.setIcon(Badges.getBadgeIcon(currentBadge));
    }
    
    public DashboardEmployee(String empID, int mode) {

        //REGSITER DATABASE LISTENER
        Refreshable.register(this);
        //==========================

        //INIT PARAMETERS
        this._empID = empID;
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

        

        //MARK EMPLOYEE PRESENT
        if (_mode == Permissions.MODE_FULL) {
            markAttendance();
        }
        
        if (_mode == MODE_VIEW) {
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        }
    }
    
    private String DatetoString(Date date) {
        return date.getDate() + "/" + (date.getMonth() + 1) + "/" + (date.getYear() + 1900);
    }
    
    private void markAttendance() {
        Attendance attendance = new Attendance();
        Date date = new Date();
        attendance.setDate(DatetoString(date));
        attendance.setEmpid(_empID);
        attendance.setStatus(Attendance.STATUS_PRESENT);
        Attendance.addOrUpdate(attendance);
    }
    
    private void initMouseListener() {

        //TABLE LISTENERS===========================================
        tablesh.addMouseListener(new AdapterTables(this::scoreHistoryTable));
        tableth.addMouseListener(new AdapterTables(this::taskHistoryTable));
        tablenot.addMouseListener(new AdapterTables(this::notificationsTable));
        //==========================================================
        //BUTTONS BACKGROUND MOUSE LISTENERS===============
        mp.addMouseListener(adapterAll);
        ct.addMouseListener(adapterAll);
        per.addMouseListener(adapterAll);
        th.addMouseListener(adapterAll);
        at.addMouseListener(adapterAll);;
        logoff.addMouseListener(adapterAll);
        settings.addMouseListener(adapterAll);
        refreshIcon.addMouseListener(adapterAll);
        notifications.addMouseListener(adapterAll);
        //=================================================

        //ADD TABLE NAMES==============================================
        tableat.setName(MyTableCellRenderer.TABLE_ATTENDANCES);
        tablesh.setName(MyTableCellRenderer.TABLE_SCORE_HISTORYS);
        tableth.setName(MyTableCellRenderer.TABLE_TASK_HISTORYS);
        tablenot.setName(MyTableCellRenderer.TABLE_NOTIFICATIONS);
        //=============================================================

        //ADD RENDERER TO TABLE========================================
        MyTableCellRenderer renderer = new MyTableCellRenderer();
        tableat.setDefaultRenderer(Object.class, renderer);
        tablesh.setDefaultRenderer(Object.class, renderer);
        tableth.setDefaultRenderer(Object.class, renderer);
        tablenot.setDefaultRenderer(Object.class, renderer);
        //=============================================================

    }

    //CLICK HANDLERS FOR TABLES
    private void scoreHistoryTable(JTable table, Object data) {
        switch (table.getSelectedColumn()) {
            case 3:
                PanelTaskView panelTaskView = new PanelTaskView(data.toString(), _mode);
                panelTaskView.setVisible(true);
                break;
            default:
                Dialogs.desciption(data.toString());
        }
    }
    
    private void taskHistoryTable(JTable table, Object data) {
        switch (table.getSelectedColumn()) {
            case 0:
                PanelTaskView panelTaskView = new PanelTaskView(data.toString(), MODE_VIEW);
                panelTaskView.setVisible(true);
                break;
            default:
                Dialogs.desciption(data.toString());
        }
    }
    
    private void notificationsTable(JTable table, Object data) {
        Dialogs.desciption(data.toString());
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        main = new javax.swing.JPanel();
        mainContainer = new javax.swing.JPanel();
        mainpanel = new javax.swing.JPanel();
        mppanel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        current_task = new javax.swing.JTextField();
        lasttask = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        badgeIcon = new javax.swing.JLabel();
        rankLabel = new javax.swing.JLabel();
        badgeLabel = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        id = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        name = new javax.swing.JTextField();
        score = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        department = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        status = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        doj = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        ctpanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        deadline = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        submitted = new javax.swing.JTextField();
        entry = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        deadline1 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        score1 = new javax.swing.JTextField();
        tid = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        employee = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        id2 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        head = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        desc = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        shpanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablesh = new javax.swing.JTable();
        thpanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableth = new javax.swing.JTable();
        atpanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableat = new javax.swing.JTable();
        notpanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablenot = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        mpc = new javax.swing.JPanel();
        mp = new javax.swing.JLabel();
        ctc = new javax.swing.JPanel();
        ct = new javax.swing.JLabel();
        perc = new javax.swing.JPanel();
        per = new javax.swing.JLabel();
        thc = new javax.swing.JPanel();
        th = new javax.swing.JLabel();
        atc = new javax.swing.JPanel();
        at = new javax.swing.JLabel();
        logoff = new javax.swing.JLabel();
        welcome = new javax.swing.JLabel();
        refreshIcon = new javax.swing.JLabel();
        settings = new javax.swing.JLabel();
        notifications = new javax.swing.JLabel();
        progressContainer = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Employee Dashboard");
        setResizable(false);

        main.setLayout(new java.awt.CardLayout());

        mainContainer.setBackground(new java.awt.Color(225, 25, 25));

        mainpanel.setBackground(new java.awt.Color(255, 255, 255));
        mainpanel.setLayout(new java.awt.CardLayout());

        mppanel.setBackground(new java.awt.Color(255, 255, 255));

        jPanel5.setBackground(new java.awt.Color(250, 250, 250));

        jLabel10.setFont(new java.awt.Font("Monospaced", 1, 16)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/calendar.png"))); // NOI18N
        jLabel10.setText("Last Task : ");

        current_task.setEditable(false);
        current_task.setBackground(new java.awt.Color(245, 245, 245));
        current_task.setFont(new java.awt.Font("Monospaced", 1, 22)); // NOI18N
        current_task.setName("ecurrenttask"); // NOI18N

        lasttask.setEditable(false);
        lasttask.setBackground(new java.awt.Color(245, 245, 245));
        lasttask.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        lasttask.setName("elasttask"); // NOI18N

        jLabel14.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/tasks.png"))); // NOI18N
        jLabel14.setText("Current Task : ");

        jPanel6.setBackground(new java.awt.Color(245, 245, 245));
        jPanel6.setName("eperformancepanel"); // NOI18N

        badgeIcon.setBackground(new java.awt.Color(255, 255, 255));
        badgeIcon.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));
        badgeIcon.setName("ebadgeicon"); // NOI18N
        badgeIcon.setOpaque(true);

        rankLabel.setFont(new java.awt.Font("Monospaced", 1, 22)); // NOI18N
        rankLabel.setName("escorerank"); // NOI18N

        badgeLabel.setFont(new java.awt.Font("Monospaced", 1, 22)); // NOI18N
        badgeLabel.setName("ebadge"); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(badgeIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rankLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(badgeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(badgeIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(rankLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(badgeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(current_task)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(lasttask, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(current_task))
                .addGap(15, 15, 15)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lasttask))
                .addGap(40, 40, 40)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(250, 250, 250));

        jLabel6.setFont(new java.awt.Font("Monospaced", 1, 16)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/id.png"))); // NOI18N
        jLabel6.setText("ID : ");

        id.setEditable(false);
        id.setBackground(new java.awt.Color(245, 245, 245));
        id.setFont(new java.awt.Font("Monospaced", 1, 22)); // NOI18N
        id.setName("eid"); // NOI18N

        jLabel7.setFont(new java.awt.Font("Monospaced", 1, 16)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/name.png"))); // NOI18N
        jLabel7.setText("Name : ");

        name.setEditable(false);
        name.setBackground(new java.awt.Color(245, 245, 245));
        name.setFont(new java.awt.Font("Monospaced", 1, 22)); // NOI18N
        name.setName("ename"); // NOI18N

        score.setEditable(false);
        score.setBackground(new java.awt.Color(245, 245, 245));
        score.setFont(new java.awt.Font("Monospaced", 1, 22)); // NOI18N
        score.setName("escore"); // NOI18N

        jLabel8.setFont(new java.awt.Font("Monospaced", 1, 16)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/department.png"))); // NOI18N
        jLabel8.setText("Department: ");

        department.setEditable(false);
        department.setBackground(new java.awt.Color(245, 245, 245));
        department.setFont(new java.awt.Font("Monospaced", 1, 22)); // NOI18N
        department.setName("edept"); // NOI18N

        jLabel9.setFont(new java.awt.Font("Monospaced", 1, 16)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/score.png"))); // NOI18N
        jLabel9.setText("Score : ");

        status.setEditable(false);
        status.setBackground(new java.awt.Color(245, 245, 245));
        status.setFont(new java.awt.Font("Monospaced", 1, 22)); // NOI18N
        status.setName("estatus"); // NOI18N

        jLabel12.setFont(new java.awt.Font("Monospaced", 1, 16)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/calendar.png"))); // NOI18N
        jLabel12.setText("DOJ : ");

        jLabel13.setFont(new java.awt.Font("Monospaced", 1, 16)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/status.png"))); // NOI18N
        jLabel13.setText("Status : ");

        doj.setEditable(false);
        doj.setBackground(new java.awt.Color(245, 245, 245));
        doj.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        doj.setName("edoj"); // NOI18N

        email.setEditable(false);
        email.setBackground(new java.awt.Color(245, 245, 245));
        email.setFont(new java.awt.Font("Monospaced", 1, 22)); // NOI18N
        email.setName("eemail"); // NOI18N

        jLabel15.setFont(new java.awt.Font("Monospaced", 1, 16)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/email.png"))); // NOI18N
        jLabel15.setText("Email : ");

        jLabel11.setFont(new java.awt.Font("Monospaced", 1, 16)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/password.png"))); // NOI18N
        jLabel11.setText("Password : ");

        password.setEditable(false);
        password.setBackground(new java.awt.Color(245, 245, 245));
        password.setFont(new java.awt.Font("Monospaced", 1, 22)); // NOI18N
        password.setName("epassword"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(name))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(password)
                            .addComponent(doj)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(department))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(score))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(status))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(email)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(id))
                .addGap(15, 15, 15)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7)
                    .addComponent(name, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE))
                .addGap(15, 15, 15)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(email))
                .addGap(15, 15, 15)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(department, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(score))
                .addGap(15, 15, 15)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(doj))
                .addGap(15, 15, 15)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(status))
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout mppanelLayout = new javax.swing.GroupLayout(mppanel);
        mppanel.setLayout(mppanelLayout);
        mppanelLayout.setHorizontalGroup(
            mppanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mppanelLayout.createSequentialGroup()
                .addGap(0, 36, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        mppanelLayout.setVerticalGroup(
            mppanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mppanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(mppanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        mainpanel.add(mppanel, "card2");

        ctpanel.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(250, 250, 250));

        jLabel19.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/calendar_small.png"))); // NOI18N
        jLabel19.setText("Entry : ");
        jLabel19.setIconTextGap(15);
        jLabel19.setMinimumSize(new java.awt.Dimension(20, 20));

        deadline.setEditable(false);
        deadline.setBackground(new java.awt.Color(245, 245, 245));
        deadline.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        deadline.setName("tentry"); // NOI18N

        jLabel20.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/calendar_small.png"))); // NOI18N
        jLabel20.setText("Assigned : ");
        jLabel20.setIconTextGap(15);
        jLabel20.setMinimumSize(new java.awt.Dimension(20, 20));

        submitted.setEditable(false);
        submitted.setBackground(new java.awt.Color(245, 245, 245));
        submitted.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        submitted.setName("tsubmitted"); // NOI18N

        entry.setEditable(false);
        entry.setBackground(new java.awt.Color(245, 245, 245));
        entry.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        entry.setName("tassigned"); // NOI18N

        jLabel21.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/calendar_small.png"))); // NOI18N
        jLabel21.setText("Submitted : ");
        jLabel21.setIconTextGap(15);
        jLabel21.setMinimumSize(new java.awt.Dimension(20, 20));

        deadline1.setEditable(false);
        deadline1.setBackground(new java.awt.Color(245, 245, 245));
        deadline1.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        deadline1.setName("tdeadline"); // NOI18N

        jLabel22.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/calendar_small.png"))); // NOI18N
        jLabel22.setText("Deadline : ");
        jLabel22.setIconTextGap(15);
        jLabel22.setMinimumSize(new java.awt.Dimension(20, 20));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(deadline, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
                            .addComponent(submitted)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(entry))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deadline1)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(entry, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(submitted, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deadline, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deadline1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(250, 250, 250));

        score1.setEditable(false);
        score1.setBackground(new java.awt.Color(245, 245, 245));
        score1.setFont(new java.awt.Font("Monospaced", 1, 15)); // NOI18N
        score1.setName("tscore"); // NOI18N

        tid.setEditable(false);
        tid.setBackground(new java.awt.Color(245, 245, 245));
        tid.setFont(new java.awt.Font("Monospaced", 1, 15)); // NOI18N
        tid.setName("tid"); // NOI18N

        jLabel23.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/status_small.png"))); // NOI18N
        jLabel23.setText("Status : ");
        jLabel23.setIconTextGap(15);
        jLabel23.setMinimumSize(new java.awt.Dimension(20, 20));

        jLabel24.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/id_small.png"))); // NOI18N
        jLabel24.setText("Task ID : ");
        jLabel24.setIconTextGap(15);
        jLabel24.setMinimumSize(new java.awt.Dimension(20, 20));

        jLabel25.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/score_small.png"))); // NOI18N
        jLabel25.setText("Score : ");
        jLabel25.setIconTextGap(15);
        jLabel25.setMinimumSize(new java.awt.Dimension(20, 20));

        employee.setEditable(false);
        employee.setBackground(new java.awt.Color(245, 245, 245));
        employee.setFont(new java.awt.Font("Monospaced", 1, 15)); // NOI18N
        employee.setName("tstatus"); // NOI18N

        jLabel26.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/department_small.png"))); // NOI18N
        jLabel26.setText("Employee : ");
        jLabel26.setIconTextGap(15);
        jLabel26.setMinimumSize(new java.awt.Dimension(20, 20));

        id2.setEditable(false);
        id2.setBackground(new java.awt.Color(245, 245, 245));
        id2.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        id2.setName("temployee"); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(tid, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(employee))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(score1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(id2)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(id2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tid, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(employee, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(score1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/header.png"))); // NOI18N
        jLabel27.setText("Header : ");
        jLabel27.setIconTextGap(10);
        jLabel27.setMinimumSize(new java.awt.Dimension(20, 20));

        head.setEditable(false);
        head.setBackground(new java.awt.Color(245, 245, 245));
        head.setFont(new java.awt.Font("Monospaced", 1, 16)); // NOI18N
        head.setName("theader"); // NOI18N
        head.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                headMouseClicked(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/description.png"))); // NOI18N
        jLabel28.setText("Description : ");
        jLabel28.setIconTextGap(10);
        jLabel28.setMinimumSize(new java.awt.Dimension(20, 20));

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/response.png"))); // NOI18N
        jLabel29.setText("Response : ");
        jLabel29.setIconTextGap(10);
        jLabel29.setMinimumSize(new java.awt.Dimension(20, 20));

        jLabel30.setFont(new java.awt.Font("Monospaced", 1, 13)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(0, 102, 0));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel30.setText("Double Click on Reponse Box for EdItor/IDE");

        desc.setEditable(false);
        desc.setBackground(new java.awt.Color(245, 245, 245));
        desc.setFont(new java.awt.Font("Monospaced", 1, 16)); // NOI18N
        desc.setName("tdesc"); // NOI18N
        desc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                descMouseClicked(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Monospaced", 1, 13)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(0, 102, 0));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Double Click on Other Box for Details");

        jLabel5.setBackground(new java.awt.Color(245, 245, 245));
        jLabel5.setFont(new java.awt.Font("Monospaced", 1, 20)); // NOI18N
        jLabel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jLabel5.setName("tresponse"); // NOI18N
        jLabel5.setOpaque(true);
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout ctpanelLayout = new javax.swing.GroupLayout(ctpanel);
        ctpanel.setLayout(ctpanelLayout);
        ctpanelLayout.setHorizontalGroup(
            ctpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ctpanelLayout.createSequentialGroup()
                .addGroup(ctpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ctpanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(ctpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ctpanelLayout.createSequentialGroup()
                                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 840, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ctpanelLayout.createSequentialGroup()
                                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(desc))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ctpanelLayout.createSequentialGroup()
                                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(head))))
                    .addGroup(ctpanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        ctpanelLayout.setVerticalGroup(
            ctpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ctpanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ctpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                .addGroup(ctpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(head, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(ctpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(desc, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(ctpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(33, 33, 33)
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel31)
                .addContainerGap())
        );

        mainpanel.add(ctpanel, "card3");

        shpanel.setBackground(new java.awt.Color(250, 250, 250));

        tablesh.setAutoCreateRowSorter(true);
        tablesh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tablesh.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        tablesh.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tablesh.setName("tablescorehistory"); // NOI18N
        tablesh.setRowHeight(50);
        tablesh.setRowMargin(10);
        jScrollPane1.setViewportView(tablesh);

        javax.swing.GroupLayout shpanelLayout = new javax.swing.GroupLayout(shpanel);
        shpanel.setLayout(shpanelLayout);
        shpanelLayout.setHorizontalGroup(
            shpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1068, Short.MAX_VALUE)
        );
        shpanelLayout.setVerticalGroup(
            shpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
        );

        mainpanel.add(shpanel, "card4");

        tableth.setAutoCreateRowSorter(true);
        tableth.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tableth.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        tableth.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableth.setName("tabletaskhistory"); // NOI18N
        tableth.setRowHeight(50);
        tableth.setRowMargin(10);
        jScrollPane2.setViewportView(tableth);

        javax.swing.GroupLayout thpanelLayout = new javax.swing.GroupLayout(thpanel);
        thpanel.setLayout(thpanelLayout);
        thpanelLayout.setHorizontalGroup(
            thpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1068, Short.MAX_VALUE)
        );
        thpanelLayout.setVerticalGroup(
            thpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
        );

        mainpanel.add(thpanel, "card5");

        tableat.setAutoCreateRowSorter(true);
        tableat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tableat.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        tableat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableat.setName("tableattendance"); // NOI18N
        tableat.setRowHeight(50);
        tableat.setRowMargin(10);
        jScrollPane3.setViewportView(tableat);

        javax.swing.GroupLayout atpanelLayout = new javax.swing.GroupLayout(atpanel);
        atpanel.setLayout(atpanelLayout);
        atpanelLayout.setHorizontalGroup(
            atpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1068, Short.MAX_VALUE)
        );
        atpanelLayout.setVerticalGroup(
            atpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
        );

        mainpanel.add(atpanel, "card6");

        tablenot.setAutoCreateRowSorter(true);
        tablenot.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tablenot.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        tablenot.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tablenot.setName("tableattendance"); // NOI18N
        tablenot.setRowHeight(50);
        tablenot.setRowMargin(10);
        jScrollPane4.setViewportView(tablenot);

        javax.swing.GroupLayout notpanelLayout = new javax.swing.GroupLayout(notpanel);
        notpanel.setLayout(notpanelLayout);
        notpanelLayout.setHorizontalGroup(
            notpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1068, Short.MAX_VALUE)
        );
        notpanelLayout.setVerticalGroup(
            notpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
        );

        mainpanel.add(notpanel, "card7");

        jLabel1.setFont(new java.awt.Font("Monospaced", 1, 26)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/home.png"))); // NOI18N
        jLabel1.setText("Dashboard");
        jLabel1.setIconTextGap(20);

        jLabel2.setFont(new java.awt.Font("Monospaced", 1, 22)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/employee.png"))); // NOI18N
        jLabel2.setText("Employee");
        jLabel2.setToolTipText("");
        jLabel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        mpc.setBackground(new java.awt.Color(225, 25, 25));

        mp.setBackground(new java.awt.Color(225, 25, 25));
        mp.setFont(new java.awt.Font("Monospaced", 1, 20)); // NOI18N
        mp.setForeground(new java.awt.Color(255, 255, 255));
        mp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/profile.png"))); // NOI18N
        mp.setText("My Profile");
        mp.setIconTextGap(5);
        mp.setOpaque(true);
        ct.setOpaque(true);
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
                .addComponent(mp, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
        mpcLayout.setVerticalGroup(
            mpcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mpcLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(mp, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        ctc.setBackground(new java.awt.Color(225, 25, 25));

        ct.setBackground(new java.awt.Color(225, 25, 25));
        ct.setFont(new java.awt.Font("Monospaced", 1, 20)); // NOI18N
        ct.setForeground(new java.awt.Color(255, 255, 255));
        ct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/task.png"))); // NOI18N
        ct.setText("Current Task");
        ct.setIconTextGap(5);
        ct.setOpaque(true);
        ct.setOpaque(true);
        ct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ctClicked(evt);
            }
        });

        javax.swing.GroupLayout ctcLayout = new javax.swing.GroupLayout(ctc);
        ctc.setLayout(ctcLayout);
        ctcLayout.setHorizontalGroup(
            ctcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ctcLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(ct, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
        ctcLayout.setVerticalGroup(
            ctcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ctcLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(ct, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        perc.setBackground(new java.awt.Color(225, 25, 25));

        per.setBackground(new java.awt.Color(225, 25, 25));
        per.setFont(new java.awt.Font("Monospaced", 1, 20)); // NOI18N
        per.setForeground(new java.awt.Color(255, 255, 255));
        per.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/performance.png"))); // NOI18N
        per.setText("Score History");
        per.setIconTextGap(5);
        per.setOpaque(true);
        ct.setOpaque(true);
        per.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                perClicked(evt);
            }
        });

        javax.swing.GroupLayout percLayout = new javax.swing.GroupLayout(perc);
        perc.setLayout(percLayout);
        percLayout.setHorizontalGroup(
            percLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(percLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(per, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
        percLayout.setVerticalGroup(
            percLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(percLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(per, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        thc.setBackground(new java.awt.Color(225, 25, 25));

        th.setBackground(new java.awt.Color(225, 25, 25));
        th.setFont(new java.awt.Font("Monospaced", 1, 20)); // NOI18N
        th.setForeground(new java.awt.Color(255, 255, 255));
        th.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/history.png"))); // NOI18N
        th.setText("Task History");
        th.setIconTextGap(5);
        th.setOpaque(true);
        ct.setOpaque(true);
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
                .addComponent(th, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        thcLayout.setVerticalGroup(
            thcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(thcLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(th, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        atc.setBackground(new java.awt.Color(225, 25, 25));

        at.setBackground(new java.awt.Color(225, 25, 25));
        at.setFont(new java.awt.Font("Monospaced", 1, 20)); // NOI18N
        at.setForeground(new java.awt.Color(255, 255, 255));
        at.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/attendance.png"))); // NOI18N
        at.setText("Attendances");
        at.setIconTextGap(5);
        at.setOpaque(true);
        ct.setOpaque(true);
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
                .addComponent(at, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
        atcLayout.setVerticalGroup(
            atcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(atcLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(at, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        logoff.setBackground(new java.awt.Color(225, 25, 25));
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

        refreshIcon.setBackground(new java.awt.Color(225, 25, 25));
        refreshIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/refresh.png"))); // NOI18N
        refreshIcon.setOpaque(true);
        refreshIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshIconMouseClicked(evt);
            }
        });

        settings.setBackground(new java.awt.Color(225, 25, 25));
        settings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/settings.png"))); // NOI18N
        settings.setOpaque(true);
        settings.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                settingsMouseClicked(evt);
            }
        });

        notifications.setBackground(new java.awt.Color(225, 25, 25));
        notifications.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/notifications.png"))); // NOI18N
        notifications.setOpaque(true);
        notifications.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                notificationsMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout mainContainerLayout = new javax.swing.GroupLayout(mainContainer);
        mainContainer.setLayout(mainContainerLayout);
        mainContainerLayout.setHorizontalGroup(
            mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainContainerLayout.createSequentialGroup()
                .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainContainerLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mpc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ctc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(perc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(thc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(atc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mainpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(mainContainerLayout.createSequentialGroup()
                        .addComponent(welcome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(380, 380, 380)
                        .addComponent(notifications)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(settings)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(refreshIcon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(logoff)))
                .addGap(5, 5, 5))
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
                                .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(refreshIcon)
                                        .addComponent(settings)
                                        .addComponent(notifications))
                                    .addComponent(logoff))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addComponent(mainpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(mainContainerLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel1)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(mpc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ctc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(perc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(thc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(atc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5))
        );

        main.add(mainContainer, "cardMain");

        progressContainer.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/progress.gif"))); // NOI18N

        jLabel32.setFont(new java.awt.Font("Roboto Black", 0, 30)); // NOI18N
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("LOADING PLEASE WAIT...");

        javax.swing.GroupLayout progressContainerLayout = new javax.swing.GroupLayout(progressContainer);
        progressContainer.setLayout(progressContainerLayout);
        progressContainerLayout.setHorizontalGroup(
            progressContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, progressContainerLayout.createSequentialGroup()
                .addContainerGap(429, Short.MAX_VALUE)
                .addGroup(progressContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(progressContainerLayout.createSequentialGroup()
                        .addGap(207, 207, 207)
                        .addComponent(jLabel4))
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 503, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(398, 398, 398))
        );
        progressContainerLayout.setVerticalGroup(
            progressContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, progressContainerLayout.createSequentialGroup()
                .addContainerGap(288, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(36, 36, 36)
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(265, 265, 265))
        );

        main.add(progressContainer, "cardProgress");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(main, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(main, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void mpClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mpClicked
        mpc.setBackground(background_clicked_container);
        ctc.setBackground(background_unclicked);
        perc.setBackground(background_unclicked);
        thc.setBackground(background_unclicked);
        atc.setBackground(background_unclicked);
        CardLayout card = (CardLayout) mainpanel.getLayout();
        card.show(mainpanel, "card2");
    }//GEN-LAST:event_mpClicked

    private void ctClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctClicked
        mpc.setBackground(background_unclicked);
        ctc.setBackground(background_clicked_container);
        perc.setBackground(background_unclicked);
        thc.setBackground(background_unclicked);
        atc.setBackground(background_unclicked);
        CardLayout card = (CardLayout) mainpanel.getLayout();
        card.show(mainpanel, "card3");
    }//GEN-LAST:event_ctClicked

    private void perClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_perClicked
        mpc.setBackground(background_unclicked);
        ctc.setBackground(background_unclicked);
        perc.setBackground(background_clicked_container);
        thc.setBackground(background_unclicked);
        atc.setBackground(background_unclicked);
        CardLayout card = (CardLayout) mainpanel.getLayout();
        card.show(mainpanel, "card4");
    }//GEN-LAST:event_perClicked

    private void thClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_thClicked
        mpc.setBackground(background_unclicked);
        ctc.setBackground(background_unclicked);
        perc.setBackground(background_unclicked);
        thc.setBackground(background_clicked_container);
        atc.setBackground(background_unclicked);
        CardLayout card = (CardLayout) mainpanel.getLayout();
        card.show(mainpanel, "card5");
    }//GEN-LAST:event_thClicked

    private void atClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_atClicked
        mpc.setBackground(background_unclicked);
        ctc.setBackground(background_unclicked);
        perc.setBackground(background_unclicked);
        thc.setBackground(background_unclicked);
        atc.setBackground(background_clicked_container);
        CardLayout card = (CardLayout) mainpanel.getLayout();
        card.show(mainpanel, "card6");
    }//GEN-LAST:event_atClicked

    private void headMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_headMouseClicked
        if (evt.getClickCount() == 2) {
            Dialogs.desciption(head.getText());
        }
    }//GEN-LAST:event_headMouseClicked

    private void descMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_descMouseClicked
        if (evt.getClickCount() == 2) {
            Dialogs.desciption(desc.getText());
        }
    }//GEN-LAST:event_descMouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        if (evt.getClickCount() == 2) {
            String taskID = tid.getText();
            if (taskID != null && !taskID.isEmpty() && !taskID.equalsIgnoreCase("NULL")) {
                Editor editor = new Editor(tid.getText(), _mode);
                editor.setVisible(true);
            }
        }
    }//GEN-LAST:event_jLabel5MouseClicked

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

    private void refreshIconMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshIconMouseClicked
        updateUI();
        updateDatabase();
    }//GEN-LAST:event_refreshIconMouseClicked

    private void settingsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsMouseClicked
        
        String temp = SettingsPersistance.getProperty(SettingsPersistance.KEY_COLOR_EMPLOYEE);
        Color color = SettingsPersistance.StringtoColor(temp);
        String theme = SettingsPersistance.getProperty(SettingsPersistance.KEY_THEME);
        
        PanelSettings panelSettings = new PanelSettings(color, theme);
        panelSettings.setOnSettingsChanged(new PanelSettings.OnSettingsChanged() {
            @Override
            public void onChanged(Color color, String theme) {
                
                SettingsPersistance.setProperty(
                        SettingsPersistance.KEY_COLOR_EMPLOYEE,
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

    private void notificationsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_notificationsMouseClicked
        CardLayout card = (CardLayout) mainpanel.getLayout();
        card.show(mainpanel, "card7");
    }//GEN-LAST:event_notificationsMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel at;
    private javax.swing.JPanel atc;
    private javax.swing.JPanel atpanel;
    private javax.swing.JLabel badgeIcon;
    private javax.swing.JLabel badgeLabel;
    private javax.swing.JLabel ct;
    private javax.swing.JPanel ctc;
    public static javax.swing.JPanel ctpanel;
    private javax.swing.JTextField current_task;
    private javax.swing.JTextField deadline;
    private javax.swing.JTextField deadline1;
    private javax.swing.JTextField department;
    private javax.swing.JTextField desc;
    private javax.swing.JTextField doj;
    private javax.swing.JTextField email;
    private javax.swing.JTextField employee;
    private javax.swing.JTextField entry;
    private javax.swing.JTextField head;
    private javax.swing.JTextField id;
    private javax.swing.JTextField id2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    public javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextField lasttask;
    private javax.swing.JLabel logoff;
    private javax.swing.JPanel main;
    private javax.swing.JPanel mainContainer;
    private javax.swing.JPanel mainpanel;
    private javax.swing.JLabel mp;
    private javax.swing.JPanel mpc;
    private javax.swing.JPanel mppanel;
    private javax.swing.JTextField name;
    private javax.swing.JLabel notifications;
    private javax.swing.JPanel notpanel;
    private javax.swing.JPasswordField password;
    private javax.swing.JLabel per;
    private javax.swing.JPanel perc;
    private javax.swing.JPanel progressContainer;
    private javax.swing.JLabel rankLabel;
    private javax.swing.JLabel refreshIcon;
    private javax.swing.JTextField score;
    private javax.swing.JTextField score1;
    private javax.swing.JLabel settings;
    private javax.swing.JPanel shpanel;
    private javax.swing.JTextField status;
    private javax.swing.JTextField submitted;
    private javax.swing.JTable tableat;
    private javax.swing.JTable tablenot;
    private javax.swing.JTable tablesh;
    private javax.swing.JTable tableth;
    private javax.swing.JLabel th;
    private javax.swing.JPanel thc;
    private javax.swing.JPanel thpanel;
    private javax.swing.JTextField tid;
    private javax.swing.JLabel welcome;
    // End of variables declaration//GEN-END:variables

}
