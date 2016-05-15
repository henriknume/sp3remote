package io.hnnt.sp3remote.model;

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
    private static StringBuilder log = new StringBuilder("");



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

    public static void setLog(String input){
        log.append(input+"\n");
    }

    public static String getLog(){
        return log.toString();
    }

    public static void clearLog(){
        log = new StringBuilder("");
    }

}
