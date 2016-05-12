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
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.hnnt.sp3remote.R;
import io.hnnt.sp3remote.events.CommandEvent;
import io.hnnt.sp3remote.events.InfoEvent;

public class LogFragment extends Fragment{

    public static final String TAG = "LogFragment.java";

    public  Context fcontext;
    private Button toggleLogButton, clearLogButton, sendLogButton;

    private TextView logTextView;

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
        View v = inflater.inflate(R.layout.fragment_log, container, false);
        logTextView     = (TextView) v.findViewById(R.id.log_textview);

        toggleLogButton = (Button)   v.findViewById(R.id.toggle_log_button);
        clearLogButton  = (Button)   v.findViewById(R.id.clear_log_button);
        sendLogButton   = (Button)   v.findViewById(R.id.send_log_button);

        createButtonListeners();
        return v;
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
        super.onResume();
    }

    @Override
    public void onStop(){
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInfoEvent(InfoEvent event){
        Log.d(TAG, "onMessageEvent(), message: " + event.message + "\n");
        logTextView.append(event.message + "\n");
    }


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
                logTextView.setText(getString(R.string.logtextview_default_text));
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
        emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text_default)+"\n\n"+logTextView.getText().toString());

        try{
            startActivity(Intent.createChooser(emailIntent, getString(R.string.email_send_email)));

        }catch (ActivityNotFoundException e){
            e.printStackTrace();
        }
    }

}