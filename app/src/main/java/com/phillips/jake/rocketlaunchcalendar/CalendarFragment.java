package com.phillips.jake.rocketlaunchcalendar;

import android.content.Context;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.phillips.jake.rocketlaunchcalendar.Data.CalendarDatSource;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    LaunchDetailsAdapter calendarAdapter;
    CalendarDatSource calendarDatSource;
    ArrayList<LaunchDetails> details;


    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        calendarDatSource = new CalendarDatSource(getActivity());

        try{
            calendarDatSource.open();
        }
        catch(SQLException e){
        }

        details = calendarDatSource.getAllLaunches();

        if(details.size() == 0){
            calendarDatSource.createDatabaseItem("Echostar 18 & BRIsat", "Ariane 5 ECA",
                    "June 17, 2016 20:30:00 UTC", "", "", "", 1, 1466195400, 1466199600);
            calendarDatSource.createDatabaseItem("New Shepard Test Flight 5", "New Shepard",
                    "June 19, 2016 14:15:00 UTC", "", "", "", 1, 1466345700, 1466345700);
            calendarDatSource.createDatabaseItem("Cartosat 2C & 19 small satellites", "PSLV XL",
                    "June 22, 2016 03:55:00 UTC", "", "", "", 1, 1466567700, 1466567700);
            calendarDatSource.createDatabaseItem("MUOS-5", "Atlas V 551",
                    "June 24, 2016 14:30:00 UTC", "", "", "", 1, 1466778600, 1466781240);
            calendarDatSource.createDatabaseItem("Development Flight 1", "Long March 7",
                    "June 25, 2016 11:30:00 UTC", "", "", "", 1, 1466854200, 1466854200);

            details = calendarDatSource.getAllLaunches();
        }

        calendarAdapter = new LaunchDetailsAdapter(getActivity(), details);

        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        populateTopView(rootView);

        final ListView listView = (ListView) rootView.findViewById(R.id.listview_calendar);
        listView.setAdapter(calendarAdapter);

        // Inflate the layout for this fragment
        return rootView;
    }

    private void populateTopView(View view){
        TextView tvNextMisson = (TextView) view.findViewById(R.id.next_launch_mission);
        TextView tvNextRockt = (TextView) view.findViewById(R.id.next_launch_rocket);
        final TextView tvDays = (TextView) view.findViewById(R.id.num_days_to_next_launch);
        final TextView tvHours = (TextView) view.findViewById(R.id.num_hours_to_next_launch);
        final TextView tvMin = (TextView) view.findViewById(R.id.num_min_to_next_launch);
        final TextView tvSec = (TextView) view.findViewById(R.id.num_seconds_to_next_launch);

        tvNextMisson.setText(details.get(0).mission);
        tvNextRockt.setText(details.get(0).rocket);

        Calendar current = Calendar.getInstance();

        long timeToLaunch = details.get(0).windowStart * 1000L - current.getTimeInMillis();

        new CountDownTimer(timeToLaunch, 1000){
            public void onTick(long millSecondsLeft){
                int days, hours, min, sec;
                days = (int) (millSecondsLeft / (1000 * 24 * 3600));
                millSecondsLeft -= days * 24 * 3600 * 1000;
                hours = (int) (millSecondsLeft / (1000 * 3600));
                millSecondsLeft -= hours * 3600 * 1000;
                min = (int) (millSecondsLeft / (1000 * 60));
                millSecondsLeft -= min * 60 * 1000;
                sec = (int) (millSecondsLeft / 1000);

                tvDays.setText(days + "");
                tvHours.setText(hours + "");
                tvMin.setText(min + "");
                tvSec.setText(sec + "");
            }
            public void onFinish(){

            }
        }.start();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
