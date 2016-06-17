package com.phillips.jake.rocketlaunchcalendar;

import android.content.Context;
import android.database.SQLException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.phillips.jake.rocketlaunchcalendar.Data.CalendarDatSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

        /*if(details.size() == 0){
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
        }*/

        FetchLaunches lauchTask = new FetchLaunches();
        lauchTask.execute("");

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


    private void convertDataToDetails (String[] launches){
        // clears database of everything
        calendarDatSource.clearDatabase();

        String mission, rocket, net, mapUrl;
        String vidURLs, infoURLs;
        String launchData;
        int status, wsstamp, westamp;
        int endIndex;


        for(int i = 0; i < launches.length; i++){
            launchData = launches[i];

            endIndex = launchData.indexOf("|");
            mission = launchData.substring(0, endIndex).trim();
            launchData = launchData.substring(endIndex + 1);

            endIndex = launchData.indexOf("|");
            rocket = launchData.substring(0, endIndex).trim();
            launchData = launchData.substring(endIndex + 1);

            endIndex = launchData.indexOf("|");
            net = launchData.substring(0, endIndex).trim();
            launchData = launchData.substring(endIndex + 1);

            endIndex = launchData.indexOf("|");
            status = Integer.getInteger(launchData.substring(0, endIndex).trim());
            launchData = launchData.substring(endIndex + 1);

            endIndex = launchData.indexOf("|");
            wsstamp = Integer.getInteger(launchData.substring(0, endIndex).trim());
            launchData = launchData.substring(endIndex + 1);

            endIndex = launchData.indexOf("|");
            westamp = Integer.getInteger(launchData.substring(0, endIndex).trim());
            launchData = launchData.substring(endIndex + 1);

            endIndex = launchData.indexOf("|");
            vidURLs = launchData.substring(0, endIndex).trim();
            launchData = launchData.substring(endIndex + 1);

            endIndex = launchData.indexOf("|");
            infoURLs = launchData.substring(0, endIndex).trim();
            launchData = launchData.substring(endIndex + 1);

            endIndex = launchData.indexOf("|");
            mapUrl = launchData.substring(0, endIndex).trim();

            calendarDatSource.createDatabaseItem(mission, rocket, net, vidURLs, infoURLs,
                    mapUrl, status, wsstamp, westamp);
        }

        details = calendarDatSource.getAllLaunches();

    }

    public class FetchLaunches extends AsyncTask<String, Void, String[]>{

        private String[] getLaunchesFromJson(String launchJsonStr, int numLaunches) throws JSONException{

            final String LL_LAUNCHES = "launches";
            final String LL_NAME = "name";
            final String LL_NET = "net";
            final String LL_STATUS = "status";
            final String LL_WSSTAMP = "wsstamp";
            final String LL_WESTAMP = "westamp";
            final String LL_VIDURL = "vidURLs";
            final String LL_INFORURL = "infoURLs";
            final String LL_MAPURL = "mapURL";

            JSONObject launchJson = new JSONObject(launchJsonStr);
            JSONArray launchArray = launchJson.getJSONArray(LL_LAUNCHES);

            String[] resultStrs = new String[numLaunches];
            for(int i = 0; i < launchArray.length(); i++){
                String name, net, mapUrl;
                String vidURLs, infoURLs;
                int status, wsstamp, westamp;

                JSONObject launch = launchArray.getJSONObject(i);

                name = launch.getString(LL_NAME);
                net = launch.getString(LL_NET);
                status = launch.getInt(LL_STATUS);
                wsstamp = launch.getInt(LL_WSSTAMP);
                westamp = launch.getInt(LL_WESTAMP);
                if(launch.getJSONArray(LL_VIDURL).length() != 0) {
                    vidURLs = launch.getJSONArray(LL_VIDURL).getString(0);
                }
                else{
                    vidURLs = "no video";
                }
                if(launch.getJSONArray(LL_INFORURL).length() != 0) {
                    infoURLs = launch.getJSONArray(LL_INFORURL).getString(0);
                }
                else{
                    infoURLs = "no info";
                }
                //mapUrl = launch.getString(LL_MAPURL);
                mapUrl = "no url";

                resultStrs[i] = name + "|" + net + "|" + status +
                        "|" + wsstamp + "|" + westamp + "|" +
                        vidURLs + "|" + infoURLs + "|" + mapUrl;
            }

            return resultStrs;
        }

        @Override
        protected String[] doInBackground(String... params){

            if(params.length == 0){
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String launchJson = null;

            int numLaunches = 10;
            String mode = "verbose";

            try{
                /*
                final String BASE_URL = "https://launchlibrary.net/1.2/launch?";
                final String MODE_PARAM = "verbose";
                final String LAUNCHES_PARAM = "next";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(MODE_PARAM, mode)
                        .appendQueryParameter(LAUNCHES_PARAM, Integer.toString(numLaunches))
                        .build();

                URL url = new URL(builtUri.toString());
                */

                URL url = new URL("https://launchlibrary.net/1.2/launch?mode=verbose");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if(inputStream == null){
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }

                if(buffer.length() == 0){
                    return null;
                }

                launchJson = buffer.toString();
            }
            catch(IOException e){
                Log.e("JSON Error", "Cal Frag",  e);
                return null;
            }
            finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(reader != null){
                    try{
                        reader.close();
                    }
                    catch(final IOException e){
                        Log.e("Reader error", "Error closing stream", e);
                    }
                }
            }

            try{
                Log.d("JSON", launchJson);
                return getLaunchesFromJson(launchJson, numLaunches);
            }
            catch(JSONException e){
                Log.e("JSON Error", e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] result){
            if(result != null){
                convertDataToDetails(result);
            }
        }
    }
}
