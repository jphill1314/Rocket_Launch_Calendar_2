package com.phillips.jake.rocketlaunchcalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jphil on 6/16/2016.
 */
public class LaunchDetailsAdapter extends ArrayAdapter<LaunchDetails> {
    public LaunchDetailsAdapter(Context context, ArrayList<LaunchDetails> launchCal){
        super(context, 0, launchCal);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LaunchDetails detail = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.calendar_list_view_item, parent, false);
        }

        TextView tvMission = (TextView) convertView.findViewById(R.id.calendar_item_mission);
        TextView tvDate = (TextView) convertView.findViewById(R.id.calendar_item_date);
        TextView tvRocket = (TextView) convertView.findViewById(R.id.calendar_item_rocket);
        TextView tvTime = (TextView) convertView.findViewById(R.id.calendar_item_window);

        tvMission.setText(detail.mission);
        tvDate.setText("Test Date");
        tvRocket.setText(detail.rocket);
        tvTime.setText("Test Time");

        return convertView;
    }
}
