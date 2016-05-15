package io.hnnt.sp3remote.events;

import java.util.LinkedHashMap;

import io.hnnt.sp3remote.Sp3Model;

/**
 * Created by nume on 2016-05-11.
 *
 */
public class LogEvent {

    public final boolean isLogLine;
    public final String message;
    private LinkedHashMap<String, String> logTable = null;

    public LogEvent(String message, boolean isLogLine){
        this.message = message;
        this.isLogLine = isLogLine;
        if(isLogLine){
            logTable = parseMessage(message);
        }
    }

    private LinkedHashMap parseMessage(String logLine){
        String trimmedLine = logLine.replaceAll("(\\r|\\n|\\s)", "");
        String[] rows = trimmedLine.split(",");
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        for(String value : rows){
            int index = 0;
            map.put(""+index, value);
        }
        /*
        * TODO: replace index with name of field
        * */
        return map;
    }

    public LinkedHashMap<String, String> getLogTable(){
        return  logTable;
    }
}
