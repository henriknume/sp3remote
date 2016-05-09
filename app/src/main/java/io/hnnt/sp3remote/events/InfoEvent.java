package io.hnnt.sp3remote.events;

/**
 * Created by nume on 2016-05-06.
 */
public class InfoEvent{

    public final String message;

    public InfoEvent(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
