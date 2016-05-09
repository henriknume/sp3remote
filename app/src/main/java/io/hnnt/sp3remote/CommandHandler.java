package io.hnnt.sp3remote;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

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
    private String currentCommand;

    public CommandHandler( UsbService usbService){
        this.usbService = usbService;
        commandQueue = new LinkedList<>();
    }

    /*
    *   receive events from fragments and put in a queue
    */
    @Subscribe
    public void onCommandEvent(CommandEvent commandEvent){
        Log.d(TAG, "onCommandEvent() A -Adding commandevent");
        commandQueue.add(commandEvent);
        Log.d(TAG, "onCommandEvent() size:" + commandQueue.size());
        sendCommand();
    }


    /*
    *   take events from first in queue and send to service
    */
    private void sendCommand(){
        Log.d(TAG, "sendCommand()");
        currentCommand = null;
        CommandEvent currentCommandEvent = commandQueue.poll();
        if(currentCommandEvent != null){
            currentCommand = currentCommandEvent.command;
            if(usbService != null && currentCommand != null){
                usbService.write(currentCommand.getBytes());
            }
        }else{
            Log.w(TAG, "sendcommand() - no usbservice, command not sent");
            clearQueue();
        }
    }

    private void reSendLastCommand(){
        Log.d(TAG, "reSendLastCommand()," + "usbser:" + (usbService != null) + "currCmd:" + currentCommand.trim());
        if(usbService != null && currentCommand != null) {
            usbService.write(currentCommand.getBytes());
            currentCommand = null;
        }
    }

    /*
    *   receive response from service...
    *       if Unknown command, send again
    *       else pass on to correct fragment
    */

    @Subscribe
    public void onResponseEvent(ResponseEvent responseEvent){
        Log.d(TAG, "onResponseEvent()");

        if(responseEvent.message.contains("Unknown command")){
            Log.d(TAG, "onResponseEvent() RS");
            reSendLastCommand();
        }else{
            if(responseEvent != null && responseEvent.message != null){

                //placeholder
                EventBus.getDefault().post(new InfoEvent(responseEvent.message));

                //should parse the response message and send to the right fragment

                /*
                    InfoEvent
                    SettingsEvent
                */
            }
            sendCommand();
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