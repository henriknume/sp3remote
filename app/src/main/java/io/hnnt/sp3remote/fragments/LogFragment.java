package io.hnnt.sp3remote.fragments;

/**
 * Created by Tobias Nielsen on 2016-04-29.
 */

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import io.hnnt.sp3remote.R;
import io.hnnt.sp3remote.model.ListviewLogAdapter;
import io.hnnt.sp3remote.model.LogItem;
import io.hnnt.sp3remote.model.Sp3Model;
import io.hnnt.sp3remote.events.CommandEvent;
import io.hnnt.sp3remote.events.InfoEvent;
import io.hnnt.sp3remote.events.LogEvent;
import io.hnnt.sp3remote.events.SettingsEvent;

public class LogFragment extends Fragment{

    public static final String TAG = "LogFragment.java";

    public  Context fcontext;
    private Button toggleLogButton, clearLogButton, sendLogButton;
    private boolean loggaIsActive = false;

    private ListView listView;
    private ListviewLogAdapter lvLogAdapter;
    private  ArrayList<LogItem> logItemList;

    public LogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_log, container, false);

        toggleLogButton = (Button)   rootView.findViewById(R.id.toggle_log_button);
        clearLogButton  = (Button)   rootView.findViewById(R.id.clear_log_button);
        sendLogButton   = (Button)   rootView.findViewById(R.id.send_log_button);

        ArrayList<LogItem> logItemList = Sp3Model.getLogItemList();
        ListView listView = (ListView)rootView.findViewById(R.id.log_items_listview);
        lvLogAdapter = new ListviewLogAdapter(getActivity(), logItemList);
        listView.setAdapter(lvLogAdapter);
        createButtonListeners();
        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart()");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume(){
        Log.d(TAG, "onResume()");
        fcontext = getContext();
        EventBus.getDefault().post(new CommandEvent("date", CommandEvent.TARGET_SETTINGS_FRAGMENT));
        //logTextView.setText(Sp3Model.getLog());
        super.onResume();
    }

    @Override
    public void onStop(){
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogEvent(LogEvent event){
        Log.d(TAG,"onLogEvent():" + event.message);
        if(event.isLogLine){
            loggaIsActive = true;
            updateLogListview(event.getLogList());
        }else{
            /* response from toggle logga button goes here */
            if(loggaIsActive){
                //logTextView.append("\n>log stopped\n");
                loggaIsActive = false;
            }else{
                //logTextView.append("\n>nlog started\n");
            }
        }
    }

    private void updateLogListview(ArrayList<LogItem> list){

        logItemList = list;
        lvLogAdapter.update(logItemList);
        lvLogAdapter.notifyDataSetChanged();


    }
/*
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInfoEvent(InfoEvent event){
        Log.d(TAG, "onMessageEvent(), message: " + event.message + "\n");

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSettingsEvent(SettingsEvent event){
        if(event.responseType == SettingsEvent.DATE_VALUE_GET)
            Sp3Model.setDate(event.responseData);
            Log.d(TAG, event.responseData);
    }

*/
    private void createButtonListeners(){

        toggleLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "toggleLogButton pressed");
                EventBus.getDefault().post(new CommandEvent("logga", CommandEvent.TARGET_LOG_FRAGMENT));
            }
        });

        clearLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clearLogButton pressed");
            }
        });

        sendLogButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    private void sendEmail(){

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                getString(R.string.email_intent),getString(R.string.email_recepient_default), null));

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject_default));
        //the message
        emailIntent.putExtra(Intent.EXTRA_TEXT,
                  Sp3Model.getDate()
                +"\n\n"
                + Sp3Model.getInfoToMail()
                +"\n"
                + getString(R.string.email_text_default)
                +"\n"
                + Sp3Model.getLogEmailFormatted()
        );

        try{
            startActivity(Intent.createChooser(emailIntent, getString(R.string.email_send_email)));

        }catch (ActivityNotFoundException e){
            e.printStackTrace();
        }
    }

}