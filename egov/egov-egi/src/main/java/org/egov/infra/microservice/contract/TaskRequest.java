package org.egov.infra.microservice.contract;

import java.util.ArrayList;
import java.util.List;

import org.egov.infra.microservice.models.RequestInfo;

public class TaskRequest {
    private RequestInfo requestInfo = new RequestInfo();
    private List<Task> tasks = new ArrayList<Task>();
    private Task task = new Task();
    private Pagination page = new Pagination();
    public RequestInfo getRequestInfo() {
        return requestInfo;
    }
    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }
    public List<Task> getTasks() {
        return tasks;
    }
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
    public Task getTask() {
        return task;
    }
    public void setTask(Task task) {
        this.task = task;
    }
    public Pagination getPage() {
        return page;
    }
    public void setPage(Pagination page) {
        this.page = page;
    }

}