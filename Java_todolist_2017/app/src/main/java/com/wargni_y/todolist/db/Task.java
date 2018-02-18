package com.wargni_y.todolist.db;

/**
 * Created by wargni_y on 04/02/2018.
 */


import android.provider.BaseColumns;

public class Task {
    public static final String DB_NAME = "NOPE.db";
    public static final int DB_VERSION = 2;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "tasks";

        public static final String TASK_TITLE = "title";

        public  static final String  TASK_DESC = "desc";

        public  static final String TASK_TIME = "time";

        public  static final String TASK_DONE = "done";
    }
}