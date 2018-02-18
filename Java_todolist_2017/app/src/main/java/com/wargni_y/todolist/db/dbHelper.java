package com.wargni_y.todolist.db;

/**
 * Created by wargni_y on 04/02/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.awt.font.TextAttribute;

public class dbHelper extends SQLiteOpenHelper {

    public dbHelper(Context context) {
        super(context, Task.DB_NAME, null, Task.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + Task.TaskEntry.TABLE + " ( " +
                Task.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Task.TaskEntry.TASK_TITLE + " TEXT NOT NULL," +
                Task.TaskEntry.TASK_DESC + " TEXT NOT NULL," +
                Task.TaskEntry.TASK_TIME + " TEXT NOT NULL," +
                Task.TaskEntry.TASK_DONE + " TEXT NOT NULL);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Task.TaskEntry.TABLE);
        onCreate(db);
    }

    public void deleteTask(String task, String desc, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Task.TaskEntry.TABLE,Task.TaskEntry.TASK_TITLE + " = ? AND "
                + Task.TaskEntry.TASK_DESC + " = ? AND " + Task.TaskEntry.TASK_TIME + " = ?", new String [] {task, desc, time});
        db.close();
    }
}