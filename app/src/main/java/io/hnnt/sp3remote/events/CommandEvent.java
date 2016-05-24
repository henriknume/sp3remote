package io.hnnt.sp3remote.events;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by nume on 2016-05-06.
 */
public class CommandEvent {

    public static final String TARGET_INFO_FRAGMENT = "target_info_fragment";
    public static final String TARGET_SETTINGS_FRAGMENT = "target_settings_fragment";
    public static final String TARGET_CONTROL_FRAGMENT = "target_control_fragment";
    public static final String TARGET_LOG_FRAGMENT = "target_log_fragment";
    public static final String TARGET_ALL_FRAGMENTS = "target_all_fragments";

    public static final String RESPONSE_TYPE_LOGEVENT = "response_type_logevent";
    public static final String RESPONSE_TYPE_INFOEVENT = "response_type_infoevent";
    public static final String RESPONSE_TYPE_SETTINGSEVENT = "response_type_settingsevent";
    public static final String RESPONSE_TYPE_CONTROLEVENT = "response_type_controlevent";

    public final String command;
    public final String responseType;
    public final String responseTarget;

    public CommandEvent(String command, String responseType, String responseTarget){
        this.command = command + "\r";
        this.responseType = responseType;
        this.responseTarget = responseTarget;
    }

    public String getCommand(){
        return command.trim();
    }


    /*
    private Queue<String> commandlist;

    public CommandEvent(){
        commandlist = new LinkedList<>();
    }

    public CommandEvent(String textCommand){
        commandlist = new LinkedList<>();
        commandlist.add(textCommand + "\r");
    }

    public void addExtra(String command){
        commandlist.add(command);
    }

    public String getFirst(){
        return commandlist.peek();
    }

    public String popCommand()
    {
        return commandlist.poll();
    }

    public int getNrOfCommands(){
        return commandlist.size();
    }

    @Override
    public String toString() {
        String result = "[";
        for(String c : commandlist){
            result += "{"+ c +"},";
        }
        result += "]";
        return result;
    }
    */
}
