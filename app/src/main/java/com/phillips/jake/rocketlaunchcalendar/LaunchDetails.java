package com.phillips.jake.rocketlaunchcalendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by jphil on 6/16/2016.
 *
 * Need to add function that converts the Unix timestamps to a String w/ month and day and a String w/ 12hr time
 */
public class LaunchDetails {

    public String mission, rocket, net, vidURL, infoURL, mapURL;
    public int status, windowStart, windowClose, id;

    public String launchDay, launchTime;

    public LaunchDetails(String mission, String rocket, String net, String vidURL, String infoURL,
                         String mapURL, int status, int windowStart, int windowClose, int id){

        this.mission = mission;
        this.net = net;
        this.rocket = rocket;
        this.vidURL = vidURL;
        this.infoURL = infoURL;
        this.mapURL = mapURL;

        this.status = status;
        this.windowStart = windowStart;
        this.windowClose = windowClose;

        this.id = id;

        setDates();
    }

    private void setDates(){
        String[] monthNames = {"January", "February", "March", "April",
            "May", "June", "July", "August", "September", "October", "November", "December"};
        Calendar local = new GregorianCalendar(TimeZone.getDefault());

        local.setTimeInMillis(windowStart * 1000L);

        launchDay = monthNames[local.get(Calendar.MONTH)] + " " + local.get(Calendar.DAY_OF_MONTH);

        DateFormat format = new SimpleDateFormat("h:mm a");
        launchTime = format.format(windowStart * 1000L);
    }
}
