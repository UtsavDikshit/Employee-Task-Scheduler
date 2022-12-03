package ets.core;

import ets.entity.Task;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class TaskQueue extends ArrayList<Task> {

    @Override
    public boolean add(Task task) {
        super.add(task);
        sort();
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Task> clctn) {
        for (Task t : clctn) {
            add(t);
        }
        return true;
    }

    public Task get() {
        return isEmpty() ? null : get(0);
    }

    private void sort() {
        Collections.sort(this, new Comparator<Task>() {
            @Override
            public int compare(Task t, Task t1) {
                return (int) (t.getExit() - t1.getExit());
            }
        });
    }
}
