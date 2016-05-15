package io.hnnt.sp3remote.fragments;

/**
 * Created by nume on 2016-04-29.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.hnnt.sp3remote.R;
import io.hnnt.sp3remote.Sp3Model;
import io.hnnt.sp3remote.events.CommandEvent;
import io.hnnt.sp3remote.events.InfoEvent;

public class InfoFragment extends Fragment{

    public static final String TAG = "InfoFragment.java";
    public static final String TAG_EVENTS = "commandhandler";
    Context fcontext;
    Button infoButton;

    TextView row0;
    TextView row1;
    TextView row2;
    TextView row3;
    TextView row4;
    TextView row5;
    TextView row6;
    TextView row7;
    TextView row8;
    TextView row9;

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        LinearLayout currentLayout = (LinearLayout) inflater.inflate(R.layout.fragment_info, container, false);
        //infoTextView = (TextView) currentLayout.findViewById(R.id.info_textview);
        infoButton = (Button) currentLayout.findViewById(R.id.info_button);

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "infobutton pressed");
                EventBus.getDefault().post(new CommandEvent("info", CommandEvent.TARGET_INFO_FRAGMENT));
            }
        });

        row0 = (TextView) currentLayout.findViewById(R.id.info_field_0_textview);
        row1 = (TextView) currentLayout.findViewById(R.id.info_field_1_textview);
        row2 = (TextView) currentLayout.findViewById(R.id.info_field_2_textview);
        row3 = (TextView) currentLayout.findViewById(R.id.info_field_3_textview);
        row4 = (TextView) currentLayout.findViewById(R.id.info_field_4_textview);
        row5 = (TextView) currentLayout.findViewById(R.id.info_field_5_textview);
        row6 = (TextView) currentLayout.findViewById(R.id.info_field_6_textview);
        row7 = (TextView) currentLayout.findViewById(R.id.info_field_7_textview);
        row8 = (TextView) currentLayout.findViewById(R.id.info_field_8_textview);
        row9 = (TextView) currentLayout.findViewById(R.id.info_field_9_textview);
        return currentLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();

    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        fcontext = getContext();
        getInfoSp3Model();
        super.onResume();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        Log.d(TAG, "onStop()");
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInfoEvent(InfoEvent event){
        Sp3Model.setInfoSp3Model(event);

        row0.setText(event.getSoftwareRelease());
        row1.setText(event.getCurrentPosition());
        row2.setText(event.getCalcSunPosition());
        row3.setText(event.getTiltZeroPosition());
        row4.setText(event.getSensorZeroPosition());
        row5.setText(event.getNrOfSunnyDays());
        row6.setText(event.getLastSunTime());
        row7.setText(event.getNrOfResets());
        row8.setText(event.getMorningPosDiff());
        boolean sun = false;
        if(event.getSunToday().equals("1")){
            sun = true;
        }
        row9.setText("" + sun);
    }

    public void getInfoSp3Model(){
        if(Sp3Model.getSoftwareRelease() != null){
            row0.setText(Sp3Model.getSoftwareRelease());
            row1.setText(Sp3Model.getCurrentPosition());
            row2.setText(Sp3Model.getCalcSunPosition());
            row3.setText(Sp3Model.getTiltZeroPosition());
            row4.setText(Sp3Model.getSensorZeroPosition());
            row5.setText(Sp3Model.getNrOfSunnyDays());
            row6.setText(Sp3Model.getLastSunTime());
            row7.setText(Sp3Model.getNrOfResets());
            row8.setText(Sp3Model.getMorningPosDiff());
            row9.setText(Sp3Model.getSunToday());
        }
    }
}