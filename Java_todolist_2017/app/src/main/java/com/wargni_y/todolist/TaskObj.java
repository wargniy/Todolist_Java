package com.wargni_y.todolist;

/**
 * Created by wargni_y on 04/02/2018.
 */

public class TaskObj {
    private final String task;
    private final String desc;
    private final String time;
    private final String done;

    public TaskObj(String task, String desc, String time, String done){
        this.task = task;
        this.desc = desc;
        this.time = time;
        this.done = done;
    }

    public String getTask(){
        return task;
    }

    public String getDescription(){
        return desc;
    }

    public String getDate() { return time; }

    public String getDone() { return done; }

}
