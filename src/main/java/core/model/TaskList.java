package core.model;

import java.util.Iterator;
import java.util.Map;

public class TaskList extends AddOnlyTaskList
        implements Iterable<Task>, MetaSetter<TaskList> {

    public TaskList() {
    }

    public TaskList(Iterable<String> urls) {
        super(urls);
    }

    public TaskList ignore() {
        data.forEach(data -> data.setIgnore(true));
        return this;
    }

    public Task get(int index) {
        return data.get(index);
    }

    public int size() {
        return data.size();
    }

    @Override
    public TaskList meta(String key, String value) {
        data.forEach(data -> data.meta(key, value));
        return this;
    }

    @Override
    public TaskList meta(Map<String, String> meta) {
        data.forEach(data -> data.meta(meta));
        return this;
    }

    @Override
    public Iterator<Task> iterator() {
        return data.iterator();
    }
}
