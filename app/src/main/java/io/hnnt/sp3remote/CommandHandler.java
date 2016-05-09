package io.hnnt.sp3remote;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.LinkedList;
import java.util.Queue;

import io.hnnt.sp3remote.events.CommandEvent;
import io.hnnt.sp3remote.events.InfoEvent;
import io.hnnt.sp3remote.events.ResponseEvent;

/**
 * Created by nume on 2016-05-06.
 */
public class CommandHandler {

    public static final String TAG = "CommandHandler.java";

    private UsbService usbService;
    private Queue<CommandEvent> commandQueue;

    private String currentCommand = null;

    public CommandHandler( UsbService usbService){
        this.usbService = usbService;
        commandQueue = new LinkedList<>();

    }

    /*
    *   receive events from fragments and put in a queue
    */
    @Subscribe
    public void onEvent(CommandEvent commandEvent){
        if(commandEvent != null){
            //Log.d(TAG, "onCommandEvent() A -Adding commandevent");
            int len = commandQueue.size();
            commandQueue.add(commandEvent);
            boolean res = (len + 1 == commandQueue.size());
            Log.d(TAG, "onCommandEvent() B - added:" + res + " size:" + commandQueue.size());
            sendCommand1();
        }
    }


    /*
    *   take events from first in queue and send to service
    */
    private void sendCommand1(){
        Log.d(TAG, "A sendCommand()");
        CommandEvent currEvent = commandQueue.poll();
        if(currEvent != null){
            currentCommand = currEvent.command;
        }
        Log.d(TAG, "C-1 currCmd:" + currentCommand.trim() + "C-2 usbser:" + usbService);
        if(usbService != null){
            Log.d(TAG, "D");
            usbService.write(currentCommand.getBytes());
            //EventBus.getDefault().post(new InfoEvent("response event from sendcommand(D)"));
        }else{
            Log.d(TAG, "E");
            EventBus.getDefault().post(new InfoEvent("response event from sendcommand(E)"));
        }
    }

    /*
    *   receive response from service...
    *       if Unknown command, send again
    *       else pass on to correkt fragment
    */

    @Subscribe
    public void onEvent(ResponseEvent responseEvent){
        Log.d(TAG, "onResponseEvent()");

        if(responseEvent.message.contains("Unknown command")){
            Log.d(TAG, "onResponseEvent() RS");
            reSendLastCommand();
        }else{
            if(responseEvent != null && responseEvent.message != null){
                EventBus.getDefault().post(new InfoEvent(responseEvent.message));
            }
        }
    }

    private void reSendLastCommand(){
        Log.d(TAG, "reSendLastCommand()," + "usbser:" + (usbService != null) + "currCmd:" + currentCommand.trim());
        if(usbService != null && currentCommand != null) {
            usbService.write(currentCommand.getBytes());
        }
    }

    /*
    *
    * */
    public void clearQueue(){
        commandQueue.clear();
    }

    public void updateService(UsbService usbService){
        this.usbService = usbService;
    }
}

/*
 EventBus.getDefault().register(this);
    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
*/