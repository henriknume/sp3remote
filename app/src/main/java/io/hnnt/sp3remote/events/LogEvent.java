package io.hnnt.sp3remote.events;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import io.hnnt.sp3remote.model.LogItem;

/**
 * Created by nume on 2016-05-11.
 *
 */
public class LogEvent {

    public final boolean isLogLine;
    public final String message;
    private ArrayList<LogItem> logList = null;

    public LogEvent(String message, boolean isLogLine){
        this.message = message;
        this.isLogLine = isLogLine;
        if(isLogLine){
            logList = parseMessage(message);
        }
    }

    private ArrayList<LogItem> parseMessage(String logLine){
        String trimmedLine = logLine.replaceAll("(\\r|\\n|\\s)", "");
        String[] rows = trimmedLine.split(",");
        ArrayList<LogItem> list = new ArrayList<>();
        for(String value : rows){
            int index = 0;
            list.add(new LogItem("Label-"+index , value));
        }
        /*
        * TODO: replace index with name of field
        * */
        return list;
    }

    public ArrayList<LogItem> getLogList(){
        if(logList != null){
            return logList;
        }else{
            ArrayList<LogItem> emptyList = new ArrayList<>();
            emptyList.add(new LogItem("No data", "toogle log to receive updated data"));
            return emptyList;
        }
    }
}
