package com.wargni_y.todolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by wargni_y on 04/02/2018.
 */

public class CustomAdapter extends ArrayAdapter<TaskObj> {

    private Context mContext;

    private static class ViewHolder{
        TextView taskName;
        TextView taskDescription;
        TextView taskDate;
    }

    CustomAdapter(ArrayList<TaskObj> data, Context context){
        super(context, R.layout.item_todo, data);
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        TaskObj newData = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_todo, parent, false);
            viewHolder.taskName = (TextView) convertView.findViewById(R.id.task_title);
            viewHolder.taskDescription = (TextView) convertView.findViewById(R.id.task_desc);
            viewHolder.taskDate = (TextView) convertView.findViewById(R.id.task_time);
            result = convertView;
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.taskName.setText(newData.getTask());
        viewHolder.taskDescription.setText(newData.getDescription());
        viewHolder.taskDate.setText(newData.getDate());
        return convertView;
    }

}