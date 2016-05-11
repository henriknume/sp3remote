package io.hnnt.sp3remote.events;

import java.util.LinkedHashMap;

/**
 * Created by nume on 2016-05-06.
 */


public class InfoEvent{

    private String softwareRelease = "-0";
    private String currentPosition = "-1";
    private String calcSunPosition = "-2";
    private String tiltZeroPosition = "-3";
    private String sensorZeroPosition = "-4";
    private String nrOfSunnyDays = "-5";
    private String lastSunTime = "-6";
    private String nrOfResets = "-7";
    private String morningPosDiff = "-8";
    private String sunToday = "-9";

    private LinkedHashMap<String, String> map;

    public String message;

    public InfoEvent(String message){
        this.message = message;
        map = parseMessage(message);
        setFields(map);
    }

    private LinkedHashMap<String, String> parseMessage(String message){
        String[] rows = message.split("[\\r\\n]+");
        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        for(String s : rows){
            if(s.contains(":")){
                String[] col = s.trim().split(":");
                result.put(col[0].trim(), col[1].trim());
            }
        }
        return result;
    }

    private void setFields(LinkedHashMap<String, String> map){
        if(map.size() == 10){
            String[] array = map.keySet().toArray(new String[0]);
            softwareRelease = map.get(array[0]);
            currentPosition = map.get(array[1]);
            calcSunPosition = map.get(array[2]);
            tiltZeroPosition = map.get(array[3]);
            sensorZeroPosition = map.get(array[4]);
            nrOfSunnyDays = map.get(array[5]);
            lastSunTime = map.get(array[6]);
            nrOfResets = map.get(array[7]);
            morningPosDiff = map.get(array[8]);
            sunToday = map.get(array[9]);
        }
    }

    @Override
    public String toString() {
        return message;
    }

    public String getSoftwareRelease() {
        return softwareRelease;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public String getCalcSunPosition() {
        return calcSunPosition;
    }

    public String getTiltZeroPosition() {
        return tiltZeroPosition;
    }

    public String getSensorZeroPosition() {
        return sensorZeroPosition;
    }

    public String getNrOfSunnyDays() {
        return nrOfSunnyDays;
    }

    public String getLastSunTime() {
        return lastSunTime;
    }

    public String getNrOfResets() {
        return nrOfResets;
    }

    public String getMorningPosDiff() {
        return morningPosDiff;
    }

    public String getSunToday() {
        return sunToday;
    }
}
