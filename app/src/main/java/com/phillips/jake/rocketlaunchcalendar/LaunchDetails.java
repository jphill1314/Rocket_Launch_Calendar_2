package com.phillips.jake.rocketlaunchcalendar;

/**
 * Created by jphil on 6/16/2016.
 */
public class LaunchDetails {

    public String mission, rocket, net, vidURL, infoURL, mapURL;
    public int status, windowStart, windowClose, id;

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
    }
}
