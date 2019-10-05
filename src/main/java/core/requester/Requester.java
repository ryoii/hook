package core.requester;

import core.model.Page;
import core.model.Task;

import java.io.IOException;

public interface Requester {

    Page getPage(Task task) throws IOException, InterruptedException;

}
