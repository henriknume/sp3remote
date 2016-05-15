package io.hnnt.sp3remote.events;

import io.hnnt.sp3remote.Sp3Model;

/**
 * Created by nume on 2016-05-11.
 */
public class LogEvent {

    public final String message;

    public LogEvent(String message){
        this.message = message;
        Sp3Model.setLog(message);
    }
}
