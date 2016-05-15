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
    private String[] labels = setLabels();

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
        if(rows.length == labels.length) {
            for (int i = 0; i < rows.length; i++) {
                int index = 0;
                list.add(new LogItem(labels[i], rows[i]));
            }
        }
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

    private String[] setLabels(){
        ArrayList<String> labels = new ArrayList<>();

        labels.add("Sensor signal 0");                  //  0
        labels.add("............. 1");                  //  1
        labels.add("............. 2");                  //  2
        labels.add("............. 3");                  //  3
        labels.add("3 if not sensor 0");                //  4
        labels.add("............... 1");                //  5
        labels.add("Current pos x");                    //  6
        labels.add("........... y");                    //  7
        labels.add("Calc. sun pos x");                  //  8
        labels.add("............. y");                  //  9
        labels.add("Calc. sun pos incl. tilt x");       // 10
        labels.add("........................ y");       // 11
        labels.add("Sun search position x");            // 12
        labels.add("................... y");            // 13
        labels.add("Sun’s speed x");                    // 14
        labels.add("........... y");                    // 15
        labels.add("SP3 speed x");                      // 16
        labels.add("......... y");                      // 17
        labels.add("Tilt compensation");                // 18
        labels.add("Run mode");                         // 19
        labels.add("Sun Switch");                       // 20
        labels.add("Setup");                            // 21
        labels.add("Clock GMT");                        // 22
        labels.add("Temperature");                      // 23
        labels.add("Tilt correction");                  // 24
        labels.add("Nr of sunny days");                 // 25
        labels.add("Latest sun date");                  // 26
        labels.add("Watchdog resets");                  // 27
        labels.add("Days in operation");                // 28
        labels.add("Morning pos error x");              // 29
        labels.add("................. y");              // 30

        String[] result = new String[labels.size()];
        return result = labels.toArray(result);
    }
}
