package io.hnnt.sp3remote.events;

import io.hnnt.sp3remote.model.Sp3Model;

/**
 * Created by nume on 2016-05-11.
 */

public class SettingsEvent{

    public static final String LON_VALUE_GET = "LON_VALUE_GET";
    public static final String LON_VALUE_SET = "LON_VALUE_SET";
    public static final String LAT_VALUE_GET = "LAT_VALUE_GET";
    public static final String LAT_VALUE_SET = "LAT_VALUE_SET";
    public static final String DATE_VALUE_GET = "DATE_VALUE_GET";
    public static final String DATE_VALUE_SET = "DATE_VALUE_SET";
    public static final String PARSE_ERROR = "PARSE_ERROR";

    public final String fullMessage;
    public final String responseType;
    public final String responseData;


    public SettingsEvent(String message){
        this.fullMessage = message;
        this.responseType = parseResponseType(message);
        this.responseData = parseData(responseType, message);
    }

    private String parseResponseType(String message){
        String type = PARSE_ERROR;
        String trimmedMessage = message.replaceAll("(\\r|\\n|\\s)", "");

        if(trimmedMessage.contains("Currentdate")) {
            type = DATE_VALUE_GET;
        }

        else if(trimmedMessage.contains("Dateandtimeupdated")) {
            type = DATE_VALUE_SET;
        }

        else if(trimmedMessage.contains("Currentlongitude")) {
            type =LON_VALUE_GET;
        }

        else if(trimmedMessage.contains("Longitudesetto")) {
            type = LON_VALUE_SET;
        }

        else if(trimmedMessage.contains("Currentlatitude")) {
            type = LAT_VALUE_GET;
        }

        else if(trimmedMessage.contains("Latitudesetto")) {
            type = LAT_VALUE_SET;
        }
        return type;
    }

    private String parseData(String responseType, String message){

        String data = "-";

        if(responseType.equals(PARSE_ERROR)){
            data = "parse_error";
        }

        else if(responseType.equals(DATE_VALUE_SET)){
            /*
            parse the set date from this:
                date 2015-10-11 11:11:11
                Date and time updated

                #
            */

            String[] rows = message.split("\n");
            for (String row : rows){
                if(row.contains("-")){
                    String[] col = row.split(" ");
                    data = col[1] + " " + col[2];
                    data.trim();
                }
            }
        }

        else if(responseType.equals(DATE_VALUE_GET)){
            /*
            parse current date from this:
                date
                Current date and time: 2016-05-13 09:02:05

                Usage: date [yyyy-mm-dd hh:mm:ss] (24-hour format)

                #
            */
            Sp3Model.setDate(message);
            String[] rows = message.split("\n");
            for (String row : rows){
                if(row.contains("Current date")){
                    String[] col = row.split(":", 2);
                    data = col[1].trim();
                }
            }
        }
        else{
            /*
            parse location values from these:
                lat 60.000
                Latitude set to: 60.000000

                #
            */
            String[] rows = message.split("\n");
            for (String row : rows){
                if(row.contains(":")){
                    String[] col = row.split(":");
                    data = col[1].trim();
                }
            }
        }

        return data;
    }
}
