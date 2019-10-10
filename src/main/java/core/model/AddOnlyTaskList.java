package core.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class AddOnlyTaskList {

    List<Task> data = new LinkedList<>();

    AddOnlyTaskList() {
    }

    AddOnlyTaskList(Iterable<String> urls) {
        add(urls);
    }

    public AddOnlyTaskList add(Task task) {
        data.add(task);
        return this;
    }

    public AddOnlyTaskList add(Collection<String> urls) {
        for (String url : urls) {
            data.add(new Task(url));
        }
        return this;
    }

    public AddOnlyTaskList add(Iterable<String> urls) {
        for (String url : urls) {
            data.add(new Task(url));
        }
        return this;
    }

    public AddOnlyTaskList add(TaskList taskList) {
        for (Task task : taskList) {
            data.add(task);
        }
        return this;
    }
}
