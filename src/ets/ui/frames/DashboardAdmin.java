package ets.ui.frames;

import ets.db.Database;
import ets.listeners.Refreshable;
import ets.ui.tables.TableModelConverter;
import ets.ui.base.BaseAdminFrame;
import ets.ui.tables.AdapterTables;
import ets.entity.Admin;
import ets.entity.Department;
import ets.entity.ProjectManager;
import ets.utils.Dialogs;
import ets.utils.Environment;
import ets.persistance.SettingsPersistance;
import ets.utils.Utils;
import ets.values.Permissions;
import ets.values.Settings;
import java.awt.CardLayout;
import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JTable;

public final class DashboardAdmin extends BaseAdminFrame implements Refreshable.Database, Refreshable.UI, Settings, Permissions {

    static {
        Environment.loadTheme();
        background_unclicked = SettingsPersistance.StringtoColor(
                SettingsPersistance.getProperty(SettingsPersistance.KEY_COLOR_ADMIN));
    }

    private Admin admin = null;
    private String _adminID = "";
    private int _mode = -1;

    @Override
    public void updateUI() {
        Environment.loadTheme();
        initColors();
        initMouseListener();
    }

    @Override
    public void updateDatabase() {

        //SHOW PROGRESS PANEL
        CardLayout card = (CardLayout) base.getLayout();
        card.show(base, "cardProgress");

        //START BACKGROUND THREAD
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    loadDatabase();
                } catch (Exception ex) {
                }

                //SHOW MAIN PANEL
                CardLayout card = (CardLayout) base.getLayout();
                card.show(base, "cardMain");
            }
        });
        t.start();
    }

    private void initColors() {
        mainContainer.setBackground(background_unclicked);
        mpco.setBackground(background_unclicked);
        mpo.setBackground(background_unclicked);
        mpmco.setBackground(background_unclicked);
        mpmo.setBackground(background_unclicked);
        sqldc.setBackground(background_unclicked);
        sqld.setBackground(background_unclicked);
        mdco.setBackground(background_unclicked);
        mdo.setBackground(background_unclicked);
        logoff.setBackground(background_unclicked);
        refreshIcon.setBackground(background_unclicked);
        settings.setBackground(background_unclicked);
        addpm.setBackground(general_background_unclicked);
        adddept.setBackground(general_background_unclicked);
    }

    private void loadDatabase() throws Exception {
        //GET PROJECT MANAGER
        admin = new Admin(_adminID);

        //UPDATE MY PROFILE
        loadProfile();

        //UPDATE WELCOME TEXT
        welcome.setText("WELCOME, " + admin.getName().toUpperCase());

        //UPDATE PROJECT MANAGERS
        List<ProjectManager> projectManagers = ProjectManager.getAll();
        tablepm.setModel(TableModelConverter.forProjectManagers(projectManagers));

        //UPDATE DEPARTMENTS
        List<Department> departments = Department.getAll();
        tabledept.setModel(TableModelConverter.forDepartments(departments));
    }

    private void loadProfile() throws Exception {
        id.setText(admin.getId());
        name.setText(admin.getName());
        password.setText(admin.getPassword());
        email.setText(admin.getEmail());
    }

    @Override
    public void dispose() {
        Refreshable.unregister(this);
    }
    
    public DashboardAdmin(String adminID, int mode) {

        //REGSITER DATABASE LISTENER
        Refreshable.register(this);
        //==========================
        
        //INIT PARAMETERS========
        this._adminID = adminID;
        this._mode = mode;
        //=======================

        //LOAD DATA AND UI
        initComponents();
        updateUI();
        updateDatabase();
        //================

        //LOAD MY PROFILE BY DEFAULT
        mpoClicked(null);

    }

    private void initMouseListener() {

        //BUTTONS BACKGROUND MOUSE LISTENERS===============
        mpo.addMouseListener(adapterAll);
        mpmo.addMouseListener(adapterAll);
        mdo.addMouseListener(adapterAll);
        sqld.addMouseListener(adapterAll);
        logoff.addMouseListener(adapterAll);
        refreshIcon.addMouseListener(adapterAll);
        settings.addMouseListener(adapterAll);
        adddept.addMouseListener(adapterButtons);
        addpm.addMouseListener(adapterButtons);
        //=================================================

        //TABLE LISTENERS==================================
        tablepm.addMouseListener(new AdapterTables(this::projectManagerTable));
        //=================================================

    }

    //CLICK HANDLERS FOR TABLES
    public void projectManagerTable(JTable table, Object data) {
        switch (table.getSelectedColumn()) {
            case 0:
                Environment.department = new ProjectManager(data.toString()).getDepartment_id();
                DashboardProjectManager dpm = new DashboardProjectManager(data.toString(), MODE_FULL);
                dpm.setVisible(true);
                break;
            default:
                Dialogs.desciption(data.toString());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        base = new javax.swing.JPanel();
        mainContainer = new javax.swing.JPanel();
        mainpanel = new javax.swing.JPanel();
        mpc = new javax.swing.JPanel();
        id = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        name = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        password = new javax.swing.JTextField();
        mpmc = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablepm = new javax.swing.JTable();
        addpm = new javax.swing.JLabel();
        mdc = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabledept = new javax.swing.JTable();
        adddept = new javax.swing.JLabel();
        sqlpanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        query = new javax.swing.JTextArea();
        fetchBtn = new javax.swing.JButton();
        executeBtn = new javax.swing.JButton();
        commitBtn = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        status = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        mpco = new javax.swing.JPanel();
        mpo = new javax.swing.JLabel();
        mpmco = new javax.swing.JPanel();
        mpmo = new javax.swing.JLabel();
        sqldc = new javax.swing.JPanel();
        sqld = new javax.swing.JLabel();
        mdco = new javax.swing.JPanel();
        mdo = new javax.swing.JLabel();
        logoff = new javax.swing.JLabel();
        welcome = new javax.swing.JLabel();
        refreshIcon = new javax.swing.JLabel();
        settings = new javax.swing.JLabel();
        progressContainer = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Admin Dashboard");
        setResizable(false);

        base.setLayout(new java.awt.CardLayout());

        mainContainer.setBackground(new java.awt.Color(0, 153, 153));

        mainpanel.setBackground(new java.awt.Color(255, 255, 255));
        mainpanel.setLayout(new java.awt.CardLayout());

        mpc.setBackground(new java.awt.Color(255, 255, 255));

        id.setEditable(false);
        id.setBackground(new java.awt.Color(204, 204, 204));
        id.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        id.setName("aid"); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/email.png"))); // NOI18N
        jLabel3.setText("Email : ");
        jLabel3.setIconTextGap(10);
        jLabel3.setMinimumSize(new java.awt.Dimension(20, 20));

        name.setEditable(false);
        name.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        name.setName("aname"); // NOI18N

        email.setEditable(false);
        email.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        email.setName("aemail"); // NOI18N

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/id.png"))); // NOI18N
        jLabel32.setText("ID : ");
        jLabel32.setIconTextGap(10);

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/name.png"))); // NOI18N
        jLabel33.setText("Name : ");
        jLabel33.setIconTextGap(10);
        jLabel33.setMinimumSize(new java.awt.Dimension(20, 20));

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/password.png"))); // NOI18N
        jLabel34.setText("Password : ");
        jLabel34.setIconTextGap(10);
        jLabel34.setMinimumSize(new java.awt.Dimension(20, 20));

        password.setEditable(false);
        password.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        password.setName("apassword"); // NOI18N

        javax.swing.GroupLayout mpcLayout = new javax.swing.GroupLayout(mpc);
        mpc.setLayout(mpcLayout);
        mpcLayout.setHorizontalGroup(
            mpcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mpcLayout.createSequentialGroup()
                .addContainerGap(241, Short.MAX_VALUE)
                .addGroup(mpcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mpcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(106, 106, 106))
        );
        mpcLayout.setVerticalGroup(
            mpcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mpcLayout.createSequentialGroup()
                .addGap(150, 150, 150)
                .addGroup(mpcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(mpcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(mpcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(mpcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(275, Short.MAX_VALUE))
        );

        mainpanel.add(mpc, "card2");

        mpmc.setBackground(new java.awt.Color(250, 250, 250));

        tablepm.setAutoCreateRowSorter(true);
        tablepm.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        tablepm.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tablepm.setName("projectmanager"); // NOI18N
        tablepm.setRowHeight(50);
        tablepm.setRowMargin(10);
        jScrollPane4.setViewportView(tablepm);

        addpm.setBackground(new java.awt.Color(255, 255, 255));
        addpm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/plus.png"))); // NOI18N
        addpm.setOpaque(true);
        addpm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addpmMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout mpmcLayout = new javax.swing.GroupLayout(mpmc);
        mpmc.setLayout(mpmcLayout);
        mpmcLayout.setHorizontalGroup(
            mpmcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 927, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mpmcLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(addpm))
        );
        mpmcLayout.setVerticalGroup(
            mpmcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mpmcLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(addpm)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 626, Short.MAX_VALUE))
        );

        mainpanel.add(mpmc, "card3");

        mdc.setBackground(new java.awt.Color(255, 255, 255));

        tabledept.setAutoCreateRowSorter(true);
        tabledept.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        tabledept.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabledept.setName("departments"); // NOI18N
        tabledept.setRowHeight(50);
        tabledept.setRowMargin(10);
        jScrollPane3.setViewportView(tabledept);

        adddept.setBackground(new java.awt.Color(255, 255, 255));
        adddept.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/plus.png"))); // NOI18N
        adddept.setOpaque(true);
        adddept.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                adddeptMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout mdcLayout = new javax.swing.GroupLayout(mdc);
        mdc.setLayout(mdcLayout);
        mdcLayout.setHorizontalGroup(
            mdcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 927, Short.MAX_VALUE)
            .addGroup(mdcLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(adddept))
        );
        mdcLayout.setVerticalGroup(
            mdcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mdcLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(adddept)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 626, Short.MAX_VALUE))
        );

        mainpanel.add(mdc, "card4");

        sqlpanel.setBackground(new java.awt.Color(255, 255, 255));

        query.setColumns(20);
        query.setFont(new java.awt.Font("Monospaced", 0, 16)); // NOI18N
        query.setRows(5);
        query.setText("show tables;");
        query.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "SQL Queries Here"));
        jScrollPane1.setViewportView(query);

        fetchBtn.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        fetchBtn.setText("Fetch");
        fetchBtn.setToolTipText("For Select Queries");
        fetchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fetchBtnActionPerformed(evt);
            }
        });

        executeBtn.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        executeBtn.setText("Execute");
        executeBtn.setToolTipText("For General Queries");
        executeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executeBtnActionPerformed(evt);
            }
        });

        commitBtn.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        commitBtn.setText("Commit");
        commitBtn.setToolTipText("For Insert,Update,Delete etc Queries");
        commitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commitBtnActionPerformed(evt);
            }
        });

        status.setEditable(false);
        status.setBackground(new java.awt.Color(0, 102, 153));
        status.setColumns(20);
        status.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        status.setForeground(new java.awt.Color(255, 255, 255));
        status.setLineWrap(true);
        status.setRows(5);
        jScrollPane5.setViewportView(status);

        table.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        table.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        table.setRowHeight(30);
        table.setRowMargin(4);
        jScrollPane2.setViewportView(table);

        javax.swing.GroupLayout sqlpanelLayout = new javax.swing.GroupLayout(sqlpanel);
        sqlpanel.setLayout(sqlpanelLayout);
        sqlpanelLayout.setHorizontalGroup(
            sqlpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sqlpanelLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(sqlpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(sqlpanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addGap(5, 5, 5))
                    .addGroup(sqlpanelLayout.createSequentialGroup()
                        .addGroup(sqlpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 917, Short.MAX_VALUE)
                            .addGroup(sqlpanelLayout.createSequentialGroup()
                                .addGroup(sqlpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fetchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(executeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(commitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane5)))
                        .addGap(5, 5, 5))))
        );
        sqlpanelLayout.setVerticalGroup(
            sqlpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sqlpanelLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(sqlpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(sqlpanelLayout.createSequentialGroup()
                        .addComponent(fetchBtn)
                        .addGap(13, 13, 13)
                        .addComponent(executeBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(commitBtn))
                    .addComponent(jScrollPane5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2)
                .addGap(7, 7, 7))
        );

        mainpanel.add(sqlpanel, "card5");

        jLabel1.setFont(new java.awt.Font("Monospaced", 1, 26)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/home.png"))); // NOI18N
        jLabel1.setText("Dashboard");
        jLabel1.setIconTextGap(20);

        jLabel2.setFont(new java.awt.Font("Monospaced", 1, 22)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/employee.png"))); // NOI18N
        jLabel2.setText("Administrator");
        jLabel2.setToolTipText("");
        jLabel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        mpco.setBackground(new java.awt.Color(0, 153, 153));

        mpo.setBackground(new java.awt.Color(0, 153, 153));
        mpo.setFont(new java.awt.Font("Monospaced", 1, 16)); // NOI18N
        mpo.setForeground(new java.awt.Color(255, 255, 255));
        mpo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/profile.png"))); // NOI18N
        mpo.setText("My Profile");
        mpo.setIconTextGap(5);
        mpo.setOpaque(true);
        mpmo.setOpaque(true);
        mpo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mpoClicked(evt);
            }
        });

        javax.swing.GroupLayout mpcoLayout = new javax.swing.GroupLayout(mpco);
        mpco.setLayout(mpcoLayout);
        mpcoLayout.setHorizontalGroup(
            mpcoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mpcoLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(mpo, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
        mpcoLayout.setVerticalGroup(
            mpcoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mpcoLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(mpo, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        mpmco.setBackground(new java.awt.Color(0, 153, 153));

        mpmo.setBackground(new java.awt.Color(0, 153, 153));
        mpmo.setFont(new java.awt.Font("Monospaced", 1, 16)); // NOI18N
        mpmo.setForeground(new java.awt.Color(255, 255, 255));
        mpmo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/task.png"))); // NOI18N
        mpmo.setText("Manage Project Managers");
        mpmo.setIconTextGap(5);
        mpmo.setOpaque(true);
        mpmo.setOpaque(true);
        mpmo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mpmoClicked(evt);
            }
        });

        javax.swing.GroupLayout mpmcoLayout = new javax.swing.GroupLayout(mpmco);
        mpmco.setLayout(mpmcoLayout);
        mpmcoLayout.setHorizontalGroup(
            mpmcoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mpmcoLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(mpmo, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
        mpmcoLayout.setVerticalGroup(
            mpmcoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mpmcoLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(mpmo, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        sqldc.setBackground(new java.awt.Color(0, 153, 153));

        sqld.setBackground(new java.awt.Color(0, 153, 153));
        sqld.setFont(new java.awt.Font("Monospaced", 1, 16)); // NOI18N
        sqld.setForeground(new java.awt.Color(255, 255, 255));
        sqld.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/database.png"))); // NOI18N
        sqld.setText(" SQL Database");
        sqld.setIconTextGap(5);
        sqld.setOpaque(true);
        mpmo.setOpaque(true);
        sqld.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sqldClicked(evt);
            }
        });

        javax.swing.GroupLayout sqldcLayout = new javax.swing.GroupLayout(sqldc);
        sqldc.setLayout(sqldcLayout);
        sqldcLayout.setHorizontalGroup(
            sqldcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sqldcLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(sqld, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
        sqldcLayout.setVerticalGroup(
            sqldcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sqldcLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(sqld, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        mdco.setBackground(new java.awt.Color(0, 153, 153));

        mdo.setBackground(new java.awt.Color(0, 153, 153));
        mdo.setFont(new java.awt.Font("Monospaced", 1, 16)); // NOI18N
        mdo.setForeground(new java.awt.Color(255, 255, 255));
        mdo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/performance.png"))); // NOI18N
        mdo.setText("Manage Departments");
        mdo.setIconTextGap(5);
        mdo.setOpaque(true);
        mpmo.setOpaque(true);
        mdo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mdoClicked(evt);
            }
        });

        javax.swing.GroupLayout mdcoLayout = new javax.swing.GroupLayout(mdco);
        mdco.setLayout(mdcoLayout);
        mdcoLayout.setHorizontalGroup(
            mdcoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mdcoLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(mdo, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
        mdcoLayout.setVerticalGroup(
            mdcoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mdcoLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(mdo, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        logoff.setBackground(new java.awt.Color(0, 153, 153));
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

        refreshIcon.setBackground(new java.awt.Color(0, 153, 153));
        refreshIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/refresh.png"))); // NOI18N
        refreshIcon.setOpaque(true);
        refreshIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshIconMouseClicked(evt);
            }
        });

        settings.setBackground(new java.awt.Color(0, 153, 153));
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
                .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(mpco, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(mpmco, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(mdco, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(sqldc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mainpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(mainContainerLayout.createSequentialGroup()
                        .addComponent(welcome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(428, 428, 428)
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
                                        .addComponent(settings))
                                    .addComponent(logoff))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addComponent(mainpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(mainContainerLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel1)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(mpco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(mpmco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(mdco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(sqldc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(5, 5, 5))
        );

        base.add(mainContainer, "cardMain");

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
                .addContainerGap(397, Short.MAX_VALUE))
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

        base.add(progressContainer, "cardProgress");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(base, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(base, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void mpoClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mpoClicked
        mpco.setBackground(background_clicked_container);
        mpmco.setBackground(background_unclicked);
        mdco.setBackground(background_unclicked);
        sqldc.setBackground(background_unclicked);
        CardLayout card = (CardLayout) mainpanel.getLayout();
        card.show(mainpanel, "card2");
    }//GEN-LAST:event_mpoClicked

    private void mpmoClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mpmoClicked
        mpco.setBackground(background_unclicked);
        mpmco.setBackground(background_clicked_container);
        mdco.setBackground(background_unclicked);
        sqldc.setBackground(background_unclicked);
        CardLayout card = (CardLayout) mainpanel.getLayout();
        card.show(mainpanel, "card3");
    }//GEN-LAST:event_mpmoClicked

    private void mdoClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mdoClicked
        mpco.setBackground(background_unclicked);
        mpmco.setBackground(background_unclicked);
        sqldc.setBackground(background_unclicked);
        mdco.setBackground(background_clicked_container);
        CardLayout card = (CardLayout) mainpanel.getLayout();
        card.show(mainpanel, "card4");
    }//GEN-LAST:event_mdoClicked

    private void logoffMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoffMouseClicked
        if (Dialogs.confirm("Are you sure want to Logout and quit ? ")) {
            Login l = new Login();
            l.setVisible(true);
            dispose();
        }
    }//GEN-LAST:event_logoffMouseClicked

    private void adddeptMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_adddeptMouseClicked
        AddDepartment addDepartment = new AddDepartment();
        addDepartment.setVisible(true);
    }//GEN-LAST:event_adddeptMouseClicked

    private void addpmMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addpmMouseClicked
        AddProjectManager addProjectManager = new AddProjectManager();
        addProjectManager.setVisible(true);
    }//GEN-LAST:event_addpmMouseClicked

    private void refreshIconMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshIconMouseClicked
        updateUI();
        updateDatabase();
    }//GEN-LAST:event_refreshIconMouseClicked

    private void settingsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsMouseClicked

        String temp = SettingsPersistance.getProperty(SettingsPersistance.KEY_COLOR_ADMIN);
        Color color = SettingsPersistance.StringtoColor(temp);
        String theme = SettingsPersistance.getProperty(SettingsPersistance.KEY_THEME);

        PanelSettings panelSettings = new PanelSettings(color, theme);
        panelSettings.setOnSettingsChanged(new PanelSettings.OnSettingsChanged() {
            @Override
            public void onChanged(Color color, String theme) {

                SettingsPersistance.setProperty(
                        SettingsPersistance.KEY_COLOR_ADMIN,
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

    private void sqldClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sqldClicked
        mpco.setBackground(background_unclicked);
        mpmco.setBackground(background_unclicked);
        sqldc.setBackground(background_clicked_container);
        mdco.setBackground(background_unclicked);
        CardLayout card = (CardLayout) mainpanel.getLayout();
        card.show(mainpanel, "card5");
    }//GEN-LAST:event_sqldClicked

    private void fetchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fetchBtnActionPerformed
        try {
            String QUERY = query.getText();
            ResultSet rs = Database.getConnection().createStatement().executeQuery(QUERY);
            table.setModel(Utils.rs_to_tm(rs));
            status.setText("Fetched Successfully");
        } catch (SQLException ex) {
            status.setText("Error : " + ex.getMessage());
        }
    }//GEN-LAST:event_fetchBtnActionPerformed

    private void executeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executeBtnActionPerformed
        try {
            String QUERY = query.getText();
            Connection connection = Database.getConnection();
            boolean isSuccess = connection.createStatement().execute(QUERY);
            status.setText("Result : " + isSuccess);
            connection.commit();
        } catch (SQLException ex) {
            status.setText("Error : " + ex.getMessage());
        }
    }//GEN-LAST:event_executeBtnActionPerformed

    private void commitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_commitBtnActionPerformed
        try {
            String QUERY = query.getText();
            Connection connection = Database.getConnection();
            int rows = connection.createStatement().executeUpdate(QUERY);
            status.setText(rows + " row(s) affected.");
            connection.commit();
        } catch (SQLException ex) {
            status.setText("Error : " + ex.getMessage());
        }
    }//GEN-LAST:event_commitBtnActionPerformed
 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel adddept;
    private javax.swing.JLabel addpm;
    private javax.swing.JPanel base;
    private javax.swing.JButton commitBtn;
    private javax.swing.JTextField email;
    private javax.swing.JButton executeBtn;
    private javax.swing.JButton fetchBtn;
    private javax.swing.JTextField id;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel logoff;
    private javax.swing.JPanel mainContainer;
    private javax.swing.JPanel mainpanel;
    private javax.swing.JPanel mdc;
    private javax.swing.JPanel mdco;
    private javax.swing.JLabel mdo;
    private javax.swing.JPanel mpc;
    private javax.swing.JPanel mpco;
    private javax.swing.JPanel mpmc;
    private javax.swing.JPanel mpmco;
    private javax.swing.JLabel mpmo;
    private javax.swing.JLabel mpo;
    private javax.swing.JTextField name;
    private javax.swing.JTextField password;
    private javax.swing.JPanel progressContainer;
    private javax.swing.JTextArea query;
    private javax.swing.JLabel refreshIcon;
    private javax.swing.JLabel settings;
    private javax.swing.JLabel sqld;
    private javax.swing.JPanel sqldc;
    private javax.swing.JPanel sqlpanel;
    public static javax.swing.JTextArea status;
    private javax.swing.JTable table;
    private javax.swing.JTable tabledept;
    private javax.swing.JTable tablepm;
    private javax.swing.JLabel welcome;
    // End of variables declaration//GEN-END:variables
}
