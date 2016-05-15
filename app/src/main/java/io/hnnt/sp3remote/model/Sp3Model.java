package io.hnnt.sp3remote.model;

import io.hnnt.sp3remote.events.InfoEvent;

/**
 * Created by Tobias Nielsen on 2016-05-13.
 */
public class Sp3Model {

    public static final String OUT_OF_RANGE = "-1";
    public static String[] infoToFragment = new String[10];
    public static String infoToMail;
    public static String date;
    public static String log;

    public static void setInfoToFragment(int i, String input){
        if(i>=0 && i<10)
            infoToFragment[i] = input;
    }

    public static String getInfoToFragment(int i){
        if((i>=0 && i<10) && infoToFragment[i] != null) {
                return infoToFragment[i];
        }else{
            return OUT_OF_RANGE;
        }
    }

    public static void setSP3Model(InfoEvent event){
        setInfoToFragment(0, event.getSoftwareRelease());
        setInfoToFragment(1, event.getCurrentPosition());
        setInfoToFragment(2, event.getCalcSunPosition());
        setInfoToFragment(3, event.getTiltZeroPosition());
        setInfoToFragment(4, event.getSensorZeroPosition());
        setInfoToFragment(5, event.getNrOfSunnyDays());
        setInfoToFragment(6, event.getLastSunTime());
        setInfoToFragment(7, event.getNrOfResets());
        setInfoToFragment(8, event.getMorningPosDiff());
        boolean sun = false;
        if(event.getCalcSunPosition().equals("1")){
            sun = true;
        }
        setInfoToFragment(9, ""+sun);
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
        log = input;
    }

    public static String getLog(){
        return log;
    }

}
