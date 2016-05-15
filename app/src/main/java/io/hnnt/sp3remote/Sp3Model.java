package io.hnnt.sp3remote;

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
