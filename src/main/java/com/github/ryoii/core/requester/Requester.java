package com.github.ryoii.core.requester;

import com.github.ryoii.core.model.Page;
import com.github.ryoii.core.model.Task;

import java.io.IOException;

public interface Requester {

    Page getPage(Task task) throws IOException, InterruptedException;

}
