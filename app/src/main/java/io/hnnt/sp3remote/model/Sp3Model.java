package io.hnnt.sp3remote.model;

import java.util.ArrayList;

import io.hnnt.sp3remote.events.InfoEvent;

/**
 * Created by Tobias Nielsen on 2016-05-13.
 */
public class Sp3Model {

    private static String softwareRelease;
    private static String currentPosition;
    private static String calcSunPosition;
    private static String tiltZeroPosition;
    private static String sensorZeroPosition;
    private static String nrOfSunnyDays;
    private static String lastSunTime;
    private static String nrOfResets;
    private static String morningPosDiff;
    private static String sunToday;

    private static String infoToMail;
    private static String date;

    private static String sp3Lat = "";
    private static String sp3Lon = "";
    private static String sp3TimeAndDate ="";

    private static ArrayList<LogItem> logItemList;

    public static ArrayList<LogItem> getLogItemList() {
        if(logItemList == null){
            ArrayList<LogItem> emptyList = new ArrayList<>();
            emptyList.add(new LogItem("No logs","Turn on logging for updated log information"));
            return emptyList;
        }else {
            return logItemList;
        }
    }

    public static String getLogEmailFormatted(){
        StringBuilder sb = new StringBuilder("");
        sb.append("-----------------------------------\n");
        sb.append("         last recorded log\n");
        sb.append("-----------------------------------\n");
        if(logItemList != null) {
            for (LogItem li : logItemList) {
                sb.append(li.label + " : " + li.value + "\n");
            }
        }
            return sb.toString();
    }

    public static void setLogItemList(ArrayList<LogItem> logItemList) {
        Sp3Model.logItemList = logItemList;
    }

    public static String getSoftwareRelease() {
        return softwareRelease;
    }

    public static void setSoftwareRelease(String softwareRelease) {
        Sp3Model.softwareRelease = softwareRelease;
    }

    public static String getCurrentPosition() {
        return currentPosition;
    }

    public static void setCurrentPosition(String currentPosition) {
        Sp3Model.currentPosition = currentPosition;
    }

    public static String getCalcSunPosition() {
        return calcSunPosition;
    }

    public static void setCalcSunPosition(String calcSunPosition) {
        Sp3Model.calcSunPosition = calcSunPosition;
    }

    public static String getTiltZeroPosition() {
        return tiltZeroPosition;
    }

    public static void setTiltZeroPosition(String tiltZeroPosition) {
        Sp3Model.tiltZeroPosition = tiltZeroPosition;
    }

    public static String getSensorZeroPosition() {
        return sensorZeroPosition;
    }

    public static void setSensorZeroPosition(String sensorZeroPosition) {
        Sp3Model.sensorZeroPosition = sensorZeroPosition;
    }

    public static String getNrOfSunnyDays() {
        return nrOfSunnyDays;
    }

    public static void setNrOfSunnyDays(String nrOfSunnyDays) {
        Sp3Model.nrOfSunnyDays = nrOfSunnyDays;
    }

    public static String getLastSunTime() {
        return lastSunTime;
    }

    public static void setLastSunTime(String lastSunTime) {
        Sp3Model.lastSunTime = lastSunTime;
    }

    public static String getNrOfResets() {
        return nrOfResets;
    }

    public static void setNrOfResets(String nrOfResets) {
        Sp3Model.nrOfResets = nrOfResets;
    }

    public static String getMorningPosDiff() {
        return morningPosDiff;
    }

    public static void setMorningPosDiff(String morningPosDiff) {
        Sp3Model.morningPosDiff = morningPosDiff;
    }

    public static String getSunToday() {
        return sunToday;
    }

    public static void setSunToday(String sunToday) {
        boolean sun = false;
        if(sunToday.equals("1"))
            sun = true;
        Sp3Model.sunToday = "" + sun;
    }

    public static void setInfoSp3Model(InfoEvent event){
        setSoftwareRelease(event.getSoftwareRelease());
        setCurrentPosition(event.getCurrentPosition());
        setCalcSunPosition(event.getCalcSunPosition());
        setTiltZeroPosition(event.getTiltZeroPosition());
        setSensorZeroPosition(event.getSensorZeroPosition());
        setNrOfSunnyDays(event.getNrOfSunnyDays());
        setLastSunTime(event.getLastSunTime());
        setNrOfResets(event.getNrOfResets());
        setMorningPosDiff(event.getMorningPosDiff());
        setSunToday(event.getSunToday());
    }


    public static void setInfoToMail(String input){
        infoToMail = input;
    }

    public static String getInfoToMail(){
        return infoToMail;
    }

    public static void setDate(String input){
        date = input;
    }

    public static String getDate(){
        return date;
    }

    public static void setSp3Lat(String input){ sp3Lat = input; }

    public static String getSp3Lat(){ return sp3Lat; }

    public static void setSp3Lon(String input){ sp3Lon = input; }

    public static String getSp3Lon(){ return sp3Lon; }

    public static void setSp3TimeAndDate(String input){
        sp3TimeAndDate = input;
    }

    public static String getSp3TimeAndDate(){
        return sp3TimeAndDate;
    }

}
