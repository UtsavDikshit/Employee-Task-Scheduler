package ets.tools;

import ets.core.NotificationParser;
import ets.entity.Employee;
import ets.entity.Notification;
import ets.entity.Task;
import ets.error.ExceptionHandler;
import ets.utils.Dialogs;
import ets.utils.Utils;
import ets.values.Permissions;
import ets.values.Settings;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;
import say.swing.JFontChooser;

public class Editor extends JFrame implements ActionListener, Settings, Permissions, SyntaxConstants {
    
    private RSyntaxTextArea textArea = null;
    private Task task = null;
    private int _mode = MODE_VIEW;
    
    public Editor(String taskID, int mode) {

        //LOAD TASK
        this.task = new Task(taskID);

        //OVERRIDE PERMISSIONS
        switch (task.getStatus()) {
            case Task.STATUS_COMPLETED:
            case Task.STATUS_UNASSIGNED:
            case Task.STATUS_UNDER_REVIEW:
                _mode = MODE_VIEW;
                break;
            case Task.STATUS_ASSIGNED_UNCOMPLETE:
                _mode = mode;
                break;
        }

        //INIT IDE OR WRITING TEXTAREA
        JPanel panel = new JPanel(new BorderLayout());
        textArea = new RSyntaxTextArea(40, 200);
        textArea.setCodeFoldingEnabled(true);
        textArea.setText(task.getWork());

        //DEFAULT SETTINGS
        textArea.setSyntaxEditingStyle(SYNTAX_STYLE_JAVA);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 22));
        //======================

        //ADD SCROLLBARS
        RTextScrollPane sp = new RTextScrollPane(textArea);
        panel.add(sp);

        //LOAD MENU BAR
        loadMenuBar();

        //CHECK PERMISSIONS
        if (_mode == MODE_VIEW) {
            textArea.setEnabled(false);
        }

        //INIT FRAME ATTRIBUTES
        setExtendedState(MAXIMIZED_BOTH);
        setContentPane(panel);
        setTitle("Editor/IDE");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }
    
    private void loadMenuBar() {
        
        JMenuBar mb = new JMenuBar();
        
        JMenu m1 = new JMenu("File");
        JMenu m2 = new JMenu("Settings");
        
        JMenuItem save = new JMenuItem("Save");
        JMenuItem submit = new JMenuItem("Submit");
        JMenuItem font = new JMenuItem("Font");
        JMenuItem language = new JMenuItem("Syntax Style");
        
        m1.add(save);
        m1.add(submit);
        m2.add(font);
        m2.add(language);
        
        mb.add(m1);
        mb.add(m2);
        
        save.addActionListener(this);
        submit.addActionListener(this);
        font.addActionListener(this);
        language.addActionListener(this);
        
        setJMenuBar(mb);
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (_mode == MODE_VIEW) {
            return;
        }
        
        switch (ae.getActionCommand()) {
            case "Font":
                fontSettings();
                break;
            
            case "Save":
                save();
                break;
            
            case "Submit":
                submit();
                break;
            
            case "Syntax Style":
                syntaxStyle();
                break;
        }
        
    }
    
    private void save() {
        try {
            
            String response = textArea.getText();
            response = response.replaceAll("\"", "\\\"");
            
            task.setWork(response);
            task.update();
            
            Dialogs.success("Saved Successfully...");
        } catch (Exception ex) {
            Dialogs.error("Error while submitting\nCheck logs for more details");
            ExceptionHandler.exception(ex);
        }
    }
    
    private void fontSettings() {
        JFontChooser fontChooser = new JFontChooser();
        int result = fontChooser.showDialog(textArea);
        if (result == JFontChooser.OK_OPTION) {
            Font font = fontChooser.getSelectedFont();
            textArea.setFont(font);
        }
    }
    
    private void submit() {
        try {
            if (Dialogs.confirm("Are you sure want to submit and quit ?")) {
                
                String response = textArea.getText();
                //TODO
                task.setWork(response);
                task.setStatus(Task.STATUS_UNDER_REVIEW);
                task.setSubmit(System.currentTimeMillis());
                task.update();
                
                Employee e = new Employee(task.getEmpid());
                String message = Utils.readFile(NotificationParser.PATH_SUBMITTED_TASK_NOTIFICATION);
                message = NotificationParser.parse(message, task, e);
                Notification notification = new Notification();
                notification.setType(Notification.TYPE_TASK_SUBMITTED);
                notification.setEmpid(e.getId());
                notification.setRemarks("None");
                notification.setStamp(System.currentTimeMillis());
                notification.setDescription(message);
                Notification.saveNotification(notification);
                
                Dialogs.success("Work sent for review to Project Manager");
                dispose();
                
            }
        } catch (Exception ex) {
            ExceptionHandler.exception(ex);
            Dialogs.error("Critical Error Occured. Kindly contact system admin for details");
            System.exit(5);
        }
    }
    
    private void syntaxStyle() {
        String[] options = {
            SYNTAX_STYLE_NONE, SYNTAX_STYLE_ACTIONSCRIPT, SYNTAX_STYLE_ASSEMBLER_X86, SYNTAX_STYLE_BBCODE, SYNTAX_STYLE_C, SYNTAX_STYLE_CLOJURE, SYNTAX_STYLE_CPLUSPLUS, SYNTAX_STYLE_CSHARP, SYNTAX_STYLE_CSS, SYNTAX_STYLE_DELPHI, SYNTAX_STYLE_DTD, SYNTAX_STYLE_FORTRAN, SYNTAX_STYLE_GROOVY, SYNTAX_STYLE_HTML, SYNTAX_STYLE_JAVA, SYNTAX_STYLE_JAVASCRIPT, SYNTAX_STYLE_JSON, SYNTAX_STYLE_JSP, SYNTAX_STYLE_LATEX, SYNTAX_STYLE_LISP, SYNTAX_STYLE_LUA, SYNTAX_STYLE_MAKEFILE, SYNTAX_STYLE_MXML, SYNTAX_STYLE_NSIS, SYNTAX_STYLE_PERL, SYNTAX_STYLE_PHP, SYNTAX_STYLE_PROPERTIES_FILE, SYNTAX_STYLE_PYTHON, SYNTAX_STYLE_RUBY, SYNTAX_STYLE_SAS, SYNTAX_STYLE_SCALA, SYNTAX_STYLE_SQL, SYNTAX_STYLE_TCL, SYNTAX_STYLE_UNIX_SHELL, SYNTAX_STYLE_WINDOWS_BATCH, SYNTAX_STYLE_XML
        };
        
        Dialogs.listItems(options, "Select Syntax Style", new Dialogs.onValueInput() {
            @Override
            public void onInput(String value) {
                if (value != null && !value.isEmpty()) {
                    textArea.setSyntaxEditingStyle(value);
                }
            }
        });
    }
    
}
