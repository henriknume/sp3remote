package io.hnnt.sp3remote;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
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
    public static final String START_OF_BUFFER_TAG = "[START]";
    public static final String END_OF_BUFFER_TAG = "[END]";

    private UsbService usbService;
    private String currentCommand;

    private ArrayList<String> inputbuffer;

    public CommandHandler( UsbService usbService){
        this.usbService = usbService;
        this.inputbuffer = new ArrayList<>();
    }

    /*
    *   receive events from fragments and put in a queue
    */
    @Subscribe
    public void onCommandEvent(CommandEvent commandEvent){
        Log.d(TAG, "onCommandEvent()");
        clearBuffer();
        if( usbService != null){
            currentCommand = commandEvent.command;
            sendCommand();
        }
    }

    private void clearBuffer(){
        inputbuffer.clear();
        inputbuffer.add(START_OF_BUFFER_TAG);
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
        Log.d(TAG, "reSendLastCommand()");
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
        String data = responseEvent.message;
        inputbuffer.add(data);

        if(data.contains("#")){
            inputbuffer.add(END_OF_BUFFER_TAG);
            String allLines = "";
            // concat
            for(String s : inputbuffer){
                allLines += s;
            }
            // remove all CRLF and space
            String trimmedResponse = allLines.replaceAll("(\\r|\\n|\\s)", "");
            // remove only CR
            String response = allLines.replaceAll("(\\r)", "");
            //Log.d(TAG, "--response:>" + response.replaceAll("(\\r)", "") + "<");
            //Log.d(TAG, "--trimmedresponse:>" + trimmedResponse + "<");
            if(trimmedResponse.contains("Unknowncommand")){
                Log.d(TAG, "onResponseEvent() resend");
                clearBuffer();
                reSendLastCommand();
            }else{
                Log.d(TAG, "onResponseEvent() send");
                EventBus.getDefault().post(new InfoEvent(response));
                clearBuffer();
            }
        }
    }

    public void updateService(UsbService usbService){
        this.usbService = usbService;
    }
}