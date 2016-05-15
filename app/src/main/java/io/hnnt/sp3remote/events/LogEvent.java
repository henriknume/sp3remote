package io.hnnt.sp3remote.events;

import io.hnnt.sp3remote.Sp3Model;

/**
 * Created by nume on 2016-05-11.
 */
public class LogEvent {

    public final boolean isLogLine;
    public final String message;

    public LogEvent(String message){
        this.message = message;
        this.isLogLine = false;
    }

    public LogEvent(String message, boolean isLogLine){
        this.message = message;
        this.isLogLine = isLogLine;
    }
}
