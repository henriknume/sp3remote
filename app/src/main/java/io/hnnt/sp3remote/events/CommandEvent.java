package io.hnnt.sp3remote.events;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by nume on 2016-05-06.
 */
public class CommandEvent {

    public final String command;

    public CommandEvent(String command){
        this.command = command + "\r";
    }

    public String getText(){
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
