package core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Links implements Iterable<String> {

    private List<String> data = new ArrayList<>();

    Links() {}

    Links(Collection<String> urls) {
        data.addAll(urls);
    }

    public Links filter(String regex) {
        Iterator<String> iterator = iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().matches(regex)) {
                iterator.remove();
            }
        }
        return this;
    }

    void add(String url) {
        data.add(url);
    }

    public TaskList toTaskList() {
        return new TaskList(this);
    }

    public TaskList toTaskList(String type) {
        return toTaskList().type(type);
    }

    @Override
    public Iterator<String> iterator() {
        return data.iterator();
    }
}
