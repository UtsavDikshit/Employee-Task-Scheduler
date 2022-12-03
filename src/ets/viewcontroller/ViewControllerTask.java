package ets.viewcontroller;

import ets.entity.Task;
import ets.utils.Utils;
import java.awt.Component;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class ViewControllerTask {

    private final HashMap<String, Component> map = new HashMap<>();

    public ViewControllerTask(Component component) {
        prepare(component, map);
    }

    public void loadTask(Task task, int mode) {
        ((JTextField) map.get("temployee")).setText(task.getEmpid());
        ((JTextField) map.get("tid")).setText(task.getId());
        ((JTextField) map.get("tstatus")).setText(task.getStatus());
        ((JTextField) map.get("tscore")).setText(String.valueOf(task.getScore()));
        ((JTextField) map.get("tassigned")).setText(Utils.convertStampLong(task.getAssigned()));
        ((JTextField) map.get("tsubmitted")).setText(Utils.convertStampLong(task.getSubmit()));
        ((JTextField) map.get("tentry")).setText(Utils.convertStampLong(task.getEntry()));
        ((JTextField) map.get("tdeadline")).setText(Utils.convertStampLong(task.getExit()));
        ((JTextField) map.get("theader")).setText(task.getHead());
        ((JTextField) map.get("tdesc")).setText(task.getDesc());
        if (task.getWork() == null) {
            return;
        }
        String work = "<html>" + task.getWork().replaceAll("\n", "<br>") + "</html>";
        ((JLabel) map.get("tresponse")).setText(work);
    }

    private void prepare(Component component, HashMap<String, Component> map) {
        if (component instanceof JPanel) {
            for (Component c1 : ((JPanel) component).getComponents()) {
                if (c1 instanceof JPanel || c1 instanceof JScrollPane) {
                    prepare(c1, map);
                } else if (c1.getName() != null) {
                    map.put(c1.getName(), c1);
                }
            }
        }
    }
}
