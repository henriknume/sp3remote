package io.hnnt.sp3remote;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import io.hnnt.sp3remote.events.CommandEvent;
import io.hnnt.sp3remote.events.ControlEvent;
import io.hnnt.sp3remote.events.InfoEvent;
import io.hnnt.sp3remote.events.LogEvent;
import io.hnnt.sp3remote.events.ResponseEvent;
import io.hnnt.sp3remote.events.SettingsEvent;

/**
 * Created by nume on 2016-05-06.
 */
public class CommandHandler {



    public static final String TAG = "CommandHandler.java";
    public static final String START_OF_BUFFER_TAG = "[START]\n";
    public static final String END_OF_BUFFER_TAG = "\n[END]\n";

    private UsbService usbService;
    private String currentCommand;
    private String currentResponseTarget;

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
            currentResponseTarget = commandEvent.responseTarget;
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
            //Log.d(TAG, "--response:>" + response.replaceAll("(\\r)", "") + "<");
            //Log.d(TAG, "--trimmedresponse:>" + trimmedResponse + "<");
            if(trimmedResponse.contains("Unknowncommand")){
                Log.d(TAG, "onResponseEvent() resend");
                clearBuffer();
                reSendLastCommand();
            }else{
                Log.d(TAG, "onResponseEvent() send to:" + currentResponseTarget);
                String response = allLines.replaceAll("(\\r)", "");
                switch (currentResponseTarget) {
                    case CommandEvent.TARGET_INFO_FRAGMENT:
                        EventBus.getDefault().post(new InfoEvent(response));;
                        break;
                    case CommandEvent.TARGET_SETTINGS_FRAGMENT:
                        EventBus.getDefault().post(new SettingsEvent(response));;
                        break;
                    case CommandEvent.TARGET_CONTROL_FRAGMENT:
                        EventBus.getDefault().post(new ControlEvent(response));;
                        break;
                    case CommandEvent.TARGET_LOG_FRAGMENT:
                        EventBus.getDefault().post(new LogEvent(response));;
                        break;
                    default: throw new IllegalArgumentException("invalid response target");
                }
                clearBuffer();
            }
        }
    }

    public void updateService(UsbService usbService){
        this.usbService = usbService;
    }
}