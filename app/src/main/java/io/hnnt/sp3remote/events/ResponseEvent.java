package io.hnnt.sp3remote.events;

/**
 * Created by nume on 2016-05-06.
 */
public class ResponseEvent {

    public final String message;

    public ResponseEvent(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
