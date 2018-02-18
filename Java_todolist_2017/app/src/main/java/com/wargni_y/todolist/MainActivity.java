package com.wargni_y.todolist;

import android.app.AppOpsManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wargni_y.todolist.db.Task;
import com.wargni_y.todolist.db.dbHelper;

import java.util.ArrayList;

import static com.wargni_y.todolist.db.Task.TaskEntry.TASK_DESC;
import static com.wargni_y.todolist.db.Task.TaskEntry.TASK_DONE;
import static com.wargni_y.todolist.db.Task.TaskEntry.TASK_TIME;
import static com.wargni_y.todolist.db.Task.TaskEntry.TASK_TITLE;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private dbHelper mHelper;
    private ListView mTaskListView;
    private CustomAdapter mAdapter;

    private Context c = this;
    public  String done = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new dbHelper(c);
        mTaskListView = findViewById(R.id.list_todo);

        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                LayoutInflater taskLayout = LayoutInflater.from(c);
                final View Poptask = taskLayout.inflate(R.layout.task,null);
                final EditText taskEditText = Poptask.findViewById(R.id.task_title);
                final EditText descEditText = Poptask.findViewById(R.id.task_desc);
                final EditText timeEditText = Poptask.findViewById(R.id.task_time);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add a new Task")
                        .setView(Poptask)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                String desc = String.valueOf(descEditText.getText());
                                String time = String.valueOf(timeEditText.getText());
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(TASK_TITLE, task);
                                values.put(TASK_DESC, desc);
                                values.put(TASK_TIME, time);
                                values.put(TASK_DONE, "false");
                                db.insert(Task.TaskEntry.TABLE, null, values);
                                db.close();
                                updateUI();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = parent.findViewById(R.id.task_title);
        TextView descTextView = parent.findViewById(R.id.task_desc);
        TextView timeTextView = parent.findViewById(R.id.task_time);
        String task = String.valueOf(taskTextView.getText());
        String desc = String.valueOf(descTextView.getText());
        String time = String.valueOf(timeTextView.getText());
        mHelper.deleteTask(task, desc, time);
        updateUI();
    }

    private void updateUI() {
        ArrayList<TaskObj> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(Task.TaskEntry.TABLE,
                new String[]{Task.TaskEntry._ID, Task.TaskEntry.TASK_TITLE, Task.TaskEntry.TASK_DESC, Task.TaskEntry.TASK_TIME, Task.TaskEntry.TASK_DONE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {

            int id = cursor.getColumnIndex(Task.TaskEntry.TASK_TITLE);
            int desc = cursor.getColumnIndex(Task.TaskEntry.TASK_DESC);
            int time = cursor.getColumnIndex(Task.TaskEntry.TASK_TIME);
            int done = cursor.getColumnIndex(Task.TaskEntry.TASK_DONE);
            taskList.add(new TaskObj(cursor.getString(id), cursor.getString(desc), cursor.getString(time), cursor.getString(done)));

        }

        if (mAdapter == null) {
            mAdapter = new CustomAdapter(taskList, c);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }
    
    public void editTask(View view) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        View parent = (View) view.getParent();
        final AlertDialog.Builder dialog = new AlertDialog.Builder(c);
        final LayoutInflater layoutDialog = LayoutInflater.from(c);
        View editView = layoutDialog.inflate(R.layout.edit_task, null);

        final EditText taskText = (EditText) editView.findViewById(R.id.task_title);
        final EditText contentText = (EditText) editView.findViewById(R.id.task_desc);
        final EditText timeText= (EditText) editView.findViewById(R.id.task_time);
        final CheckBox check = (CheckBox) editView.findViewById(R.id.task_done);

        final TextView oldTaskText = (TextView) parent.findViewById(R.id.task_title);
        final TextView oldContentText = (TextView) parent.findViewById(R.id.task_desc);

        String title = String.valueOf(oldTaskText.getText());
        String content = String.valueOf(oldContentText.getText());
        Cursor cursor = db.rawQuery("SELECT * FROM tasks WHERE " + Task.TaskEntry.TASK_TITLE + " ='" + title +"' AND " + Task.TaskEntry.TASK_DESC + " = '" + content + "';", null);
        while (cursor.moveToNext()) {
            taskText.setText(cursor.getString(cursor.getColumnIndex(Task.TaskEntry.TASK_TITLE)), TextView.BufferType.EDITABLE);
            contentText.setText(cursor.getString(cursor.getColumnIndex(Task.TaskEntry.TASK_DESC)), TextView.BufferType.EDITABLE);
            timeText.setText(cursor.getString(cursor.getColumnIndex(Task.TaskEntry.TASK_TIME)), TextView.BufferType.EDITABLE);
            done = cursor.getString(cursor.getColumnIndex(Task.TaskEntry.TASK_DONE));
            if (done.equalsIgnoreCase("true"))
            {
                check.setChecked(true);
            }
            else
            {
                check.setChecked(false);
            }
        }

        //taskText.setText(String.valueOf(oldTaskText.getText()), TextView.BufferType.EDITABLE);
        //contentText.setText(String.valueOf(oldContentText.getText()), TextView.BufferType.EDITABLE);

        dialog.setView(editView);
        dialog.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String task = String.valueOf(taskText.getText().toString());
                String description = String.valueOf(contentText.getText().toString());
                String time = String.valueOf(timeText.getText().toString());
                if (check.isChecked())
                    done = "true";
                else
                    done = "false";
                SQLiteDatabase db = mHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                if (task.isEmpty()) {
                    dialog.dismiss();
                    Toast.makeText(c, "ERROR EMPTY MESSAGE", Toast.LENGTH_SHORT).show();
                } else {
                    values.put(Task.TaskEntry.TASK_TITLE, task);
                    values.put(Task.TaskEntry.TASK_DESC, description);
                    values.put(Task.TaskEntry.TASK_TIME, time);
                    values.put(Task.TaskEntry.TASK_DONE, done);
                    db.updateWithOnConflict(Task.TaskEntry.TABLE, values, Task.TaskEntry.TASK_TITLE + " = ? AND " + Task.TaskEntry.TASK_DESC + " = ? ",
                            new String[]{oldTaskText.getText().toString(),  oldContentText.getText().toString()}, SQLiteDatabase.CONFLICT_REPLACE);
                    db.close();
                    updateUI();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(c, "CANCELED", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
