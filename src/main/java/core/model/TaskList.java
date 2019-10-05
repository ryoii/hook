package core.model;

import java.util.Iterator;

public class TaskList extends AddOnlyTaskList
        implements Iterable<Task>, MetaSetter<TaskList>{

    public TaskList() {}

    public TaskList(Iterable<String> urls) {
        super(urls);
    }

    public TaskList ignore() {
        data.forEach(data -> data.setIgnore(true));
        return this;
    }

    @Override
    public TaskList meta(String key, String value) {
        data.forEach(data -> data.meta(key, value));
        return this;
    }

    @Override
    public Iterator<Task> iterator() {
        return data.iterator();
    }
}
