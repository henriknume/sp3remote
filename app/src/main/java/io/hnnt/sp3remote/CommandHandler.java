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
    private String currentCommand;

    public CommandHandler( UsbService usbService){
        this.usbService = usbService;
    }

    /*
    *   receive events from fragments and put in a queue
    */
    @Subscribe
    public void onCommandEvent(CommandEvent commandEvent){
        Log.d(TAG, "onCommandEvent()");
        if( usbService != null){
            currentCommand = commandEvent.command;
            sendCommand();
        }
    }


    /*
    *   take events from first in queue and send to service
    */
    private void sendCommand(){
        if(usbService != null && currentCommand != null){
            Log.d(TAG, "sendCommand() - send");
            usbService.write(currentCommand.getBytes());
        }else{
            Log.d(TAG, "sendcommand() - no usbservice, command not sent");
            currentCommand = null;
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
            Log.d(TAG, "onResponseEvent() resend");
            reSendLastCommand();
        }else{
            //placeholder
            EventBus.getDefault().post(new InfoEvent(responseEvent.message));

            //should parse the response message and send to the right fragment

            /*
                InfoEvent
                SettingsEvent
            */
        }
    }

    /*
    *
    * */

    public void updateService(UsbService usbService){
        this.usbService = usbService;
    }
}